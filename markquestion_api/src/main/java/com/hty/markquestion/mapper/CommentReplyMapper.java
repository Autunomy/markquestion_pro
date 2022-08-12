package com.hty.markquestion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hty.markquestion.pojo.CommentReply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReplyMapper extends BaseMapper<CommentReply> {
    public List<CommentReply> queryCommentReply(@Param("id") Integer id);
}
