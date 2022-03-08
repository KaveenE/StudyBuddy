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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AccountAlreadyExistsException;
import util.exception.AccountDoesNotExistException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;
import util.security.CryptographicHelper;

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
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new AccountAlreadyExistsException());
        }

        return newAdminEntity.getAccountId();
    }

    @Override
    public AdminEntity retrieveAdminByUsername(String username) throws AccountDoesNotExistException {
        Query query = em.createQuery("SELECT a FROM AdminEntity a WHERE a.username = :username");
        query.setParameter("username", username);

        try {
            return (AdminEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AccountDoesNotExistException("Admin Username: " + username + " does not exist!");
        }
    }

    @Override
    public AdminEntity adminLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            AdminEntity adminEntity = retrieveAdminByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + adminEntity.getSalt()));

            if (adminEntity.getPassword().equals(passwordHash)) {
                return adminEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (AccountDoesNotExistException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
}
