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
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.GroupEntityDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.KanbanBoardAlreadyExistsException;
import util.exception.KanbanBoardDoesNotExistException;
import util.exception.KanbanCardAlreadyExistsException;
import util.exception.KanbanCardDoesNotExistException;
import util.exception.KanbanListAlreadyExistsException;
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
    public Long createNewKanbanBoard(KanbanBoard newKanbanBoard, Long groupId) throws DoesNotExistException, InputDataValidationException, AlreadyExistsException, UnknownPersistenceException {
        EJBHelper.throwValidationErrorsIfAny(newKanbanBoard);
        EJBHelper.requireNonNull(groupId, new GroupEntityDoesNotExistException("Group Id must not be null"));

        try {
            GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(groupId);

            newKanbanBoard.setGroup(group);
            group.getKanbanBoards().add(newKanbanBoard);

            em.persist(newKanbanBoard);
            em.flush();

        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new KanbanBoardAlreadyExistsException());
        }

        return newKanbanBoard.getKanbanBoardId();
    }

    @Override
    public List<KanbanBoard> retrieveAllKanbanBoards() {
        return em.createQuery("SELECT kb FROM KanbanBoard kb")
                .getResultList();
    }

    @Override
    public void updateKanbanBoard(KanbanBoard kanbanBoard) throws DoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(kanbanBoard);

        KanbanBoard kanbanBoardToUpdate = em.find(KanbanBoard.class, kanbanBoard.getKanbanBoardId());
        EJBHelper.requireNonNull(kanbanBoardToUpdate, new KanbanBoardDoesNotExistException());

        kanbanBoardToUpdate.setHeading(kanbanBoard.getHeading());

    }

    @Override
    public void deleteKanbanBoard(Long kanbanBoardId) throws DoesNotExistException {
        KanbanBoard kanbanBoardToDelete = em.find(KanbanBoard.class, kanbanBoardId);
        EJBHelper.requireNonNull(kanbanBoardToDelete, new KanbanBoardDoesNotExistException());

        kanbanBoardToDelete.getGroup().getKanbanBoards().remove(kanbanBoardToDelete);
        kanbanBoardToDelete.setGroup(null);
        em.remove(kanbanBoardToDelete);

    }

    @Override
    public Long createNewKanbanList(KanbanList newKanbanList, Long kanbanBoardId) throws UnknownPersistenceException, AlreadyExistsException, DoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(newKanbanList);
        EJBHelper.requireNonNull(kanbanBoardId, new KanbanBoardDoesNotExistException("Kanban board Id must not be null"));

        KanbanBoard kanbanBoard = retrieveKanbanBoardById(kanbanBoardId);
        newKanbanList.setKanbanBoard(kanbanBoard);
        kanbanBoard.getKanbanLists().add(newKanbanList);

        try {
            em.persist(newKanbanList);
            em.flush();
        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new KanbanListAlreadyExistsException());
        }

        return newKanbanList.getKanbanListId();
    }

    @Override
    public List<KanbanList> retrieveAllKanbanLists() {
        return em.createQuery("SELECT kb FROM KanbanList kb")
                .getResultList();
    }

    @Override
    public void updateKanbanList(KanbanList kanbanList) throws DoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(kanbanList);
        KanbanList kanbanListToUpdate = em.find(KanbanList.class, kanbanList.getKanbanListId());
        EJBHelper.requireNonNull(kanbanListToUpdate, new KanbanListDoesNotExistException());
        //So what to update?
    }

    @Override
    public void deleteKanbanList(Long kanbanListId) throws DoesNotExistException {
        KanbanList kanbanListToDelete = em.find(KanbanList.class, kanbanListId);
        EJBHelper.requireNonNull(kanbanListToDelete, new KanbanListDoesNotExistException());

        kanbanListToDelete.getKanbanBoard().getKanbanLists().remove(kanbanListToDelete);
        kanbanListToDelete.setKanbanBoard(null);
        em.remove(kanbanListToDelete);

    }

    @Override
    public Long createNewKanbanCard(KanbanCard newKanbanCard, Long kanbanListId, Long authorStudentId) throws UnknownPersistenceException, DoesNotExistException, DoesNotExistException, AlreadyExistsException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(newKanbanCard);
        EJBHelper.requireNonNull(kanbanListId, new KanbanListDoesNotExistException("Kanban board Id must not be null"));
        EJBHelper.requireNonNull(authorStudentId, new StudentDoesNotExistException("Student Id must not be null"));

        KanbanList kanbanList = retrieveKanbanListById(kanbanListId);
        StudentEntity student = studentSessionBean.retrieveStudentById(authorStudentId);

        newKanbanCard.setKanbanList(kanbanList);
        kanbanList.getKanbanCards().add(newKanbanCard);
        newKanbanCard.setAuthor(student);

        try {
            em.persist(newKanbanCard);
            em.flush();

        } catch (PersistenceException ex) {
            AlreadyExistsException.throwAlreadyExistsOrUnknownException(ex, new KanbanCardAlreadyExistsException());
        }

        return newKanbanCard.getKanbanCardId();
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
    public void deleteKanbanCard(Long kanbanCardId) throws DoesNotExistException {
        KanbanCard kanbanCard = em.find(KanbanCard.class, kanbanCardId);
        EJBHelper.requireNonNull(kanbanCard, new KanbanCardDoesNotExistException());

        kanbanCard.getKanbanList().getKanbanCards().remove(kanbanCard);
        kanbanCard.setKanbanList(null);
        kanbanCard.getAssignedStudents().forEach(s -> s.getAssignedCards().remove(kanbanCard));
        kanbanCard.setAssignedStudents(new ArrayList<>());

        em.remove(kanbanCard);
    }

    @Override
    public List<KanbanCard> retrieveKanbanCardsAssignedToStudents(Long StudentId) throws DoesNotExistException, InputDataValidationException {
        List<KanbanCard> kanbanCards = studentSessionBean.retrieveStudentById(StudentId).getAssignedCards();
        kanbanCards.forEach(card -> card.getAssignedStudents());

        return kanbanCards;
    }

    @Override
    public KanbanBoard retrieveKanbanBoardById(Long kanbanBoardId) throws DoesNotExistException {
        KanbanBoard board = em.find(KanbanBoard.class, kanbanBoardId);
        EJBHelper.requireNonNull(board, new KanbanBoardDoesNotExistException());

        board.getKanbanLists();

        return board;

    }

    @Override
    public List<KanbanBoard> retrieveKanbanBoardsByGroupId(Long groupId) throws DoesNotExistException, InputDataValidationException {
        List<KanbanBoard> kanbanBoards = null;

        GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(groupId);
        kanbanBoards = group.getKanbanBoards();

        return kanbanBoards;
    }

    @Override
    public KanbanList retrieveKanbanListById(Long kanbanListId) throws DoesNotExistException {
        KanbanList list = em.find(KanbanList.class, kanbanListId);
        EJBHelper.requireNonNull(list, new KanbanListDoesNotExistException());
        return list;
    }

    @Override
    public List<KanbanList> retrieveKanbanListsBykanbanBoardId(Long kanbanBoardId) throws DoesNotExistException {
        return retrieveKanbanBoardById(kanbanBoardId).getKanbanLists();
    }

    @Override
    public KanbanCard retrieveKanbanCardById(Long kanbanCardId) throws DoesNotExistException {
        KanbanCard card = em.find(KanbanCard.class, kanbanCardId);
        EJBHelper.requireNonNull(card, new KanbanCardDoesNotExistException());
        return card;
    }

    @Override
    public List<KanbanCard> retrieveKanbanCardsBykanbanListId(Long kanbanListId) throws DoesNotExistException {
        KanbanList list = retrieveKanbanListById(kanbanListId);
        return list.getKanbanCards();
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
            
            EJBHelper.throwValidationErrorsIfAny(board);
            
            for (KanbanList l : board.getKanbanLists()) {
                EJBHelper.throwValidationErrorsIfAny(l);
                for (KanbanCard c : l.getKanbanCards()) {
                    EJBHelper.throwValidationErrorsIfAny(c);
                }
            }

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
