/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.StudentSessionBeanLocal;
import entities.StudentEntity;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 * REST Web Service
 *
 * @author larby
 */
@Path("Student")
public class StudentResource {

    StudentSessionBeanLocal studentSessionBean;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of StudentResource
     */
    public StudentResource() {
        studentSessionBean = new SessionBeanLookup().lookupStudentSessionBeanLocal();
    }

    @Path("studentLogin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response studentLogin(@QueryParam("username") String username, @QueryParam("password") String password) {
        try {
            StudentEntity student = studentSessionBean.studentLogin(username, password);
            String result = new ObjectMapper().writeValueAsString(student);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveStudentById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveStudentById(@QueryParam("studentId") Long studentId) {
        try {
            StudentEntity studentEntity = studentSessionBean.retrieveStudentById(studentId);
            String result = new ObjectMapper().writeValueAsString(studentEntity);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("studentRegister")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response studentRegister(StudentEntity student) {
        try {
            Long id = studentSessionBean.createNewStudent(student);
            return Response.status(Status.OK).entity(id).build();
        } catch (AlreadyExistsException | InputDataValidationException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("studentPurchasePremium")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response studentPurchasePremium(StudentEntity student) {
        try {
            studentSessionBean.updateAccountPremium(student);
            return Response.status(Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("studentPurchaseCredits")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response studentPurchaseCredits(StudentEntity student, @QueryParam("credits") Long credits) {
        try {
            studentSessionBean.updateAccountPurchaseCredits(student, credits);
            return Response.status(Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}