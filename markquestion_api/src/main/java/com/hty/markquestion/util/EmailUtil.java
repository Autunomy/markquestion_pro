package com.hty.markquestion.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//邮件发送的工具类
@Component
public class EmailUtil {
    //发件人邮箱
    private String from = "1156388927@qq.com";

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * @param to 收件人
     * @param title 邮件标题
     * @param content 邮件内容
     */
    //发送普通文本邮件
    public void sendTextEmail(String to,String title,String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);//发件人
        message.setTo(to);//收件人
        message.setSubject(title);//邮件名
        message.setText(content);//邮件内容
        javaMailSender.send(message);
    }

    /**
     * @param to 收件人
     * @param title 邮件标题
     * @param content 邮件内容
     */
    //发送HTML文本(有乱码问题)
    public void sendHTMLEmail(String to,String title,String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //是否发送的邮件是富文本（附件，图片，html等）
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);

        messageHelper.setFrom(from);// 发送人的邮箱
        messageHelper.setTo(to);//发给谁  对方邮箱
        messageHelper.setSubject(title);//标题
        messageHelper.setText(content,true);//false，显示原始html代码，无效果

        javaMailSender.send(mimeMessage);
    }
}
