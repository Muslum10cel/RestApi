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
public class LogInResponse {

    private byte log_level;
    
    public LogInResponse() {
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
}
