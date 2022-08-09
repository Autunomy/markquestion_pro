package com.hty.markquestion.controller.manage;

import com.hty.markquestion.mapper.LogMapper;
import com.hty.markquestion.pojo.Log;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//后台controller
@Controller
@RequestMapping(value = "/manage")
@Slf4j
public class ManageController {

    @Autowired
    LogMapper logMapper;

    //后台首页
    @ApiOperation("后台首页")
    @GetMapping(value = "/index")
    public String index(Model model) {
        //查询所有日志
        List<Log> logs = logMapper.selectList(null);
        model.addAttribute("logs", logs);
        //log.info(logs.toString());
        return "manager/index";
    }

}
