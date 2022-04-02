/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.ModuleSessionBeanLocal;
import entities.ModuleEntity;
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
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;

/**
 *
 * @author SCXY
 */
@Path("Module")
public class ModuleResource {

    ModuleSessionBeanLocal moduleSessionBean = lookupModuleSessionBeanLocal();

    public ModuleResource() {
        moduleSessionBean = lookupModuleSessionBeanLocal();
    }

    @Path("retrieveModulesBySchool/{school}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveModulesBySchool(@PathParam("school") String school) {
        try {
            List<ModuleEntity> Modules = moduleSessionBean.retrieveAllModulesBySchoolName(school);
            String result = new ObjectMapper().writeValueAsString(Modules);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveModule/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveModule(@PathParam("id") Long id) {
        try {
            ModuleEntity Module = moduleSessionBean.retrieveModuleById(id);
            String result = new ObjectMapper().writeValueAsString(Module);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private ModuleSessionBeanLocal lookupModuleSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (ModuleSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/ModuleSessionBean!ejb.session.stateless.ModuleSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
