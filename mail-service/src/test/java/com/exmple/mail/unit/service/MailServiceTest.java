package com.exmple.mail.unit.service;

import com.exmple.mail.service.MailService;
import javax.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private MimeMessage mimeMessage;
    @Mock
    private MimeMessageHelper mimeMessageHelper;
    @InjectMocks
    private MailService mailService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(mailService, "mailSenderName", "test@gmail.com");
    }

    @Test
    public void sendMessagePositiveCase() throws Exception {
        String recipient = "test@gmail.com";
        String text = "Text";
        String title = "Title";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        mailService.sendMessage(recipient, text, title);
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }
}
