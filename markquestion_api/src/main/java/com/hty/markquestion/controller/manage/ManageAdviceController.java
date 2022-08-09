package com.hty.markquestion.controller.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hty.markquestion.mapper.AdviceMapper;
import com.hty.markquestion.mapper.AdviceTagMapper;
import com.hty.markquestion.pojo.Advice;
import com.hty.markquestion.pojo.AdviceTag;
import com.hty.markquestion.util.MarkDown2HtmlUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/manage/advice")
public class ManageAdviceController {

    @Autowired
    AdviceMapper adviceMapper;
    @Autowired
    AdviceTagMapper adviceTagMapper;

    @GetMapping("/showAdvice")
    @ApiOperation("显示所有的推荐信息")
    public String showAdvice(Model model) {
        List<Advice> advices = adviceMapper.selectList(null);
        model.addAttribute("advices", advices);
        return "manager/manage/manage_advice";
    }

    @GetMapping("/deleteAdvice")
    @ApiOperation("删除推荐")
    public String deleteAdvice(@RequestParam("id") String id) {
        int i = adviceMapper.deleteById(Integer.valueOf(id));
        return "redirect:/manage/advice/showAdvice";
    }

    @GetMapping("/toUpdateAdvice")
    @ApiOperation("跳转到更新推荐页面")
    public String toUpdateAdvice(@RequestParam("id") String id, Model model) {
        Advice advice = adviceMapper.selectById(id);
        model.addAttribute("advice", advice);
        return "manager/update/update_advice";
    }

    @PostMapping("/updateAdvice")
    @ApiOperation("更新推荐")
    public String updateAdvice(@RequestParam("id") String id,
                               @RequestParam("adviceName") String adviceName,
                               @RequestParam("adviceAuthor") String adviceAuthor,
                               @RequestParam("adviceTag") String adviceTag,
                               @RequestParam("adviceDescribe") String adviceDescribe,
                               @RequestParam("adviceImg") MultipartFile img,
                               @RequestParam("adviceContent") String adviceContent,
                               Model model) throws IOException {

        Advice advice = adviceMapper.selectById(id);
        advice.setAdviceName(adviceName);
        advice.setAdviceAuthor(adviceAuthor);
        advice.setAdviceTag(adviceTag);
        advice.setAdviceDescribe(adviceDescribe);
        advice.setAdviceContent(adviceContent);
        advice.setUpdateTime(new Date());
//        System.out.println(adviceContent);

        //将该推荐的标签的num进行更新 若不存在该标签，就创建一个标签
        QueryWrapper<AdviceTag> qw = new QueryWrapper<>();
        qw.eq("advice_tag_name",adviceTag);
        AdviceTag tag = adviceTagMapper.selectOne(qw);
        if(tag == null){
            tag = new AdviceTag();
            tag.setAdviceTagName(adviceTag);
            tag.setAdviceTagNum(1);
            tag.setCreateTime(new Date());
            adviceTagMapper.insert(tag);
        }

        //----------------------本地---------------------------------------------

//        if (!img.isEmpty()) {
//            //判断文件是否有后缀
//            String name = img.getOriginalFilename();
//            if (name != null && !name.contains(".")) {//文件名中没有后缀
//                model.addAttribute("msg", "文件格式不正确");
//            } else {
//                //获取文件后缀
//                String last = "";
//                for (int i = name.length() - 1; i >= 0; --i) {
//                    if (name.charAt(i) == '.') break;
//                    last = name.charAt(i) + "" + last;
//                }
//
//
//                String fileName = advice.getId() + "." + last;
//
//                //创建文件
////                File file = new File("/markquestion/images/advice/" + fileName);
////                System.out.println(fileName);
////                if (!file.exists()) {
////                    boolean newFile = file.createNewFile();
////                    System.out.println(newFile);
////                }
//
//                img.transferTo(new File("E:\\images\\advice\\" + fileName));
//                advice.setAdviceImg("/images/advice/" + fileName);
//            }
//        }

        //----------------------------------------------------------------------

        //-------------------------服务器-----------------------------------------
        if (!img.isEmpty()) {
            //判断文件是否有后缀
            String name = img.getOriginalFilename();
            if (name != null && !name.contains(".")) {//文件名中没有后缀
                model.addAttribute("msg", "文件格式不正确");
            } else {
                //获取文件后缀
                String last = "";
                for (int i = name.length() - 1; i >= 0; --i) {
                    if (name.charAt(i) == '.') break;
                    last = name.charAt(i) + "" + last;
                }
                //拼接文件名
                String fileName = advice.getId() + "." + last;

//                //创建文件
//                File file = new File("/markquestion/images/advice/" + fileName);
//                if (!file.exists()) {
//                    boolean newFile = file.createNewFile();
//                }
                //保存文件
                img.transferTo(new File("/markquestion/images/advice/"+fileName));
                //更新数据库
                advice.setAdviceImg("/images/advice/" + fileName);
            }
        }
        //-----------------------------------------------------------------------

        adviceMapper.updateById(advice);


        return "redirect:/manage/advice/showAdvice";
    }

    @GetMapping("/toAddAdvice")
    @ApiOperation("跳转到推荐添加页面")
    public String toAddAdvice() {
        return "manager/add/add_advice";
    }

    @PostMapping("/addAdvice")
    @ApiOperation("添加推荐")
    public String addAdvice(@RequestParam("adviceName") String adviceName,
                            @RequestParam("adviceAuthor") String adviceAuthor,
                            @RequestParam("adviceTag") String adviceTag,
                            @RequestParam("adviceDescribe") String adviceDescribe,
                            @RequestParam("adviceImg") MultipartFile img,
                            @RequestParam("adviceContent") String adviceContent,
                            Model model) throws IOException {
        if (!img.isEmpty()) {
            Advice advice = new Advice();
            advice.setAdviceName(adviceName);
            advice.setAdviceAuthor(adviceAuthor);
            advice.setAdviceTag(adviceTag);
            advice.setAdviceDescribe(adviceDescribe);
            advice.setAdviceContent(adviceContent);
            advice.setCreateTime(new Date());
            advice.setUpdateTime(new Date());

            //将该推荐的标签的num进行更新 若不存在该标签，就创建一个标签
            QueryWrapper<AdviceTag> qw = new QueryWrapper<>();
            qw.eq("advice_tag_name",adviceTag);
            AdviceTag tag = adviceTagMapper.selectOne(qw);
            if(tag != null){
                tag.setAdviceTagNum(tag.getAdviceTagNum()+1);
                adviceTagMapper.updateById(tag);
            }else{
                tag = new AdviceTag();
                tag.setAdviceTagName(adviceTag);
                tag.setAdviceTagNum(1);
                tag.setCreateTime(new Date());
                adviceTagMapper.insert(tag);
            }

            //----------------------本地---------------------------------------------

//            //判断文件是否有后缀
//            String name = img.getOriginalFilename();
//            if (name != null && !name.contains(".")) {//文件名中没有后缀
//                model.addAttribute("msg", "文件格式不正确");
//            } else {
//                //获取文件后缀
//                String last = "";
//                for (int i = name.length() - 1; i >= 0; --i) {
//                    if (name.charAt(i) == '.') break;
//                    last = name.charAt(i) + "" + last;
//                }
//
//
//                List<Advice> nums = adviceMapper.selectList(null);
//                int num = nums.size();
//                String fileName = (num + 1) + "." + last;
//
//                //创建文件
////                File file = new File("/markquestion/images/advice/" + fileName);
////                System.out.println(fileName);
////                if (!file.exists()) {
////                    boolean newFile = file.createNewFile();
////                    System.out.println(newFile);
////                }
//
//                img.transferTo(new File("E:\\images\\advice\\" + fileName));
//                advice.setAdviceImg("/images/advice/" + fileName);
//            }

            //----------------------------------------------------------------------

            //-------------------------服务器-----------------------------------------
            //判断文件是否有后缀
            String name = img.getOriginalFilename();
            if (name != null && !name.contains(".")) {//文件名中没有后缀
                model.addAttribute("msg", "文件格式不正确");
            } else {
                //获取文件后缀
                String last = "";
                for (int i = name.length() - 1; i >= 0; --i) {
                    if (name.charAt(i) == '.') break;
                    last = name.charAt(i) + "" + last;
                }
            List<Advice> nums = adviceMapper.selectList(null);
            int num = nums.size();
            String fileName = (num + 1) + "." + last;

//                //创建文件
                File file = new File("/markquestion/images/advice/" + fileName);
                if (!file.exists()) {
                    boolean newFile = file.createNewFile();
                }
                //保存文件
                img.transferTo(new File("/markquestion/images/advice/" + fileName));
                //更新数据库
                advice.setAdviceImg("/images/advice/" + fileName);
            }
            //-----------------------------------------------------------------------
            adviceMapper.insert(advice);
        }
        return "redirect:/manage/advice/showAdvice";
    }

    @GetMapping("/showMainAdvice")
    @ApiOperation("后台的推荐详情展示页面")
    public String showMainAdvice(Model model,
                                 @RequestParam("id") Integer id){
        QueryWrapper<Advice> qw = new QueryWrapper<>();
        qw.eq("id",id);
        Advice advice = adviceMapper.selectOne(qw);
        advice.setAdviceContent(MarkDown2HtmlUtils.markdown2Html(advice.getAdviceContent()));
        model.addAttribute("advice",advice);

        String content = MarkDown2HtmlUtils.markdown2Html(advice.getAdviceContent());
        model.addAttribute("content",content);

        //增加浏览数量
        advice.setAdviceWatch(advice.getAdviceWatch()+1);
        adviceMapper.updateById(advice);

        return "manager/advice/show_advice";
    }
}
