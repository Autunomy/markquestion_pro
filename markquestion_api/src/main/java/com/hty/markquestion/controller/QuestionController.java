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

    //跳转到前台首页
    @ApiOperation("跳转到前台首页")
    @GetMapping("/index")
    public String index(@ApiParam("视图") Model model) {
        //添加所有的题解
        List<Question> res = questionMapper.selectList(null);

        //将题解总数添加到视图中
        model.addAttribute("questionNum", res.size());

        //由于要做分页，一页显示15个，所以首次查询只显示前15个
        List<Question> questions = res.subList(0, 15);
        model.addAttribute("questions", questions);
//        log.info(questions.toString());


        //添加比赛日程
        List<Contest> result = contestMapper.selectList(null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Contest contest : result) {
            sdf.format(contest.getContestTime());
        }
        List<Contest> contests = new ArrayList<>();
        //只有未过期的比赛才显示 0表示未过期
        for (Contest contest : result) {
            if (contest.getIsExpired() == 0) {
                contests.add(contest);
            }
        }
        model.addAttribute("contests", contests);
//        log.info(contests.toString());

        //添加所有的平台(题目来源)
        List<Platform> platforms = platformMapper.selectList(null);
        model.addAttribute("platforms", platforms);
//        log.info(platforms.toString());

        //添加所有的题解标签
        List<QuestionTag> questionTags = questionTagMapper.selectList(null);
        model.addAttribute("questionTags", questionTags);
//        log.info(questionTags.toString());

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage", webBasicMessage);

        return "index";
    }


    @ApiOperation("题解详情页面")
    @GetMapping("/showQuestion")
    public String showQuestion(@ApiParam("题解id") @RequestParam("id") Integer id, Model model) {
        //将题解信息插入进去
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Question question = questionMapper.selectOne(wrapper);

        //当前文章的访问量+1
        question.setWatch(question.getWatch() + 1);
        //需要更新一下数据库
        questionMapper.updateById(question);

        //对正文的markdown格式进行转换
        question.setSolution(MarkDown2HtmlUtils.parse(question.getSolution()));
        model.addAttribute("question", question);
//        log.info(question.toString());

        //搜索出作者信息
        QueryWrapper<AuthorMessage> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("username", question.getAuthor());
        AuthorMessage authorMessage = authorMessageMapper.selectOne(wrapper2);
//        log.info(authorMessage.toString());
        model.addAttribute("authorMessage", authorMessage);

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage", webBasicMessage);

        return "problems/show_problems";
    }

    @ApiOperation("获取全部题目(AJAX请求)")
    @GetMapping("/getAllQuestion")
    @ResponseBody
    public String getAllQuestion() {
        List<Question> questions = questionMapper.selectList(null);
        //分页需求
//        List<Question> res = questions.subList(0, 15);
        return JSON.toJSONString(questions);
    }

    @ApiOperation("分页获取所有题目(AJAX请求)")
    @GetMapping("/getPageAllQuestion")
    @ResponseBody
    public String getPageAllQuestion(@RequestParam("page") String page) {
        //查出所有题目
        List<Question> res = questionMapper.selectList(null);
        //将页数转换为整型
        Integer num = Integer.valueOf(page);
        //获取子集合的结尾
        int end = Math.min(res.size(), (num - 1) * 15 + 15);
        //获取当前页的题解
        List<Question> questions = res.subList((num - 1) * 15, end);
        return JSON.toJSONString(questions);
    }

    @ApiOperation("根据题目来源查询题目(AJAX请求)")
    @GetMapping("/getQuestionByPlatform")
    @ResponseBody
    public String getQuestionByPlatform(@RequestParam("platform") String platform) throws UnsupportedEncodingException {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_from", platform);
        List<Question> questions = questionMapper.selectList(queryWrapper);

        String questionJson = JSON.toJSONString(questions);
        return questionJson;
    }


    @ApiOperation("根据题目来源查询题目(AJAX请求)")
    @GetMapping("/getQuestionByTag")
    @ResponseBody
    public String getQuestionByTag(@RequestParam("tag") String tag) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag", tag);
        List<Question> questions = questionMapper.selectList(queryWrapper);
//        System.out.println(questions.toString());
        return JSON.toJSONString(questions);
    }

    @ApiOperation("根据搜索框查询题目(AJAX请求)")
    @GetMapping(value = "/getQuestionBySearch")
    @ResponseBody
    public String getQuestionBySearch(@RequestParam("search") String search) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        //模糊查询
        queryWrapper.like("question_name", search);
        List<Question> questions = questionMapper.selectList(queryWrapper);
//        System.out.println(questions.toString());
        return JSON.toJSONString(questions);
    }


//------------------------------------------------------------------------------------------------

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
