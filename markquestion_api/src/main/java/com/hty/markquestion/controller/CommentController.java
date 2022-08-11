package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.CommentMapper;
import com.hty.markquestion.pojo.Comment;
import com.hty.markquestion.pojo.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;


    /***
     * 根据题解id获取当前题解下的所有非回复评论  按照日期由近到远
     * @return
     */
    @GetMapping("/queryAllCommentByQuestionId")
    @ResponseBody
    public String queryAllCommentByQuestionId(@RequestParam("qid") Integer qid){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_id",qid);
        queryWrapper.orderByDesc("comment_date");
        queryWrapper.eq("comment_id",0);
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        Response response = new Response(ResponseMessage.SUCCESS, commentList);
        return JSON.toJSONString(response);
    }


    /***
     * 保存评论
     * @param qid 题解id
     * @param content 内容
     * @param uid 用户id
     * @param commentId 回复的评论的id 非回复评论为0
     * @return
     */
    @PostMapping("/saveComment")
    @ResponseBody
    public String saveComment(@RequestParam("qid") String qid,
                              @RequestParam("content") String content,
                              @RequestParam("uid") String uid,
                              @RequestParam("commentId") String commentId){
        Comment comment = new Comment();
        comment.setQuestionId(Integer.parseInt(qid));
        comment.setContent(content);
        comment.setUserId(Integer.parseInt(uid));
        comment.setIsReply(Integer.parseInt(commentId) == 0?0:1);
        comment.setCommentId(Integer.parseInt(commentId));
        comment.setCommentDate(new Date());
        commentMapper.insert(comment);
        Response response = new Response(ResponseMessage.SUCCESS);
        return JSON.toJSONString(response);
    }


    /***
     * 获取某个评论的回复评论
     * @param id
     * @return
     */
    @GetMapping("/queryReplyCommentById")
    @ResponseBody
    public String queryReplyCommentById(@RequestParam("id") Integer id){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id",id);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        Response response = new Response(ResponseMessage.SUCCESS, comments);
        return JSON.toJSONString(response);
    }
}
