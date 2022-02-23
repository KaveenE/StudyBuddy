/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AdvertisementEntity;
import entities.ModuleEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AccountDoesNotExistException;
import util.exception.AdvertismentAlreadyExistsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModuleAlreadyExistsException;
import util.exception.ModuleDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author SCXY
 */
@Stateless
public class ModuleEntitySessionBean implements ModuleEntitySessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<ModuleEntity> retrieveAllModules() {
        Query query = em.createQuery("SELECT m FROM ModuleEntity m");

        return query.getResultList();
    }

    public ModuleEntity retrieveModuleById(Long moduleId) throws InputDataValidationException, DoesNotExistException {
        ModuleEntity module = em.find(ModuleEntity.class, moduleId);
        EJBHelper.requireNonNull(module, new ModuleDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(module);

        return module;
    }
    
    @Override
    public Long createNewModule(ModuleEntity newModuleEntity) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException {
        EJBHelper.throwValidationErrorsIfAny(newModuleEntity);
        
        try {
            em.persist(newModuleEntity);
            em.flush();
        }
        catch(PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new ModuleAlreadyExistsException());
        }
        
        return newModuleEntity.getmoduleId();
    }
}
