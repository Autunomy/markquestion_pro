package com.hty.markquestion.service;

import com.hty.markquestion.mapper.CommentReplyMapper;
import com.hty.markquestion.pojo.CommentReply;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentReplyService {
    @Autowired
    private CommentReplyMapper commentReplyMapper;

    public List<CommentReply> queryCommentReply(Integer id) {
        List<CommentReply> commentReplyList = commentReplyMapper.queryCommentReply(id);
        List<CommentReply> response = new ArrayList<>();
        for (CommentReply commentReply : commentReplyList) {
            response.add(commentReply);
            List<CommentReply> res = queryCommentReply(commentReply.getId());
            if(res == null){
                continue;
            }
            for (CommentReply x : res) {
                response.add(x);
            }
        }
        if(response.size() == 0) return null;
        return response;
    }

}
