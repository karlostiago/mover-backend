package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.repository.SenderRepository;
import com.ctsousa.mover.service.SenderService;
import com.ctsousa.mover.service.customServices.CustomSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;
import java.time.LocalDateTime;

@Component
public class SenderServiceImpl extends AbstractServiceImpl <SenderEntity, Long> implements SenderService, CustomSenderService  {

    private final SenderRepository senderRepository;
    private final JavaMailSender javaMailSender;

    public SenderServiceImpl(JpaRepository<SenderEntity, Long> repository, SenderRepository securityCodeRepository, JavaMailSender javaMailSender) {
        super(repository);
        this.senderRepository = securityCodeRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public SenderEntity sendSecurityCode(Long clientId, String email) {
        String codeSecurity = RandomStringUtils.randomNumeric(6);
        SenderEntity securityCode = new SenderEntity();

        securityCode.setClientId(clientId);
        securityCode.setEmail(email);
        securityCode.setCode(codeSecurity);
        securityCode.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        repository.save(securityCode);
        sendEmailSecurityCode(email, codeSecurity);
        return securityCode;
    }

    @Override
    @Async
    public void sendEmailSecurityCode(String email, String code) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Código de Segurança");
            helper.setText("Seu código de segurança é: " + code, true);

            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new NotificationException("Não foi possível enviar o código de segurança por e-mail.");
        }
    }

    public SenderEntity validateSecurityCode(Long clientId, String email, String code) {
        SenderEntity securityCode = senderRepository.findByClientIdAndEmailAndCode(clientId, email, code);

        if (securityCode != null && securityCode.getExpiryDate().isAfter(LocalDateTime.now())) {
            return securityCode;
        }
         throw new NotificationException("Codigo enviado foi expirado......");
    }
}
