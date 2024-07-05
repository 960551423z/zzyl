package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.PostDto;
import com.zzyl.vo.PostVo;

import java.util.List;

/**
 * 岗位表服务类
 */
public interface PostService {

    /**
     *  多条件查询岗位表分页列表
     * @param postDto 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<PostVo>
     */
    PageResponse<PostVo> findPostPage(PostDto postDto, int pageNum, int pageSize);

    /**
     *  创建岗位表
     * @param postDto 对象信息
     * @return PostDto
     */
    PostVo createPost(PostDto postDto);

    /**
     *  修改岗位表
     * @param postDto 对象信息
     * @return Boolean
     */
    Boolean updatePost(PostDto postDto);

    /**
     *  多条件查询岗位表列表
     * @param postDto 查询条件
     * @return: List<PostDto>
     */
    List<PostVo> findPostList(PostDto postDto);

    /**
     *  人员对应职位
     * @param userId 查询条件
     * @return: List<PostVo>
     */
    List<PostVo> findPostVoListByUserId(Long userId);

    /**
     * 删除岗位
     * @param postIds
     * @return
     */
    int deletePostByIds(String[] postIds);
}
