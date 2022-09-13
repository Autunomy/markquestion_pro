package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.ContestMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.Contest;
import com.hty.markquestion.pojo.WebBasicMessage;
import com.hty.markquestion.pojo.vo.PageInfo;
import com.hty.markquestion.pojo.vo.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


//-----------------------------------------------------------------------------------------
    @GetMapping("/queryContestByDate")
    @ResponseBody
    public String queryContestByDate(@RequestParam("date") String date){
        QueryWrapper<Contest> queryWrapper = new QueryWrapper();
        queryWrapper.like("contest_time",date);
        List<Contest> contestList = contestMapper.selectList(queryWrapper);
        Response response = new Response(ResponseMessage.SUCCESS, contestList);
        return JSON.toJSONString(response);
    }

    @PostMapping("/queryContestPage")
    @ResponseBody
    public String queryContestPage(String currentPage,String pageSize){
        Page<Contest> page = new Page<>();
        page.setCurrent(Long.parseLong(currentPage));
        page.setSize(Long.parseLong(pageSize));

        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("contest_time");

        List<Contest> contestList = contestMapper.selectPage(page, queryWrapper).getRecords();

        Integer total = contestMapper.selectCount(null);

        Response response = new Response(ResponseMessage.SUCCESS,contestList);
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage),Integer.valueOf(pageSize),total));
        return JSON.toJSONString(response);
    }

    @GetMapping("/deleteContestById")
    @ResponseBody
    public String deleteContestById(Integer id){
        int rows = contestMapper.deleteById(id);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS,null);
        }else{
            response = new Response(ResponseMessage.ERROR,null);
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("/addContest")
    @ResponseBody
    public String addContest(String contestName,String contestTime,String platform,String link) throws ParseException {
        Contest contest = new Contest();
        contest.setContestName(contestName);
        contest.setContestTime(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(contestTime).getTime()));
        contest.setPlatform(platform);
        contest.setLink(link);

        int rows = contestMapper.insert(contest);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS,null);
        }else{
            response = new Response(ResponseMessage.ERROR,null);
        }

        return JSON.toJSONString(response);
    }

    @PostMapping("/updateContest")
    @ResponseBody
    public String updateContest(String id,String platform,String link,String contestTime,String contestName) throws ParseException {
        Contest contest = contestMapper.selectById(Integer.valueOf(id));
        contest.setPlatform(platform);
        contest.setLink(link);
        contest.setContestTime(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(contestTime).getTime()));
        if(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(contestTime).getTime()).getTime() >= System.currentTimeMillis()){
            contest.setIsExpired(0);
        }else{
            contest.setIsExpired(1);
        }
        contest.setContestName(contestName);
        int rows = contestMapper.updateById(contest);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }
}
