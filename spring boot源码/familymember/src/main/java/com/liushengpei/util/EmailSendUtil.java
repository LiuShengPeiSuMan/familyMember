package com.liushengpei.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

import static com.liushengpei.util.ConstantToolUtil.EMAIL_FROM;

public class EmailSendUtil {

    /**
     * 发送纯文本邮件
     *
     * @Param to 收件人地址
     * @Param subject 邮件标题
     * @Param text 邮件内容
     */
    public static String pureTextEmail(JavaMailSenderImpl sender,String to, String subject, String text) {
        JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
        String msg = "";
        //定制纯文本邮件信息
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //发件人地址
        mailMessage.setFrom(EMAIL_FROM);
        //收件人地址
        mailMessage.setTo(to);
        //邮件标题
        mailMessage.setSubject(subject);
        //邮件内容
        mailMessage.setText(text);
        try {
            //发送邮件
            sender.send(mailMessage);
            msg = "发送成功";
        } catch (Exception e) {
            msg = "发送失败";
            e.printStackTrace();
        }
        return msg;
    }


    /**
     * 发送带附件和图片的邮件
     *
     * @Param to 收件人地址
     * @Param subject 邮件标题
     * @Param text 邮件内容
     * @Param filePath 附件地址
     * @Param rscId 静态资源唯一标识
     * @Param rscPath 静态资源地址
     */
    public static String enclosureImageEmail(JavaMailSenderImpl sender,String to, String subject, String text,
                                             String filePath, String rscId, String rscPath) {
        JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
        String msg = "";
        //定制复杂邮箱信息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(EMAIL_FROM);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text);
            //设置邮件静态资源
            FileSystemResource resource = new FileSystemResource(new File(rscPath));
            messageHelper.addInline(rscId, resource);
            //设置邮箱附件
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            messageHelper.addAttachment(fileName, file);
            sender.send(message);
            msg = "发送成功";
        } catch (MessagingException e) {
            msg = "发送失败" + e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }
}
