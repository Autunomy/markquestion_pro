package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.ContestMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.Contest;
import com.hty.markquestion.pojo.WebBasicMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

//比赛日程表controller
@Controller
@RequestMapping(value = "/contest")
@Slf4j
public class ContestController {
    @Autowired
    ContestMapper contestMapper;

    @Autowired
    WebBasicMessageMapper webBasicMessageMapper;

    @ApiOperation("获取比赛日程表")
    @GetMapping(value = "/showContest")
    public String showContest(Model model){
        QueryWrapper<Contest> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.orderByDesc("contest_time");
        List<Contest> contests = contestMapper.selectList(queryWrapper1);
        //判断每个比赛是否过期
        Date date = new Date();
        for (Contest contest : contests) {
            if(contest.getContestTime().getTime() <= date.getTime()){
                contest.setIsExpired(1);
                QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id",contest.getId());
                contestMapper.update(contest,queryWrapper);
            }
        }
        model.addAttribute("contests",contests);
//        log.info(contests.toString());

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);
        return "table/contest_table";
    }

    @ApiOperation("获取全部比赛日程(AJAX异步请求)")
    @GetMapping("/getAllContest")
    @ResponseBody
    public String getAllContest(){
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("contest_time");
        List<Contest> contests = contestMapper.selectList(queryWrapper);
        return JSON.toJSONString(contests);
    }

    @ApiOperation("获取未开始比赛日程(AJAX异步请求)")
    @GetMapping("/getNotBeginContest")
    @ResponseBody
    public String getNotBeginContest(){
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_expired",0);
        List<Contest> contests = contestMapper.selectList(queryWrapper);
        return JSON.toJSONString(contests);
    }

    @ApiOperation("获取已过期比赛日程(AJAX异步请求)")
    @GetMapping("/getHaveBeginContest")
    @ResponseBody
    public String getHaveBeginContest(){
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_expired",1);
        List<Contest> contests = contestMapper.selectList(queryWrapper);
        return JSON.toJSONString(contests);
    }

}
