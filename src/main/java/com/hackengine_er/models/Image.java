/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine_er.models;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muslumoncel
 */
@XmlRootElement
public class Image {

    private String username,filename,base64;
    
    public Image() {
    }

    public Image(String username, String filename, String base64) {
        this.username = username;
        this.filename = filename;
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public String getFilename() {
        return filename;
    }

    public String getUsername() {
        return username;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
