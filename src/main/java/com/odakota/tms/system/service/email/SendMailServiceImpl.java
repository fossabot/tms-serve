//package com.odakota.tms.system.service.email;
//
//import com.odakota.tms.enums.TemplateName;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.InputStreamSource;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.messaging.MessagingException;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * @author haidv
// * @version 1.0
// */
//@Service
//public class SendMailServiceImpl implements SendMailService {
//
//    private final TemplateEngine templateEngine;
//
//    @Autowired
//    public SendMailServiceImpl(TemplateEngine templateEngine) {
//        this.templateEngine = templateEngine;
//    }
//
//    public void send(List<String> recipients,
//                     String subject,
//                     TemplateName templateName,
//                     Map<String, Object> data,
//                     String[] attachments) throws MessagingException {
//
//        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, IS_MULTIPART,
//                                                                    StandardCharsets.UTF_8.name());
//        composeMessageHeader(recipients, subject, attachments, mimeMessageHelper);
//        mimeMessageHelper.setText(this.buildMessage(templateName, data), IS_HTML);
//        this.mailSender.send(mimeMessage);
//    }
//
//    public void send(List<String> recipients,
//                     String subject,
//                     TemplateName templateName,
//                     Map<String, Object> data,
//                     String[] attachments,
//                     String imageResourceName,
//                     byte[] imageBytes,
//                     String imageContentType) throws MessagingException {
//
//        InputStreamSource imageSource = new ByteArrayResource(imageBytes);
//        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, IS_MULTIPART,
//                                                                    StandardCharsets.UTF_8.name());
//        composeMessageHeader(recipients, subject, attachments, mimeMessageHelper);
//        mimeMessageHelper.setText(this.buildMessage(templateName, data), IS_HTML);
//        mimeMessageHelper.addInline(imageResourceName, imageSource, imageContentType);
//        this.mailSender.send(mimeMessage);
//    }
//
//    private void composeMessageHeader(List<String> recipients,
//                                      String subject,
//                                      String[] attachments,
//                                      MimeMessageHelper messageHelper) throws MessagingException {
//        String[] recipientsArr = new String[recipients.size()];
//        messageHelper.setFrom(sendMailFrom);
//        messageHelper.setTo(recipients.toArray(recipientsArr));
//        messageHelper.setSubject(subject);
//        if (attachments != null) {
//            for (String filename : attachments) {
//                FileSystemResource file = new FileSystemResource(filename);
//                messageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
//            }
//        }
//    }
//
//    private String buildMessage(TemplateName templateName, Map<String, Object> data) {
//        Context context = new Context();
//        for (Map.Entry<String, Object> entry : data.entrySet()) {
//            context.setVariable(entry.getKey(), entry.getValue());
//        }
//        return templateEngine.process(templateName.getValue(), context);
//    }
//}
