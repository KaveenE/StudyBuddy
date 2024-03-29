/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.GroupEntity;
import entities.MessageEntity;
import entities.ModuleEntity;
import entities.StudentEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AccessRightsException;
import util.exception.AdvertismentAlreadyExistsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.GroupAccessRightsException;
import util.exception.GroupEntityDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModuleDoesNotExistException;
import util.exception.SystemException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author larby
 */
@Stateless
public class GroupEntitySessionBean implements GroupEntitySessionBeanLocal {

    @EJB(name = "KanbanSessionBeanLocal")
    private KanbanSessionBeanLocal kanbanSessionBeanLocal;
    @EJB(name = "StudentSessionBeanLocal")
    private StudentSessionBeanLocal studentSessionBeanLocal;
    @EJB(name = "ModuleSessionBeanLocal")
    private ModuleSessionBeanLocal moduleSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<GroupEntity> retrieveAllGroupEntity() {
        return em.createQuery("SELECT g FROM GroupEntity g WHERE g.isDeleted = false")
                .getResultList();
    }

    @Override
    public List<GroupEntity> retrieveAllOpenGroups(Long schoolId) {
        Query query = em.createQuery("SELECT g FROM GroupEntity g WHERE g.moduleEntity.school.schoolId = :schoolId AND g.isOpen = TRUE");
        query.setParameter("schoolId", schoolId);

        return query.getResultList();
    }

    @Override
    public List<GroupEntity> retrieveAllMyGroups(Long studentId) {
        Query query = em.createQuery("SELECT g FROM GroupEntity g WHERE g.poster.accountId = :studentId AND g.isOpen = TRUE");
        query.setParameter("studentId", studentId);

        return query.getResultList();
    }

    @Override
    public Long createNewGroupEntity(GroupEntity newGroupEntity, Long moduleId, Long studentId) throws InputDataValidationException, AlreadyExistsException, UnknownPersistenceException, DoesNotExistException {
        EJBHelper.throwValidationErrorsIfAny(newGroupEntity);

        try {
            EJBHelper.requireNonNull(moduleId, new ModuleDoesNotExistException("The new group must be associated with a module"));
            StudentEntity studentEntity = studentSessionBeanLocal.retrieveStudentById(studentId);
            ModuleEntity moduleEntity = moduleSessionBeanLocal.retrieveModuleById(moduleId);

            newGroupEntity.setModuleEntity(moduleEntity);
            moduleEntity.getGroups().add(newGroupEntity);
            newGroupEntity.setPoster(studentEntity);
            studentEntity.getGroupsPosted().add(newGroupEntity);
            studentEntity.getGroups().add(newGroupEntity);
            newGroupEntity.getGroupMembers().add(studentEntity);

//          Set up kanban board
            em.persist(newGroupEntity);
            em.flush();

            kanbanSessionBeanLocal.createDefaultKanbanBoard(newGroupEntity.getGroupId());
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new AdvertismentAlreadyExistsException());
        }

        return newGroupEntity.getGroupId();
    }

    @Override
    public GroupEntity retrieveGroupEntityById(Long groupId) throws InputDataValidationException, DoesNotExistException {
        GroupEntity groupEntity = em.find(GroupEntity.class, groupId);
        if (groupEntity.getIsDeleted()) {
            throw new GroupEntityDoesNotExistException();
        }
        groupEntity.getKanbanBoards().size();
        EJBHelper.requireNonNull(groupEntity, new GroupEntityDoesNotExistException());
        return groupEntity;
    }

    @Override
    public void applyToGroup(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException {
        GroupEntity group = retrieveGroupEntityById(groupId);
        StudentEntity studentEntity = studentSessionBeanLocal.retrieveStudentById(studentId);
        if (group.getCandidates().contains(studentEntity)) {
            throw new SystemException("You already applied to this group before!");
        }
        if (group.getPoster() == studentEntity) {
            throw new SystemException("You can't join your own group!");
        }

        if (group.getPoster() != studentEntity) {
            group.getCandidates().add(studentEntity);
            studentEntity.getGroupsApplied().add(group);
            em.flush();
        }
//        if (group.getCandidates().contains(studentEntity)) {
//            throw new SystemException("You already applied to this group before!");
//        } else {
//            throw new SystemException("You can't join your own group!");
//        }
    }

    @Override
    public void approveReq(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException {
        GroupEntity group = retrieveGroupEntityById(groupId);
        StudentEntity studentEntity = studentSessionBeanLocal.retrieveStudentById(studentId);
        group.getGroupMembers().add(studentEntity);
        //remove student as group candidate
        group.getCandidates().remove(studentEntity);
        studentEntity.getGroups().add(group);
        studentEntity.getGroupsApplied().remove(group);
        em.flush();
    }

    @Override
    public void disapproveReq(Long groupId, Long studentId) throws InputDataValidationException, DoesNotExistException {
        GroupEntity group = retrieveGroupEntityById(groupId);
        StudentEntity studentEntity = studentSessionBeanLocal.retrieveStudentById(studentId);
        group.getCandidates().remove(studentEntity);
        studentEntity.getGroupsApplied().remove(group);
        em.flush();
    }

    //Only poster is allowed to change name and description
    @Override
    public void updateGroup(GroupEntity groupEntity, Long studentId) throws InputDataValidationException, DoesNotExistException, AccessRightsException {
        EJBHelper.throwValidationErrorsIfAny(groupEntity);

        GroupEntity groupEntityToUpdate = retrieveGroupEntityById(groupEntity.getGroupId());

        if (groupEntityToUpdate.getPoster().getAccountId().equals(studentId)) {
            groupEntityToUpdate.setDescription(groupEntity.getDescription());
            groupEntityToUpdate.setGroupName(groupEntity.getGroupName());
        } else {
            throw new GroupAccessRightsException();
        }
    }

    @Override
    public void updateMapMarkers(GroupEntity groupEntity) throws InputDataValidationException, DoesNotExistException {
        GroupEntity groupEntityToUpdate = retrieveGroupEntityById(groupEntity.getGroupId());
        groupEntityToUpdate.setMapMarkerNum(groupEntity.getMapMarkerNum());
        groupEntityToUpdate.setMapMarkerCoord(groupEntity.getMapMarkerCoord());
    }

    @Override
    public void deleteGroup(Long groupId) throws DoesNotExistException, InputDataValidationException {
        GroupEntity groupEntityToDelete = retrieveGroupEntityById(groupId);

        groupEntityToDelete.setIsDeleted(true);
    }

    @Override
    public void approveMemberToGroup(Long groupId, Long studentId) throws DoesNotExistException {
        try {
            GroupEntity group = retrieveGroupEntityById(groupId);
            StudentEntity student = studentSessionBeanLocal.retrieveStudentById(studentId);

            if (!group.getGroupMembers().contains(student) || group.getCandidates().contains(student)) {
                group.getGroupMembers().add(student);
                student.getGroups().add(group);
                group.getCandidates().remove(student);
                student.getGroupsApplied().remove(group);
            }
        } catch (InputDataValidationException ex) {
            Logger.getLogger(GroupEntitySessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void rejectCandidateFromGroup(Long groupId, Long studentId) throws DoesNotExistException {
        try {
            GroupEntity group = retrieveGroupEntityById(groupId);
            StudentEntity student = studentSessionBeanLocal.retrieveStudentById(studentId);

            if (group.getCandidates().contains(student)) {
                group.getCandidates().remove(student);
                student.getGroupsApplied().remove(group);
            }
        } catch (InputDataValidationException ex) {
            Logger.getLogger(GroupEntitySessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void leaveGroup(Long groupId, Long studentId) throws DoesNotExistException, SystemException {
        try {
            GroupEntity group = retrieveGroupEntityById(groupId);
            StudentEntity student = studentSessionBeanLocal.retrieveStudentById(studentId);

            if (group.getGroupMembers().contains(student) && !group.getPoster().equals(student)) {
                group.getGroupMembers().remove(student);
                student.getGroups().remove(group);
            } else {
                throw new SystemException("The Group Poster cannot leave!");
            }
        } catch (InputDataValidationException ex) {
            Logger.getLogger(GroupEntitySessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void cancelApplicationToGroup(Long groupId, Long studentId) throws DoesNotExistException {
        rejectCandidateFromGroup(groupId, studentId);
    }

    @Override
    public void closeGroup(Long groupId) throws DoesNotExistException {
        try {
            GroupEntity group = retrieveGroupEntityById(groupId);
            group.setIsOpen(Boolean.FALSE);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(GroupEntitySessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Long addNewMessage(MessageEntity messageEntity) throws DoesNotExistException, InputDataValidationException {
        System.out.println("Finding group with id: " + messageEntity.getGroup().getGroupId());
        GroupEntity group = retrieveGroupEntityById(messageEntity.getGroup().getGroupId());
        StudentEntity sender = studentSessionBeanLocal.retrieveStudentById(messageEntity.getSender().getAccountId());

        MessageEntity message = new MessageEntity(messageEntity.getContent(), group, sender, messageEntity.getMediaType());

        sender.getMessages().add(message);
        group.getMessages().add(message);

        em.persist(message);
        em.flush();
        return message.getMessageId();
    }

    @Override
    public List<MessageEntity> retrieveMessagesByGroupId(Long groupId) throws DoesNotExistException {
        return em.createQuery("SELECT m FROM MessageEntity m JOIN m.group g WHERE g.groupId = :groupId")
                .setParameter("groupId", groupId)
                .getResultList();
    }

    @Override
    public void changeMessageContent(Long messageId, String newContent) throws DoesNotExistException, InputDataValidationException {
        MessageEntity message = em.find(MessageEntity.class, messageId);

        if (message == null) {
            throw new DoesNotExistException("This message does not exist[" + messageId + "]");
        }

        message.setContent(newContent);

        em.flush();
    }

    @Override
    public MessageEntity retrieveMessageEntityById(Long messageId) throws DoesNotExistException {
        MessageEntity message = em.find(MessageEntity.class, messageId);

        if (message == null) {
            throw new DoesNotExistException("This message does not exist[" + messageId + "]");
        }

        return message;
    }

}
