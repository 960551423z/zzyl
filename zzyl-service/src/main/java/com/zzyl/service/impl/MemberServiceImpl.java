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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    /**
     * 新增
     * @param member 用户信息
     */
    @Override
    public void save(Member member) {
        //判断id是否存在，不存在则新增，否则是更新
        if (ObjectUtil.isEmpty(member.getId())) {
            //随机组装昵称，随机词组+手机号后4位
            String nickName = DEFAULT_NICKNAME_PREFIX.get((int)(Math.random() * DEFAULT_NICKNAME_PREFIX.size())) + StringUtils.substring(member.getPhone(), 7);
            member.setName(nickName);
            memberMapper.save(member);
            return;
        }
        update(member);
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
        JSONObject jsonObject = wechatService.getOpenid(userLoginRequestDto.getCode());
        // 2 若code不正确，则获取不到openid，响应失败
        if (ObjectUtil.isNotEmpty(jsonObject.getInt("errcode"))) {
            throw new BaseException(jsonObject.getStr("errmsg"));
        }
        String openId = jsonObject.getStr("openid");

        /*
        * 3 根据openid从数据库查询用户
        * 3.1 如果为新用户，此处返回为null
        * 3.2 如果为已经登录过的老用户，此处返回为user对象 （包含openId,phone,unionId等字段）
         */
        Member member = getByOpenid(openId);

        /*
         * 4 构造用户数据，设置openId,unionId
         * 4.1 如果member为null，则为新用户，需要构建新的member对象，并设置openId
         * 4.2 如果member不为null，则为老用户，无需设置openId
         */

        member = ObjectUtil.isNotEmpty(member) ? member : Member.builder()
                .openId(openId)
                .build();


        // 5 调用微信开放平台小程序的api获取微信绑定的手机号
        String phone = wechatService.getPhone(userLoginRequestDto.getPhoneCode());

        /*
         * 6 新用户绑定手机号或者老用户更新手机号
         * 6.1 如果user.getPhone()为null，则为新用户，需要设置手机号，并保存数据库
         * 6.2 如果user.getPhone()不为null，但是与微信获取到的手机号不一样  则表示用户改了微信绑定的手机号，需要设置手机号，并保存数据库
         * 以上俩种情况，都需要重新设置手机号，并保存数据库
         */
        if (ObjectUtil.notEqual(member.getPhone(), phone)) {
            member.setPhone(phone);
            save(member);
        }

        // 7 将用户ID存入token
        Map<String, Object> claims = MapUtil.<String, Object>builder()
                .put(Constants.JWT_USERID, member.getId()).build();
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
