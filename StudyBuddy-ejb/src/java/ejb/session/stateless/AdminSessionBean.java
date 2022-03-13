/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import entities.AdminEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.auth.login.AccountNotFoundException;
import util.exception.AdminDoesNotExistException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
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
    @EJB
    private AccountSessionBeanLocal accountSessionBeanLocal;

    @Override
    public List<AdminEntity> retrieveAllAdmins() {
        List<? extends AccountEntity> adminEntities = accountSessionBeanLocal.retrieveAllAccounts(AdminEntity.class);
        return (List<AdminEntity>) adminEntities;
    }

    @Override
    public AdminEntity retrieveAdminById(Long adminId) throws DoesNotExistException, InputDataValidationException {
        AccountEntity account = accountSessionBeanLocal.retrieveAccountById(adminId);
        if (!(account instanceof AdminEntity)) {
            throw new AdminDoesNotExistException();
        }
        return (AdminEntity) account;
    }

    @Override
    public AdminEntity retrieveAdminByUsername(String username) throws DoesNotExistException {
        AccountEntity account = accountSessionBeanLocal.retrieveAccountByUsername(username);
        if (!(account instanceof AdminEntity)) {
            throw new AdminDoesNotExistException();
        }
        return (AdminEntity) account;
    }

    @Override
    public Long createNewAdminEntity(AdminEntity newAdminEntity) throws AlreadyExistsException, UnknownPersistenceException, InputDataValidationException {
        return accountSessionBeanLocal.createNewAccount(newAdminEntity);
    }

    @Override
    public AdminEntity adminLogin(String username, String password) throws InvalidLoginCredentialException {
        AccountEntity account = accountSessionBeanLocal.login(username, password);
        if (!(account instanceof AdminEntity)) {
            throw new InvalidLoginCredentialException();
        }
        return (AdminEntity) account;
    }

    //Password deliberately not updated as only the account owner can change the password
    @Override
    public void updateAccountAdmin(AdminEntity adminEntity) throws AccountNotFoundException, DoesNotExistException, InputDataValidationException {

        EJBHelper.throwValidationErrorsIfAny(adminEntity);

        AccountEntity accountToUpdate = accountSessionBeanLocal.retrieveAccountById(adminEntity.getAccountId());

        if (!(accountToUpdate instanceof AdminEntity)) {
            throw new AccountNotFoundException();
        } else {
            accountToUpdate.setEmail(adminEntity.getEmail());
        }
    }
}
