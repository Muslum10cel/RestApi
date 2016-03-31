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
public class VaccineDateResponse {

    private String vaccine_name, vaccine_date;

    public VaccineDateResponse() {
    }

    public VaccineDateResponse(String vaccine_name, String vaccine_date) {
        this.vaccine_name = vaccine_name;
        this.vaccine_date = vaccine_date;
    }

    public String getVaccine_date() {
        return vaccine_date;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setVaccine_date(String vaccine_date) {
        this.vaccine_date = vaccine_date;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }
}
