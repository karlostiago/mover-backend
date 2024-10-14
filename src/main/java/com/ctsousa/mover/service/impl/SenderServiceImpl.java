package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.InspectionRepository;
import com.ctsousa.mover.repository.SenderRepository;
import com.ctsousa.mover.service.SenderService;
import static com.ctsousa.mover.core.util.ImageUtil.addPhotoAttachment;
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

@Component
public class SenderServiceImpl extends BaseServiceImpl<SenderEntity, Long> implements SenderService {
    private static final Logger logger = LoggerFactory.getLogger(SenderServiceImpl.class);

    private final SenderRepository senderRepository;
    private final InspectionRepository inspectionRepository;
    private final JavaMailSender javaMailSender;

    public SenderServiceImpl(JpaRepository<SenderEntity, Long> repository,
                             SenderRepository securityCodeRepository,
                             InspectionRepository inspectionRepository,
                             JavaMailSender javaMailSender) {
        super(repository);
        this.senderRepository = securityCodeRepository;
        this.inspectionRepository = inspectionRepository;
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
            MimeMessage mimeMessage = createMimeMessage(emailAnalyst, "Análise de Fotos - Contrato ID: " + contractId, emailBody);
            processPhotosForEmail(mimeMessage, photos);
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

    @Override
    public void sendApprovalEmail(String clientEmail, Long contractId) {
        logger.info("Enviando e-mail de aprovação para o cliente com email: {} para o contrato de id {}", clientEmail, contractId);

        String emailBody = "Sua auto inspeção para o contrato " + contractId + " foi aprovada com sucesso.\n\nParabéns!";

        try {
            MimeMessage mimeMessage = createMimeMessage(clientEmail, "Aprovação de Inspeção - Contrato ID: " + contractId, emailBody);
            javaMailSender.send(mimeMessage);
            logger.info("E-mail de aprovação enviado com sucesso para o cliente {}.", clientEmail);
        } catch (MessagingException e) {
            logger.error("Falha ao enviar o e-mail de aprovação: {}", e.getMessage());
            throw new NotificationException("Falha ao enviar o e-mail de aprovação.", Severity.ERROR);
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar o e-mail de aprovação: {}", e.getMessage());
            throw new NotificationException("Erro inesperado ao enviar o e-mail.", Severity.ERROR);
        }
    }

    @Override
    public void sendRejectionEmail(String clientEmail, Long contractId, List<InspectionPhotoEntity> photos) {
        logger.info("Enviando e-mail de rejeição para o cliente com email: {} para o contrato de id {}", clientEmail, contractId);

        String emailBody = "Sua auto inspeção para o contrato " + contractId + " foi rejeitada.\n\nPor favor, refaça a inspeção e envie novas fotos para análise.";

        try {
            MimeMessage mimeMessage = createMimeMessage(clientEmail, "Rejeição de Inspeção - Contrato ID: " + contractId, emailBody);
            processPhotosForEmail(mimeMessage, photos);
            javaMailSender.send(mimeMessage);
            logger.info("E-mail de rejeição enviado com sucesso para o cliente {}.", clientEmail);
        } catch (MessagingException e) {
            logger.error("Falha ao enviar o e-mail de rejeição: {}", e.getMessage());
            throw new NotificationException("Falha ao enviar o e-mail de rejeição.", Severity.ERROR);
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar o e-mail de rejeição: {}", e.getMessage());
            throw new NotificationException("Erro inesperado ao enviar o e-mail.", Severity.ERROR);
        }
    }

    private MimeMessage createMimeMessage(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        return mimeMessage;
    }

    private void processPhotosForEmail(MimeMessage mimeMessage, List<InspectionPhotoEntity> photos) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String emailBody = "Fotos para análise do contrato.\n\n";
        helper.setText(emailBody, true);

        for (InspectionPhotoEntity photo : photos) {
            if (inspectionRepository.existsPhotoInAnalysis(photo)) {
                addPhotoAttachment(helper, photo);
            } else if (inspectionRepository.existsPhotoRejected(photo)) {
                logger.warn("A foto com ID {} foi rejeitada.", photo.getId());
            } else if (inspectionRepository.existsPhotoApproved(photo)) {
                logger.info("A foto com ID {} foi aprovada.", photo.getId());
            } else {
                logger.warn("A foto com ID {} é inválida ou não contém dados.", photo.getId());
            }
        }
    }
}