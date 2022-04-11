/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.RatingSessionBeanLocal;
import entities.RatingEntity;
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
@Path("Rating")
public class RatingResource {

    RatingSessionBeanLocal ratingSessionBean;

    @Context
    private UriInfo context;

    public RatingResource() {
        ratingSessionBean = new SessionBeanLookup().lookupRatingSessionBeanLocal();
    }

    @Path("createRating")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRating(RatingEntity ratingEntity, @QueryParam("rateeId") Long rateeId, @QueryParam("raterId") Long raterId) {
        try {
            System.out.printf("RateeId:%d     RaterId:%d",rateeId,raterId);
            Long ratingId = ratingSessionBean.createNewRating(ratingEntity, rateeId, raterId);
            return Response.status(Response.Status.OK).entity(ratingId).build();
        } catch (InputDataValidationException | AlreadyExistsException | DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveRatingByRaterRateeId")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRatingByRaterRateeId(@QueryParam("raterId") Long raterId, @QueryParam("rateeId") Long rateeId) {
        try {
            RatingEntity rating = ratingSessionBean.retrieveRatingByRaterRateeId(raterId, rateeId);
            return Response.status(Response.Status.OK).entity(rating).build();
        } catch (InputDataValidationException | DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

}
