/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.GroupEntity;
import entities.KanbanBoard;
import entities.ModuleEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.AccessRightsException;
import util.exception.AdvertismentAlreadyExistsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.GroupAccessRightsException;
import util.exception.GroupEntityDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModuleDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author larby
 */
@Stateless
public class GroupEntitySessionBean implements GroupEntitySessionBeanLocal {

    @EJB(name = "ModuleSessionBeanLocal")
    private ModuleSessionBeanLocal moduleSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<GroupEntity> retrieveAllGroupEntity() {
        return em.createQuery("SELECT g FROM GroupEntity g WHERE g.isDeleted = false")
                .getResultList();
    }

    @Override
    public Long createNewGroupEntity(GroupEntity newGroupEntity, Long moduleId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException {
        EJBHelper.throwValidationErrorsIfAny(newGroupEntity);

        try {
            EJBHelper.requireNonNull(moduleId, new ModuleDoesNotExistException("The new group must be associated with a module"));

            ModuleEntity moduleEntity = moduleSessionBeanLocal.retrieveModuleById(moduleId);
            newGroupEntity.setModuleEntity(moduleEntity);
            moduleEntity.getGroups().add(newGroupEntity);
            
//          Set up kanban board
            

            em.persist(newGroupEntity);
            em.flush();
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new AdvertismentAlreadyExistsException());
        }

        return newGroupEntity.getGroupId();
    }

    @Override
    public GroupEntity retrieveGroupEntityById(Long groupId) throws InputDataValidationException, DoesNotExistException {
        GroupEntity groupEntity = em.find(GroupEntity.class, groupId);
        if(groupEntity.getIsDeleted()){
            throw new GroupEntityDoesNotExistException();
        }
        EJBHelper.requireNonNull(groupEntity, new GroupEntityDoesNotExistException());
        return groupEntity;
    }

    //Only poster is allowed to change name and description
    public void updateGroup(GroupEntity groupEntity, Long studentId) throws InputDataValidationException, DoesNotExistException, AccessRightsException {
        EJBHelper.throwValidationErrorsIfAny(groupEntity);

        GroupEntity groupEntityToUpdate = retrieveGroupEntityById(groupEntity.getGroupId());

        if (groupEntityToUpdate.getPoster().getAccountId().equals(studentId)) {
            groupEntityToUpdate.setDescription(groupEntity.getDescription());
            groupEntityToUpdate.setGroupName(groupEntity.getGroupName());
        } else {
            throw new GroupAccessRightsException();
        }
    }

    @Override
    public void deleteGroup(Long groupId) throws DoesNotExistException, InputDataValidationException {
        GroupEntity groupEntityToDelete = retrieveGroupEntityById(groupId);

        groupEntityToDelete.setIsDeleted(true);
    }

}
