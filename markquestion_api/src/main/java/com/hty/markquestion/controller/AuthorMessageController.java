package com.hty.markquestion.controller;

import com.hty.markquestion.mapper.AuthorMessageMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.AuthorMessage;
import com.hty.markquestion.pojo.WebBasicMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/author")
@Controller
@Slf4j
public class AuthorMessageController {
    @Autowired
    AuthorMessageMapper authorMessageMapper;

    @Autowired
    WebBasicMessageMapper webBasicMessageMapper;

    @ApiOperation("展示作者信息页面")
    @GetMapping("/showAuthorMessage")
    public String showAuthorMessage(Model model){
        AuthorMessage authorMessage = authorMessageMapper.selectOne(null);
        model.addAttribute("authorMessage", authorMessage);
        if(authorMessage.getGender() == 0){
            model.addAttribute("gender","女");
        }else{
            model.addAttribute("gender","男");
        }
//        log.info(authorMessage.toString());

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);
        return "author/author_message";
    }

}
