package com.zzyl.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zzyl.base.PageResponse;
import com.zzyl.constant.Constants;
import com.zzyl.dto.UserLoginRequestDto;
import com.zzyl.entity.Member;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.MemberMapper;
import com.zzyl.properties.JwtTokenManagerProperties;
import com.zzyl.service.*;
import com.zzyl.utils.JwtUtil;
import com.zzyl.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户管理
 */
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private JwtTokenManagerProperties jwtTokenManagerProperties;

    @Resource
    private WechatService wechatService;

    @Resource
    private MemberMapper memberMapper;


    static ArrayList DEFAULT_NICKNAME_PREFIX = Lists.newArrayList(
            "生活更美好",
            "大桔大利",
            "日富一日",
            "好柿开花",
            "柿柿如意",
            "一椰暴富",
            "大柚所为",
            "杨梅吐气",
            "天生荔枝"
    );

    private static final String[] OPERATOR_PREFIXES = {
            "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
            "145", "147", "150", "151", "152", "153", "155", "156", "157", "158", "159",
            "176", "177", "178", "180", "181", "182", "183", "184", "185", "186", "187", "188"
    };

    static String RandomPhone() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // 不包括以0开头的四位数
        int firstDigit = random.nextInt(9) + 1; // 1-9之间的随机数
        sb.append(firstDigit);

        // 接下来的三位数字可以是0-9之间的任意数字
        for (int i = 0; i < 3; i++) {
            int digit = random.nextInt(10); // 0-9之间的随机数
            sb.append(digit);
        }

        return sb.toString();
    }


    /**
     * 随机生成手机号
     * @return 11位手机号
     */
    static String generateRandomPhoneNumber() {
        Random random = new Random();

        // 随机选择一个运营商号段
        String prefix = OPERATOR_PREFIXES[random.nextInt(OPERATOR_PREFIXES.length)];

        // 生成后八位随机数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10)); // 0-9之间的随机数
        }

        // 拼接完整的手机号
        return prefix + sb;
    }

    /**
     * 新增
     *
     * @param member 用户信息
     */
    @Override
    public void save(Member member) {
        //判断id是否存在，不存在则新增，否则是更新
//        if (ObjectUtil.isEmpty(member.getId())) {
//            //随机组装昵称，随机词组+手机号后4位
//            String nickName = DEFAULT_NICKNAME_PREFIX.get((int)(Math.random() * DEFAULT_NICKNAME_PREFIX.size()))
//                    + RandomPhone();
//            member.setName(nickName);
//            memberMapper.save(member);
//            return;
//        }
//        update(member);


        //随机组装昵称，随机词组+手机号后4位
        String nickName = DEFAULT_NICKNAME_PREFIX.get((int) (Math.random() * DEFAULT_NICKNAME_PREFIX.size()))
                + RandomPhone();
        member.setName(nickName);
        memberMapper.save(member);

    }

    /**
     * 根据openid查询用户
     *
     * @param openId 微信ID
     * @return 用户信息
     */
    @Override
    public Member getByOpenid(String openId) {
        return memberMapper.getByOpenid(openId);
    }

    /**
     * 登录
     *
     * @param userLoginRequestDto 登录code
     * @return 用户信息
     */
    @Override
    public LoginVo login(UserLoginRequestDto userLoginRequestDto) throws IOException {
        // 1 调用微信开放平台小程序的api，根据code获取openid
        String openId = wechatService.getOpenId(userLoginRequestDto.getCode());

        // 查询数据库中是否存在
        Member member = getByOpenid(openId);


        // 不存在则添加到数据库中
        if (member == null) {
            // 新用户
            String phone = generateRandomPhoneNumber();
            member.setPhone(phone);
            save(member);
        }




        // 7 将用户ID存入token
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.JWT_USERID, member.getId());
        claims.put(Constants.JWT_USERNAME, member.getName());

        // 8 封装token，响应结果
        String token = JwtUtil.createJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), jwtTokenManagerProperties.getTtl(), claims);
        LoginVo loginVO = new LoginVo();
        loginVO.setToken(token);
        loginVO.setNickName(member.getName());
        return loginVO;
    }

    /**
     * 更新用户信息
     *
     * @param member 用户信息
     * @return 更新结果
     */
    @Override
    public int update(Member member) {
        return memberMapper.update(member);
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public Member getById(Long id) {
        return memberMapper.selectById(id);
    }

    @Resource
    private ContractService contractService;

    @Resource
    private MemberElderService memberElderService;

    @Resource
    private OrderService orderService;


    /**
     * 分页查询用户列表
     *
     * @param page     当前页码
     * @param pageSize 每页数量
     * @param phone    手机号
     * @param nickname 昵称
     * @return 分页结果
     */
    @Override
    public PageResponse<MemberVo> page(Integer page, Integer pageSize, String phone, String nickname) {
        PageHelper.startPage(page, pageSize);
        Page<List<Member>> listPage = memberMapper.page(phone, nickname);

        PageResponse<MemberVo> pageResponse = PageResponse.of(listPage, MemberVo.class);
        List<Long> ids = pageResponse.getRecords().stream().map(MemberVo::getId).distinct().collect(Collectors.toList());

        pageResponse.getRecords().forEach(v -> {
            List<ContractVo> contractVos = contractService.listByMemberPhone(v.getPhone());
            v.setIsSign(contractVos.isEmpty() ? "否" : "是");
            List<OrderVo> orderVos = orderService.listByMemberId(v.getId());
            v.setOrderCount(orderVos.size());
            List<MemberElderVo> memberElderVos = memberElderService.listByMemberId(v.getId());
            List<String> collect = memberElderVos.stream().map(m -> m.getElderVo().getName()).collect(Collectors.toList());
            v.setElderNames(String.join(",", collect));
        });
        return pageResponse;
    }
}
