package com.hty.markquestion.controller.manage;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hty.markquestion.mapper.AccessLogMapper;
import com.hty.markquestion.mapper.AccessMessageMapper;
import com.hty.markquestion.pojo.AccessLog;
import com.hty.markquestion.pojo.AccessMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//管理访客
@RequestMapping("/manage/access")
@Slf4j
@Controller
public class ManageAccessController {
    @Autowired
    AccessMessageMapper accessMessageMapper;

    @Autowired
    AccessLogMapper accessLogMapper;

    @Autowired
    @ApiOperation("跳转到访客管理页面")
    @GetMapping("/showAccess")
    public String showAccess(){
        return "manager/manage/manage_access_user";
    }

    @ApiOperation("显示最近100次访问记录(AJAX请求)")
    @GetMapping("/getHundredAccess")
    @ResponseBody
    public String getHundredAccess() throws UnsupportedEncodingException {
        List<AccessLog> res = accessLogMapper.selectList(null);
        List<AccessLog> accessLogs = new ArrayList<>();
        int size = res.size();
        if(size > 100){
            int i=1;
            while(i <= 100){
                if(size-i < 0 || size-i >= size || res.get(size-i) == null) break;
                accessLogs.add(res.get(size-i));
                i++;
            }
        }else{
            accessLogs = res;
        }
        return JSON.toJSONString(accessLogs);
    }

    @ApiOperation("显示所有访客(AJAX请求)")
    @GetMapping("/getAllAccess")
    @ResponseBody
    public String getAllAccess() {
        List<AccessMessage> accessMessages = accessMessageMapper.selectList(null);
        return JSON.toJSONString(accessMessages);
    }
}
