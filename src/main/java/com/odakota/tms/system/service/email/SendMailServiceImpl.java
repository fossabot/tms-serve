package com.odakota.tms.system.service.email;

import com.odakota.tms.enums.TemplateName;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class SendMailServiceImpl implements SendMailService {

    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    private static final String SEND_GRID_ENDPOINT_SEND_EMAIL = "mail/send";
    private final TemplateEngine templateEngine;
    @Value("${email.send.from}")
    private String sendMailFrom;
    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    @Autowired
    public SendMailServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMail(String subject, TemplateName templateName, Map<String, Object> data, List<String> sendToEmails,
                         List<String> ccEmails, List<String> bccEmails) {
        Mail mail = buildMailToSend(subject, templateName, data, sendToEmails, ccEmails, bccEmails);
        send(mail);
    }

    private void send(Mail mail) {
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(SEND_GRID_ENDPOINT_SEND_EMAIL);
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Mail buildMailToSend(String subject, TemplateName templateName, Map<String, Object> data,
                                 List<String> sendToEmails, List<String> ccEmails, List<String> bccEmails) {
        Email fromEmail = new Email();
        fromEmail.setEmail(sendMailFrom);
        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.setSubject(subject);
        mail.addPersonalization(getPersonalization(sendToEmails, ccEmails, bccEmails));

        Content content = new Content();
        content.setType(CONTENT_TYPE_TEXT_PLAIN);
        content.setValue(buildContent(templateName, data));
        mail.addContent(content);
        return mail;
    }

    private Personalization getPersonalization(List<String> sendToEmails, List<String> ccEmails,
                                               List<String> bccEmails) {
        Personalization personalization = new Personalization();

        //Add sendToEmails
        if (sendToEmails != null) {
            for (String email : sendToEmails) {
                Email to = new Email();
                to.setEmail(email);
                personalization.addTo(to);
            }
        }

        //Add ccEmail
        if (ccEmails != null) {
            for (String email : ccEmails) {
                Email cc = new Email();
                cc.setEmail(email);
                personalization.addCc(cc);
            }
        }

        //Add bccEmail
        if (bccEmails != null) {
            for (String email : bccEmails) {
                Email bcc = new Email();
                bcc.setEmail(email);
                personalization.addBcc(bcc);
            }
        }
        return personalization;
    }

    private String buildContent(TemplateName templateName, Map<String, Object> data) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return templateEngine.process(templateName.getValue(), context);
    }

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
}
