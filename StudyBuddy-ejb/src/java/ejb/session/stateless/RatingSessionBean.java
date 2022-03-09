/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.RatingEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.RatingAlreadyExistsException;
import util.exception.RatingDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author wenting
 */
@Stateless
public class RatingSessionBean implements RatingSessionBeanLocal {
    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<RatingEntity> retrieveAllRatings() {
        Query query = em.createQuery("SELECT r FROM RatingEntity r");

        return query.getResultList();
    }

    @Override
    public RatingEntity retrieveRatingById(Long ratingId) throws InputDataValidationException, DoesNotExistException {
        RatingEntity rating = em.find(RatingEntity.class, ratingId);
        EJBHelper.requireNonNull(rating, new RatingDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(rating);

        return rating;
    }

    @Override
    public Long createNewRating(RatingEntity newRatingEntity) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException {
        EJBHelper.throwValidationErrorsIfAny(newRatingEntity);

        try {
            em.persist(newRatingEntity);
            em.flush();
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new RatingAlreadyExistsException());
        }

        return newRatingEntity.getRatingId();
    }

    //only supports updating of the isResolved attribute currently
    @Override
    public void updateRating(RatingEntity ratingEntity) throws InputDataValidationException, DoesNotExistException {
        RatingEntity ratingEntityToUpdate = retrieveRatingById(ratingEntity.getRatingId());
        ratingEntityToUpdate.setRating(ratingEntityToUpdate.getRating());
        ratingEntityToUpdate.setRatingDescription(ratingEntityToUpdate.getRatingDescription());
    }
}
