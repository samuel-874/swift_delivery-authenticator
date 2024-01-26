package com.swiftdelivery.authenticator.model.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;



    public void sendSimpleMail(String toEmail,String subject,String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("samuelab846@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Mail sent to " + toEmail);
    }

    public void sendMail(
            String toEmail,
            String subject,
            String body
    ) throws MessagingException,
            UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        helper.setFrom(new InternetAddress("samuelabiodun1205@gmail.com","Swift Delivery"));
        helper.setTo(toEmail);
        helper.setText(body,true);
        helper.setSubject(subject);

        mailSender.send(message);
        System.out.println("Custom mail sent to " + toEmail);
    }

}
