package com.hty.markquestion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hty.markquestion.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

}
