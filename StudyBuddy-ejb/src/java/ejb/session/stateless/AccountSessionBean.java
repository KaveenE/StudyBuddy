/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import entities.StudentEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.security.auth.login.AccountNotFoundException;
import util.exception.AccountAlreadyExistsException;
import util.exception.AccountDoesNotExistException;
import util.exception.AdminAlreadyExistsException;
import util.exception.AdminDoesNotExistException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StudentAlreadyExistsException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;
import util.security.CryptographicHelper;

/**
 *
 * @author SCXY
 */
@Stateless
public class AccountSessionBean implements AccountSessionBeanLocal {

    @EJB(name = "StudentSessionBeanLocal")
    private StudentSessionBeanLocal studentSessionBeanLocal;

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<AccountEntity> retrieveAllAccounts() {
        return this.retrieveAllAccounts(AccountEntity.class);

    }

    @Override
    public List<AccountEntity> retrieveAllAccounts(Class subClass) {
        Query query = em.createQuery("SELECT a FROM AccountEntity a WHERE TYPE(a) = :subClass")
                .setParameter("subClass", subClass);

        return query.getResultList();
    }

    @Override
    public AccountEntity retrieveAccountById(Long accountId) throws DoesNotExistException, InputDataValidationException {
        AccountEntity account = em.find(AccountEntity.class, accountId);
        EJBHelper.requireNonNull(account, new AccountDoesNotExistException());
        EJBHelper.throwValidationErrorsIfAny(account);

        return account;
    }

    @Override
    public AccountEntity retrieveAccountByUsername(String username) throws AccountDoesNotExistException {
        Query query = em.createQuery("SELECT a FROM AccountEntity a WHERE a.username = :username");
        query.setParameter("username", username);

        try {
            return (AccountEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AccountDoesNotExistException("Account Username: " + username + " does not exist!");
        }
    }

    @Override
    public AccountEntity retrieveAccountByEmail(String email) throws DoesNotExistException, InputDataValidationException {
        Query query = em.createQuery("SELECT a FROM AccountEntity a WHERE a.email = :email");
        query.setParameter("email", email.toLowerCase());
        
        try {
            return (AccountEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AccountDoesNotExistException("Email: " + email + " does not exist!");
        }
    }

    @Override
    public Long createNewAccount(AccountEntity newAccountEntity) throws AlreadyExistsException, UnknownPersistenceException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(newAccountEntity);

        errorMsgForDupEmailUsername(newAccountEntity);

        try {
            em.persist(newAccountEntity);
            em.flush();
        } catch (PersistenceException ex) {
            if (newAccountEntity instanceof StudentEntity) {
                AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new StudentAlreadyExistsException());
            }
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new AdminAlreadyExistsException());
        }

        return newAccountEntity.getAccountId();
    }

    @Override
    public AccountEntity login(String username, String password) throws InvalidLoginCredentialException {
        try {
            AccountEntity accountEntity = retrieveAccountByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + accountEntity.getSalt()));

            if (accountEntity.getPassword().equals(passwordHash)) {
                return accountEntity;
            } else {
                throw new InvalidLoginCredentialException("Invalid password");
            }
        } catch (AccountDoesNotExistException ex) {
            throw new InvalidLoginCredentialException("Invalid username");
        }
    }

    //Only accessible by account owner!
    @Override
    public void updatePassword(AccountEntity accountToUpdatePassword, String newPassword) throws AccountNotFoundException, DoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(accountToUpdatePassword);
        AccountEntity account = retrieveAccountById(accountToUpdatePassword.getAccountId());
        String newSalt = CryptographicHelper.getInstance().generateRandomString(32);
        account.setSalt(newSalt);

        account.setPassword(newPassword);
    }

    //Couldn't refactor createAccount to include specific messages properly due to some bug (or i suck)
    //So had to create separate method, iterate thru the entities again..
    //Poor performance
    private void errorMsgForDupEmailUsername(AccountEntity newAccountEntity) throws StudentAlreadyExistsException {
        String errorMsg = "";
        List<StudentEntity> students = studentSessionBeanLocal.retrieveAllStudents();
        boolean dupEmail = students.stream().anyMatch(stud -> stud.getEmail().equals(newAccountEntity.getEmail()));
        boolean dupUsername = students.stream().anyMatch(stud -> stud.getUsername().equals(newAccountEntity.getUsername()));

        if (dupEmail) {
            errorMsg = "You have already registered with this email";
        }

        if (dupUsername) {
            errorMsg = "Username has already been taken";
        }

        if (!errorMsg.isEmpty()) {
            throw new StudentAlreadyExistsException(errorMsg);
        }
    }
}
