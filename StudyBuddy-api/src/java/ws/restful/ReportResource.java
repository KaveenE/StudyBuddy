/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.ReportSessionBeanLocal;
import entities.ReportEntity;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("Report")
public class ReportResource {

    ReportSessionBeanLocal reportSessionBean;

    @Context
    private UriInfo context;
    
    public ReportResource() {
        reportSessionBean = new SessionBeanLookup().lookupReportSessionBeanLocal();
    }

    @Path("createReport")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReport(ReportEntity reportEntity, @QueryParam("reportedId") Long reportedId, @QueryParam("reporterId") Long reporterId) {
        try {
            Long reportId = reportSessionBean.createNewReport(reportEntity, reportedId, reporterId);
            return Response.status(Response.Status.OK).entity(reportId).build();
        } catch (InputDataValidationException | AlreadyExistsException | DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveRatingByRaterRateeId")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReportByReportedReporteeId(@QueryParam("reportedId") Long reportedId, @QueryParam("reporterId") Long reporterId) {
        try {
            ReportEntity report = reportSessionBean.retrieveReportByReportedReporteeId(reportedId, reporterId);
            String result = new ObjectMapper().writeValueAsString(report);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (InputDataValidationException | DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

}
