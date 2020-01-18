package com.odakota.tms.system.service.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class SmsService {

    private final AmazonSNS amazonSNS;

    @Autowired
    public SmsService(AmazonSNS amazonSNS) {
        this.amazonSNS = amazonSNS;
    }

    public void sendSMSMessage(String message, String phoneNumber) {
        Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.5") //Sets the max price to 0.50 USD.
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Promotional") //Sets the type to promotional.
                .withDataType("String"));
        amazonSNS.publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
                                              .withMessageAttributes(smsAttributes));
    }
}
