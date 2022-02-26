/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AdminEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.AccountAlreadyExistsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author larby
 */
@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<AdminEntity> retrieveAllAdminEntities() {
        return em.createQuery("SELECT a FROM AdminEntity a")
                .getResultList();
    }

    @Override
    public AdminEntity retrieveAccountById(Long accountId) throws DoesNotExistException, InputDataValidationException {
        AdminEntity adminEntity = em.find(AdminEntity.class, accountId);
        EJBHelper.requireNonNull(adminEntity, new DoesNotExistException("Admin does not exist"));
        
        return adminEntity;
    }

    @Override
    public Long createNewAdminEntity(AdminEntity newAdminEntity) throws AlreadyExistsException, UnknownPersistenceException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(newAdminEntity);
        
        try {
            em.persist(newAdminEntity);
            em.flush();
        } catch(PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new AccountAlreadyExistsException());
        }
        
        return newAdminEntity.getAccountId();
    }

}
