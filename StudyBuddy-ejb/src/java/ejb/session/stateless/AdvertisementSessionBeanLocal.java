/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AdvertisementEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author SCXY
 */
@Local
public interface AdvertisementSessionBeanLocal {

    public List<AdvertisementEntity> retrieveAllAdvertisements();

    public AdvertisementEntity retrieveAdvertisementById(Long advertisementId) throws DoesNotExistException, InputDataValidationException;

    public Long createNewAdvertisement(AdvertisementEntity newAdvertisementEntity) throws AlreadyExistsException, UnknownPersistenceException, InputDataValidationException;

    public void updateAdvertisement(AdvertisementEntity advertisementToUpdate) throws InputDataValidationException, DoesNotExistException;

    public void clickAdvertisement(Long advertisementId) throws InputDataValidationException, DoesNotExistException;
    
}
