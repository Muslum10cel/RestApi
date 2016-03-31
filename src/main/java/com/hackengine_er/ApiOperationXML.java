/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine_er;

import com.hackengine.models.AddBabyResponse;
import com.hackengine.models.AddCommentResponse;
import com.hackengine.models.Baby;
import com.hackengine.models.Comment;
import com.hackengine.models.Image;
import com.hackengine.models.LogInResponse;
import com.hackengine.models.PasswordUpdateResponse;
import com.hackengine.models.User;
import com.hackengine.models.RegisterUserResponse;
import com.hackengine.models.SendMailResponse;
import com.hackengine.models.UploadImageResponse;
import com.hackengine.models.Vaccine;
import com.hackengine.models.VaccineDateResponse;
import com.hackengine.models.VaccineStatusResponse;
import com.hackengine.models.VaccineUpdateResponse;
import com.hackengine.models.VerificationCode;
import com.hackengine.models.VerificationCodeResponse;
import com.hackengine.muslumyusuf.DBOperations;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author muslumoncel
 */
@Path("vaccineapp/xml")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiOperationXML {

    private static final DBOperations dbOperations = new DBOperations();

    @POST
    @Path("/log_in")
    public LogInResponse logIn(User loginUser) {
        return dbOperations.logIn(loginUser);
    }

    @POST
    @Path("/register")
    public RegisterUserResponse register(User registerUser) {
        return dbOperations.register(registerUser);
    }

    @POST
    @Path("/addBaby")
    public AddBabyResponse addBaby(Baby baby) {
        return dbOperations.addBaby(baby);
    }

    @PUT
    @Path("/update_DaBT_IPA_HIB")
    public VaccineUpdateResponse update_DaBT_IPA_HIB(Baby baby) {
        return dbOperations.update_DaBT_IPA_HIB(baby);
    }

    @PUT
    @Path("/update_Hepatit_A")
    public VaccineUpdateResponse update_Hepatit_A(Baby baby) {
        return dbOperations.update_Hepatit_A(baby);
    }

    @PUT
    @Path("/update_Hepatit_B")
    public VaccineUpdateResponse update_Hepatit_B(Baby baby) {
        return dbOperations.update_Hepatit_B(baby);
    }

    @PUT
    @Path("/update_KKK")
    public VaccineUpdateResponse update_KKK(Baby baby) {
        return dbOperations.update_KKK(baby);
    }

    @PUT
    @Path("/update_KPA")
    public VaccineUpdateResponse update_KPA(Baby baby) {
        return dbOperations.update_KPA(baby);
    }

    @PUT
    @Path("/update_OPA")
    public VaccineUpdateResponse update_OPA(Baby baby) {
        return dbOperations.update_OPA(baby);
    }

    @PUT
    @Path("/update_RVA")
    public VaccineUpdateResponse update_RVA(Baby baby) {
        return dbOperations.update_RVA(baby);
    }

    @PUT
    @Path("/update_Vaccines")
    public VaccineUpdateResponse update_Vaccines(Baby baby) {
        return dbOperations.update_Vaccines(baby);
    }

    @POST
    @Path("/addComment")
    public AddCommentResponse addComment(Comment comment) {
        return dbOperations.addComment(comment);
    }

    @GET
    @Path("/getVaccineStatusDetails/{baby_id}")
    public List<VaccineStatusResponse> getVaccineStatusDetailsOfBaby(@PathParam("baby_id") int baby_id) {
        return dbOperations.completedAndIncompletedVaccines(baby_id);
    }

    @POST
    @Path("/updatePassword")
    public PasswordUpdateResponse updatePassword(User user) {
        return dbOperations.forgottenPassword(user);
    }

    @POST
    @Path("/getComments")
    public List<Comment> getComments(Vaccine vaccine) {
        return dbOperations.getComments(vaccine);
    }

    @POST
    @Path("/getBabies")
    public List<Baby> getBabies(User user) {
        return dbOperations.getBabies(user);
    }

    @GET
    @Path("/getAllVaccineNames")
    public List<Vaccine> getAllVaccineNames() {
        return dbOperations.getAllVaccineNames();
    }

    @GET
    @Path("/getVaccineDateDetails/{baby_id}")
    public List<VaccineDateResponse> getVaccineDateDetailsOfBaby(@PathParam("baby_id") int baby_id) {
        return dbOperations.getVaccinesDateDetailsOfBaby(baby_id);
    }

    @POST
    @Path("/sendMailToUser")
    public SendMailResponse sendMailToUser(User user) {
        return dbOperations.sendMailToUser(user);
    }

    @POST
    @Path("/checkVerificationCode")
    public VerificationCodeResponse checkVerificationCode(VerificationCode code) {
        return dbOperations.checkVerificationCode(code);
    }

    @POST
    @Path("/uploadImage")
    public UploadImageResponse uploadImage(Image image) {
        return dbOperations.uploadImage(image);
    }
}