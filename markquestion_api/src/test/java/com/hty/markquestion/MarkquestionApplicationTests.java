package com.hty.markquestion;

import com.hty.markquestion.mapper.*;
import com.hty.markquestion.pojo.*;
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

    @Test
    void test1() {
        List<Video> friendLinks = videoMapper.selectList(null);
        System.out.println(friendLinks);
    }

}
