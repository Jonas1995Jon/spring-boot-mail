package com.jonas.mail.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import static org.junit.Assert.*;

/**
 * Desc：
 * Author Jonas
 * 2018/10/22 8:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Resource
    TemplateEngine templateEngine;

    @Test
    public void hello() {

        mailService.hello();

    }

    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail("jonas1995@vip.qq.com", "请假", "领导，您好！今天我生病了，我想请一天假。");
    }

    @Test
    public void sendHtmlMail() throws MessagingException {

        String content = "<html>\n"+
                "<body>\n"+
                    "<h3>领导，您好！今天我生病了，我想请一天假。<h3/>"+
                "<body/>\n"+
                "<html/>";

        mailService.sendHtmlMail("jonas1995@vip.qq.com", "请假", content);
    }

    @Test
    public void sendAttachmentsMail() throws MessagingException {
        String filePath = "C:/4042/公司账号.txt";
        mailService.sendAttachmentsMail("jonas1995@vip.qq.com", "公司账号", "这个附件里面包含了我在公司使用的全部帐号", filePath);
    }

    @Test
    public void sendInlinResourceMail() throws MessagingException {
        String imgPath = "C:/4042/1.jpg";
        String recId = "neo01";
        String content = "<html>\n"+
                    "<body>\n领导，您好！今天我生病了，我想请一天假。图片："+
                        "<img src='cid:"+recId+"' style=\"width:50%\"></img>"+
                    "<body/>" +
                    "<html/>";

        mailService.sendInlinResourceMail("jonas1995@vip.qq.com", "请假", content, imgPath, recId);
    }

    //模板邮件
    @Test
    public void templateMail() throws MessagingException {
        Context context = new Context();
        context.setVariable("name", "list");

        String emailContent = templateEngine.process("emailTemplate", context);

        mailService.sendHtmlMail("jonas1995@vip.qq.com", "这是一个模板邮件", emailContent);
    }
}