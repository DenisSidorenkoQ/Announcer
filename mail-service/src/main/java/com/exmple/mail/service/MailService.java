package com.exmple.mail.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableAsync
public class MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailSenderName;

    // TODO: давай порассуждаем какие могут быть проблемы с @Async? Как можно сделать без @Async, но с гарантированной
    //  доставкой сообщения? Сразу предупреждаю, это вопрос со звездочкой.
    // Проблемы с @Async могут быть связаны с тем, что асинхронные методы не гарантируют доставку сообщения.
    // Если нам нужна гарантированная доставка сообщения, то можно использовать различные брокеры сообщений и возможно Spring Integration.
    // К примеру в Kafka гарантировать доставку сообщений можно при помощи acknowledgements и повторной отправке сообщений в случае ошибки.
    @Async
    public void sendMessage(String recipient, String text, String title) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailSenderName);
            helper.setTo(recipient);
            helper.setSubject(title);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            // TODO: какие тут проблемы?
            // При неудачной отправке сообщения мы не оповещаем пользователя и не решаем появившуюся проблеме
            e.printStackTrace();
        }

    }
}
