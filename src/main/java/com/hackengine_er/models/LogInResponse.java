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
public class LogInResponse {

    private SQLException exception;
    private byte log_level;
    private int user_not_available;

    public LogInResponse() {
    }

    public LogInResponse(int user_not_available) {
        this.user_not_available = user_not_available;
    }

    public LogInResponse(SQLException exception) {
        this.exception = exception;
    }

    public LogInResponse(byte log_level) {
        this.log_level = log_level;
    }

    public byte getLog_level() {
        return log_level;
    }

    public void setLog_level(byte log_level) {
        this.log_level = log_level;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }

    public int getUser_not_available() {
        return user_not_available;
    }

    public void setUser_not_available(int user_not_available) {
        this.user_not_available = user_not_available;
    }
}
