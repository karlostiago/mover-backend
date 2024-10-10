package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.SenderRepository;
import com.ctsousa.mover.service.SenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import static com.ctsousa.mover.core.util.ImageUtil.addPhotoAttachment;
import static com.ctsousa.mover.core.util.ImageUtil.isPhotoValid;

@Component
public class SenderServiceImpl extends BaseServiceImpl<SenderEntity, Long> implements SenderService  {
    private static final Logger logger = LoggerFactory.getLogger(SenderServiceImpl.class);

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

    @Override
    public void sendPhotosForAnalysis(String emailAnalyst, Long contractId, List<InspectionPhotoEntity> photos) {
        logger.info("Enviando e-mail para {} com fotos do contrato de id {}", emailAnalyst, contractId);

        String emailBody = "Fotos para análise do contrato " + contractId + ".\n\n";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailAnalyst);
            helper.setSubject("Análise de Fotos - Contrato ID: " + contractId);
            helper.setText(emailBody, true);

            for (InspectionPhotoEntity photo : photos) {
                if (isPhotoValid(photo)) {
                    addPhotoAttachment(helper, photo);
                } else {
                    logger.warn("A foto com ID {} é inválida ou não contém dados.", photo != null ? photo.getId() : "null");
                }
            }

            javaMailSender.send(mimeMessage);
            logger.info("E-mail enviado com sucesso para {}.", emailAnalyst);
        } catch (MessagingException e) {
            logger.error("Falha ao enviar o e-mail: {}", e.getMessage());
            throw new NotificationException("Falha ao enviar o e-mail com as fotos para análise.", Severity.ERROR);

        } catch (Exception e) {
            logger.error("Erro inesperado: {}", e.getMessage());
            throw new NotificationException("Erro inesperado ao enviar o e-mail.", Severity.ERROR);
        }
    }
}