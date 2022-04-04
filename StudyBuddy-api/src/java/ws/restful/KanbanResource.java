/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.KanbanSessionBeanLocal;
import entities.KanbanBoard;
import entities.KanbanCard;
import entities.KanbanList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author SCXY
 */
@Path("Kanban")
public class KanbanResource {

    KanbanSessionBeanLocal kanbanSessionBean;

    @Context
    private UriInfo context;

    public KanbanResource() {
        kanbanSessionBean = new SessionBeanLookup().lookupKanbanSessionBeanLocal();
    }

    @Path("createNewKanbanBoard")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createKanbanBoard(KanbanBoard newKanbanBoard, @QueryParam("groupId") Long groupId) {
        try {
            Long kanbanBoardId = kanbanSessionBean.createNewKanbanBoard(newKanbanBoard, groupId);
            return Response.status(Response.Status.OK).entity(kanbanBoardId).build();
        } catch (DoesNotExistException | InputDataValidationException | AlreadyExistsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("createDefaultKanbanBoard")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDefaultKanbanBoard(@QueryParam("groupId") Long groupId) {
        try {
            Long kanbanBoardId = kanbanSessionBean.createDefaultKanbanBoard(groupId);
            return Response.status(Response.Status.OK).entity(kanbanBoardId).build();
        } catch (DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveKanbanBoardsByGroupId")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveKanbanBoardsByGroupId(@QueryParam("groupId") Long groupId) {
        try {
            List<KanbanBoard> kanbanBoards = kanbanSessionBean.retrieveKanbanBoardsByGroupId(groupId);
            String result = new ObjectMapper().writeValueAsString(kanbanBoards);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("updateKanbanBoard")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateKanbanBoard(KanbanBoard kanbanBoard) {
        try {
            kanbanSessionBean.updateKanbanBoard(kanbanBoard);
            return Response.status(Response.Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("deleteKanbanBoard")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteKanbanBoard(@QueryParam("kanbanBoardId") Long kanbanBoardId) {
        try {
            kanbanSessionBean.deleteKanbanBoard(kanbanBoardId);
            return Response.status(Response.Status.OK).build();
        } catch (DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("createNewKanbanList")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewKanbanList(KanbanList newKanbanList, @QueryParam("kanbanBoardId") Long kanbanBoardId) {
        try {
            Long kanbanListId = kanbanSessionBean.createNewKanbanList(newKanbanList, kanbanBoardId);
            return Response.status(Response.Status.OK).entity(kanbanListId).build();
        } catch (DoesNotExistException | InputDataValidationException | AlreadyExistsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("updateKanbanList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateKanbanList(KanbanList kanbanList) {
        try {
            kanbanSessionBean.updateKanbanList(kanbanList);
            return Response.status(Response.Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("deleteKanbanList")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteKanbanList(@QueryParam("kanbanBoardId") Long kanbanListId) {
        try {
            kanbanSessionBean.deleteKanbanList(kanbanListId);
            return Response.status(Response.Status.OK).build();
        } catch (DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("createNewKanbanCard")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewKanbanCard(KanbanCard newKanbanCard, @QueryParam("kanbanBoardId") Long kanbanListId, @QueryParam("kanbanBoardId") Long authorStudentId) {
        try {
            Long kanbanCardId = kanbanSessionBean.createNewKanbanCard(newKanbanCard, kanbanListId, authorStudentId);
            return Response.status(Response.Status.OK).entity(kanbanCardId).build();
        } catch (DoesNotExistException | InputDataValidationException | AlreadyExistsException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("updateKanbanCard")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateKanbanCard(KanbanCard kanbanCard) {
        try {
            kanbanSessionBean.updateKanbanCard(kanbanCard);
            return Response.status(Response.Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("deleteKanbanCard")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteKanbanCard(@QueryParam("kanbanCardId") Long kanbanCardId) {
        try {
            kanbanSessionBean.deleteKanbanCard(kanbanCardId);
            return Response.status(Response.Status.OK).build();
        } catch (DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
}
