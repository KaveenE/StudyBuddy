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
import util.exception.GroupEntityDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.KanbanBoardDoesNotExistException;
import util.exception.KanbanCardDoesNotExistException;
import util.exception.KanbanListDoesNotExistException;
import util.exception.StudentDoesNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author larby
 */
@Local
public interface KanbanSessionBeanLocal {
    public Long createNewKanbanBoard(KanbanBoard newKanbanBoard, Long groupId) throws UnknownPersistenceException, GroupEntityDoesNotExistException, InputDataValidationException;
    
    public List<KanbanBoard> retrieveAllKanbanBoards();

    public KanbanBoard retrieveKanbanBoardById(Long kanbanBoardId) throws KanbanBoardDoesNotExistException;

    public List<KanbanBoard> retrieveKanbanBoardsByGroupId(Long groupId) throws GroupEntityDoesNotExistException;
    
    public void updateKanbanBoard(KanbanBoard kanbanBoard) throws KanbanBoardDoesNotExistException, InputDataValidationException;
    
    public void deleteKanbanBoard(Long kanbanBoardId) throws KanbanBoardDoesNotExistException;
    
    
    public Long createNewKanbanList(KanbanList newKanbanList, Long kanbanBoardId) throws UnknownPersistenceException, KanbanBoardDoesNotExistException, InputDataValidationException;
    
    public List<KanbanList> retrieveAllKanbanLists();

    public KanbanList retrieveKanbanListById(Long kanbanListId) throws KanbanListDoesNotExistException;

    public List<KanbanList> retrieveKanbanListsBykanbanBoardId(Long kanbanBoardId) throws KanbanBoardDoesNotExistException;
    
    public void updateKanbanList(KanbanList kanbanList) throws KanbanListDoesNotExistException, InputDataValidationException;
    
    public void deleteKanbanList(Long kanbanListId) throws KanbanListDoesNotExistException;
    
    
    public Long createNewKanbanCard(KanbanCard newKanbanCard, Long kanbanListId, Long authorStudentId) throws UnknownPersistenceException, StudentDoesNotExistException, KanbanListDoesNotExistException, InputDataValidationException;
    
    public List<KanbanCard> retrieveAllKanbanCards();

    public KanbanCard retrieveKanbanCardById(Long kanbanCardId) throws KanbanCardDoesNotExistException;

    public List<KanbanCard> retrieveKanbanCardsBykanbanListId(Long kanbanListId) throws KanbanListDoesNotExistException;
    
    public List<KanbanCard> retrieveKanbanCardsAssignedToStudents(Long StudentId) throws StudentDoesNotExistException;

    public void updateKanbanCard(KanbanCard KanbanCard) throws KanbanCardDoesNotExistException, InputDataValidationException;
    
    public void deleteKanbanCard(Long kanbanCardId) throws KanbanCardDoesNotExistException;

    
    public Long createDefaultKanbanBoard(Long GroupId) throws GroupEntityDoesNotExistException;
}
