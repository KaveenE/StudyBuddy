/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.GroupEntity;
import entities.ModuleEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.AdvertismentAlreadyExistsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.GroupEntityDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author larby
 */
@Stateless
public class GroupEntitySessionBean implements GroupEntitySessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<GroupEntity> retrieveAllGroupEntity() {
        return em.createQuery("SELECT g FROM GroupEntity g")
                .getResultList();
    }

    @Override
    public Long createNewGroupEntity(GroupEntity newGroupEntity) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException {
        EJBHelper.throwValidationErrorsIfAny(newGroupEntity);
        
        try {
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
        EJBHelper.requireNonNull(groupEntity, new GroupEntityDoesNotExistException());
        return groupEntity;
    }
}
