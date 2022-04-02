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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

    ModuleSessionBeanLocal moduleSessionBean;

    public ModuleResource() {
        moduleSessionBean = new SessionBeanLookup().lookupModuleSessionBeanLocal();
    }

    @Path("retrieveModulesBySchool")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveModulesBySchool(@QueryParam("schoolName") String schoolName) {
        try {
            List<ModuleEntity> Modules = moduleSessionBean.retrieveAllModulesBySchoolName(schoolName);
            String result = new ObjectMapper().writeValueAsString(Modules);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveModule")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveModule(@QueryParam("moduleId") Long moduleId) {
        try {
            ModuleEntity Module = moduleSessionBean.retrieveModuleById(moduleId);
            String result = new ObjectMapper().writeValueAsString(Module);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
