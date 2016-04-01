/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heckengine_er.responses;

import java.sql.SQLException;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muslumoncel
 */
@XmlRootElement
public class RegisterUserResponse {

    private int register_response;
    private SQLException exception;
    
    public RegisterUserResponse() {
    }
    
    public RegisterUserResponse(SQLException exception){
        this.exception=exception;
    } 

    public RegisterUserResponse(int response) {
        this.register_response = response;
    }

    public int getRegister_response() {
        return register_response;
    }

    public void setRegister_response(int register_response) {
        this.register_response = register_response;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }
}
