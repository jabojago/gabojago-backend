package com.example.gabojago_server.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePwEmailService {

    private final JavaMailSender emailsender;

    private String ePw = createKey(); //인증번호

    @Value("${spring.mail.username}")
    private String id;

    private static final String SPECIAL_CHARS = "!@#$%^&*";

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + ePw);

        MimeMessage message = emailsender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);    //수신자
        message.setSubject("가보자Go 임시 비밀번호입니다.");   //메일 제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">가보자Go 비밀번호 재설정을 위한 임시 비밃번호입니다.</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 임시 비밀번호로 로그인 후 새로운 비밀번호로 변경 부탁드립니다.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id, "gabojago_Admin"));

        return message;
    }


    //인증 코드 만들기
    public static String createKey(){
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        while(key.length()<8 || !(hasLowerCase && hasUpperCase && hasNumber && hasSpecialChar)){
            int idx = random.nextInt(4);

            switch (idx){
                case 0:
                    if(!hasLowerCase) {
                        key.append((char) ((int) random.nextInt(26) + 97));
                        hasLowerCase = true;
                    }
                    break;
                case 1:
                    if(!hasUpperCase) {
                        key.append((char) ((int) random.nextInt(26) + 65));
                        hasUpperCase = true;
                    }
                    break;
                case 2:
                    if(!hasNumber) {
                        key.append(random.nextInt(10));
                        hasNumber = true;
                    }
                    break;
                case 3:
                    if(!hasSpecialChar) {
                        int charIdx = random.nextInt(SPECIAL_CHARS.length());
                        char specialChar = SPECIAL_CHARS.charAt(charIdx);
                        key.append(specialChar);
                        hasSpecialChar = true;
                    }
                    break;
            }
        }
        return key.toString();
    }

    //메일 발송
    public String sendSimpleMessage(String to) throws Exception {
        MimeMessage message = createMessage(to);

        try{
            emailsender.send(message);
        }catch (MailException es){
            es.printStackTrace();
            throw new IllegalAccessException();
        }
        return ePw;
    }

}
