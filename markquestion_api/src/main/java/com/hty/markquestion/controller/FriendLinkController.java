package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.FriendLinkMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.FriendLink;
import com.hty.markquestion.pojo.WebBasicMessage;
import com.hty.markquestion.pojo.vo.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//友链controller
@Controller
@RequestMapping(value = "/friendLink")
@Slf4j
public class FriendLinkController {
    @Autowired
    FriendLinkMapper friendLinkMapper;

    @Autowired
    WebBasicMessageMapper webBasicMessageMapper;

    @ApiOperation("获取友链列表")
    @GetMapping("/showFriendLink")
    public String showFriendLink(Model model) {
        List<FriendLink> friendLinks = friendLinkMapper.selectList(null);
        model.addAttribute("friendLinks", friendLinks);
//        log.info(friendLinks.toString());

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);
        return "table/link_table";
    }

    //--------------------------------------------------------------------------------------
    @GetMapping("/queryFriendLinkList")
    @ResponseBody
    public String queryFriendLinkList(){
        List<FriendLink> friendLinkList = friendLinkMapper.selectList(null);
        Response response = new Response(ResponseMessage.SUCCESS, friendLinkList);
        return JSON.toJSONString(response);
    }
}
