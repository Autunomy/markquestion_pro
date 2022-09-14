package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.FriendLinkMapper;
import com.hty.markquestion.mapper.FriendLinkTagMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.*;
import com.hty.markquestion.pojo.vo.PageInfo;
import com.hty.markquestion.pojo.vo.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

//友链controller
@Controller
@RequestMapping(value = "/friendLink")
@Slf4j
public class FriendLinkController {
    @Autowired
    FriendLinkMapper friendLinkMapper;

    @Autowired
    FriendLinkTagMapper friendLinkTagMapper;

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

    /***
     * 该方法是分页版本的查询
     * @param currentPage
     * @param pageSize
     * @return
     */
    @PostMapping("/queryFriendLinkList")
    @ResponseBody
    public String queryFriendLinkList(String currentPage,String pageSize){
        Page<FriendLink> page = new Page<>();
        page.setCurrent(Long.parseLong(currentPage));
        page.setSize(Long.parseLong(pageSize));

        QueryWrapper<FriendLink> queryWrapper = new QueryWrapper<>();

        List<FriendLink> friendLinkList = friendLinkMapper.selectPage(page, queryWrapper).getRecords();

        Integer total = friendLinkMapper.selectCount(null);

        Response response = new Response(ResponseMessage.SUCCESS,friendLinkList);
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage),Integer.valueOf(pageSize),total));
        return JSON.toJSONString(response);
    }

    /***
     * 该方法是不分页版本的查询
     * @return
     */
    @GetMapping("/getFriendLink")
    @ResponseBody
    public String getFriendLink(){
        List<FriendLink> friendLinkList = friendLinkMapper.selectList(null);
        Response response = new Response(ResponseMessage.SUCCESS,friendLinkList);
        return JSON.toJSONString(response);
    }

    /***
     * 获取全部的友链标签
     * @return
     */
    @GetMapping("/queryFriendLinkTag")
    @ResponseBody
    public String queryFriendLinkTag(){
        List<FriendLinkTag> friendLinkList = friendLinkTagMapper.selectList(null);
        Response response = new Response(ResponseMessage.SUCCESS,friendLinkList);
        return JSON.toJSONString(response);
    }

    /***
     * 添加一个友链
     * @param linkName
     * @param tag
     * @param link
     * @param introduce
     * @param description
     * @return
     */
    @PostMapping("/addFriendLink")
    @ResponseBody
    public String addFriendLink(String linkName,String tag,String link,String introduce,String description){
        Response response = null;
        FriendLink friendLink = new FriendLink();
        friendLink.setLinkName(linkName);
        friendLink.setLink(link);
        friendLink.setTag(tag);
        friendLink.setIntroduce(introduce);
        friendLink.setDescription(description);
        friendLink.setCreateTime(new Date());
        friendLink.setUpdateTime(new Date());
        int rows = friendLinkMapper.insert(friendLink);
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }

    /***
     * 添加一个友链标签
     * @param name
     * @return
     */
    @PostMapping("/addFriendLinkTag")
    @ResponseBody
    public String addFriendLinkTag(String name){
        Response response = null;
        FriendLinkTag friendLinkTag = new FriendLinkTag();
        friendLinkTag.setName(name);
        friendLinkTag.setCreateTime(new Date());
        friendLinkTag.setUpdateTime(new Date());
        int rows = friendLinkTagMapper.insert(friendLinkTag);
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }


    @PostMapping("/searchFriendLinkByName")
    @ResponseBody
    public String searchFriendLinkByName(String searchCondition,String currentPage,String pageSize){
        List<FriendLink> friendLinkList = friendLinkMapper.searchFriendLink(searchCondition, (Integer.parseInt(currentPage) - 1) * Integer.parseInt(pageSize), Integer.valueOf(pageSize));
        //获取搜索出来的条数 用来分页
        Integer total = friendLinkMapper.searchCount(searchCondition);
        Response response = new Response(ResponseMessage.SUCCESS, friendLinkList);
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage), Integer.valueOf(pageSize), total));
        return JSON.toJSONString(response);
    }

    @GetMapping("/deleteFriendLinkById")
    @ResponseBody
    public String deleteFriendLinkById(String id){
        int rows = friendLinkMapper.deleteById(Integer.valueOf(id));
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("/updateFriendLink")
    @ResponseBody
    public String updateFriendLink(String id,String linkName,String link,String tag,String introduce,String description){
        FriendLink friendLink = friendLinkMapper.selectById(Integer.valueOf(id));
        friendLink.setLinkName(linkName);
        friendLink.setLink(link);
        friendLink.setTag(tag);
        friendLink.setIntroduce(introduce);
        friendLink.setDescription(description);
        int rows = friendLinkMapper.updateById(friendLink);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }

    @GetMapping("/queryAllFriendLinkTag")
    @ResponseBody
    public String queryAllFriendLinkTag(){
        List<FriendLinkTag> friendLinkTagList = friendLinkTagMapper.selectList(null);
        Response response = new Response(ResponseMessage.SUCCESS, friendLinkTagList);
        return JSON.toJSONString(response);
    }

    @GetMapping("/deleteFriendLinkTag")
    @ResponseBody
    public String deleteFriendLinkTag(String id){
        int rows = friendLinkTagMapper.deleteById(Integer.valueOf(id));
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }
}
