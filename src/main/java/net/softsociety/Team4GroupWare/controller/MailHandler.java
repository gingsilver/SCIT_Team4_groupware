package net.softsociety.Team4GroupWare.controller;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailHandler {

    private final JavaMailSender sender;
    private final MimeMessage mimeMessage;
    private final MimeMessageHelper mimeMessageHelper;

    public MailHandler(JavaMailSender javaMailSender) throws MessagingException {
        this.sender = javaMailSender;
        this.mimeMessage = javaMailSender.createMimeMessage();
        this.mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    }

    public void setFrom(String fromAddress) throws MessagingException {
        mimeMessageHelper.setFrom(fromAddress);
    }

    public void setTo(InternetAddress[] email) throws MessagingException { // 여기를 프로세스 객체로 바꿈
        mimeMessageHelper.setTo(email);
    }

    public void setTo(String[] email) throws MessagingException { // 여기도 프로세스 객체로 바꿈
        mimeMessageHelper.setTo(email);
        InternetAddress[] emailList = new InternetAddress[email.length];
        for (int i = 0; i < emailList.length; i++) {
            emailList[i] = parseEmail(emailList[i]);
        }
        setTo(emailList);
    }

    private InternetAddress parseEmail(InternetAddress internetAddress) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSubject(String subject) throws MessagingException {
        mimeMessageHelper.setSubject(subject);
    }

    public void setText(String text, boolean useHtml) throws MessagingException {
        mimeMessageHelper.setText(text, useHtml);
    }

    public void setAttach(String fileName, String path) throws MessagingException, IOException {
        File file = new ClassPathResource(path).getFile();
        FileSystemResource fsr = new FileSystemResource(file);

        mimeMessageHelper.addAttachment(fileName, fsr);
    }

    public void setInline(String fileName, String path) throws MessagingException, IOException {
        File file = new ClassPathResource(path).getFile();
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        mimeMessageHelper.addInline(fileName, fileSystemResource);
    }

    public void send() {
        sender.send(mimeMessage);
    }

}
