/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.GroupEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author larby
 */
@Local
public interface GroupEntitySessionBeanLocal {
    public List<GroupEntity> retrieveAllGroupEntity();

    public Long createNewGroupEntity(GroupEntity newGroupEntity) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException;

    public GroupEntity retrieveGroupEntityById(Long groupId) throws InputDataValidationException, DoesNotExistException;
    
}
