package com.odakota.tms.system.service.email;

import com.odakota.tms.enums.TemplateName;

import java.util.List;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
public interface SendMailService {

    void sendMail(String subject, TemplateName templateName, Map<String, Object> data, List<String> sendToEmails,
                  List<String> ccEmails, List<String> bccEmails);
}
