package com.hty.markquestion.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.AdviceMapper;
import com.hty.markquestion.mapper.AdviceTagMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.Advice;
import com.hty.markquestion.pojo.AdviceTag;
import com.hty.markquestion.pojo.WebBasicMessage;
import com.hty.markquestion.util.MarkDown2HtmlUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @ApiOperation("跳转到推荐页面")
    @GetMapping("/toAdvicePage")
    public String toAcivcePage(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
                               Model model) {
//        System.out.println("controller-----------------");
        List<Advice> select = adviceMapper.selectList(null);
        List<Advice> advices = null;

        if (pageNum * pageSize > select.size() - 1) {
            advices = select.subList((pageNum - 1) * pageSize, select.size());
        } else {
            advices = select.subList((pageNum - 1) * pageSize, pageNum * pageSize);
        }
        //分页出来的内容
        model.addAttribute("advices", advices);
        //当前页数
        model.addAttribute("pageNum", pageNum);
        //一页的大小
        model.addAttribute("pageSize", pageSize);
        //总页数数(用于判断是否可以翻下一页)
        model.addAttribute("adviceSize", select.size() / pageSize + (select.size() % pageSize == 0 ? 0 : 1));

        //查询所有分类
        List<AdviceTag> adviceTags = adviceTagMapper.selectList(null);
        model.addAttribute("adviceTags", adviceTags);

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "advice/advice_main";
    }

    @ApiOperation("分页-跳转上一页")
    @GetMapping("/toLastAdvicePage")
    public String toLastAdvicePage(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
                                   Model model) {
        //页数减一
        if (pageNum > 1) pageNum--;
        List<Advice> select = adviceMapper.selectList(null);
        List<Advice> advices = null;
        if (pageNum * pageSize > select.size() - 1) {
            advices = select.subList((pageNum - 1) * pageSize, select.size());
        } else {
            advices = select.subList((pageNum - 1) * pageSize, pageNum * pageSize);
        }
        //分页出来的内容
        model.addAttribute("advices", advices);
        //当前页数
        model.addAttribute("pageNum", pageNum);
        //一页的大小
        model.addAttribute("pageSize", pageSize);
        //总页数数(用于判断是否可以翻下一页)
        model.addAttribute("adviceSize", select.size() / pageSize + (select.size() % pageSize == 0 ? 0 : 1));

        //查询所有分类
        List<AdviceTag> adviceTags = adviceTagMapper.selectList(null);
        model.addAttribute("adviceTags", adviceTags);

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "advice/advice_main";
    }

    @ApiOperation("分页-跳转下一页")
    @GetMapping("/toNextAdvicePage")
    public String toNextAdvicePage(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
                                   Model model) {

        List<Advice> select = adviceMapper.selectList(null);
        //页数+1
        if (pageNum < select.size() / pageSize + (select.size() % pageSize == 0 ? 0 : 1)) pageNum++;
        List<Advice> advices = null;
        if (pageNum * pageSize > select.size() - 1) {
            advices = select.subList((pageNum - 1) * pageSize, select.size());
        } else {
            advices = select.subList((pageNum - 1) * pageSize, pageNum * pageSize);
        }

        //分页出来的内容
        model.addAttribute("advices", advices);
        //当前页数
        model.addAttribute("pageNum", pageNum);
        //一页的大小
        model.addAttribute("pageSize", pageSize);
        //总页数数(用于判断是否可以翻下一页)
        model.addAttribute("adviceSize", select.size() / pageSize + (select.size() % pageSize == 0 ? 0 : 1));


        //查询所有分类
        List<AdviceTag> adviceTags = adviceTagMapper.selectList(null);
        model.addAttribute("adviceTags", adviceTags);

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "advice/advice_main";
    }

    @ApiOperation("展示推荐的详细信息")
    @GetMapping("/showAdvice")
    public String showAdvice(Model model,
                             @RequestParam("id") Integer id) {
        QueryWrapper<Advice> qw = new QueryWrapper<>();
        qw.eq("id", id);
        Advice advice = adviceMapper.selectOne(qw);
        model.addAttribute("advice", advice);
        String content = MarkDown2HtmlUtils.markdown2Html(advice.getAdviceContent());
        model.addAttribute("content", content);

        //增加浏览数量
        advice.setAdviceWatch(advice.getAdviceWatch() + 1);
        adviceMapper.updateById(advice);

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "advice/advice_show";
    }

    @ApiOperation("搜索框查询接口，可以根据名称和标签查询")
    @GetMapping("/search")
    public String search(@RequestParam("search") String search,Model model) {
        //先根据名称查询
        QueryWrapper<Advice> qw1 = new QueryWrapper<>();
        qw1.like("advice_name",search);
        List<Advice> res1 = adviceMapper.selectList(qw1);
        //根据标签查询
        QueryWrapper<Advice> qw2 = new QueryWrapper<>();
        qw2.like("advice_tag",search);
        List<Advice> res2 = adviceMapper.selectList(qw2);

        //合并两个集合并去重
        List<Advice> advices = res1;
        for (Advice res : res2) {
            if(!advices.contains(res)) advices.add(res);
        }
        model.addAttribute("advices",advices);
        model.addAttribute("search",search);

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "advice/show_advice_search";
    }
}