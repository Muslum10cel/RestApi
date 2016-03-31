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
public class VaccineUpdateResponse {

    private int vaccine_update_response;
    private SQLException exception;

    public VaccineUpdateResponse() {
    }

    public VaccineUpdateResponse(SQLException exception) {
        this.exception = exception;
    }

    public VaccineUpdateResponse(int vaccine_update_response) {
        this.vaccine_update_response = vaccine_update_response;
    }

    public int getVaccine_update_response() {
        return vaccine_update_response;
    }

    public void setVaccine_update_response(int vaccine_update_response) {
        this.vaccine_update_response = vaccine_update_response;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }

}
