package com.gamingarena.services.mail;

import com.gamingarena.payload.RespStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmailId;



    public RespStatusEnum sendEmail(
            String to,
            String subject,
            String body
    ){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmailId);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try{
            javaMailSender.send(message);
            return RespStatusEnum.SUCCESS;

        }catch (Exception ex){

            ex.printStackTrace();
            return RespStatusEnum.ERROR;

        }
    }


}
