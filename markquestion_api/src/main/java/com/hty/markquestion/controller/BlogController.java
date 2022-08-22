package com.hty.markquestion.controller;

import com.alibaba.fastjson.JSON;
import com.hty.markquestion.constant.ResponseMessage;
import com.hty.markquestion.mapper.BlogClassMapper;
import com.hty.markquestion.mapper.BlogCommentMapper;
import com.hty.markquestion.mapper.BlogMapper;
import com.hty.markquestion.mapper.PlatformMapper;
import com.hty.markquestion.pojo.Platform;
import com.hty.markquestion.pojo.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogClassMapper blogClassMapper;
    @Autowired
    BlogCommentMapper blogCommentMapper;
    @Autowired
    PlatformMapper platformMapper;


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
            filename = UUID.randomUUID().toString().replace('-','x');
            filename += last;
            image.transferTo(new File("E:\\images\\blog\\"+filename));
        }
        return "http://localhost:8080/images/blog/"+filename;
    }

    @GetMapping("/delPic")
    public String delPic(String path){
        path = path.substring(path.lastIndexOf("/")+1);
        Response response = null;
        boolean isSuccess = FileSystemUtils.deleteRecursively(new File("E:/images/blog/" + path));
        if(isSuccess){
            response = new Response(ResponseMessage.SUCCESS,null);
        }else{
            response = new Response(ResponseMessage.LOGIN_FAIL,null);
        }
        return JSON.toJSONString(response);
    }




}
