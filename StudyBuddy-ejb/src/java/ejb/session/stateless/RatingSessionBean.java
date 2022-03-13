/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import entities.RatingEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AccountDoesNotExistException;
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

    @EJB(name = "AccountSessionBeanLocal")
    private AccountSessionBeanLocal accountSessionBeanLocal;

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
    public Long createNewRating(RatingEntity newRatingEntity, Long rateeId, Long raterId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException {
        EJBHelper.throwValidationErrorsIfAny(newRatingEntity);

        try {
            EJBHelper.requireNonNull(rateeId, new AccountDoesNotExistException("The new rating must be associated with a ratee"));
            EJBHelper.requireNonNull(raterId, new AccountDoesNotExistException("The new rating must be associated with a rater"));

            AccountEntity raterAccount = accountSessionBeanLocal.retrieveAccountById(raterId);
            AccountEntity rateeAccount = accountSessionBeanLocal.retrieveAccountById(rateeId);
            em.persist(newRatingEntity);
            raterAccount.getRatingOthers().add(newRatingEntity);
            newRatingEntity.setRater(raterAccount);
            rateeAccount.getRatingByOthers().add(newRatingEntity);
            newRatingEntity.setRatee(rateeAccount);
            em.flush();
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new RatingAlreadyExistsException());
        }

        return newRatingEntity.getRatingId();
    }

    @Override
    public void updateRating(RatingEntity ratingEntity) throws InputDataValidationException, DoesNotExistException {
        EJBHelper.throwValidationErrorsIfAny(ratingEntity);

        RatingEntity ratingEntityToUpdate = retrieveRatingById(ratingEntity.getRatingId());
        ratingEntityToUpdate.setRating(ratingEntityToUpdate.getRating());
        ratingEntityToUpdate.setRatingDescription(ratingEntityToUpdate.getRatingDescription());
    }
}
