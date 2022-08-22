package com.hty.markquestion.controller;

import com.hty.markquestion.mapper.BlogClassMapper;
import com.hty.markquestion.mapper.BlogCommentMapper;
import com.hty.markquestion.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogClassMapper blogClassMapper;
    @Autowired
    BlogCommentMapper blogCommentMapper;




}
