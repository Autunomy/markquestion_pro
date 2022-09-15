package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.AdviceMapper;
import com.hty.markquestion.mapper.AdviceTagMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.Advice;
import com.hty.markquestion.pojo.AdviceTag;
import com.hty.markquestion.pojo.Question;
import com.hty.markquestion.pojo.WebBasicMessage;
import com.hty.markquestion.pojo.vo.PageInfo;
import com.hty.markquestion.pojo.vo.Response;
import com.hty.markquestion.util.MarkDown2HtmlUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/advice")
public class AdviceController {
    @Autowired
    AdviceMapper adviceMapper;
    @Autowired
    AdviceTagMapper adviceTagMapper;
    @Autowired
    WebBasicMessageMapper webBasicMessageMapper;

    @GetMapping("queryAdviceList")
    @ResponseBody
    public String queryAdviceList(){
        List<Advice> advice = adviceMapper.selectList(null);
        Response response = new Response(ResponseMessage.SUCCESS, advice);
        return JSON.toJSONString(response);
    }

    /***
     * （后台使用） 分页获取推荐信息
     * @param currentPage
     * @param pageSize
     * @return
     */
    @PostMapping("/queryAllAdvice")
    @ResponseBody
    public String queryAllAdvice(String currentPage,String pageSize){
        Page<Advice> page = new Page<>();
        page.setCurrent(Long.parseLong(currentPage));//设置当前页码
        page.setSize(Long.parseLong(pageSize));//设置页面大小

        //当前页面的数据
        List<Advice> adviceList = adviceMapper.selectPage(page, null).getRecords();
        //总数据量
        Integer total = adviceMapper.selectCount(null);

        Response response = new Response(ResponseMessage.SUCCESS, adviceList);

        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage), Integer.valueOf(pageSize), total));
        return JSON.toJSONString(response);
    }

    @GetMapping("/getAdviceTagList")
    @ResponseBody
    public String getAdviceTagList(){
        List<AdviceTag> adviceTagList = adviceTagMapper.selectList(null);
        Response response = new Response(ResponseMessage.SUCCESS,adviceTagList);
        return JSON.toJSONString(response);
    }
}