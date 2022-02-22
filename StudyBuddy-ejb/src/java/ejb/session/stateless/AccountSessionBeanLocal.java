/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author SCXY
 */
@Local
public interface AccountSessionBeanLocal {

    public List<AccountEntity> retrieveAllAccounts();

    public AccountEntity retrieveAccountById(Long accountId);

    public Long createNewAccount(AccountEntity newAccountEntity);
    
}
