/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine.models;

import java.sql.SQLException;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muslumoncel
 */
@XmlRootElement
public class Comment {

    private String username, vaccine_name, comment;
    private Date comment_date;
    private SQLException exception;

    public Comment(SQLException exception) {
        this.exception = exception;
    }

    public Comment() {
    }

    public Comment(String username, String vaccine_name, String comment) {
        this.username = username;
        this.vaccine_name = vaccine_name;
        this.comment = comment;
    }

    public Comment(String username, String vaccine_name, String comment, Date comment_date) {
        this.username = username;
        this.vaccine_name = vaccine_name;
        this.comment = comment;
        this.comment_date = comment_date;
    }

    public String getComment() {
        return comment;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public String getUsername() {
        return username;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }

    public void setUsername(String username) {
        this.username = username;
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
