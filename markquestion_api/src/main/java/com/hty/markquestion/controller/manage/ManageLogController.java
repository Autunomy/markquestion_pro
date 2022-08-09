package com.hty.markquestion.controller.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.LogMapper;
import com.hty.markquestion.pojo.Log;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@Slf4j
@RequestMapping("/manage/log")
public class ManageLogController {
    @Autowired
    LogMapper logMapper;

    @GetMapping("/toAddLog")
    @ApiOperation("跳转到添加日志页面")
    public String toAddLog() {
        return "manager/add/add_log";
    }

    @PostMapping("/addLog")
    @ApiOperation("添加日志并返回首页")
    public String addLog(@RequestParam("logMsg") String logMsg, Model model) {
        Log log1 = new Log();
        log1.setLog(logMsg);
        log1.setLogDate(new Date());
        int update = logMapper.insert(log1);
        if (update == 1) {
            log.info("添加日志成功");
        } else {
            log.error("添加日志失败");
        }
        return "redirect:/manage/index";
    }


    @GetMapping("/deleteLog")
    @ApiOperation("删除日志并返回首页")
    public String deleteLog(@RequestParam("id") Integer id) {
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        int delete = logMapper.delete(wrapper);
        if (delete > 0) {
            log.info("删除日志成功");
        } else {
            log.error("删除日志失败");
        }
        return "redirect:/manage/index";
    }

    @GetMapping("/toUpdateLog")
    @ApiOperation("跳转到日志更新页面")
    public String toUpdateLog(@RequestParam("id") Integer id, Model model) {
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Log res = logMapper.selectOne(wrapper);
        model.addAttribute("log", res);
        log.info(res.toString());
        return "manager/update/update_log";
    }

    @PostMapping("/updateLog")
    @ApiOperation("更新日志")
    public String updateLog(@RequestParam("logMsg") String logMsg, @RequestParam("id") Integer id, Model model) {
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        Log res = new Log();
        res.setLog(logMsg);
        int update = logMapper.update(res, wrapper);
        if(update > 0){
            log.info("修改成功");
        }else{
            log.info("修改失败");
        }
        return "redirect:/manage/index";
    }
}
