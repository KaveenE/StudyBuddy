/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.AdvertisementSessionBeanLocal;
import entities.AdvertisementEntity;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;

/**
 * REST Web Service
 *
 * @author SCXY
 */
@Path("Advertisement")
public class AdvertisementResource {

    AdvertisementSessionBeanLocal advertisementSessionBean;

    @Context
    private UriInfo context;

    public AdvertisementResource() {
        this.advertisementSessionBean = new SessionBeanLookup().lookupAdvertisementSessionBeanLocal();
    }

    @Path("retrieveAllAdvertisements")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAdvertisements() {
        try {
            List<AdvertisementEntity> advertisements = advertisementSessionBean.retrieveAllAdvertisements();
            String result = new ObjectMapper().writeValueAsString(advertisements);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("clickAdvertisement/{advertisementId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response clickAdvertisement(@PathParam("advertisementId") Long advertisementId) {
        try {
            advertisementSessionBean.clickAdvertisement(advertisementId);
            return Response.status(Response.Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

}
