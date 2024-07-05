package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.dto.PostDto;
import com.zzyl.entity.Post;
import com.zzyl.vo.PostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {


    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPostNo(@Param("postNo")String postNo);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    Page<List<Post>> selectPage(@Param("postDto") PostDto postDto);

    List<Post> selectList(@Param("postDto")PostDto postDto);

    List<PostVo> findPostVoListByUserId(@Param("userId") Long userId);

    /**
     * 通过岗位ID查询岗位使用数量
     * @param postIds
     * @return
     */
    Integer checkPostHasUser(@Param("postIds") String[] postIds);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    public int deletePostByIds(@Param("postIds") String[] postIds);

    void deletePostByDeptNo(@Param("deptId") String deptId);
}
