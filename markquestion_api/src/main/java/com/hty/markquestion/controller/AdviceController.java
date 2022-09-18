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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
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
    public String getAdviceTagList(){
        List<AdviceTag> adviceTagList = adviceTagMapper.selectList(null);
        Response response = new Response(ResponseMessage.SUCCESS,adviceTagList);
        return JSON.toJSONString(response);
    }

    @PostMapping("/uploadImg")
    public String uploadImg(MultipartFile file) throws IOException {
        Response response = null;
        //------------------本地代码---------------------------------------
//        if(!file.isEmpty()){
//            //判断文件是否有后缀
//            String name = file.getOriginalFilename();
//            if(name != null && !name.contains(".")){//文件名中没有后缀
//                response = new Response(ResponseMessage.ERROR,"文件格式不正确");
//            }else{
//                //获取文件后缀
//                String last = "";
//                for(int i=name.length()-1;i>=0;--i){
//                    if(name.charAt(i) == '.') break;
//                    last = name.charAt(i)+""+last;
//                }
//                String fileName = UUID.randomUUID().toString().replaceAll("-"," ").substring(0,6)+"."+last;
////                System.out.println("文件名---->"+fileName);
//                file.transferTo(new File("E:\\images\\advice\\"+fileName));
//                response = new Response(ResponseMessage.SUCCESS,fileName);
//            }
//        }
        //----------------------------------------------------------------

        //-----------------------服务器代码--------------------------------
        if(!file.isEmpty()){
            //判断文件是否有后缀
            String name = file.getOriginalFilename();
            if(name != null && !name.contains(".")){//文件名中没有后缀
                response = new Response(ResponseMessage.ERROR,"文件格式不正确");
            }else{
                //获取文件后缀
                String last = "";
                for(int i=name.length()-1;i>=0;--i){
                    if(name.charAt(i) == '.') break;
                    last = name.charAt(i)+""+last;
                }
                //拼接文件名
                String fileName =  UUID.randomUUID().toString().replaceAll("-"," ").substring(0,6)+last;
                //保存文件
                file.transferTo(new File("/markquestion/images/advice/"+fileName));
                response = new Response(ResponseMessage.SUCCESS,fileName);
            }
        }
        //----------------------------------------------------------------
        return JSON.toJSONString(response);
    }

    @GetMapping("/deleteImg")
    public String deleteImg(String name){
        Response response = null;
        //----------------------本地--------------------------
//        String path = "E:\\images\\advice\\"+name;
//        File file = new File(path);
//        if(file.exists()){
//            if(file.delete()){
//                response = new Response(ResponseMessage.SUCCESS);
//            }else{
//                response = new Response(ResponseMessage.ERROR);
//            }
//        }else{
//            response = new Response(ResponseMessage.ERROR);
//        }
        //---------------------服务器-------------------------
        String path = "/markquestion/images/"+name;
        File file = new File(path);
        if(file.exists()){
            if(file.delete()){
                response = new Response(ResponseMessage.SUCCESS);
            }else{
                response = new Response(ResponseMessage.ERROR);
            }
        }else{
            response = new Response(ResponseMessage.ERROR);
        }

        //----------------------------------------------------

        return JSON.toJSONString(response);
    }

    @GetMapping("/addTag")
    public String addTag(String tagName){
        QueryWrapper<AdviceTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("advice_tag_name",tagName);
        AdviceTag adviceTag = adviceTagMapper.selectOne(queryWrapper);
        Response response = null;
        if(adviceTag == null){
            adviceTag = new AdviceTag();
            adviceTag.setAdviceTagName(tagName);
            adviceTag.setAdviceTagNum(0);
            adviceTag.setCreateTime(new Date());
            adviceTagMapper.insert(adviceTag);
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ADVICE_TAG_EXISTS);
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("/addAdvice")
    public String addAdvice(String adviceName,
                            String author,
                            String adviceTag,
                            String adviceDescription,
                            String adviceContent,
                            String adviceImg){
        //推荐标签数量+1
        QueryWrapper<AdviceTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("advice_tag_name",adviceTag);

        AdviceTag adviceTag1 = adviceTagMapper.selectOne(queryWrapper);
        adviceTag1.setAdviceTagNum(adviceTag1.getAdviceTagNum()+1);
        adviceTagMapper.updateById(adviceTag1);

        Advice advice = new Advice();
        advice.setAdviceName(adviceName);
        advice.setAdviceAuthor(author);
        advice.setAdviceTag(adviceTag);
        advice.setAdviceDescribe(adviceDescription);
        advice.setAdviceContent(adviceContent);
        if(adviceImg == null || adviceImg.equals("")){
            adviceImg = "1.JPG";
        }
        advice.setAdviceImg("/images/advice/"+adviceImg);
        advice.setCreateTime(new Date());
        advice.setUpdateTime(new Date());

        int rows = adviceMapper.insert(advice);
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }

    @GetMapping("/deleteAdvice")
    public String deleteAdvice(String id){
        Response response = null;

        Advice advice = adviceMapper.selectById(Integer.valueOf(id));
        //将对标签的数量-1
        QueryWrapper<AdviceTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("advice_tag_name", advice.getAdviceTag());
        AdviceTag adviceTag = adviceTagMapper.selectOne(queryWrapper);
        adviceTag.setAdviceTagNum(adviceTag.getAdviceTagNum()-1);
        adviceTagMapper.updateById(adviceTag);

        //删除图片
        //----------------------本地--------------------------
//        String path = "E:"+advice.getAdviceImg();
//        File file = new File(path);
//        //图片不能是默认图片
//        if(file.exists() && !advice.getAdviceImg().equals("/images/advice/1.JPG")){
//            if(!file.delete()){
//                response = new Response(ResponseMessage.ERROR,"删除图片删除失败");
//            }
//        }else{
//            response = new Response(ResponseMessage.ERROR,"删除图片删除失败");
//        }
        //---------------------服务器-------------------------
        String path = "/markquestion"+advice.getAdviceImg();
        File file = new File(path);
        //图片不能是默认图片
        if(file.exists() && !advice.getAdviceImg().equals("/images/advice/1.JPG")){
            if(!file.delete()){
                response = new Response(ResponseMessage.ERROR,"删除图片删除失败");
            }
        }else{
            response = new Response(ResponseMessage.ERROR,"删除图片删除失败");
        }

        //----------------------------------------------------

        int rows = adviceMapper.deleteById(Integer.valueOf(id));

        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("/updateAdvice")
    public String updateAdvice(String id,
                               String adviceName,
                               String author,
                               String adviceTag,
                               String adviceDescription,
                               String adviceContent,
                               String adviceImg){

        Advice advice = adviceMapper.selectById(Integer.valueOf(id));
        advice.setId(Integer.valueOf(id));
        advice.setAdviceName(adviceName);
        advice.setAdviceAuthor(author);
        advice.setAdviceTag(adviceTag);
        advice.setAdviceDescribe(adviceDescription);
        advice.setAdviceContent(adviceContent);

        //首先删除原图片 如果原图像是默认图片就不删除
        //----------------------本地--------------------------
//        String path = "E:"+advice.getAdviceImg();
//        System.out.println(path);
//        File file = new File(path);
//        if(file.exists() && !advice.getAdviceImg().equals("/images/advice/1.JPG")){
//            file.delete();
//        }
        //---------------------服务器-------------------------
        String path = "/markquestion"+advice.getAdviceImg();
        File file = new File(path);
        if(file.exists() && !advice.getAdviceImg().equals("/images/advice/1.JPG")){
            file.delete();
        }

        //----------------------------------------------------

        advice.setAdviceImg("/images/advice/"+adviceImg);
        advice.setCreateTime(new Date());
        advice.setUpdateTime(new Date());

        Response response = null;
        int rows = adviceMapper.updateById(advice);
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }

    @GetMapping("/deleteAdviceTag")
    public String deleteAdviceTag(String id){
        int rows = adviceTagMapper.deleteById(Integer.valueOf(id));
        Response response = null;
        if(rows == 1){
            response = new Response(ResponseMessage.SUCCESS);
        }else{
            response = new Response(ResponseMessage.ERROR);
        }
        return JSON.toJSONString(response);
    }

    /***
     * 按照名称搜索推荐信息
     * @param search
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/searchAdvice")
    public String searchQuestion(@RequestParam("search") String search,
                                 @RequestParam("currentPage") String currentPage,
                                 @RequestParam("pageSize") String pageSize){
        List<Advice> adviceList = adviceMapper.searchAdvice(search, (Integer.parseInt(currentPage) - 1) * Integer.parseInt(pageSize), Integer.valueOf(pageSize));
        //获取搜索出来的条数 用来分页
        Integer total = adviceMapper.searchCount(search);
        Response response = new Response(ResponseMessage.SUCCESS, adviceList);
        response.setPageInfo(new PageInfo(Integer.valueOf(currentPage), Integer.valueOf(pageSize), total));
        return JSON.toJSONString(response);
    }

}