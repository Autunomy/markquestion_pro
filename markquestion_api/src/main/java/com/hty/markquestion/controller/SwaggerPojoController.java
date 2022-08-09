package com.hty.markquestion.controller;

import com.hty.markquestion.pojo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

//这个controller是为了让swagger对实体类也返回文档
@Controller
public class SwaggerPojoController {
    @PostMapping("/authorMessage")
    public AuthorMessage authorMessage(){
        return new AuthorMessage();
    }

    @PostMapping("/contest")
    public Contest contest(){
        return new Contest();
    }

    @PostMapping("/friendLink")
    public FriendLink friendLink(){
        return new FriendLink();
    }

    @PostMapping("/friendLinkTag")
    public FriendLinkTag friendLinkTag(){
        return new FriendLinkTag();
    }

    @PostMapping("/log")
    public Log log(){
        return new Log();
    }

    @PostMapping("/platform")
    public Platform platform(){
        return new Platform();
    }

    @PostMapping("/question")
    public Question question(){
        return new Question();
    }

    @PostMapping("/questionTag")
    public QuestionTag questionTag(){
        return new QuestionTag();
    }

    @PostMapping("/video")
    public Video video(){
        return new Video();
    }

    @PostMapping("/accessLog")
    public AccessLog accessLog(){
        return new AccessLog();
    }

    @PostMapping("/accessMessage")
    public AccessMessage accessMessage(){
        return new AccessMessage();
    }

    @PostMapping("/advice")
    public Advice advice(){
        return new Advice();
    }

    @PostMapping("/adviceTag")
    public AdviceTag adviceTag(){
        return new AdviceTag();
    }

    @PostMapping("/webBasicMessage")
    public WebBasicMessage webBasicMessage(){
        return new WebBasicMessage();
    }

    @PostMapping("/practice")
    public Practice practice(){
        return new Practice();
    }

    @PostMapping("/practiceTag")
    public PracticeTag practiceTag(){
        return new PracticeTag();
    }

    @PostMapping("/practiceUser")
    public PracticeUser practiceUser(){
        return new PracticeUser();
    }
}
