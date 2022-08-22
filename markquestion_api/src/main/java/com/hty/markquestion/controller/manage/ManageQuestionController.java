package com.hty.markquestion.controller.manage;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.*;
import com.hty.markquestion.mapper.QuestionMapper;
import com.hty.markquestion.pojo.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/manage/question")
public class ManageQuestionController {
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

    @ApiOperation("题解详情页面")
    @GetMapping("/showQuestionMessage")
    public String showQuestion(@ApiParam("题解id") @RequestParam("id") Integer id, Model model) {
        //搜索当前题解
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Question question = questionMapper.selectOne(wrapper);

        //对正文的markdown格式进行转换
        question.setSolution(MarkDown2HtmlUtils.parse(question.getSolution()));
        model.addAttribute("question", question);
//        log.info(question.toString());

        //搜索出作者信息
        QueryWrapper<AuthorMessage> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("username",question.getAuthor());
        AuthorMessage authorMessage = authorMessageMapper.selectOne(wrapper2);
//        log.info(authorMessage.toString());
        model.addAttribute("authorMessage", authorMessage);

        //显示网站基本信息
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "manager/problems/show_problems";
    }

    @GetMapping("/showQuestion")
    @ApiOperation("跳转到题解管理页面")
    public String showQuestion(Model model){
        //添加所有的题解
        List<Question> questions = questionMapper.selectList(null);
        model.addAttribute("questions", questions);
        //log.info(questions.toString());

        //添加比赛日程
        List<Contest> result = contestMapper.selectList(null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Contest contest : result) {
            sdf.format(contest.getContestTime());
        }
        List<Contest> contests = new ArrayList<>();
        //只有未过期的比赛才显示 0表示未过期
        for (Contest contest : result) {
            if(contest.getIsExpired() == 0){
                contests.add(contest);
            }
        }
        model.addAttribute("contests", contests);
        //log.info(contests.toString());

        //添加所有的平台(题目来源)
        List<Platform> platforms = platformMapper.selectList(null);
        model.addAttribute("platforms", platforms);
        //log.info(platforms.toString());

        //添加所有的题解标签
        List<QuestionTag> questionTags = questionTagMapper.selectList(null);
        model.addAttribute("questionTags", questionTags);
        //log.info(questionTags.toString());

        return "manager/manage/manage_questions";
    }

    @GetMapping("/toAddQuestion")
    @ApiOperation("跳转到题解添加页面")
    public String toAddQuestion(Model model){
        //需要将题目来源返回回去
        List<Platform> platforms = platformMapper.selectList(null);
        model.addAttribute("platforms",platforms);
        //log.info(platforms.toString());

        return "manager/add/add_question";
    }

//    @PostMapping("/addQuestion")
//    @ApiOperation("添加一个题解")
//    public String addQuestion(@RequestParam("question_name") String question_name,
//                              @RequestParam("question_from") String question_from,
//                              @RequestParam("author") String author,
//                              @RequestParam("tag") String tag,
//                              @RequestParam("solution") String solution){
//        QueryWrapper<QuestionTag> wrapper = new QueryWrapper<>();
//        wrapper.eq("tag_name",tag);
//        QuestionTag questionTag = questionTagMapper.selectOne(wrapper);
//        //判断这个标签是否存在 不存在就创建和这个标签
//        if(questionTag == null){
//            QuestionTag newTag = new QuestionTag();
//            newTag.setTagName(tag);
//            questionTagMapper.insert(newTag);
//        }
//
//        Question question = new Question();
//        question.setSolution(solution);
//        question.setQuestionFrom(question_from);
//        question.setAuthor(author);
//        question.setQuestionName(question_name);
//        question.setTag(tag);
//        question.setUpdateTime(new Date());
//        question.setCreateTime(new Date());
//        questionMapper.insert(question);
//
//        return "redirect:/manage/question/showQuestion";
//    }

    @ApiOperation("删除一个题解")
    @GetMapping("/deleteQuestion")
    public String deleteQuestion(@RequestParam("id") Integer id){
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        //根据id删除
        int delete = questionMapper.delete(wrapper);
        return "redirect:/manage/question/showQuestion";
    }

    @ApiOperation("跳转到题解修改页面")
    @GetMapping("/toUpdateQuestion")
    public String toUpdateQuestion(@RequestParam("id") Integer id,Model model){
        //查询要修改的题解
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        Question question = questionMapper.selectOne(wrapper);
        //log.info(question.toString());
        model.addAttribute("question",question);

        //添加题目来源
        QueryWrapper<Platform> wrapper1 = new QueryWrapper<>();
        wrapper1.ne("platform",question.getQuestionFrom());
        List<Platform> platforms = platformMapper.selectList(wrapper1);
        model.addAttribute("platforms",platforms);

        return "manager/update/update_question";
    }

    @ApiOperation("题解更新页面")
    @PostMapping("/updateQuestion")
    public String updateQuestion(@RequestParam("id") Integer id,
                                 @RequestParam("question_name") String question_name,
                                 @RequestParam("question_from") String question_from,
                                 @RequestParam("author") String author,
                                 @RequestParam("tag") String tag,
                                 @RequestParam("createTime") String createTime,
                                 @RequestParam("solution") String solution,
                                 Model model) throws ParseException {
        QueryWrapper<QuestionTag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_name",tag);
        QuestionTag questionTag = questionTagMapper.selectOne(wrapper);
        //判断这个标签是否存在 不存在就创建和这个标签
        if(questionTag == null){
            QuestionTag newTag = new QuestionTag();
            newTag.setTagName(tag);
            questionTagMapper.insert(newTag);
        }

        QueryWrapper<Question> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("id",id);
        Question question = new Question();
        question.setSolution(solution);
        question.setQuestionFrom(question_from);
        question.setAuthor(author);
        question.setQuestionName(question_name);
        question.setTag(tag);
        question.setUpdateTime(new Date());
        question.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime));
        int i = questionMapper.update(question,wrapper1);
//        System.out.println(i+"--------------------------------------");


        return "redirect:/manage/question/showQuestion";
    }

    //------------------------------------------------------------------------------------------------

}
