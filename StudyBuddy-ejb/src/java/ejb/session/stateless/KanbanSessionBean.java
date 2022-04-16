/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.GroupEntity;
import entities.KanbanBoard;
import entities.KanbanCard;
import entities.StudentEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.enumeration.CardType;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.GroupEntityDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.KanbanBoardAlreadyExistsException;
import util.exception.KanbanBoardDoesNotExistException;
import util.exception.KanbanCardAlreadyExistsException;
import util.exception.KanbanCardDoesNotExistException;
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
    public Long createNewKanbanCard(KanbanCard newKanbanCard, Long kanbanBoardId, Long authorStudentId) throws UnknownPersistenceException, AlreadyExistsException, DoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(newKanbanCard);
        EJBHelper.requireNonNull(kanbanBoardId, new KanbanBoardDoesNotExistException("Kanban board Id must not be null"));
        EJBHelper.requireNonNull(authorStudentId, new StudentDoesNotExistException("Student Id must not be null"));

        KanbanBoard kanbanBoard = retrieveKanbanBoardById(kanbanBoardId);
        newKanbanCard.setKanbanBoard(kanbanBoard);
        kanbanBoard.getKanbanCards().add(newKanbanCard);

        StudentEntity author = studentSessionBean.retrieveStudentById(authorStudentId);
        newKanbanCard.setAuthor(author);

        List<StudentEntity> assignedStudents = new ArrayList<>();
        for (StudentEntity assignedStudent : newKanbanCard.getAssignedStudents()) {
            assignedStudents.add(studentSessionBean.retrieveStudentById(assignedStudent.getAccountId()));
        }
        newKanbanCard.setAssignedStudents(assignedStudents);
        assignedStudents.forEach(stud -> stud.getAssignedCards().add(newKanbanCard));

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
        return em.createQuery("SELECT kc FROM KanbanCard kc")
                .getResultList();
    }

    @Override
    public void updateKanbanCard(KanbanCard kanbanCard) throws DoesNotExistException, InputDataValidationException {
        EJBHelper.throwValidationErrorsIfAny(kanbanCard);

        try {
            KanbanCard kanbanCardToUpdate = em.find(KanbanCard.class, kanbanCard.getKanbanCardId());

            if (kanbanCardToUpdate == null) {
                throw new KanbanCardDoesNotExistException("This kanban card does not exist!");
            }

            kanbanCardToUpdate.setHeading(kanbanCard.getHeading());
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
            throw new KanbanCardDoesNotExistException("This kanban card does not exist!");
        }
    }

    @Override
    public void deleteKanbanCard(Long kanbanCardId) throws DoesNotExistException {
        KanbanCard kanbanCardToDelete = retrieveKanbanCardById(kanbanCardId);
        EJBHelper.requireNonNull(kanbanCardToDelete, new KanbanCardDoesNotExistException());

        kanbanCardToDelete.getKanbanBoard().getKanbanCards().remove(kanbanCardToDelete);
        kanbanCardToDelete.setKanbanBoard(null);
        kanbanCardToDelete.getAssignedStudents().forEach(s -> s.getAssignedCards().remove(kanbanCardToDelete));
        kanbanCardToDelete.setAssignedStudents(new ArrayList<>());

        em.remove(kanbanCardToDelete);
        em.flush();
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

        board.getKanbanCards().size();

        return board;

    }

    //testing
    @Override
    public List<KanbanCard> retrieveKanbanCardsByGroupId(Long groupId) throws DoesNotExistException, InputDataValidationException {
        KanbanBoard kanbanBoard = this.retrieveKanbanBoardsByGroupId(groupId).get(0);
        List<KanbanCard> kanbanCards = kanbanBoard.getKanbanCards();
        kanbanCards.size();
        return kanbanCards;
    }

    @Override
    public List<KanbanBoard> retrieveKanbanBoardsByGroupId(Long groupId) throws DoesNotExistException, InputDataValidationException {
        List<KanbanBoard> kanbanBoards = null;

        GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(groupId);
        kanbanBoards = group.getKanbanBoards();
        return kanbanBoards;
    }

    @Override
    public KanbanCard retrieveKanbanCardById(Long kanbanCardId) throws DoesNotExistException {
        KanbanCard card = em.find(KanbanCard.class, kanbanCardId);
        card.getAssignedStudents().size();
        EJBHelper.requireNonNull(card, new KanbanCardDoesNotExistException());
        return card;
    }

    @Override
    public List<KanbanCard> retrieveKanbanCardByBoardId(Long kanbanBoardId) throws DoesNotExistException {
        return retrieveKanbanBoardById(kanbanBoardId).getKanbanCards();
    }

    @Override
    public Long createDefaultKanbanBoard(Long GroupId) throws GroupEntityDoesNotExistException {
        try {
            GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(GroupId);

            StudentEntity poster = group.getPoster();

            KanbanBoard board = new KanbanBoard(String.format("%s's Kanban Board", group.getGroupName()), group);
            group.setKanbanBoards(new ArrayList<>(Arrays.asList(board)));

            board.getKanbanCards().add(new KanbanCard("Sample Heading", "Sample Description", poster, CardType.TASKS, null, board));
            board.getKanbanCards().add(new KanbanCard("Sample Heading", "Sample Description", poster, CardType.INPROGRESS, null, board));
            board.getKanbanCards().add(new KanbanCard("Sample Heading", "Sample Description", poster, CardType.FINISHED, null, board));

            EJBHelper.throwValidationErrorsIfAny(board);
            for (KanbanCard l : board.getKanbanCards()) {
                EJBHelper.throwValidationErrorsIfAny(l);
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
