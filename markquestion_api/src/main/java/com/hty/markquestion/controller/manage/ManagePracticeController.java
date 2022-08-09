package com.hty.markquestion.controller.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.PlatformMapper;
import com.hty.markquestion.mapper.PracticeMapper;
import com.hty.markquestion.mapper.PracticeTagMapper;
import com.hty.markquestion.mapper.PracticeUserMapper;
import com.hty.markquestion.pojo.Platform;
import com.hty.markquestion.pojo.Practice;
import com.hty.markquestion.pojo.PracticeTag;
import com.hty.markquestion.pojo.PracticeUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/manage/practice")
public class ManagePracticeController {
    @Autowired
    PracticeMapper practiceMapper;

    @Autowired
    PracticeTagMapper practiceTagMapper;

    @Autowired
    PracticeUserMapper practiceUserMapper;

    @Autowired
    PlatformMapper platformMapper;

    @GetMapping("/toPracticeMain")
    @ApiOperation("跳转到练习管理首页")
    public String toPracticeMain(Model model){
        //需要将题库添加进去
        List<PracticeTag> practiceTags = practiceTagMapper.selectList(null);
        model.addAttribute("practiceTags",practiceTags);
        return "manager/practice/practice_main";
    }

    @GetMapping("/toCreatePracticeTag")
    @ApiOperation("跳转到题库创建页面")
    public String toCreatePracticeTag(){
        return "manager/add/add_practice_tag";
    }

    @GetMapping("/createPracticeTag")
    @ApiOperation("创建一个新的题库")
    public String createPracticeTag(@RequestParam("practiceTagName") String practiceTagName,
                                    Model model){
        QueryWrapper<PracticeTag> qw = new QueryWrapper<>();
        qw.eq("practice_tag_name",practiceTagName);
        PracticeTag practiceTag = practiceTagMapper.selectOne(qw);
        //只有当这个题库不存在的时候才能创建
        if(practiceTag == null){
            practiceTag = new PracticeTag();
            practiceTag.setPracticeTagName(practiceTagName);
            practiceTag.setPracticeTagNum(0);
            practiceTagMapper.insert(practiceTag);
            model.addAttribute("msg","添加成功");
        }else{
            model.addAttribute("msg","添加失败，已经存在这个题库");
        }
        return "redirect:/manage/practice/toPracticeMain";
    }

    @GetMapping("/toAddPractice")
    @ApiOperation("跳转到添加练习页面")
    public String toAddPractice(@RequestParam(value = "practiceTagName",defaultValue = "") String practiceTagName,
                                Model model){
        //访问这个接口有两种情况，如果是直接在某个题库中访问，那么就会带有practiceTagName参数，就需要将这个参数自动填入到表单中
        if(practiceTagName != null && !practiceTagName.equals("")){
            QueryWrapper<PracticeTag> qw = new QueryWrapper<>();
            qw.eq("practice_tag_name",practiceTagName);
            PracticeTag practiceTag = practiceTagMapper.selectOne(qw);
            model.addAttribute("practiceTag",practiceTag);
        }else{//反之就需要将所有题库添加进去
            model.addAttribute("practiceTag",null);
            List<PracticeTag> practiceTags = practiceTagMapper.selectList(null);
            model.addAttribute("practiceTags",practiceTags);
        }

        //将题目来源添加上去，这个题目来源是根据之前题解部分的题目来源是一起的
        List<Platform> platforms = platformMapper.selectList(null);
        model.addAttribute("platforms",platforms);


        return "manager/add/add_practice";
    }

    @PostMapping("/addPractice")
    @ApiOperation("添加练习")
    public String addPractice(@RequestParam("practiceName") String practiceName,
                              @RequestParam("practiceChanges") String practiceChanges,
                              @RequestParam("answer") String answer,
                              @RequestParam("practiceTag") String practiceTag,
                              @RequestParam("practiceFrom") String practiceFrom,
                              @RequestParam("practiceExplain") String practiceExplain,
                              @RequestParam("isRadioOrSelect") String isRadioOrSelect,
                              Model model){
        //将题目添加到数据库中
        Practice practice = new Practice();
        practice.setPracticeName(practiceName);
        practice.setPracticeChanges(practiceChanges);
        practice.setPracticeAnswer(answer);
        practice.setPracticeTag(practiceTag);
        practice.setPracticeFrom(practiceFrom);
        practice.setPracticeExplain(practiceExplain);
        practice.setPracticeIsRadioOrSelect(isRadioOrSelect);
        practice.setCreateTime(new Date());
        practice.setUpdateTime(new Date());
        practice.setPracticeRight(0);
        practice.setPracticeFalse(0);
        practiceMapper.insert(practice);

        //将当前标签下的题库的题目数量+1
        QueryWrapper<PracticeTag> qw = new QueryWrapper<>();
        qw.eq("practice_tag_name",practiceTag);
        PracticeTag practiceTagName = practiceTagMapper.selectOne(qw);
        practiceTagName.setPracticeTagNum(practiceTagName.getPracticeTagNum()+1);
        practiceTagMapper.updateById(practiceTagName);

        model.addAttribute("msg","添加成功");

        return "redirect:/manage/practice/toPracticeMain";
    }

    @GetMapping("/toPracticeTagShow")
    @ApiOperation("题库详情页面")
    public String toPracticeTagShow(@RequestParam("practiceTag") String practiceTag,Model model){
        QueryWrapper<Practice> qw = new QueryWrapper<>();
        qw.eq("practice_tag",practiceTag);
        List<Practice> practices = practiceMapper.selectList(qw);

        //我们需要将所有的题目进行截取,这样就可以看到每个题目的题目信息
        for (Practice practice : practices) {
            String practiceName = practice.getPracticeName();
            if(practiceName.length() >= 20){
                practice.setPracticeName(practiceName.substring(0,20)+"...");
            }
        }

        model.addAttribute("practices",practices);
        //将当前题库的名称返回给前端，这样才能不报错
        model.addAttribute("practiceTag",practiceTag);
        return "manager/practice/practice_tag_show";
    }

//    @GetMapping("/deletePractice")
//    @ApiOperation("删除一个题目")
//    public String deletePractice(@RequestParam("id") String id,
//                                 Model model){
//        Practice practice = practiceMapper.selectById(id);
//        practiceMapper.deleteById(Integer.valueOf(id));
//        model.addAttribute("msg","删除成功");
//        //注意这里如果是请求重定向的话就会出现乱码 只能使用请求转发
//        return "forward:/manage/practice/toPracticeTagShow?practiceTag="+practice.getPracticeTag();
//    }

    @GetMapping("/toUpdatePractice")
    @ApiOperation("跳转到练习修改页面")
    public String toUpdatePractice(@RequestParam("id") String id,Model model){
        //将要修改的题解返回
        Practice practice = practiceMapper.selectById(Integer.valueOf(id));
        model.addAttribute("practice",practice);

        //还要将所有题库信息返回 但是要删除当前题解所属的题库
        List<PracticeTag> practiceTags = practiceTagMapper.selectList(null);
        int remove = 0;
        for (PracticeTag practiceTag : practiceTags) {
            if(practiceTag.getPracticeTagName().equals(practice.getPracticeTag())){
                break;
            }
            remove++;
        }
        practiceTags.remove(remove);
        model.addAttribute("practiceTags",practiceTags);

        //将所有题目来源信息返回 并且删除当前题目所属的来源
        List<Platform> platforms = platformMapper.selectList(null);
        remove = 0;
        for (Platform platform : platforms) {
            if(platform.getPlatform().equals(practice.getPracticeFrom())){
                break;
            }
            remove++;
        }
        platforms.remove(remove);
        model.addAttribute("platforms",platforms);

        return "manager/update/update_practice";
    }

    @PostMapping("/updatePractice")
    @ApiOperation("修改练习")
    public String updatePractice(@RequestParam("id") String id,
                                 @RequestParam("practiceName") String practiceName,
                                 @RequestParam("practiceChanges") String practiceChanges,
                                 @RequestParam("answer") String answer,
                                 @RequestParam("practiceTag") String practiceTag,
                                 @RequestParam("practiceFrom") String practiceFrom,
                                 @RequestParam("practiceExplain") String practiceExplain,
                                 @RequestParam("isRadioOrSelect") String isRadioOrSelect,
                                 Model model){
        Practice practice = practiceMapper.selectById(Integer.valueOf(id));

        //将原题库的题目数量-1
        QueryWrapper<PracticeTag> qw1 = new QueryWrapper<>();
        qw1.eq("practice_tag_name",practice.getPracticeTag());
        PracticeTag practiceOldTag = practiceTagMapper.selectOne(qw1);//原题库信息
        practiceOldTag.setPracticeTagNum(practiceOldTag.getPracticeTagNum()-1);
        practiceTagMapper.updateById(practiceOldTag);
        //将新题库题目数量+1
        qw1 = null;
        qw1 = new QueryWrapper<>();
        qw1.eq("practice_tag_name",practiceTag);
        PracticeTag practiceNewTag = practiceTagMapper.selectOne(qw1);//新题库信息
        practiceNewTag.setPracticeTagNum(practiceNewTag.getPracticeTagNum()+1);
        practiceTagMapper.updateById(practiceNewTag);

        //给对象的属性设置值
        practice.setPracticeName(practiceName);
        practice.setPracticeChanges(practiceChanges);
        practice.setPracticeAnswer(answer);
        practice.setPracticeTag(practiceTag);
        practice.setPracticeFrom(practiceFrom);
        practice.setPracticeExplain(practiceExplain);
        practice.setPracticeIsRadioOrSelect(isRadioOrSelect);
        practice.setUpdateTime(new Date());
        practiceMapper.updateById(practice);
        model.addAttribute("msg","修改成功");

        QueryWrapper<Practice> qw2 = new QueryWrapper<>();
        qw2.eq("practice_tag",practiceTag);
        List<Practice> practices = practiceMapper.selectList(qw2);
        model.addAttribute("practices",practices);
        //将当前题库的名称返回给前端，这样才能不报错
        model.addAttribute("practiceTag",practiceTag);

        return "manager/practice/practice_tag_show";
    }

    @GetMapping("/toUpdatePracticeTag")
    @ApiOperation("跳转到题库修改页面")
    public String toUpdatePracticeTag(@RequestParam("id") String id,
                                      Model model){
        PracticeTag practiceTag = practiceTagMapper.selectById(Integer.valueOf(id));
        model.addAttribute("practiceTag",practiceTag);
        return "manager/update/update_practice_tag";
    }

    @PostMapping("/updatePracticeTag")
    @ApiOperation("修改题库信息")
    public String updatePracticeTag(@RequestParam("id") String id,
                                    @RequestParam("oldPracticeTagName") String oldPracticeTagName,
                                    @RequestParam("newPracticeTagName") String newPracticeTagName,
                                    Model model){
        if(!oldPracticeTagName.equals(newPracticeTagName)){
            //修改练习题库的信息
            PracticeTag practiceTag = practiceTagMapper.selectById(Integer.valueOf(id));
            practiceTag.setPracticeTagName(newPracticeTagName);
            practiceTagMapper.updateById(practiceTag);

            //修改所有当前题库下的题目的信息
            QueryWrapper<Practice> qw = new QueryWrapper<>();
            qw.eq("practice_tag",oldPracticeTagName);
            List<Practice> practices = practiceMapper.selectList(qw);
            for (Practice practice : practices) {
                practice.setPracticeTag(newPracticeTagName);
                practiceMapper.updateById(practice);
            }

            model.addAttribute("msg","修改成功");
        }

        return "redirect:/manage/practice/toPracticeMain";
    }

    @GetMapping("/managePracticeUser")
    @ApiOperation("跳转到用户管理页面")
    public String managePracticeUser(Model model){
        List<PracticeUser> practiceUsers = practiceUserMapper.selectList(null);
        model.addAttribute("practiceUsers",practiceUsers);
        return "manager/manage/manage_practice_user";
    }

    @GetMapping("/deletePracticeUser")
    @ApiOperation("删除一个用户")
    public String deletePracticeUser(@RequestParam("id") String id,
                                     Model model){
        practiceUserMapper.deleteById(Integer.valueOf(id));
        model.addAttribute("msg","删除成功");
        return "redirect:/manage/practice/managePracticeUser";
    }

    @GetMapping("/toUpdatePracticeUser")
    @ApiOperation("跳转到用户修改页面")
    public String toUpdatePracticeUser(@RequestParam("id") String id,
                                       Model model){
        PracticeUser practiceUser = practiceUserMapper.selectById(Integer.valueOf(id));
        model.addAttribute("practiceUser",practiceUser);
        return "/manager/update/update_practice_user";
    }

    @PostMapping("/updatePracticeUser")
    @ApiOperation("修改用户信息")
    public String updatePracticeUser(@RequestParam("id") String id,
                                     @RequestParam("practiceUserId") String practiceUserId,
                                     @RequestParam("practiceUserUsername") String practiceUserUsername,
                                     @RequestParam("practiceUserEmail") String practiceUserEmail,
                                     Model model){

        PracticeUser practiceUser = practiceUserMapper.selectById(Integer.valueOf(id));
        practiceUser.setPracticeUserId(practiceUserId);
        practiceUser.setPracticeUserUsername(practiceUserUsername);
        practiceUser.setPracticeUserEmail(practiceUserEmail);
        practiceUserMapper.updateById(practiceUser);
        model.addAttribute("msg","修改成功");

        return "redirect:/manage/practice/managePracticeUser";
    }

    @GetMapping("/searchPracticeUser")
    @ApiOperation("根据用户编号查询用户信息")
    public String searchPracticeUser(@RequestParam("search") String search,Model model){
        QueryWrapper<PracticeUser> qw = new QueryWrapper<>();
        qw.like("practice_user_id",search);
        List<PracticeUser> practiceUsers = practiceUserMapper.selectList(qw);
        model.addAttribute("practiceUsers",practiceUsers);
        return "manager/manage/manage_practice_user";
    }

    @GetMapping("/searchPractice")
    @ApiOperation("根据题号查询题目信息")
    public String searchPractice(@RequestParam("id") String id,Model model){
        Practice practice = practiceMapper.selectById(Integer.valueOf(id));
        model.addAttribute("practice",practice);
        return "manager/practice/practice_search_show";
    }
}
