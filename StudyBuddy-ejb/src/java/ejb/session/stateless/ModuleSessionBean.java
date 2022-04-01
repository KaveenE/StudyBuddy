/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.ModuleEntity;
import entities.SchoolEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModuleAlreadyExistsException;
import util.exception.ModuleDoesNotExistException;
import util.exception.SchoolDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author SCXY
 */
@Stateless
public class ModuleSessionBean implements ModuleSessionBeanLocal {

    @EJB(name = "SchoolSessionBeanLocal")
    private SchoolSessionBeanLocal schoolSessionBeanLocal;

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<ModuleEntity> retrieveAllModules() {
        Query query = em.createQuery("SELECT m FROM ModuleEntity m");

        return query.getResultList();
    }
    
    @Override
    public List<ModuleEntity> retrieveAllModulesBySchoolName(String schoolName) {
        Query query = em.createQuery("SELECT m FROM ModuleEntity m WHERE m.isDeleted = FALSE AND m.school.name =:schoolName");

        query.setParameter("schoolName", schoolName);

        return query.getResultList();
    }

    @Override
    public ModuleEntity retrieveModuleById(Long moduleId) throws InputDataValidationException, DoesNotExistException {
        ModuleEntity module = em.find(ModuleEntity.class, moduleId);
        if(module.getIsDeleted()) {
            throw new ModuleDoesNotExistException();
        }
        EJBHelper.requireNonNull(module, new ModuleDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(module);

        return module;
    }

    @Override
    public Long createNewModule(ModuleEntity newModuleEntity, Long schoolId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException {
        EJBHelper.throwValidationErrorsIfAny(newModuleEntity);

        try {
            EJBHelper.requireNonNull(schoolId, new SchoolDoesNotExistException("The new module must be associated with a school"));

            SchoolEntity schoolEntity = schoolSessionBeanLocal.retrieveSchoolById(schoolId);

            em.persist(newModuleEntity);
            newModuleEntity.setSchool(schoolEntity);
            schoolEntity.getModuleEntities().add(newModuleEntity);
            em.flush();
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new ModuleAlreadyExistsException());
        }

        return newModuleEntity.getModuleId();
    }

    @Override
    public void updateModule(ModuleEntity moduleEntity) throws InputDataValidationException, DoesNotExistException {
        EJBHelper.throwValidationErrorsIfAny(moduleEntity);
        ModuleEntity moduleEntityToUpdate = retrieveModuleById(moduleEntity.getModuleId());

        moduleEntityToUpdate.setName(moduleEntity.getName());
        moduleEntityToUpdate.setCode(moduleEntity.getCode());
        moduleEntityToUpdate.setIsDeleted(moduleEntity.getIsDeleted());
    }

}
