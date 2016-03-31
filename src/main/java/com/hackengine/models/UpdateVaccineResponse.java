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
public class UpdateVaccineResponse {

    private int update_response;

    public UpdateVaccineResponse() {
    }

    public UpdateVaccineResponse(int update_response) {
        this.update_response = update_response;
    }

    public int getUpdate_response() {
        return update_response;
    }

    public void setUpdate_response(int update_response) {
        this.update_response = update_response;
    }
}
