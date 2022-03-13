/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.RatingEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AlreadyExistsException;
import util.exception.CreateNewRatingException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wenting
 */
@Local
public interface RatingSessionBeanLocal {

    public List<RatingEntity> retrieveAllRatings();

    public Long createNewRating(RatingEntity newRatingEntity, Long rateeId, Long raterId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, CreateNewRatingException, DoesNotExistException;

    public RatingEntity retrieveRatingById(Long ratingId) throws InputDataValidationException, DoesNotExistException;

    public void updateRating(RatingEntity ratingEntity) throws InputDataValidationException, DoesNotExistException;

}
