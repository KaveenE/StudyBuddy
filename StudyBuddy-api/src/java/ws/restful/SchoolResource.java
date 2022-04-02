/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.SchoolSessionBeanLocal;
import entities.SchoolEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;

/**
 *
 * @author SCXY
 */
@Path("School")
public class SchoolResource {

    SchoolSessionBeanLocal schoolSessionBean = lookupSchoolSessionBeanLocal();

    @javax.ws.rs.core.Context
    private UriInfo context;

    public SchoolResource() {
        schoolSessionBean = lookupSchoolSessionBeanLocal();
    }

    @Path("retrieveAllSchools")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSchools() {
        try {
            List<SchoolEntity> schools = schoolSessionBean.retrieveAllSchools();
            String result = new ObjectMapper().writeValueAsString(schools);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    //not sure if needed
    @Path("retrieveSchool/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveBook(@PathParam("id") Long id) {
        try {
            SchoolEntity school = schoolSessionBean.retrieveSchoolById(id);
            String result = new ObjectMapper().writeValueAsString(school);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private SchoolSessionBeanLocal lookupSchoolSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (SchoolSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/SchoolSessionBean!ejb.session.stateless.SchoolSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
