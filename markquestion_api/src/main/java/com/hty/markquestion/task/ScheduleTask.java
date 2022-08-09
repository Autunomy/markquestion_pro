package com.hty.markquestion.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.ContestMapper;
import com.hty.markquestion.pojo.Contest;
import com.hty.markquestion.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//定时任务
@Configuration
@EnableAsync
@EnableScheduling
public class ScheduleTask {
    @Autowired
    ContestMapper contestMapper;

    @Autowired
    EmailUtil emailUtil;//邮件发送工具类

    //判断所有的未开始比赛是否开始
    @Scheduled(cron = "0/1 * * * * ?")
    public void judgeContestIsExpired() {
        List<Contest> contests = contestMapper.selectList(null);
        //判断每一个比赛是否过期
        Date date = new Date();
        for (Contest contest : contests) {
            if (contest.getContestTime().getTime() < date.getTime() && contest.getIsExpired() == 0) {//已过期
                contest.setIsExpired(1);
                contestMapper.updateById(contest);
            }
        }
    }

    //添加邮件发送的定时任务  提醒比赛
    @Scheduled(cron = "0/1 * * * * ?")
    public void sendMailTipContest() {
        //获取目前未过期的比赛
        QueryWrapper<Contest> qw = new QueryWrapper<>();
        qw.eq("is_expired", 0);
        List<Contest> contests = contestMapper.selectList(qw);
        Date date = new Date();
        for (Contest contest : contests) {
            //判断当前比赛是否需要发邮件
            if(contest.getIsMailTip() == 1){
                //判断是否到了发邮件的时候(开始比赛前15分钟)
                long time = (contest.getContestTime().getTime() - 15 * 60 * 1000L)/1000;
//                System.out.println(time==date.getTime()/1000);
                if(time == date.getTime()/1000){
                    String to = "1156388927@qq.com";
                    String title = contest.getPlatform() + "的比赛就要开始了";
                    String content = "开始时间:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contest.getContestTime()))
                            +"     比赛地址:"+contest.getLink();
                    //发送
                    System.out.println("-----正在发送比赛提醒-----");
                    emailUtil.sendTextEmail(to, title, content);
                    System.out.println("-----发送成功-----");
                }
            }
        }
    }
}
