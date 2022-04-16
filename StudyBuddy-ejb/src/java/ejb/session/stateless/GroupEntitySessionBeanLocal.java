/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.GroupEntity;
import entities.MessageEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AccessRightsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.SystemException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author larby
 */
@Local
public interface GroupEntitySessionBeanLocal {

    public List<GroupEntity> retrieveAllGroupEntity();

    public Long createNewGroupEntity(GroupEntity newGroupEntity, Long moduleId, Long studentId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException;

    public GroupEntity retrieveGroupEntityById(Long groupId) throws InputDataValidationException, DoesNotExistException;

    public void deleteGroup(Long groupId) throws DoesNotExistException, InputDataValidationException;

    public void updateGroup(GroupEntity groupEntity, Long studentId) throws InputDataValidationException, DoesNotExistException, AccessRightsException;

    public List<GroupEntity> retrieveAllOpenGroups(Long schoolId);

    public void applyToGroup(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException;


    public void disapproveReq(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException;

    public void approveReq(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException;

    public List<GroupEntity> retrieveAllMyGroups(Long studentId);

  
    
    public void approveMemberToGroup(Long groupId, Long studentId) throws DoesNotExistException;
    
    public void rejectCandidateFromGroup(Long groupId, Long studentId) throws DoesNotExistException;
    
    public void leaveGroup(Long groupId, Long studentId) throws DoesNotExistException, SystemException;
    
    public void cancelApplicationToGroup(Long groupId, Long studentId) throws DoesNotExistException;
    
    public void closeGroup(Long groupId) throws DoesNotExistException;
    
    public Long addNewMessage(MessageEntity messageEntity) throws DoesNotExistException, InputDataValidationException;
    
    public List<MessageEntity> retrieveMessagesByGroupId(Long groupId) throws DoesNotExistException;
    
    public void changeMessageContent(Long messageId, String newContent) throws DoesNotExistException, InputDataValidationException;

    public MessageEntity retrieveMessageEntityById(Long messageId) throws DoesNotExistException;

    public void updateMapMarkers(GroupEntity groupEntity) throws InputDataValidationException, DoesNotExistException;
}