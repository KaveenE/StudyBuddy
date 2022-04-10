/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.GroupEntity;
import entities.StudentEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AccessRightsException;
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

    public Long createNewGroupEntity(GroupEntity newGroupEntity, Long moduleId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException;

    public GroupEntity retrieveGroupEntityById(Long groupId) throws InputDataValidationException, DoesNotExistException;

    public void deleteGroup(Long groupId) throws DoesNotExistException, InputDataValidationException;

    public void updateGroup(GroupEntity groupEntity, Long studentId) throws InputDataValidationException, DoesNotExistException, AccessRightsException;

    public List<GroupEntity> retrieveAllOpenGroups(Long schoolId);

    public void applyToGroup(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException;

    public void disapproveReq(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException;

    public void approveReq(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException;

    public List<GroupEntity> retrieveAllMyGroups(Long studentId);

  
}
