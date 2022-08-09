package com.hty.markquestion.controller.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.ContestMapper;
import com.hty.markquestion.mapper.PlatformMapper;
import com.hty.markquestion.pojo.Contest;
import com.hty.markquestion.pojo.Platform;
import com.hty.markquestion.util.EmailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Controller
@RequestMapping("/manage/contest")
public class ManageContestController {
    @Autowired
    ContestMapper contestMapper;

    @Autowired
    PlatformMapper platformMapper;

    @Autowired
    EmailUtil emailUtil;//邮件发送工具类


    @GetMapping("/showContest")
    @ApiOperation("跳转到题解管理页面")
    public String showContest(Model model) {
        List<Contest> contests = contestMapper.selectList(null);
        model.addAttribute("contests", contests);

        return "manager/manage/manage_contest_table";
    }

    @GetMapping("/deleteContest")
    @ApiOperation("删除一个比赛日程")
    public String deleteContest(@RequestParam("id") Integer id) {
        int i = contestMapper.deleteById(id);
        return "redirect:/manage/contest/showContest";
    }

    @GetMapping("/toUpdateContest")
    @ApiOperation("跳转到比赛日程修改页面")
    public String updateContest(@RequestParam("id") Integer id, Model model) {
        //添加比赛日程
        QueryWrapper<Contest> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Contest contest = contestMapper.selectOne(wrapper);
        model.addAttribute("contest", contest);

        //日期格式进行修改
        Date contestTime = contest.getContestTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = sdf.format(contestTime);
        String replace = format.replace(' ', 'T');
        model.addAttribute("datetime", replace);

        //添加平台
        QueryWrapper<Platform> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("platform", contest.getPlatform());
        List<Platform> platforms = platformMapper.selectList(queryWrapper);
        model.addAttribute("platforms", platforms);

        //判断是否需要邮件提示
        if(contest.getIsMailTip() == 0){
            model.addAttribute("checked","");
        }else{
            model.addAttribute("checked","checked");
        }
        return "manager/update/update_contest";
    }

    @PostMapping("/updateContest")
    @ApiOperation("修改比赛日程")
    public String updateContest(@RequestParam("id") Integer id,
                                @RequestParam("platform") String platform,
                                @RequestParam("date") String date,
                                @RequestParam("link") String link,
                                @RequestParam("isStartEmail") List<String> isStartEmail) throws ParseException {
        Contest contest = new Contest();
        contest.setId(id);
        contest.setPlatform(platform);
        String replace = date.replace('T', ' ');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parse = sdf.parse(replace);
        contest.setContestTime(parse);
        contest.setLink(link);
        //判断比赛是否过期
        if (parse.getTime() < new Date().getTime()) {
            contest.setIsExpired(1);
        } else {
            contest.setIsExpired(0);
        }
        //判断是否开启邮件提醒功能
        if (isStartEmail.contains("yes")) {
            //设置在比赛的前15分钟发送邮件
//            Timer timer = new Timer();
//            Date taskTime = new Date(contest.getContestTime().getTime() - 15 * 60 * 1000L);
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    String to = "1156388927@qq.com";
//                    String title = contest.getPlatform() + "的比赛就要开始了";
//                    String content = "开始时间:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contest.getContestTime()))
//                            +"     比赛地址:"+contest.getLink();
//                    //发送
//                    emailUtil.sendTextEmail(to, title, content);
//
//                    System.out.println("邮件收到的时间为-------->" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(taskTime));
//                }
//            },taskTime);
            contest.setIsMailTip(1);
        }else{
            contest.setIsMailTip(0);
        }

        int i = contestMapper.updateById(contest);
        return "redirect:/manage/contest/showContest";
    }

    @GetMapping("/toAddContest")
    @ApiOperation("跳转到比赛日程添加页面")
    public String toAddContest(Model model) {
        List<Platform> platforms = platformMapper.selectList(null);
        model.addAttribute("platforms", platforms);

        return "manager/add/add_contest";
    }

    @PostMapping("/addContest")
    @ApiOperation("添加比赛日程")
    public String addContest(@RequestParam("platform") String platform,
                             @RequestParam("date") String date,
                             @RequestParam("link") String link,
                             @RequestParam("isStartEmail") List<String> isStartEmail) throws ParseException {
        Contest contest = new Contest();
        contest.setPlatform(platform);
        contest.setContestTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date.replace('T', ' ')));
        contest.setLink(link);
        if (contest.getContestTime().getTime() < new Date().getTime()) {
            contest.setIsExpired(1);
        } else {
            contest.setIsExpired(0);
        }

        //判断是否开启邮件提醒功能
        if (isStartEmail.contains("yes")) {
            //设置在比赛的前15分钟发送邮件
//            Timer timer = new Timer();
//
//            Date taskTime = new Date(contest.getContestTime().getTime() - 15 * 60 * 1000L);
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    String to = "1156388927@qq.com";
//                    String title = contest.getPlatform() + "的比赛就要开始了";
//                    String content = "开始时间:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contest.getContestTime()))
//                            +"     比赛地址:"+contest.getLink();
//                    //发送
//                    emailUtil.sendTextEmail(to, title, content);
//
//                    System.out.println("邮件收到的时间为-------->" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(taskTime));
//                }
//            },taskTime);
            contest.setIsMailTip(1);
        }

        int insert = contestMapper.insert(contest);

        return "redirect:/manage/contest/showContest";
    }

    @GetMapping("/toAddContestPlatform")
    @ApiOperation("跳转到题目来源添加页面")
    public String toAddQuestionFrom() {
        return "manager/add/add_platform";
    }

    @PostMapping("/addContestPlatform")
    @ApiOperation("添加题目来源")
    public String addQuestionFrom(@RequestParam("platform") String platform) {
        System.out.println(platform + "----------------------");
        QueryWrapper<Platform> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("platform", platform);
        Platform platform1 = platformMapper.selectOne(queryWrapper);
        if (platform1 == null) {
            Platform newPlatform = new Platform();
            newPlatform.setPlatform(platform);
            newPlatform.setCreateTime(new Date());
            int insert = platformMapper.insert(newPlatform);
        }
        return "redirect:/manage/contest/showContest";
    }
}
