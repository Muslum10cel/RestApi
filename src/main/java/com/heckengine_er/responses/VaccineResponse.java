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
public class VaccineResponse {

    private String vaccine_name;
    private SQLException exception;

    public VaccineResponse(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }
    
    public VaccineResponse(SQLException exception){
        this.exception=exception;
    }
    
    public VaccineResponse() {
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }
}
