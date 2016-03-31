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
public class VaccineStatusResponse {

    private String vaccine_name;
    private int status;
    private SQLException exception;

    public VaccineStatusResponse() {
    }

    public VaccineStatusResponse(SQLException exception) {
        this.exception = exception;
    }

    public VaccineStatusResponse(String vaccine_name, int status) {
        this.vaccine_name = vaccine_name;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setStatus(int status) {
        this.status = status;
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
