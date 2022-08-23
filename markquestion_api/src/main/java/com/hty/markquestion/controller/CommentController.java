package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.CommentMapper;
import com.hty.markquestion.mapper.CommentReplyMapper;
import com.hty.markquestion.pojo.Comment;
import com.hty.markquestion.pojo.CommentReply;
import com.hty.markquestion.pojo.Question;
import com.hty.markquestion.pojo.vo.PageInfo;
import com.hty.markquestion.pojo.vo.Response;
import com.hty.markquestion.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentReplyService commentReplyService;


    /***
     * 根据题解id获取当前题解下的所有非回复评论  按照日期由近到远
     * @return
     */
    @GetMapping("/queryAllCommentByQuestionId")
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
     * 获取某个评论的回复评论 使用的是新的实体类封装当前评论的回复评论，获取回复评论的时候
     * 需要使用到递归，来不断获取回复评论的回复评论
     * @param id
     * @return
     */
    @GetMapping("/queryReplyCommentById")
    public String queryReplyCommentById(@RequestParam("id") Integer id){
        List<CommentReply> commentReplyList = commentReplyService.queryCommentReply(id);
        Response response = new Response(ResponseMessage.SUCCESS, commentReplyList);
        return JSON.toJSONString(response);
    }

    /***
     * 根据题解id分页获取其全部评论
     * @param id
     * @return
     */
    @PostMapping("/queryAllCommentById")
    public String queryAllCommentById(Integer id,String currentPage,String pageSize){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_id",id);

        //设置分页数据
        Page<Comment> page = new Page<>();
        page.setCurrent(Long.parseLong(currentPage));//设置当前页码
        page.setSize(Long.parseLong(pageSize));//设置页面大小

        List<Comment> commentList = commentMapper.selectPage(page,queryWrapper).getRecords();

        //获取评论数量
        Integer total = commentMapper.selectCount(queryWrapper);

        Response response = new Response(ResponseMessage.SUCCESS, commentList);
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage), Integer.valueOf(pageSize), total));

        return JSON.toJSONString(response);
    }

    @GetMapping("/deleteCommentById")
    public String deleteCommentById(Integer id){
        int rows = commentMapper.deleteById(id);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS,null);
        }else{
            response = new Response(ResponseMessage.ERROR,null);
        }
        return JSON.toJSONString(response);
    }
}
