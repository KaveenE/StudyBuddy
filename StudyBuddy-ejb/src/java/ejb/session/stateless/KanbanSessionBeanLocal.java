/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.KanbanBoard;
import entities.KanbanCard;
import entities.KanbanList;
import java.util.List;
import javax.ejb.Local;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.GroupEntityDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.KanbanCardDoesNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author larby
 */
@Local
public interface KanbanSessionBeanLocal {

    public Long createNewKanbanBoard(KanbanBoard newKanbanBoard, Long groupId) throws DoesNotExistException, InputDataValidationException, AlreadyExistsException, UnknownPersistenceException;

    public List<KanbanBoard> retrieveAllKanbanBoards();

    public KanbanBoard retrieveKanbanBoardById(Long kanbanBoardId) throws DoesNotExistException;

    public List<KanbanBoard> retrieveKanbanBoardsByGroupId(Long groupId) throws DoesNotExistException, InputDataValidationException;

    public void updateKanbanBoard(KanbanBoard kanbanBoard) throws DoesNotExistException, InputDataValidationException;

    public void deleteKanbanBoard(Long kanbanBoardId) throws DoesNotExistException;

    public Long createNewKanbanList(KanbanList newKanbanList, Long kanbanBoardId) throws UnknownPersistenceException, AlreadyExistsException, DoesNotExistException, InputDataValidationException;

    public List<KanbanList> retrieveAllKanbanLists();

    public KanbanList retrieveKanbanListById(Long kanbanListId) throws DoesNotExistException;

    public List<KanbanList> retrieveKanbanListsBykanbanBoardId(Long kanbanBoardId) throws DoesNotExistException;

    public void updateKanbanList(KanbanList kanbanList) throws DoesNotExistException, InputDataValidationException;

    public void deleteKanbanList(Long kanbanListId) throws DoesNotExistException;

    public Long createNewKanbanCard(KanbanCard newKanbanCard, Long kanbanListId, Long authorStudentId) throws UnknownPersistenceException, DoesNotExistException, DoesNotExistException, AlreadyExistsException, InputDataValidationException;

    public List<KanbanCard> retrieveAllKanbanCards();

    public KanbanCard retrieveKanbanCardById(Long kanbanCardId) throws DoesNotExistException;

    public List<KanbanCard> retrieveKanbanCardsBykanbanListId(Long kanbanListId) throws DoesNotExistException;

    public List<KanbanCard> retrieveKanbanCardsAssignedToStudents(Long StudentId) throws DoesNotExistException, InputDataValidationException;

    public void updateKanbanCard(KanbanCard KanbanCard) throws KanbanCardDoesNotExistException, InputDataValidationException;

    public void deleteKanbanCard(Long kanbanCardId) throws DoesNotExistException;

    public Long createDefaultKanbanBoard(Long GroupId) throws GroupEntityDoesNotExistException;
}
