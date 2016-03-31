/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine_er.models;

import javax.mail.MessagingException;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muslumoncel
 */
@XmlRootElement
public class SendMailResponse {

    private int send_mail_response;
    private MessagingException exception;

    public SendMailResponse() {
    }

    public SendMailResponse(MessagingException exception) {
        this.exception = exception;
    }

    public SendMailResponse(int send_mail_response) {
        this.send_mail_response = send_mail_response;
    }

    public int getSend_mail_response() {
        return send_mail_response;
    }

    public void setSend_mail_response(int send_mail_response) {
        this.send_mail_response = send_mail_response;
    }

    public MessagingException getException() {
        return exception;
    }

    public void setException(MessagingException exception) {
        this.exception = exception;
    }
}
