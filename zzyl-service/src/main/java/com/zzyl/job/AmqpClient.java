package com.zzyl.job;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.zzyl.constant.Constants;
import com.zzyl.entity.AlertRule;
import com.zzyl.entity.DeviceData;
import com.zzyl.mapper.AlertRuleMapper;
import com.zzyl.mapper.DeviceDataMapper;
import com.zzyl.mapper.DeviceMapper;
import com.zzyl.properties.AliIoTConfigProperties;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.DeviceDataVo;
import com.zzyl.vo.DeviceVo;
import com.zzyl.websocket.WebSocketServer;
import org.apache.commons.codec.binary.Base64;
import org.apache.qpid.jms.JmsConnection;
import org.apache.qpid.jms.JmsConnectionListener;
import org.apache.qpid.jms.message.JmsInboundMessageDispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AmqpClient implements ApplicationRunner {
    private final static Logger logger = LoggerFactory.getLogger(AmqpClient.class);


    private static final String GREATER_THAN_OR_EQUAL = ">=";
    private static final String LESS_THAN = "<";

    @Autowired
    private AliIoTConfigProperties aliIoTConfigProperties;

    //控制台服务端订阅中消费组状态页客户端ID一栏将显示clientId参数。
    //建议使用机器UUID、MAC地址、IP等唯一标识等作为clientId。便于您区分识别不同的客户端。
    private static String clientId;

    static {
        try {
            clientId = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    // 指定单个进程启动的连接数
    // 单个连接消费速率有限，请参考使用限制，最大64个连接
    // 连接数和消费速率及rebalance相关，建议每500QPS增加一个连接
    private static int connectionCount = 4;

    //业务处理异步线程池，线程池参数可以根据您的业务特点调整，或者您也可以用其他异步方式处理接收到的消息。
    @Autowired
    private ExecutorService executorService;

    public void start() throws Exception {
        List<Connection> connections = new ArrayList<>();

        //参数说明，请参见AMQP客户端接入说明文档。
        for (int i = 0; i < connectionCount; i++) {
            long timeStamp = System.currentTimeMillis();
            //签名方法：支持hmacmd5、hmacsha1和hmacsha256。
            String signMethod = "hmacsha1";

            //userName组装方法，请参见AMQP客户端接入说明文档。
            String userName = clientId + "-" + i + "|authMode=aksign"
                    + ",signMethod=" + signMethod
                    + ",timestamp=" + timeStamp
                    + ",authId=" + aliIoTConfigProperties.getAccessKeyId()
                    + ",iotInstanceId=" + aliIoTConfigProperties.getIotInstanceId()
                    + ",consumerGroupId=" + aliIoTConfigProperties.getConsumerGroupId()
                    + "|";
            //计算签名，password组装方法，请参见AMQP客户端接入说明文档。
            String signContent = "authId=" + aliIoTConfigProperties.getAccessKeyId() + "&timestamp=" + timeStamp;
            String password = doSign(signContent, aliIoTConfigProperties.getAccessKeySecret(), signMethod);
            String connectionUrl = "failover:(amqps://" + aliIoTConfigProperties.getHost() + ":5671?amqp.idleTimeout=80000)"
                    + "?failover.reconnectDelay=30";

            Hashtable<String, String> hashtable = new Hashtable<>();
            hashtable.put("connectionfactory.SBCF", connectionUrl);
            hashtable.put("queue.QUEUE", "default");
            hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
            Context context = new InitialContext(hashtable);
            ConnectionFactory cf = (ConnectionFactory) context.lookup("SBCF");
            Destination queue = (Destination) context.lookup("QUEUE");
            // 创建连接。
            Connection connection = cf.createConnection(userName, password);
            connections.add(connection);

            ((JmsConnection) connection).addConnectionListener(myJmsConnectionListener);
            // 创建会话。
            // Session.CLIENT_ACKNOWLEDGE: 收到消息后，需要手动调用message.acknowledge()。
            // Session.AUTO_ACKNOWLEDGE: SDK自动ACK（推荐）。
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            connection.start();
            // 创建Receiver连接。
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(messageListener);
        }

        logger.info("amqp  is started successfully, and will exit after server shutdown ");
    }

    private MessageListener messageListener = message -> {
        try {
            //异步处理收到的消息，确保onMessage函数里没有耗时逻辑
            executorService.submit(() -> processMessage(message));
        } catch (Exception e) {
            logger.error("submit task occurs exception ", e);
        }
    };

    @Resource
    private DeviceDataMapper deviceDataMapper;

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 在这里处理您收到消息后的具体业务逻辑。
     */
    private void processMessage(Message message) {
        try {
            byte[] body = message.getBody(byte[].class);
            String content = new String(body);
            String topic = message.getStringProperty("topic");
            String messageId = message.getStringProperty("messageId");
            logger.info("receive message"
                    + ",\n topic = " + topic
                    + ",\n messageId = " + messageId
                    + ",\n content = " + content);
            Content c = JSONUtil.toBean(content, Content.class);
            List<DeviceVo> devices = deviceMapper.selectByDeviceIds(Lists.newArrayList(c.getIotId()));
            if (ObjectUtil.isEmpty(devices)) {
                logger.error("设备不存在" + c.getIotId());
                return;
            }
            DeviceVo device = devices.get(0);
            List<DeviceDataVo> list = new ArrayList<>();
            c.getItems().forEach((key, value) -> {
                //过滤上报数据，是否符合报警规则
                Integer status = alertFilter(key, value, c);
                DeviceData build = DeviceData.builder()
                        .alarmTime(LocalDateTimeUtil.of(value.getTime()))
                        .deviceName(c.getDeviceName())
                        .iotId(c.getIotId())
                        .productId(c.getProductKey())
                        .functionName(key)
                        .dataValue(value.getValue() + "")
                        .noteName(device.getNickname())
                        .productName(device.getProductName())
                        .accessLocation(device.getRemark())
                        .status(status + "").build();

                deviceDataMapper.insert(build);

                if (status.equals(2) && device.getLocationType().equals(1)) {
                    // 获取楼层ID
                    String[] strings = device.getDeviceDescription().split(",");
                    webSocketServer.sendToAllClient(strings[0]);
                }

                DeviceDataVo deviceDataVo = BeanUtil.toBean(build, DeviceDataVo.class);
                list.add(deviceDataVo);
            });
            redisTemplate.opsForHash().put(Constants.DEVICE_LASTDATA_CACHE_KEY, c.getIotId(), JSONUtil.toJsonStr(list));
        } catch (Exception e) {
            logger.error("processMessage occurs error ", e);
        }
    }


    /**
     * 告警过滤
     * @param key     物模型key  比如 心率：HeartRate
     * @param value   物模型数据是一个Item对象（value,time）
     * @param content 接收到的所有数据
     * @return
     */
    private Integer alertFilter(String key, Content.Item value, Content content) {

        //线程安全的原子类，数据状态 (0:正常  1:异常  2:待处理  3:已处理）
        AtomicReference<Integer> finalStatus = new AtomicReference<>(0);

        //查询产品下所有的设备报警规则
        List<AlertRule> alertRules = alertRuleMapper.selectByFunctionId(key, content.getIotId(), content.getProductKey());
        List<AlertRule> allDeviceAlertRules = alertRuleMapper.selectByFunctionId(key, "-1", content.getProductKey());
        alertRules.addAll(allDeviceAlertRules);

        //如果告警规则数据为空，不需要过滤数据，所有数据正常，状态为0
        if (CollUtil.isEmpty(alertRules)) {
            return finalStatus.get();
        }

        //遍历告警规则，检索当前数据是否符合告警规则
        alertRules.forEach(alertRule -> {

            //临时状态  0 正常   1 异常   2 待处理
            Integer status = 0;

            //报警生效时段
            String[] aepArr = alertRule.getAlertEffectivePeriod().split("~");

            //上报时间
            LocalDateTime time = LocalDateTimeUtil.of(value.getTime());

            //报警生效时段-开始时间
            LocalDateTime startTime = LocalDateTime.of(time.toLocalDate(), LocalTime.parse(aepArr[0], DateTimeFormatter.ISO_LOCAL_TIME));

            //报警生效时段-结束时间
            LocalDateTime endTime = LocalDateTime.of(time.toLocalDate(), LocalTime.parse(aepArr[1], DateTimeFormatter.ISO_LOCAL_TIME));

            //如果上报数据不在生效时段，则停止检查
            if (startTime.isAfter(time) || endTime.isBefore(time)) {
                return;
            }

            //报警规则阈值 与 上报数据进行比对
            //返回值为-1， 表示左边的数比右边的数小, 阈值 < 上报的数据
            int val = alertRule.getValue().compareTo((float) value.getValue());

            //如果告警规则的操作符为 小于 && 阈值 > 上报的数据
            if (alertRule.getOperator().equals(GREATER_THAN_OR_EQUAL) && val < 0) {
                status = 1;
                if (finalStatus.get() != 2) {
                    finalStatus.set(1);
                }
            }

            //如果告警规则的操作符为 小于 && 阈值 < 上报的数据
            if (alertRule.getOperator().equals(LESS_THAN) && val > 0) {
                status = 1;
                if (finalStatus.get() != 2) {
                    finalStatus.set(1);
                }
            }
            //如果没有触发阈值，则中断匹配
            if (status == 0) {
                return;
            }

            //设备ID
            String deviceId = content.getIotId();

            //沉默周期缓存Key
            String slientCacheKey = alertRule.getId() + "_" + deviceId + "_" + key + "_silent";

            //沉默周期的数据
            String silent = redisTemplate.opsForValue().get(slientCacheKey);

            // 沉默周期内 直接跳过
            if (ObjectUtil.isNotEmpty(silent)) {
                return;
            }

            //数据采样时间缓存key
            String aggTimeKey = "aggregation_" + alertRule.getId() + "_" + deviceId + "_" + key;

            //数据采样次数缓存key
            String aggCountkey = alertRule.getId() + "_" + deviceId + "_" + key + "_aggregation";

            //获取采样数据
            //采样时间
            String aggTimeData = redisTemplate.boundValueOps(aggTimeKey).get();
            //采样次数
            String aggCountData = redisTemplate.boundValueOps(aggCountkey).get();

            //判断采样数据是否为空
            if (ObjectUtil.isEmpty(aggTimeData) || ObjectUtil.isEmpty(aggCountData)) {
                //持续周期为1
                if (alertRule.getDuration().equals(1)) {

                    // 异常并满足告警条件 则删除采样时间 采样次数
                    redisTemplate.delete(aggTimeKey);
                    redisTemplate.delete(aggCountkey);

                    // 转化为告警沉默周期
                    redisTemplate.opsForValue().set(slientCacheKey, value.getValue() + "", Integer.parseInt(Optional.of(alertRule.getRemark()).orElse("5")), TimeUnit.MINUTES);
                    finalStatus.set(2);
                    return;
                }

                // 第一次异常 不满足告警条件持续周期次数 则新增采样时间
                redisTemplate.opsForValue().set(aggCountkey, 1 + "", alertRule.getDataAggregationPeriod() * alertRule.getDuration(), TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(aggTimeKey, value.getTime() + "");
                return;
            }

            // 不满足聚合周期,不处理数据
            if (LocalDateTimeUtil.of(Long.parseLong(aggTimeData)).plusMinutes(alertRule.getDataAggregationPeriod()).isAfter(LocalDateTimeUtil.of(value.getTime()))) {
                return;
            }

            // 满足采样时间,假如数据是正常的，则删除临时存储的数据
            if (status == 0) {
                // 正常 则删除采样时间 采样次数
                redisTemplate.delete(aggTimeKey);
                redisTemplate.delete(aggCountkey);
                return;
            }

            // 新增异常次数
            int count = Integer.parseInt(aggCountData) + 1;
            redisTemplate.opsForValue().set(aggCountkey, count + "", alertRule.getDataAggregationPeriod() * alertRule.getDuration(), TimeUnit.MINUTES);

            // 异常次数是否满足持续周期
            if (count != alertRule.getDuration()) {
                // 异常 但不满足告警条件持续周期次数 则新增采样时间
                redisTemplate.opsForValue().set(aggTimeKey, value.getTime() + "");
                return;
            }

            // 满足持续周期
            // 异常并满足告警条件 则删除采样时间 采样次数
            redisTemplate.delete(aggTimeKey);
            redisTemplate.delete(aggCountkey);

            // 转化为告警沉默周期
            redisTemplate.opsForValue().set(slientCacheKey, value.getValue() + "", Integer.parseInt(alertRule.getRemark()), TimeUnit.MINUTES);
            //数据设置为待处理状态
            finalStatus.set(2);
        });
        return finalStatus.get();
    }

    private JmsConnectionListener myJmsConnectionListener = new JmsConnectionListener() {
        /**
         * 连接成功建立。
         */
        @Override
        public void onConnectionEstablished(URI remoteURI) {
            logger.info("onConnectionEstablished, remoteUri:{}", remoteURI);
        }

        /**
         * 尝试过最大重试次数之后，最终连接失败。
         */
        @Override
        public void onConnectionFailure(Throwable error) {
            logger.error("onConnectionFailure, {}", error.getMessage());
        }

        /**
         * 连接中断。
         */
        @Override
        public void onConnectionInterrupted(URI remoteURI) {
            logger.info("onConnectionInterrupted, remoteUri:{}", remoteURI);
        }

        /**
         * 连接中断后又自动重连上。
         */
        @Override
        public void onConnectionRestored(URI remoteURI) {
            logger.info("onConnectionRestored, remoteUri:{}", remoteURI);
        }

        @Override
        public void onInboundMessage(JmsInboundMessageDispatch envelope) {
        }

        @Override
        public void onSessionClosed(Session session, Throwable cause) {
        }

        @Override
        public void onConsumerClosed(MessageConsumer consumer, Throwable cause) {
        }

        @Override
        public void onProducerClosed(MessageProducer producer, Throwable cause) {
        }
    };

    /**
     * 计算签名，password组装方法，请参见AMQP客户端接入说明文档。
     */
    private static String doSign(String toSignString, String secret, String signMethod) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), signMethod);
        Mac mac = Mac.getInstance(signMethod);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(toSignString.getBytes());
        return Base64.encodeBase64String(rawHmac);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        start();
    }
}
