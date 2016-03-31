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
public class RegisterUserResponse {

    private int register_response;
    
    public RegisterUserResponse() {
    }

    public RegisterUserResponse(int response) {
        this.register_response = response;
    }

    public int getResponse() {
        return register_response;
    }

    public void setResponse(int response) {
        this.register_response = response;
    }
}
