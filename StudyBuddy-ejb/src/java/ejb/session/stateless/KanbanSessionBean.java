/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.GroupEntity;
import entities.KanbanBoard;
import entities.KanbanCard;
import entities.KanbanList;
import entities.StudentEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.DoesNotExistException;
import util.exception.GroupEntityDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.KanbanBoardDoesNotExistException;
import util.exception.KanbanCardDoesNotExistException;
import util.exception.KanbanListDoesNotExistException;
import util.exception.StudentDoesNotExistException;
import util.exception.UnknownPersistenceException;
import util.helper.EJBHelper;

/**
 *
 * @author larby
 */
@Stateless
public class KanbanSessionBean implements KanbanSessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private GroupEntitySessionBeanLocal groupEntitySessionBean;

    @Override
    public Long createNewKanbanBoard(KanbanBoard newKanbanBoard, Long groupId) throws UnknownPersistenceException, GroupEntityDoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(newKanbanBoard);

        if (groupId == null) {
            throw new GroupEntityDoesNotExistException("Group Id must not be null");
        }

        try {
            GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(groupId);

            newKanbanBoard.setGroup(group);
            group.getKanbanBoard().add(newKanbanBoard);

            em.persist(newKanbanBoard);
            em.flush();

            return newKanbanBoard.getKanbanBoardId();
        } catch (DoesNotExistException ex) {
            throw new GroupEntityDoesNotExistException("Group Id must not be null");
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Unknown Persistence Error");
        }
    }

    @Override
    public List<KanbanBoard> retrieveAllKanbanBoards() {
        return em.createQuery("SELECT kb FROM KanbanBoard kb")
                .getResultList();
    }

    @Override
    public void updateKanbanBoard(KanbanBoard kanbanBoard) throws KanbanBoardDoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(kanbanBoard);

        try {
            KanbanBoard kanbanBoardToUpdate = em.find(KanbanBoard.class, kanbanBoard.getKanbanBoardId());

            if (kanbanBoardToUpdate == null) {
                throw new KanbanBoardDoesNotExistException("This kanban board does not exist!");
            }

            kanbanBoardToUpdate.setHeading(kanbanBoard.getHeading());
        } catch (IllegalArgumentException ex) {
            throw new KanbanBoardDoesNotExistException("This kanban board does not exist!");
        }
    }

    @Override
    public void deleteKanbanBoard(Long kanbanBoardId) throws KanbanBoardDoesNotExistException {
        try {
            KanbanBoard kanbanBoard = em.find(KanbanBoard.class, kanbanBoardId);

            if (kanbanBoard == null) {
                throw new KanbanBoardDoesNotExistException("Kanban board does not exist!");
            }

            kanbanBoard.getGroup().getKanbanBoard().remove(kanbanBoard);
            kanbanBoard.setGroup(null);

            em.remove(kanbanBoard);
        } catch (IllegalArgumentException ex) {
            throw new KanbanBoardDoesNotExistException("This kanban board does not exist!");
        }
    }

    @Override
    public Long createNewKanbanList(KanbanList newKanbanList, Long kanbanBoardId) throws UnknownPersistenceException, KanbanBoardDoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(newKanbanList);

        if (kanbanBoardId == null) {
            throw new KanbanBoardDoesNotExistException("Kanban board Id must not be null");
        }

        KanbanBoard kanbanBoard = retrieveKanbanBoardById(kanbanBoardId);

        newKanbanList.setKanbanBoard(kanbanBoard);
        kanbanBoard.getKanbanLists().add(newKanbanList);

        try {
            em.persist(newKanbanList);
            em.flush();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Unkown Persistence Error");
        }

        return newKanbanList.getKanbanListId();
    }

    @Override
    public List<KanbanList> retrieveAllKanbanLists() {
        return em.createQuery("SELECT kb FROM KanbanList kb")
                .getResultList();
    }

    @Override
    public void updateKanbanList(KanbanList kanbanList) throws KanbanListDoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(kanbanList);

        try {
            KanbanList kanbanListToUpdate = em.find(KanbanList.class, kanbanList.getKanbanListId());

            if (kanbanListToUpdate == null) {
                throw new KanbanListDoesNotExistException("This kanban list does not exist!");
            }

            kanbanListToUpdate.setHeading(kanbanList.getHeading());
        } catch (IllegalArgumentException ex) {
            throw new KanbanListDoesNotExistException("This kanban list does not exist!");
        }
    }

    @Override
    public void deleteKanbanList(Long kanbanListId) throws KanbanListDoesNotExistException {
        try {
            KanbanList kanbanList = em.find(KanbanList.class, kanbanListId);

            if (kanbanList == null) {
                throw new KanbanListDoesNotExistException("Kanban list does not exist!");
            }

            kanbanList.getKanbanBoard().getKanbanLists().remove(kanbanList);
            kanbanList.setKanbanBoard(null);

            em.remove(kanbanList);
        } catch (IllegalArgumentException ex) {
            throw new KanbanListDoesNotExistException("This kanban list does not exist!");
        }
    }

    @Override
    public Long createNewKanbanCard(KanbanCard newKanbanCard, Long kanbanListId, Long authorStudentId) throws UnknownPersistenceException, StudentDoesNotExistException, KanbanListDoesNotExistException, InputDataValidationException {
        try {
            EJBHelper.throwValidationErrorsIfAny(newKanbanCard);

            if (kanbanListId == null) {
                throw new KanbanListDoesNotExistException("Kanban board Id must not be null");
            }

            if (authorStudentId == null) {
                throw new StudentDoesNotExistException("Student Id must not be null");

            }

            KanbanList kanbanList = retrieveKanbanListById(kanbanListId);
            StudentEntity student = studentSessionBean.retrieveStudentById(authorStudentId);

            newKanbanCard.setKanbanList(kanbanList);
            kanbanList.getKanbanCards().add(newKanbanCard);

            newKanbanCard.setAuthor(student);

            em.persist(newKanbanCard);
            em.flush();

            return newKanbanCard.getKanbanCardId();
        } catch (DoesNotExistException ex) {
            throw new StudentDoesNotExistException("This student does not exist");
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Unkown Persistence Error");
        }
    }

    @Override
    public List<KanbanCard> retrieveAllKanbanCards() {
        return em.createQuery("SELECT kb FROM KanbanCard kb")
                .getResultList();
    }

    @Override
    public void updateKanbanCard(KanbanCard kanbanCard) throws KanbanCardDoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(kanbanCard);

        try {
            KanbanCard kanbanCardToUpdate = em.find(KanbanCard.class, kanbanCard.getKanbanCardId());

            if (kanbanCardToUpdate == null) {
                throw new KanbanCardDoesNotExistException("This kanban card does not exist!");
            }

            kanbanCardToUpdate.setDeadlineStart(kanbanCard.getDeadlineStart());
            kanbanCardToUpdate.setDeadlineEnd(kanbanCard.getDeadlineEnd());
            kanbanCardToUpdate.setTitle(kanbanCard.getTitle());
            kanbanCardToUpdate.setDescription(kanbanCard.getDescription());

            kanbanCardToUpdate.getAssignedStudents().forEach(
                    s -> {
                        StudentEntity student = em.find(StudentEntity.class, s.getAccountId());
                        if (student != null) {
                            student.getAssignedCards().remove(kanbanCardToUpdate);
                        }
                    }
            );

            kanbanCardToUpdate.setAssignedStudents(new ArrayList<>());

            kanbanCard.getAssignedStudents().forEach(
                    s -> {
                        StudentEntity student = em.find(StudentEntity.class, s.getAccountId());
                        if (student != null) {
                            student.getAssignedCards().add(kanbanCardToUpdate);
                            kanbanCardToUpdate.getAssignedStudents().add(student);
                        }
                    }
            );

        } catch (IllegalArgumentException ex) {
            throw new KanbanCardDoesNotExistException("This kanban list does not exist!");
        }
    }

    @Override
    public void deleteKanbanCard(Long kanbanCardId) throws KanbanCardDoesNotExistException {
        try {
            KanbanCard kanbanCard = em.find(KanbanCard.class, kanbanCardId);

            if (kanbanCard == null) {
                throw new KanbanCardDoesNotExistException("Kanban board does not exist!");
            }

            kanbanCard.getKanbanList().getKanbanCards().remove(kanbanCard);
            kanbanCard.setKanbanList(null);

            kanbanCard.getAssignedStudents().forEach(
                    s -> {
                        s.getAssignedCards().remove(kanbanCard);
                    }
            );

            kanbanCard.setAssignedStudents(new ArrayList<>());

            em.remove(kanbanCard);
        } catch (IllegalArgumentException ex) {
            throw new KanbanCardDoesNotExistException("This kanban board does not exist!");
        }
    }

    @Override
    public List<KanbanCard> retrieveKanbanCardsAssignedToStudents(Long StudentId) throws StudentDoesNotExistException {
        List<KanbanCard> res = null;

        try {
            StudentEntity student = studentSessionBean.retrieveStudentById(StudentId);

            res = em.createQuery("SELECT kc FROM KanbanCard kc WHERE :student MEMBER OF kc.assignedStudents")
                    .setParameter("student", student)
                    .getResultList();

            res.forEach(c -> {
                c.getAssignedStudents();
            });
        } catch (DoesNotExistException ex) {
            throw new StudentDoesNotExistException("Student not exist");
        } catch (InputDataValidationException ex) {
            Logger.getLogger(KanbanSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    @Override
    public KanbanBoard retrieveKanbanBoardById(Long kanbanBoardId) throws KanbanBoardDoesNotExistException {
        try {
            KanbanBoard board = em.find(KanbanBoard.class, kanbanBoardId);
            if (board == null) {
                throw new KanbanBoardDoesNotExistException("Kanban Board does not exist!");
            }

            board.getKanbanLists();

            return board;
        } catch (IllegalArgumentException ex) {
            throw new KanbanBoardDoesNotExistException("Kanban Board does not exist!");
        }
    }

    @Override
    public List<KanbanBoard> retrieveKanbanBoardsByGroupId(Long groupId) throws GroupEntityDoesNotExistException {
        List<KanbanBoard> res = null;

        try {
            GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(groupId);

            res = em.createQuery("SELECT kc FROM KanbanBoard kc WHERE kc.group = :group")
                    .setParameter("group", group)
                    .getResultList();
        } catch (DoesNotExistException ex) {
            throw new GroupEntityDoesNotExistException("Group not exist");
        } catch (InputDataValidationException ex) {
            Logger.getLogger(KanbanSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    @Override
    public KanbanList retrieveKanbanListById(Long kanbanListId) throws KanbanListDoesNotExistException {
        try {
            KanbanList list = em.find(KanbanList.class, kanbanListId);
            if (list == null) {
                throw new KanbanListDoesNotExistException("Kanban List does not exist!");
            }

            return list;
        } catch (IllegalArgumentException ex) {
            throw new KanbanListDoesNotExistException("Kanban List does not exist!");
        }
    }

    @Override
    public List<KanbanList> retrieveKanbanListsBykanbanBoardId(Long kanbanBoardId) throws KanbanBoardDoesNotExistException {
        List<KanbanList> res = null;

        KanbanBoard board = retrieveKanbanBoardById(kanbanBoardId);

        res = em.createQuery("SELECT kc FROM KanbanList kc WHERE kc.kanbanBoard = :board")
                .setParameter("board", board)
                .getResultList();

        return res;
    }

    @Override
    public KanbanCard retrieveKanbanCardById(Long kanbanCardId) throws KanbanCardDoesNotExistException {
        try {
            KanbanCard card = em.find(KanbanCard.class, kanbanCardId);
            if (card == null) {
                throw new KanbanCardDoesNotExistException("Kanban Card does not exist!");
            }

            return card;
        } catch (IllegalArgumentException ex) {
            throw new KanbanCardDoesNotExistException("Kanban Card does not exist!");
        }
    }

    @Override
    public List<KanbanCard> retrieveKanbanCardsBykanbanListId(Long kanbanListId) throws KanbanListDoesNotExistException {
        List<KanbanCard> res = null;

        KanbanList list = retrieveKanbanListById(kanbanListId);

        res = em.createQuery("SELECT kc FROM KanbanCard kc WHERE kc.kanbanList = :list")
                .setParameter("list", list)
                .getResultList();

        return res;
    }

    @Override
    public Long createDefaultKanbanBoard(Long GroupId) throws GroupEntityDoesNotExistException {
        try {
            GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(GroupId);
            
            StudentEntity poster = group.getPoster();
            
            KanbanBoard board = new KanbanBoard(String.format("%s's Kanban Board", group.getGroupName()), group);
            
            board.getKanbanLists().add(new KanbanList("Back log", board));
            board.getKanbanLists().add(new KanbanList("Tasks", board));
            board.getKanbanLists().add(new KanbanList("Doing", board));
            board.getKanbanLists().add(new KanbanList("Finished", board));
            board.getKanbanLists().add(new KanbanList("Archive", board));
            
            board.getKanbanLists().forEach(l -> l.getKanbanCards().add(new KanbanCard("New Card", "", null, null, poster, l)));
            
            em.persist(board);
            em.flush();
            
            return board.getKanbanBoardId();
        } catch (InputDataValidationException ex) {
            Logger.getLogger(KanbanSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DoesNotExistException ex) {
            throw new GroupEntityDoesNotExistException("This group does not exist!");
        }
        
        return null;
    }

}
