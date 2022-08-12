package com.hty.markquestion;

import com.hty.markquestion.mapper.*;
import com.hty.markquestion.mapper.QuestionMapper;
import com.hty.markquestion.pojo.*;
import com.hty.markquestion.service.CommentReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MarkquestionApplicationTests {
    @Autowired
    AuthorMessageMapper authorMessageMapper;

    @Autowired
    ContestMapper contestMapper;

    @Autowired
    FriendLinkMapper friendLinkMapper;

    @Autowired
    FriendLinkTagMapper friendLinkTagMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    PlatformMapper platformMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionTagMapper questionTagMapper;

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    CommentReplyService commentReplyService;

    @Test
    void test1() {
        System.out.println(commentReplyService.queryCommentReply(8));
    }

}
