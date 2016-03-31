/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine_er;

import com.hackengine.models.AddBabyResponse;
import com.hackengine.models.Baby;
import com.hackengine.models.LogInResponse;
import com.hackengine.models.User;
import com.hackengine.models.RegisterUserResponse;
import com.hackengine.muslumyusuf.DBOperations;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author muslumoncel
 */
@Path("vaccineapp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiOperations {

    private static final DBOperations dbOperations = new DBOperations();

    @POST
    @Path("/log_in")
    public LogInResponse logIn(User loginUser) {
        return new LogInResponse((byte) dbOperations.logIn(loginUser));
    }

    @POST
    @Path("/register")
    public RegisterUserResponse register(User registerUser) {
        return new RegisterUserResponse(dbOperations.register(registerUser));
    }

    @POST
    @Path("/addBaby")
    public AddBabyResponse addBaby(Baby baby) {
        return new AddBabyResponse(dbOperations.addBaby(baby));
    }

    
    
    
}
