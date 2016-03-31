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
public class UploadImageResponse {

    private int upload_image_response;
    private SQLException exception;

    public UploadImageResponse() {
    }

    public UploadImageResponse(SQLException exception) {
        this.exception = exception;
    }

    public UploadImageResponse(int upload_image_response) {
        this.upload_image_response = upload_image_response;
    }

    public SQLException getException() {
        return exception;
    }

    public int getUpload_image_response() {
        return upload_image_response;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }

    public void setUpload_image_response(int upload_image_response) {
        this.upload_image_response = upload_image_response;
    }
}
