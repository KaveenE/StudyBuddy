/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import entities.AdminEntity;
import entities.StudentEntity;
import java.util.List;
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
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;
import util.security.CryptographicHelper;

/**
 *
 * @author SCXY
 */
@Stateless
public class AccountSessionBean implements AccountSessionBeanLocal {

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
    public Long createNewAccount(AccountEntity newAccountEntity) throws AlreadyExistsException, UnknownPersistenceException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(newAccountEntity);

        try {
            em.persist(newAccountEntity);
            em.flush();
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new AccountAlreadyExistsException());
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
                throw new InvalidLoginCredentialException();
            }
        } catch (AccountDoesNotExistException ex) {
            throw new InvalidLoginCredentialException();
        }
    }

    @Override
    public void updateAccount(AccountEntity accountToUpdate) throws AccountNotFoundException, DoesNotExistException, InputDataValidationException //update account
    {

        AccountEntity account = retrieveAccountById(accountToUpdate.getAccountId());
        account.setEmail(accountToUpdate.getEmail());
        account.setPassword(accountToUpdate.getPassword());

    }
}
