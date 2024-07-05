
package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员Mapper接口
 */
@Mapper
public interface MemberMapper {

    /**
     * 保存会员信息
     * @param member 会员实体类
     * @return 返回保存结果
     */
    int save(Member member);

    /**
     * 根据ID查询会员信息
     * @param id 会员ID
     * @return 返回会员实体类
     */
    Member selectById(Long id);

    /**
     * 更新会员信息
     * @param member 会员实体类
     * @return 返回更新结果
     */
    int update(Member member);

    /**
     * 根据ID删除会员信息
     * @param id 会员ID
     * @return 返回删除结果
     */
    int deleteById(Long id);

    /**
     * 根据openid查询会员信息
     * @param openId 微信openid
     * @return 返回会员实体类
     */
    Member getByOpenid(String openId);

    /**
     * 分页
     * @param phone phone
     * @param nickname nickname
     * @return 返回
     */
    Page<List<Member>> page(@Param("phone") String phone, @Param("name") String nickname);
}


