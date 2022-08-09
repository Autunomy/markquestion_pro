package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.PracticeMapper;
import com.hty.markquestion.mapper.PracticeTagMapper;
import com.hty.markquestion.mapper.PracticeUserMapper;
import com.hty.markquestion.mapper.WebBasicMessageMapper;
import com.hty.markquestion.pojo.Practice;
import com.hty.markquestion.pojo.PracticeTag;
import com.hty.markquestion.pojo.PracticeUser;
import com.hty.markquestion.pojo.WebBasicMessage;
import com.hty.markquestion.util.ImgTypeStatic;
import com.hty.markquestion.util.MarkDown2HtmlUtils;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/practice")
public class PracticeController {

    @Autowired
    PracticeMapper practiceMapper;

    @Autowired
    PracticeTagMapper practiceTagMapper;

    @Autowired
    PracticeUserMapper practiceUserMapper;

    @Autowired
    WebBasicMessageMapper webBasicMessageMapper;

    @GetMapping("/toPracticeMain")
    @ApiOperation("跳转到练习首页")
    public String toPracticeMain(Model model, HttpServletRequest request) {
        //判断用户是否存在
        HttpSession session = request.getSession();
        PracticeUser practiceUser = (PracticeUser) session.getAttribute("practice_user");
        if (practiceUser != null) {
            //用户信息
            model.addAttribute("practiceUser", practiceUser);

            if (practiceUser.getPracticeUserPracticeNum() != null && practiceUser.getPracticeUserPracticeNum() != 0) {
                //正确率
                Double rightPer = 100.0 - (practiceUser.getPracticeUserPracticeWrongNum() * 100 / practiceUser.getPracticeUserPracticeNum() * 100.0) / 100.0;
                model.addAttribute("rightPer", rightPer);
            } else {
                model.addAttribute("rightPer", 0);
            }
            //用户错误的题号使用一个集合存储 前提条件是有这个字符串，且字符串不为空
            if (practiceUser.getPracticeUserWrongPractice() != null && practiceUser.getPracticeUserWrongPractice().length() != 0) {
//                System.out.println(practiceUser.getPracticeUserWrongPractice());
                String[] split = practiceUser.getPracticeUserWrongPractice().split("#");
//                System.out.println(Arrays.toString(split));
                List<Practice> wrongPractices = new ArrayList<>();
                for (String s : split) {
                    Practice temp = practiceMapper.selectById(Integer.valueOf(s));
                    wrongPractices.add(temp);
                }
                model.addAttribute("wrongPractices", wrongPractices);
            }
        }

        //将题库种类传给前端
        List<PracticeTag> practiceTags = practiceTagMapper.selectList(null);
        model.addAttribute("practiceTags", practiceTags);

        //还需要将网站的基本信息加入页面
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "practice/practice_main";
    }

    @PostMapping("/loginOrSign")
    @ApiOperation("登陆接口")
    public String loginOrSign(@RequestParam("userId") String userId,
                              @RequestParam("headImg") MultipartFile headImg,
                              HttpServletRequest request,
                              Model model) throws IOException {
        if (userId == null || userId.length() != 36) {//防止用户使用自定义的id，必须使用系统自定义的id
            model.addAttribute("msg", "id格式不正确");
            return "redirect:/practice/toPracticeMain";
        }

        HttpSession session = request.getSession();
        QueryWrapper<PracticeUser> qw = new QueryWrapper<>();
        qw.eq("practice_user_id", userId);
        PracticeUser practiceUser = practiceUserMapper.selectOne(qw);
        //判断用户是否存在
        if (practiceUser == null) {//不存在就是注册
            practiceUser = new PracticeUser();
            String fileName = "";
            //存储头像
            if (!headImg.isEmpty()) {//如果用户自己上传了头像就使用用户头像
                //判断文件是否有后缀
                String name = headImg.getOriginalFilename();
                if (name != null && !name.contains(".")) {//文件名中没有后缀
                    fileName = "test.JPG";
                    model.addAttribute("msg", "文件格式不正确");
                } else {
                    //获取文件后缀
                    String last = "";
                    for (int i = name.length() - 1; i >= 0; --i) {
                        if (name.charAt(i) == '.') break;
                        last = name.charAt(i) + "" + last;
                    }
                    //判断是否符合类型
                    if (ImgTypeStatic.isInType(last)) {
                        //拼接文件名
                        fileName = userId + "." + last;
                        //保存文件
                        headImg.transferTo(new File("/markquestion/images/practice/userHead/" + fileName));
                    }
                }
            } else {//用户没有上传头像就使用系统默认头像
                fileName = "test.JPG";
            }

            practiceUser.setPracticeUserId(userId);
            practiceUser.setPracticeUserImg("/images/practice/userHead/" + fileName);
            practiceUser.setCreateTime(new Date());
            practiceUser.setPracticeUserPracticeNum(0);
            practiceUser.setPracticeUserPracticeWrongNum(0);
            practiceUser.setPracticeUserWrongPractice("");
            practiceUser.setPracticeUserUsername("UserName");

            //插入数据库
            practiceUserMapper.insert(practiceUser);
        }
        //将用户信息放在session中
        session.setAttribute("practice_user", practiceUser);

        return "redirect:/practice/toPracticeMain";
    }

    @GetMapping("/makeUserId")
    @ResponseBody
    @ApiOperation("使用AJAX获取生成ID")
    public String makeUserId() {
        return UUID.randomUUID().toString();
    }

    @GetMapping("/signOut")
    @ApiOperation("注销")
    public String signOut(HttpServletRequest request) {
        request.getSession().removeAttribute("practice_user");
        return "redirect:/practice/toPracticeMain";
    }

    @GetMapping("/getWrongPractice")
    @ResponseBody
    @ApiOperation("使用AJAX获取错误的题目的信息")
    public String getWrongPractice(@RequestParam(value = "id",defaultValue = "") String id) {
        if (id == null || id.equals("")) return "";
        Practice practice = practiceMapper.selectById(Integer.valueOf(id));

        //对题目和解析内容进行markdown转换
        practice.setPracticeName(MarkDown2HtmlUtils.markdown2Html(practice.getPracticeName()));
        practice.setPracticeExplain(MarkDown2HtmlUtils.markdown2Html(practice.getPracticeExplain()));

        return JSON.toJSONString(practice);
    }

    @GetMapping("/beginDoPractice")
    @ApiOperation("开始做题,每次取一道题,下一题按钮也是调用这个接口")
    public String beginDoPractice(Model model,
                                  @RequestParam("tagName") String tagName) {
        //获取当前题目的所有题目
        QueryWrapper<Practice> qw = new QueryWrapper<>();
        qw.eq("practice_tag", tagName);
        //题目数量
        Integer integer = practiceMapper.selectCount(qw);
        List<Practice> practices = practiceMapper.selectList(qw);

        //如果当前题库没有题目就直接返回
        if (practices.size() == 0) return "redirect:/practice/toPracticeMain";

        //使用随机数模块随机生成题目
        Random random = new Random();
        Practice practice = practices.get(Math.abs(random.nextInt()) % integer);

        //题目需要做markdown转换
        practice.setPracticeName(MarkDown2HtmlUtils.markdown2Html(practice.getPracticeName()));

        model.addAttribute("practice", practice);
        model.addAttribute("tagName", tagName);

        //还需要将网站的基本信息加入页面
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "practice/practice_begin";
    }

    @PostMapping("/judgeRadioPractice")
    @ApiOperation("判断单选题目是否正确")
    public String judgePractice(@RequestParam(value = "answer",defaultValue = "") String answer,
                                @RequestParam("id") String id,
                                @RequestParam("tagName") String tagName,
                                Model model,
                                HttpServletRequest request) {
        //用户没有选择答案 则默认直接退出答题
        if(answer == null || answer.equals("")){
            return "redirect:/practice/toPracticeMain";
        }

        //先获取到当前题目
        Practice practice = practiceMapper.selectById(Integer.valueOf(id));
        //将选项进行拆分
        String[] changes = practice.getPracticeChanges().split("###");
        boolean flag = false;
        //使用session来获取用户信息，这样用来对用户数据进行统计
        HttpSession session = request.getSession();
        PracticeUser practiceUser = (PracticeUser) session.getAttribute("practice_user");
        //如果当前有登陆的用户才能+1
        if(practiceUser != null){
            //做过题目总数+1
            practiceUser.setPracticeUserPracticeNum(practiceUser.getPracticeUserPracticeNum() + 1);
        }else{
            return "redirect:/practice/toPracticeMain";
        }

        //因为在数据库中存储的是A B C D这种答案，需要对应到下标中
        int rightAnswer = practice.getPracticeAnswer().charAt(0) - 'A';
        //如果用户选择了值的时候才进行判断是否正确
        if (answer != null && answer.equals(changes[rightAnswer])) {
            flag = true;
        }

        if (flag) {//题目正确
            //让当前题目做对人数+1
            practice.setPracticeRight(practice.getPracticeRight()+1);
            model.addAttribute("isRight", "true");
        } else{
            //让当前题目做错人数+1
            practice.setPracticeFalse(practice.getPracticeFalse()+1);
            model.addAttribute("isRight", "false");
            practiceUser.setPracticeUserPracticeWrongNum(practiceUser.getPracticeUserPracticeWrongNum() + 1);
            //获取错误的题目
            String wrongHistory = practiceUser.getPracticeUserWrongPractice();

            //用户错过题目
            if (wrongHistory != null && !wrongHistory.equals("")) {
                String[] split = wrongHistory.split("#");
                boolean isHas = false;
                //判断这个题是否已经错过 如果错过就不需要在添加进去
                for (String s : split) {
                    if (s.equals(practice.getId() + "")) {
                        isHas = true;
                        break;
                    }
                }
                //将错题添加进去
                if (!isHas) {
                    practiceUser.setPracticeUserWrongPractice(wrongHistory + "#" + practice.getId());
                }
            } else {//用户没有错过题目
                practiceUser.setPracticeUserWrongPractice(practice.getId()+"");
            }
        }

        //更新题目信息
        practiceMapper.updateById(practice);

        //更新用户信息
        practiceUserMapper.updateById(practiceUser);

        //需要将题目内容和解析的内容都添加入markdown解析
        practice.setPracticeName(MarkDown2HtmlUtils.markdown2Html(practice.getPracticeName()));
        practice.setPracticeExplain(MarkDown2HtmlUtils.markdown2Html(practice.getPracticeExplain()));

        model.addAttribute("practice", practice);

        //将正确答案返回
        char reAnswer = (char) ('A'+rightAnswer);
        model.addAttribute("answer", reAnswer);
        //为了下一题按钮可以正常工作
        model.addAttribute("tagName", tagName);


        //还需要将网站的基本信息加入页面
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "practice/practice_begin";
    }

    @PostMapping("/judgeSelectPractice")
    @ApiOperation("判断多选题目是否正确")
    public String judgeSelectPractice(@RequestParam(value = "answer") String[] answer,
                                      @RequestParam("id") String id,
                                      @RequestParam("tagName") String tagName,
                                      Model model,
                                      HttpServletRequest request){
        //用户没有选择答案 则默认直接退出答题
        if(answer == null || answer.length == 0){
            return "redirect:/practice/toPracticeMain";
        }

        //先获取到当前题目
        Practice practice = practiceMapper.selectById(Integer.valueOf(id));
        //将选项进行拆分
        String[] changes = practice.getPracticeChanges().split("###");
        //答案
        String[] rightAnswer = practice.getPracticeAnswer().split("###");
        //正确答案的下标
        int[] rightAnswerIndex = new int[rightAnswer.length];
        for(int i=0;i<rightAnswerIndex.length;++i){
            rightAnswerIndex[i] = rightAnswer[i].charAt(0)-'A';
        }

        //使用session来获取用户信息，这样用来对用户数据进行统计
        HttpSession session = request.getSession();
        PracticeUser practiceUser = (PracticeUser) session.getAttribute("practice_user");
        //如果当前有登陆的用户才能+1
        if(practiceUser != null){
            //做过题目总数+1
            practiceUser.setPracticeUserPracticeNum(practiceUser.getPracticeUserPracticeNum() + 1);
        }else{
            return "redirect:/practice/toPracticeMain";
        }

        boolean flag = false;
        //如果用户选择了值的时候才进行判断是否正确
        if(rightAnswerIndex.length == answer.length){//当选项数量相同的时候再判断答案是否正确
            for(int i=0;i<rightAnswerIndex.length;++i){
                if(!changes[rightAnswerIndex[i]].equals(answer[i])){//如果不相同则错误
                    break;
                }
            }
            //循环完成则说明答案正确
            flag = true;
        }

        if (flag) {//题目正确
            //让当前题目做对人数+1
            practice.setPracticeRight(practice.getPracticeRight()+1);
            model.addAttribute("isRight", "true");
        } else{
            //让当前题目做错人数+1
            practice.setPracticeFalse(practice.getPracticeFalse()+1);
            model.addAttribute("isRight", "false");
            practiceUser.setPracticeUserPracticeWrongNum(practiceUser.getPracticeUserPracticeWrongNum() + 1);
            //获取错误的题目
            String wrongHistory = practiceUser.getPracticeUserWrongPractice();

            //用户错过题目
            if (wrongHistory != null && !wrongHistory.equals("")) {
                String[] split = wrongHistory.split("#");
                boolean isHas = false;
                //判断这个题是否已经错过 如果错过就不需要在添加进去
                for (String s : split) {
                    if (s.equals(practice.getId() + "")) {
                        isHas = true;
                        break;
                    }
                }
                //将错题添加进去
                if (!isHas) {
                    practiceUser.setPracticeUserWrongPractice(wrongHistory + "#" + practice.getId());
                }
            } else {//用户没有错过题目
                practiceUser.setPracticeUserWrongPractice(practice.getId()+"");
            }
        }

        //更新题目信息
        practiceMapper.updateById(practice);

        //更新用户信息
        practiceUserMapper.updateById(practiceUser);

        //需要将题目内容和解析的内容都添加入markdown解析
        practice.setPracticeName(MarkDown2HtmlUtils.markdown2Html(practice.getPracticeName()));
        practice.setPracticeExplain(MarkDown2HtmlUtils.markdown2Html(practice.getPracticeExplain()));

        model.addAttribute("practice", practice);
        //将用户选择的答案加入进去
        String reAnswer = "";
        for (String s : rightAnswer) {
            reAnswer+=s;
        }
        model.addAttribute("answer", reAnswer);
        //为了下一题按钮可以正常工作
        model.addAttribute("tagName", tagName);

//        //将转换后的markdown在转换回来
//        practice.setPracticeName(name);
//        practice.setPracticeExplain(explain);



        //还需要将网站的基本信息加入页面
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "practice/practice_begin";
    }

    @GetMapping("/toChangeUserMessage")
    @ApiOperation("跳转到个人信息修改页面")
    public String toChangeUserMessage(Model model,
                                      @RequestParam("id") String id) {
        PracticeUser practiceUser = practiceUserMapper.selectById(Integer.valueOf(id));
        model.addAttribute("practiceUser", practiceUser);

        //还需要将网站的基本信息加入页面
        WebBasicMessage webBasicMessage = webBasicMessageMapper.selectOne(null);
        model.addAttribute("webBasicMessage",webBasicMessage);

        return "practice/change_user_message";
    }

    @PostMapping("/changeUserMessage")
    @ApiOperation("修改个人信息")
    public String changeUserMessage(Model model,
                                    @RequestParam("id") String id,
                                    @RequestParam("userName") String userName,
                                    @RequestParam("email") String email,
                                    @RequestParam("userHeadImg") MultipartFile userHeadImg,
                                    HttpServletRequest request) throws IOException {
        //先获取到这个用户
        PracticeUser practiceUser = practiceUserMapper.selectById(Integer.valueOf(id));
        practiceUser.setPracticeUserUsername(userName);
        practiceUser.setPracticeUserEmail(email);
        //将头像进行更新
        if (!userHeadImg.isEmpty()) {
            //判断文件是否有后缀
            String name = userHeadImg.getOriginalFilename();
            if (name != null && !name.contains(".")) {//文件名中没有后缀
                model.addAttribute("msg", "文件格式不正确");
            } else {
                //获取文件后缀
                String last = "";
                for (int i = name.length() - 1; i >= 0; --i) {
                    if (name.charAt(i) == '.') break;
                    last = name.charAt(i) + "" + last;
                }

                if (ImgTypeStatic.isInType(last)) {
                    //拼接文件名
                    String fileName = practiceUser.getPracticeUserId() + "." + last;
                    //保存文件
                    userHeadImg.transferTo(new File("/markquestion/images/practice/userHead/" + fileName));
                    practiceUser.setPracticeUserImg("/images/practice/userHead/" + fileName);
                }
            }

        }
        practiceUserMapper.updateById(practiceUser);
        model.addAttribute("msg", "信息修改成功");

        //这里获取session的目的就是为了修改当前用户的信息，并且能够同步更新在页面中
        HttpSession session = request.getSession();
        session.setAttribute("practice_user", practiceUser);


        return "redirect:/practice/toPracticeMain";
    }
}
