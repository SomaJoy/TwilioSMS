package com.twilioSms.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilioSms.payload.SmsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send-sms")
public class SmsController {

    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNo;

    @PostMapping
    public ResponseEntity<String> sendSms(@RequestBody SmsRequest smsRequest){
        try{
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message message = Message.creator(
                    new PhoneNumber(smsRequest.getTo()),
                    new PhoneNumber(twilioPhoneNo),
                    smsRequest.getMessage()).create();
            return ResponseEntity.ok("SMS sent successfully. SID : "+ message.getAccountSid());
        }
        catch (Exception e){
            return new ResponseEntity<>("Failed to send SMS : "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
