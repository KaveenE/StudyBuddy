/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.GroupEntitySessionBeanLocal;
import entities.GroupEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.exception.AccessRightsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author SCXY
 */
@Path("Group")
public class GroupResource {

    GroupEntitySessionBeanLocal groupEntitySessionBean;

    @Context
    private UriInfo context;

    public GroupResource() {
        groupEntitySessionBean = new SessionBeanLookup().lookupGroupEntitySessionBeanLocal();
    }

    @Path("createGroup/{moduleId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(GroupEntity newGroupEntity,@PathParam("moduleId") Long moduleId) {
        try {
            Long groupId = groupEntitySessionBean.createNewGroupEntity(newGroupEntity, moduleId);
            return Response.status(Status.OK).entity(groupId).build();
        } catch (InputDataValidationException | AlreadyExistsException | DoesNotExistException ex) {
           return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("updateGroup/{studentId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGroup(GroupEntity groupEntityToUpdate,@PathParam("studentId") Long studentId) {
        try {
            groupEntitySessionBean.updateGroup(groupEntityToUpdate, studentId);
            return Response.status(Status.OK).build();
        } catch (AccessRightsException | DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }

}
