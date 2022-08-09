package com.hty.markquestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//解决EmailUtil类中JavaMailSender注入不进去的问题
@Configuration
public class MailConfig {
    @Bean
    public JavaMailSenderImpl JavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");
        mailSender.setUsername("1156388927@qq.com");
        mailSender.setPassword("ojuwpdxmjolabagg");
        return  mailSender;
    }
}
