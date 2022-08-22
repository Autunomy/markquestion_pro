package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.UserMapper;
import com.hty.markquestion.pojo.User;
import com.hty.markquestion.pojo.vo.Response;
import com.hty.markquestion.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public String login(String username,String password){
        Response response;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            response = new Response(ResponseMessage.USER_NOT_FOUND,null);
        }else{
            if(user.getPassword().equals(password)){
                String access_token = JwtUtil.genToken(username);
                response = new Response(ResponseMessage.SUCCESS,access_token);
            }else{
                response = new Response(ResponseMessage.LOGIN_FAIL,null);
            }
        }
        return JSON.toJSONString(response);
    }

}
