/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import java.util.List;
import javax.ejb.Local;
import javax.security.auth.login.AccountNotFoundException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author SCXY
 */
@Local
public interface AccountSessionBeanLocal {

    public List<AccountEntity> retrieveAllAccounts();

    public AccountEntity retrieveAccountById(Long accountId) throws DoesNotExistException, InputDataValidationException;

    public Long createNewAccount(AccountEntity newAccountEntity) throws AlreadyExistsException, UnknownPersistenceException, InputDataValidationException;

    public void updateAccount(AccountEntity accountToUpdate) throws AccountNotFoundException, DoesNotExistException, InputDataValidationException;
    
}
