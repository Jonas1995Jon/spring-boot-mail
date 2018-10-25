package com.jonas.mail.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Desc：发送邮件
 * Author Jonas
 * 2018/10/22 8:55
 */
@Service
public class MailService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    public void hello(){
        System.out.println("Hello World!");
    }

    //简单的文本邮件
    public void sendSimpleMail(String to, String subject, String content){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);

    }

    //html格式的邮件
    public void sendHtmlMail(String to, String subject, String content) {

        logger.info("发送HTML格式的邮件开始：{}, {}, {}", to, subject, content);

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(message);
            logger.info("发送HTML格式的邮件成功");
        } catch (MessagingException e) {
            logger.error("发送HTML格式的邮件失败：", e);
        }

    }

    //附件邮件
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);
        //发送多个附件，可以通过传递集合遍历
        //helper.addAttachment(fileName2, file2);
        mailSender.send(message);

    }

    //图片邮件
    public void sendInlinResourceMail(String to, String subject, String content, String recPath, String recId) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        FileSystemResource res = new FileSystemResource(new File(recPath));
        helper.addInline(recId, res);
        //发送多张图片，可以通过传递集合遍历
        //helper.addInline(recId2, res2);
        mailSender.send(message);

    }

}
