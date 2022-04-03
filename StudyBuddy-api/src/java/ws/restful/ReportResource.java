/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.ReportSessionBeanLocal;
import entities.ReportEntity;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    public ReportResource() {
        reportSessionBean = new SessionBeanLookup().lookupReportSessionBeanLocal();
    }

    @Path("createReport")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRating(ReportEntity reportEntity, @QueryParam("reportedId") Long reportedId, @QueryParam("reporterId") Long reporterId) {
        try {
            Long reportId = reportSessionBean.createNewReport(reportEntity, reportedId, reporterId);
            return Response.status(Response.Status.OK).entity(reportId).build();
        } catch (InputDataValidationException | AlreadyExistsException | DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

}
