package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.*;
import com.hty.markquestion.mapper.QuestionMapper;
import com.hty.markquestion.pojo.*;
import com.hty.markquestion.pojo.vo.PageInfo;
import com.hty.markquestion.pojo.vo.Response;
import com.hty.markquestion.util.MarkDown2HtmlUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//题解controller
@Slf4j
@Controller
@RequestMapping(value = "/question")
public class QuestionController {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionTagMapper questionTagMapper;

    @Autowired
    ContestMapper contestMapper;

    @Autowired
    PlatformMapper platformMapper;

    @Autowired
    AuthorMessageMapper authorMessageMapper;

    @Autowired
    WebBasicMessageMapper webBasicMessageMapper;

    /***
     * 分页查询题解
     * @param currentPage 当前页码
     * @param pageSize 页面大小
     * @return
     */
    @GetMapping("/queryQuestionPage")
    @ResponseBody
    public String queryQuestionPage(@RequestParam("currentPage") String currentPage,
                                    @RequestParam("pageSize") String pageSize) {
        Page<Question> page = new Page<>();
        page.setCurrent(Long.parseLong(currentPage));//设置当前页码
        page.setSize(Long.parseLong(pageSize));//设置页面大小

        //当前页面的数据
        List<Question> questionList = questionMapper.selectPage(page, null).getRecords();
        //总数据量
        Integer total = questionMapper.selectCount(null);

        Response response = new Response(ResponseMessage.SUCCESS, questionList);

        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage), Integer.valueOf(pageSize), total));
        return JSON.toJSONString(response);
    }

    /***
     * 按照题解id查询
     * @param qid 题解id
     * @return
     */
    @GetMapping("/queryQuestionById")
    @ResponseBody
    public String queryQuestionById(@RequestParam("qid") String qid) {
        Question question = questionMapper.selectById(Integer.valueOf(qid));
        Response response = new Response(ResponseMessage.SUCCESS, question);
        return JSON.toJSONString(response);
    }

    /***
     * 按照作者分页查询题解
     * @param author 作者
     * @param currentPage 当前页码
     * @param pageSize 分页大小
     * @return
     */
    @PostMapping("/queryQuestionPageByAuthor")
    @ResponseBody
    public String queryQuestionByAuthor(@RequestParam("author") String author,
                                        @RequestParam("currentPage") String currentPage,
                                        @RequestParam("pageSize") String pageSize) {
        //设置查询条件
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author", author);
        //设置分页数据
        Page<Question> page = new Page<>();
        page.setCurrent(Long.parseLong(currentPage));//设置当前页码
        page.setSize(Long.parseLong(pageSize));//设置页面大小
        //查询
        List<Question> questionList = questionMapper.selectPage(page, queryWrapper).getRecords();
        //获取当前作者的题解数量
        Integer total = questionMapper.selectCount(queryWrapper);
        //封装
        Response response = new Response(ResponseMessage.SUCCESS, questionList);
        //设置分页信息
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage), Integer.valueOf(pageSize), total));
        return JSON.toJSONString(response);
    }


    /***
     * 将题解浏览量+1
     * @param id
     * @return
     */
    @GetMapping("/addWatch")
    @ResponseBody
    public String addWatch(@RequestParam("id") String id) {
        Question question = questionMapper.selectById(Integer.valueOf(id));
        question.setWatch(question.getWatch() + 1);
        questionMapper.updateById(question);
        Response response = new Response(ResponseMessage.SUCCESS);
        return JSON.toJSONString(response);
    }

    /***
     * 搜索题解 按照 id  题目名称 题目来源  作者查询
     * @param search
     * @return
     */
    @GetMapping("/searchQuestion")
    @ResponseBody
    public String searchQuestion(@RequestParam("search") String search,
                                 @RequestParam("currentPage") String currentPage,
                                 @RequestParam("pageSize") String pageSize) {
        List<Question> questionList = questionMapper.searchQuestion(search, (Integer.parseInt(currentPage) - 1) * Integer.parseInt(pageSize), Integer.valueOf(pageSize));
        //获取搜索出来的条数 用来分页
        Integer total = questionMapper.searchCount(search);
        Response response = new Response(ResponseMessage.SUCCESS, questionList);
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage), Integer.valueOf(pageSize), total));
        return JSON.toJSONString(response);
    }

    //图片上传
    @PostMapping("/uploadPic")
    public String uploadPic(@RequestParam("image") MultipartFile image) throws IOException {
        String filename = "";
        if (!image.isEmpty()) {
            //获取文件名
            String imageName = image.getOriginalFilename();
            //获取后缀
            String last = imageName.substring(imageName.lastIndexOf('.'));
            //生成文件名
            filename = UUID.randomUUID().toString().replace('-', 'x');
            filename += last;
            image.transferTo(new File("E:\\images\\blog\\" + filename));
        }
        return "http://localhost:8080/images/blog/" + filename;
    }

    //删除图片
    @GetMapping("/delPic")
    public String delPic(String path) {
        path = path.substring(path.lastIndexOf("/") + 1);
        Response response = null;
        boolean isSuccess = FileSystemUtils.deleteRecursively(new File("E:/images/blog/" + path));
        if (isSuccess) {
            response = new Response(ResponseMessage.SUCCESS, null);
        } else {
            response = new Response(ResponseMessage.LOGIN_FAIL, null);
        }
        return JSON.toJSONString(response);
    }


    /***
     * 获取全部的题解来源
     * @return
     */
    @GetMapping("/getQuestionFrom")
    @ResponseBody
    public String getQuestionFrom() {
        List<Platform> platforms = platformMapper.selectList(null);
        Response response = new Response(ResponseMessage.SUCCESS, platforms);
        return JSON.toJSONString(response);
    }

    /***
     * 添加题解
     * @param questionName
     * @param solution
     * @param questionFrom
     * @param author
     * @param tag
     * @param level
     * @param link
     * @return
     */
    @PostMapping("/addQuestion")
    @ResponseBody
    public String addBlog(String questionName,
                          String solution,
                          String questionFrom,
                          String author,
                          String tag,
                          String level,
                          String link) {
        Question question = new Question();
        question.setQuestionName(questionName);
        question.setSolution(solution);
        question.setQuestionFrom(questionFrom);
        question.setAuthor(author);
        question.setTag(tag);
        question.setLevel(level);
        question.setLink(link);
        question.setWatch(0);
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        Response response = null;

        int rows = questionMapper.insert(question);
        if (rows == 1) {
            response = new Response(ResponseMessage.SUCCESS, null);
        } else {
            response = new Response(ResponseMessage.ERROR, null);
        }
        return JSON.toJSONString(response);
    }

    /***
     * 修改题解
     * @param id
     * @param questionName
     * @param solution
     * @param questionFrom
     * @param author
     * @param tag
     * @param level
     * @param link
     * @return
     * @throws ParseException
     */
    @PostMapping("/updateQuestion")
    @ResponseBody
    public String updateQuestion(Integer id,
                                 String questionName,
                                 String solution,
                                 String questionFrom,
                                 String author,
                                 String tag,
                                 String level,
                                 String link) throws ParseException {

        Question question = questionMapper.selectById(id);
        question.setSolution(solution);
        question.setQuestionFrom(questionFrom);
        question.setAuthor(author);
        question.setQuestionName(questionName);
        question.setTag(tag);
        question.setUpdateTime(new Date());
        question.setLevel(level);
        question.setLink(link);
        int rows = questionMapper.updateById(question);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS,null);
        }else{
            response = new Response(ResponseMessage.ERROR,null);
        }

        return JSON.toJSONString(response);
    }

    @RequestMapping("/deleteQuestionById")
    @ResponseBody
    public String deleteQuestionById(Integer id){
        int rows = questionMapper.deleteById(id);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS,null);
        }else{
            response = new Response(ResponseMessage.ERROR,null);
        }
        return JSON.toJSONString(response);
    }
}
