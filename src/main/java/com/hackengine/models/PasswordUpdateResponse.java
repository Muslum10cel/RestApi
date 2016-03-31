/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine.models;

import java.sql.SQLException;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muslumoncel
 */
@XmlRootElement
public class PasswordUpdateResponse {

    private int password_update_response;
    private SQLException exception;

    public PasswordUpdateResponse() {
    }

    public PasswordUpdateResponse(SQLException exception) {
        this.exception = exception;
    }

    public PasswordUpdateResponse(int password_update_response) {
        this.password_update_response = password_update_response;
    }

    public int getPassword_update_response() {
        return password_update_response;
    }

    public void setPassword_update_response(int password_update_response) {
        this.password_update_response = password_update_response;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }
}
