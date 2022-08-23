package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.MessageBoardMapper;
import com.hty.markquestion.pojo.MessageBoard;
import com.hty.markquestion.pojo.vo.PageInfo;
import com.hty.markquestion.pojo.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/messageBoard")
public class MessageBoardController {

    @Autowired
    private MessageBoardMapper messageBoardMapper;


    //保存留言
    @PostMapping("/saveMessage")
    public String saveMessage(@RequestParam("message") String message){
        MessageBoard messageBoard = new MessageBoard();
        messageBoard.setMessage(message);
        messageBoard.setCreateDate(new Date());
        messageBoardMapper.insert(messageBoard);
        Response response = new Response(ResponseMessage.SUCCESS);
        return JSON.toJSONString(response);
    }

    //分页获取留言，第一页是最近的留言
    @PostMapping("/queryMessageBoard")
    public String queryMessageBoard(@RequestParam("currentPage") String currentPage,
                                    @RequestParam("pageSize") String pageSize){
        QueryWrapper<MessageBoard> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        Page<MessageBoard> page = new Page<>();
        page.setCurrent(Long.parseLong(currentPage));
        page.setSize(Long.parseLong(pageSize));
        //查询
        List<MessageBoard> messageList = messageBoardMapper.selectPage(page, queryWrapper).getRecords();
        Integer total = messageBoardMapper.selectCount(null);
        Response response = new Response(ResponseMessage.SUCCESS, messageList);
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage),Integer.valueOf(pageSize),total));
        return JSON.toJSONString(response);
    }

    @GetMapping("/deleteMessage")
    public String deleteMessage(Integer id){
        int rows = messageBoardMapper.deleteById(id);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS,null);
        }else{
            response = new Response(ResponseMessage.ERROR,null);
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("/searchMessage")
    public String searchMessage(@RequestParam("search") String search,
                                @RequestParam("currentPage") String currentPage,
                                @RequestParam("pageSize") String pageSize){
        QueryWrapper<MessageBoard> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("message",search);
        queryWrapper.orderByDesc("create_date");
        Page<MessageBoard> page = new Page<>();
        page.setCurrent(Long.parseLong(currentPage));
        page.setSize(Long.parseLong(pageSize));
        //查询
        List<MessageBoard> messageList = messageBoardMapper.selectPage(page, queryWrapper).getRecords();
        Integer total = messageBoardMapper.selectCount(queryWrapper);
        Response response = new Response(ResponseMessage.SUCCESS, messageList);
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage),Integer.valueOf(pageSize),total));
        return JSON.toJSONString(response);
    }

}
