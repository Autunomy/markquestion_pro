package com.hty.markquestion.controller;

import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.WebBasicMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/webBasicMessage")
public class WebBasicMessageController {
    @Autowired
    WebBasicMessageMapper webBasicMessageMapper;

}
