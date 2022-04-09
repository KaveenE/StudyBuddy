/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.GroupEntitySessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entities.GroupEntity;
import entities.StudentEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

    StudentSessionBeanLocal studentSessionBean = lookupStudentSessionBeanLocal();

    GroupEntitySessionBeanLocal groupEntitySessionBean;

    @Context
    private UriInfo context;

    public GroupResource() {
        groupEntitySessionBean = new SessionBeanLookup().lookupGroupEntitySessionBeanLocal();
    }

    @Path("retrieveGroupsByStudentId/{studentId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveGroupsByStudentId(@PathParam("studentId") Long studentId) {
        StudentEntity student;
        try {
            student = studentSessionBean.retrieveStudentById(studentId);
            List<GroupEntity> groups = student.getGroups();
            String result = new ObjectMapper().writeValueAsString(groups);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {            
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("groupById/{groupId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveGroupById(@PathParam("groupId") Long groupId) {
        try {
            GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(groupId);
            
            String res = new ObjectMapper().writeValueAsString(group);
            return Response.ok(res, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {            
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllOpenGroups/{schoolId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllOpenGroups(@PathParam("schoolId") Long schoolId) {
        try {
            List<GroupEntity> groups = groupEntitySessionBean.retrieveAllOpenGroups(schoolId);
            String result = new ObjectMapper().writeValueAsString(groups);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("createGroup/{moduleId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(GroupEntity newGroupEntity, @PathParam("moduleId") Long moduleId) {
        try {
            Long groupId = groupEntitySessionBean.createNewGroupEntity(newGroupEntity, moduleId);
            return Response.status(Status.OK).entity(groupId).build();
        } catch (InputDataValidationException | AlreadyExistsException | DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("applyToGroup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response applyToGroup(@QueryParam("studentId") Long studentId, @QueryParam("groupId") Long groupId) {
        try {
            groupEntitySessionBean.applyToGroup(groupId, studentId);
            return Response.status(Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("updateGroup/{studentId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGroup(GroupEntity groupEntityToUpdate, @PathParam("studentId") Long studentId) {
        try {
            groupEntitySessionBean.updateGroup(groupEntityToUpdate, studentId);
            return Response.status(Status.OK).build();
        } catch (AccessRightsException | DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
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
