/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AdvertisementEntity;
import entities.ModuleEntity;
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
public interface ModuleEntitySessionBeanLocal {

    public List<ModuleEntity> retrieveAllModules();

    public Long createNewModule(AdvertisementEntity newModuleEntity) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException;

    public ModuleEntity retrieveModuleById(Long moduleId) throws InputDataValidationException, DoesNotExistException;
    
}
