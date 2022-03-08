/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AccountEntity;
import entities.AdminEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AccountDoesNotExistException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author larby
 */
@Local
public interface AdminSessionBeanLocal {

    public List<AdminEntity> retrieveAllAdminEntities();

    public AdminEntity retrieveAccountById(Long accountId) throws DoesNotExistException, InputDataValidationException;

    public Long createNewAdminEntity(AdminEntity newAdminEntity) throws AlreadyExistsException, UnknownPersistenceException, InputDataValidationException;

    public AdminEntity retrieveAdminByUsername(String username) throws AccountDoesNotExistException;

    public AdminEntity adminLogin(String username, String password) throws InvalidLoginCredentialException;

}
