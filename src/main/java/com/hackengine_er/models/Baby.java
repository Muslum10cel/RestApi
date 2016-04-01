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
public class Baby {

    private String username, baby_name, date_of_birth;
    private int babyID, vaccineFlag;
    private SQLException exception;

    public Baby() {
    }

    public Baby(SQLException exception) {
        this.exception = exception;
    }

    /**
     * This constructor is used when updating any vaccine
     * 
     * @param babyID
     * @param vaccineFlag
     */
    public Baby(int babyID, int vaccineFlag) {
        this.babyID = babyID;
        this.vaccineFlag = vaccineFlag;
    }

    
    
    public Baby(int babyID, String baby_name) {
        this.babyID = babyID;
        this.baby_name = baby_name;
    }

    /**
     * This constructor is used for adding baby
     * 
     * @param username
     * @param baby_name
     * @param date_of_birth
     */
    
    public Baby(String username, String baby_name, String date_of_birth) {
        this.username = username;
        this.baby_name = baby_name;
        this.date_of_birth = date_of_birth;
    }

    public String getBaby_name() {
        return baby_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getUsername() {
        return username;
    }

    public void setBaby_name(String baby_name) {
        this.baby_name = baby_name;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBabyID() {
        return babyID;
    }

    public int getVaccineFlag() {
        return vaccineFlag;
    }

    public void setBabyID(int babyID) {
        this.babyID = babyID;
    }

    public void setVaccineFlag(int vaccineFlag) {
        this.vaccineFlag = vaccineFlag;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }
}
