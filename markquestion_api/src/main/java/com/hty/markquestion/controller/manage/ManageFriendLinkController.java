package com.hty.markquestion.controller.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.FriendLinkMapper;
import com.hty.markquestion.mapper.FriendLinkTagMapper;
import com.hty.markquestion.pojo.FriendLink;
import com.hty.markquestion.pojo.FriendLinkTag;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/manage/friendLink")
public class ManageFriendLinkController {

    @Autowired
    FriendLinkMapper friendLinkMapper;

    @Autowired
    FriendLinkTagMapper friendLinkTagMapper;

    @GetMapping("/showFriendLink")
    @ApiOperation("跳转到友链管理页面")
    public String showFriendLink(Model model) {
        List<FriendLink> friendLinks = friendLinkMapper.selectList(null);
        model.addAttribute("friendLinks", friendLinks);
        return "manager/manage/manage_link_table";
    }

    @GetMapping("/deleteFriendLink")
    @ApiOperation("删除一个友链")
    public String deleteFriendLink(@RequestParam("id") Integer id) {
        int i = friendLinkMapper.deleteById(id);
        return "redirect:/manage/friendLink/showFriendLink";
    }

    @GetMapping("/toUpdateFriendLink")
    @ApiOperation("跳转到友链修改页面")
    public String toUpdateFriendLink(@RequestParam("id") Integer id, Model model) {
        FriendLink friendLink = friendLinkMapper.selectById(id);
        model.addAttribute("friendLink", friendLink);

        QueryWrapper<FriendLinkTag> friendLinkTagQueryWrapper = new QueryWrapper<>();
        friendLinkTagQueryWrapper.ne("name",friendLink.getTag());
        List<FriendLinkTag> friendLinkTags = friendLinkTagMapper.selectList(friendLinkTagQueryWrapper);
        model.addAttribute("friendLinkTags",friendLinkTags);
        return "manager/update/update_link";
    }

    @PostMapping("/updateFriendLink")
    @ApiOperation("修改友链")
    public String updateFriendLink(@RequestParam("id") Integer id,
                                   @RequestParam("name") String name,
                                   @RequestParam("tag") String tag,
                                   @RequestParam("link") String link,
//                                   @RequestParam("createTime") String createTime,
                                   @RequestParam("description") String description) throws ParseException {
//        System.out.println(createTime+"------------------------------------");
        FriendLink friendLink = new FriendLink();
        friendLink.setId(id);
        friendLink.setLinkName(name);
        friendLink.setTag(tag);
        friendLink.setLink(link);
        friendLink.setDescription(description);
        friendLink.setCreateTime(new Date());
        friendLink.setUpdateTime(new Date());
        friendLinkMapper.updateById(friendLink);

        //判断这个友链标签是否存在
        QueryWrapper<FriendLinkTag> wrapper = new QueryWrapper<>();
        wrapper.eq("name",tag);
        FriendLinkTag friendLinkTag = friendLinkTagMapper.selectOne(wrapper);
        if(friendLinkTag == null){
            FriendLinkTag newTag = new FriendLinkTag();
            newTag.setName(tag);
            newTag.setCreateTime(new Date());
            newTag.setUpdateTime(new Date());
            friendLinkTagMapper.insert(newTag);
        }

        return "redirect:/manage/friendLink/showFriendLink";
    }

    @GetMapping("/toAddFriendLink")
    @ApiOperation("跳转到友链添加页面")
    public String toAddFriendLink(Model model){
        List<FriendLinkTag> friendLinkTags = friendLinkTagMapper.selectList(null);
        model.addAttribute("friendLinkTags",friendLinkTags);
        return "manager/add/add_link";
    }

    @PostMapping("/addFriendLink")
    @ApiOperation("添加一个友链")
    public String addFriendLink(@RequestParam("name") String name,
                                @RequestParam("tag") String tag,
                                @RequestParam("link") String link,
                                @RequestParam("description") String description){
        FriendLink friendLink = new FriendLink();
        friendLink.setLinkName(name);
        friendLink.setTag(tag);
        friendLink.setLink(link);
        friendLink.setDescription(description);
        friendLink.setCreateTime(new Date());
        friendLink.setUpdateTime(new Date());
        int insert = friendLinkMapper.insert(friendLink);
        return "redirect:/manage/friendLink/showFriendLink";
    }

    @GetMapping("/toAddFriendLinkTag")
    @ApiOperation("跳转到友链标签添加页面")
    public String toAddFriendLinkTag(){
        return "manager/add/add_link_tag";
    }

    @PostMapping("/addFriendLinkTag")
    @ApiOperation("添加一个友链标签")
    public String addFriendLinkTag(@RequestParam("tag")String tag){
        QueryWrapper<FriendLinkTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",tag);
        FriendLinkTag friendLinkTag1 = friendLinkTagMapper.selectOne(queryWrapper);
        if(friendLinkTag1 == null){
            FriendLinkTag friendLinkTag = new FriendLinkTag();
            friendLinkTag.setName(tag);
            friendLinkTag.setUpdateTime(new Date());
            friendLinkTag.setCreateTime(new Date());
            friendLinkTagMapper.insert(friendLinkTag);
        }
        return "redirect:/manage/friendLink/showFriendLink";
    }

}
