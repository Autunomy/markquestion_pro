package com.hty.markquestion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

//扫描Mapper文件夹
@MapperScan("com.hty.markquestion.mapper")
@SpringBootApplication
@EnableScheduling//开启定时任务(为了定时发送邮件)
public class MarkquestionApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MarkquestionApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MarkquestionApplication.class);
    }
}
