/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AdvertisementEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AdvertisementDoesNotExistException;
import util.exception.AdvertismentAlreadyExistsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author SCXY
 */
@Stateless
public class AdvertisementSessionBean implements AdvertisementSessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<AdvertisementEntity> retrieveAllAdvertisements() {
        Query query = em.createQuery("SELECT a FROM AdvertisementEntity a");

        return query.getResultList();
    }

    @Override
    public AdvertisementEntity retrieveAdvertisementById(Long advertisementId) throws DoesNotExistException, InputDataValidationException {
        AdvertisementEntity advertisment = em.find(AdvertisementEntity.class, advertisementId);
        EJBHelper.requireNonNull(advertisment, new AdvertisementDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(advertisment);

        return advertisment;
    }

    @Override
    public Long createNewAdvertisement(AdvertisementEntity newAdvertisementEntity) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException {
        EJBHelper.throwValidationErrorsIfAny(newAdvertisementEntity);
        
        try {
            em.persist(newAdvertisementEntity);
            em.flush();
        }
        catch(PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new AdvertismentAlreadyExistsException());
        }
        
        return newAdvertisementEntity.getAdvertisementId();
    }
}
