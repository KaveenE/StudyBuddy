/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
public interface ModuleSessionBeanLocal {

    public List<ModuleEntity> retrieveAllModules();

    public Long createNewModule(ModuleEntity newModuleEntity, Long schoolId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException;

    public ModuleEntity retrieveModuleById(Long moduleId) throws InputDataValidationException, DoesNotExistException;

    public void updateModule(ModuleEntity moduleEntity) throws InputDataValidationException, DoesNotExistException;

}
