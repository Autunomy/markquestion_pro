package com.hty.markquestion.controller.manage;

import com.hty.markquestion.mapper.AuthorMessageMapper;
import com.hty.markquestion.pojo.AuthorMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
@Slf4j
@RequestMapping("/manage/author")
public class ManageAuthorMessageController {

    @Autowired
    AuthorMessageMapper authorMessageMapper;

    @GetMapping("/toManageAuthorMessage")
    @ApiOperation("跳转到博主信息管理首页")
    public String toManageAuthorMessage(Model model){
        AuthorMessage authorMessage = authorMessageMapper.selectOne(null);
        model.addAttribute("authorMessage",authorMessage);
        return "manager/manage/manage_author_message";
    }

    @PostMapping("/updateAuthorMessage")
    @ApiOperation("修改博主信息")
    public String updateAuthorMessage(@RequestParam("id") Integer id,
                                      @RequestParam("username") String username,
                                      @RequestParam("gender") Integer gender,
                                      @RequestParam("hobby") String hobby,
                                      @RequestParam("xingzuo") String xingzuo,
                                      @RequestParam("description") String description,
                                      @RequestParam("email") String email,
                                      @RequestParam("qq") String qq,
                                      @RequestParam("wechat") String wechat,
                                      @RequestParam("phone") String phone,
                                      @RequestParam("headPath") String headPath,
                                      @RequestParam("headImg") MultipartFile headImg,
                                      Model model) throws IOException {
        AuthorMessage authorMessage = new AuthorMessage();
        authorMessage.setId(id);
        authorMessage.setUsername(username);
        authorMessage.setGender(gender);
        authorMessage.setHobby(hobby);
        authorMessage.setConstellation(xingzuo);
        authorMessage.setDescription(description);
        authorMessage.setEmail(email);
        authorMessage.setQq(qq);
        authorMessage.setWechat(wechat);
        authorMessage.setPhone(phone);
        authorMessage.setHeadImg(headPath);
        authorMessage.setUpdate_time(new Date());


        //上传头像
        //------------------本地代码---------------------------------------
//        if(!headImg.isEmpty()){
//            //判断文件是否有后缀
//            String name = headImg.getOriginalFilename();
//            if(name != null && !name.contains(".")){//文件名中没有后缀
//                model.addAttribute("msg","文件格式不正确");
//            }else{
//                //获取文件后缀
//                String last = "";
//                for(int i=name.length()-1;i>=0;--i){
//                    if(name.charAt(i) == '.') break;
//                    last = name.charAt(i)+""+last;
//                }
////                System.out.println("后缀----->"+last);
////                System.out.println(ResourceUtils.getURL("classpath:").getPath());
//
//
//                String fileName = "head."+last;
////                System.out.println("文件名---->"+fileName);
//                headImg.transferTo(new File("E:\\images\\head\\"+fileName));
//                authorMessage.setHeadImg("/local/images/head/"+fileName);
//            }
//        }
        //----------------------------------------------------------------

        //-----------------------服务器代码--------------------------------
        if(!headImg.isEmpty()){
            //判断文件是否有后缀
            String name = headImg.getOriginalFilename();
            if(name != null && !name.contains(".")){//文件名中没有后缀
                model.addAttribute("msg","文件格式不正确");
            }else{
                //获取文件后缀
                String last = "";
                for(int i=name.length()-1;i>=0;--i){
                    if(name.charAt(i) == '.') break;
                    last = name.charAt(i)+""+last;
                }
                //拼接文件名
                String fileName = "head."+last;
                //保存文件
                headImg.transferTo(new File("/markquestion/images/head/"+fileName));
                //更新数据库
                authorMessage.setHeadImg("/images/head/"+fileName);
            }
        }
        //----------------------------------------------------------------

        //更新
        int res = authorMessageMapper.updateById(authorMessage);

        return "redirect:/manage/author/toManageAuthorMessage";
    }
}
