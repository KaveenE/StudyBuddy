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
import ws.datamodel.UpgradeAccountReq;

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
    @Consumes(MediaType.TEXT_PLAIN)
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

    @Path("registerStudent")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerStudent(StudentEntity student) {
        try {
            Long id = studentSessionBean.createNewStudent(student);
            return this.retrieveStudentById(id);
        } catch (AlreadyExistsException | InputDataValidationException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("updateStudent")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(StudentEntity student) {
        try {
            studentSessionBean.updateAccountStudent(student);
            return Response.status(Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

//    @Path("upgradeAccount/{studentId}")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response upgradeAccount(@QueryParam("studentId") Long studentId) {
//        try {
//            System.out.println(studentId);
//            studentSessionBean.upgradeAccount(studentId);
//            return Response.status(Status.OK).build();
//        } catch (DoesNotExistException | InputDataValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
//        }   
//    }
//    
    @Path("upgradeAccount")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upgradeAccount(UpgradeAccountReq upgradeAccountReq) {
        if (upgradeAccountReq != null) {
            try {
                System.out.println(">>>>>>>>>>>>>>Upgrade account RWS" + upgradeAccountReq.getStudentEntity());
                studentSessionBean.upgradeAccount(upgradeAccountReq.getStudentEntity());

                return Response.status(Response.Status.OK).build();

            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Upgrade Account request").build();
        }
    }
}
