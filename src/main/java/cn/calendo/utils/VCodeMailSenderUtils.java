package cn.calendo.utils;

import cn.calendo.common.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * mail-sender-utils
 */

@Component
public class VCodeMailSenderUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    public String sendVCodeMail(Integer vcode, String from, String to) {
        String text = "【eghtax】 验证码：" + vcode + "\n" + "15分钟内有效，请勿将验证码泄露给他人，如非本人操作请忽略。";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from + "(eghtax官方)");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("验证码");
        simpleMailMessage.setText(text);
        try {
            javaMailSender.send(simpleMailMessage);
            System.out.println("验证码发送成功");
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return vcode.toString();
    }

}
