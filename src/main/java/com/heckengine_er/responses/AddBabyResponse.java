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
public class AddBabyResponse {

    private String exception;
    private int add_baby_response;

    public AddBabyResponse() {
    }

    public AddBabyResponse(String exception) {
        this.exception=exception;
    }
    
    public AddBabyResponse(int add_baby_response) {
        this.add_baby_response = add_baby_response;
    }

    public int getAdd_baby_response() {
        return add_baby_response;
    }

    public void setAdd_baby_response(int add_baby_response) {
        this.add_baby_response = add_baby_response;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
