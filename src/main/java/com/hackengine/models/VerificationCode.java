/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine.models;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muslumoncel
 */
@XmlRootElement
public class VerificationCode {

    private String email, verificationcode;

    public VerificationCode() {
    }

    public VerificationCode(String email, String verificationcode) {
        this.email = email;
        this.verificationcode = verificationcode;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationcode() {
        return verificationcode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }
}
