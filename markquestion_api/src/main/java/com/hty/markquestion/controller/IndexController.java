package com.hty.markquestion.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.AccessLogMapper;
import com.hty.markquestion.mapper.AccessMessageMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.AccessLog;
import com.hty.markquestion.pojo.AccessMessage;
import com.hty.markquestion.pojo.WebBasicMessage;
import com.hty.markquestion.util.GetClientIp;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//这个Controller主要是为了跳转到首页
@Controller
@RequestMapping("/")
@Slf4j
public class IndexController {
    @Autowired
    WebBasicMessageMapper webBasicMessageMapper;

    @Autowired
    AccessLogMapper accessLogMapper;

    @Autowired
    AccessMessageMapper accessMessageMapper;

    @GetMapping("/")
    @ApiOperation("这个Controller主要是为了跳转到首页(统计网站浏览量 只有访问当前路径才将浏览量+1) 记录访客ip")
    public String toIndex(){
        String ip = GetClientIp.getIp();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String address = GetClientIp.getIpCity(ip);
        if(address == null) address = "";
        //将本次访问记录进数据库中
        AccessLog accessLog = new AccessLog();
        accessLog.setIp(ip);
        accessLog.setWatchTime(new Date());
        accessLog.setAddress(address);
        accessLogMapper.insert(accessLog);

        //将当前访问加入到网站的访问统计中
        QueryWrapper<AccessMessage> qw = new QueryWrapper<>();
        qw.eq("ip",ip);
        AccessMessage accessMessage = accessMessageMapper.selectOne(qw);
        if(accessMessage == null){
            accessMessage = new AccessMessage();
            accessMessage.setIp(ip);
            accessMessage.setAddress(address);
            accessMessage.setNumber(1);
            accessMessageMapper.insert(accessMessage);
        }else{
            accessMessage.setAddress(address);
            accessMessage.setNumber(accessMessage.getNumber()+1);
            accessMessageMapper.updateById(accessMessage);
        }

        //打印日志，显示访问的ip
//        log.info("访问网站的ip---------->"+ GetClientIp.getIp()+"\n"+
//                "访问时间-------------->"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n"+
//                "ip位于国内的地址------->"+ GetClientIp.getIpCity(GetClientIp.getIp()));

        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        webBasicMessage.setWatchNum(webBasicMessage.getWatchNum()+1);
        webBasicMessageMapper.updateById(webBasicMessage);
        return "redirect:/question/index";
    }
}
