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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.AlreadyExistsException;
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

    StudentSessionBeanLocal studentSessionBean = lookupStudentSessionBeanLocal();

    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of StudentResource
     */
    public StudentResource() {
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
    
    @Path("studentRegister")
    @POST
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

    private StudentSessionBeanLocal lookupStudentSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StudentSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/StudentSessionBean!ejb.session.stateless.StudentSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
