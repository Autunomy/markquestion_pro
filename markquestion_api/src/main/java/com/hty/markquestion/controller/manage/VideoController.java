package com.hty.markquestion.controller.manage;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//视频controller
@Controller
@RequestMapping(value = "/manage/video")
public class VideoController {

    @RequestMapping("/showAllVideos")
    @ApiOperation("视频首页")
    public String showAllVideos(Model model){
        return "manager/video/show_videos";
    }
}
