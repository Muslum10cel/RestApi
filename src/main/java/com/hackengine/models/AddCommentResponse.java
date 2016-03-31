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
public class AddCommentResponse {

    private int add_comment_status;
    private SQLException exception;

    public AddCommentResponse(int add_comment_status) {
        this.add_comment_status = add_comment_status;
    }

    public AddCommentResponse(SQLException exception) {
        this.exception = exception;
    }

    public AddCommentResponse() {
    }

    public int getAdd_comment_status() {
        return add_comment_status;
    }

    public void setAdd_comment_status(int add_comment_status) {
        this.add_comment_status = add_comment_status;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(SQLException exception) {
        this.exception = exception;
    }
}
