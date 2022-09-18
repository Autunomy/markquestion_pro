package com.hty.markquestion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hty.markquestion.pojo.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogMapper extends BaseMapper<Blog> {
    List<Blog> queryAllBlog(@Param("currentPage") Integer currentPage,@Param("pageSize") Integer pageSize);
}
