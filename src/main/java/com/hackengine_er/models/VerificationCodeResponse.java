/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine_er.models;

import java.sql.SQLException;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muslumoncel
 */
@XmlRootElement
public class VerificationCodeResponse {

    private int verification_code_response;
    private SQLException exception;

    public VerificationCodeResponse() {
    }

    public VerificationCodeResponse(SQLException exception) {
        this.exception = exception;
    }

    public VerificationCodeResponse(int verification_code_response) {
        this.verification_code_response = verification_code_response;
    }

    public SQLException getException() {
        return exception;
    }

    public int getVerification_code_response() {
        return verification_code_response;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }

    public void setVerification_code_response(int verification_code_response) {
        this.verification_code_response = verification_code_response;
    }
}
