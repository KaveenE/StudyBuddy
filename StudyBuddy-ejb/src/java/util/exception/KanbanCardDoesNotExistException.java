/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author larby
 */
public class KanbanCardDoesNotExistException extends DoesNotExistException {

    public KanbanCardDoesNotExistException() {
        this("Kanban card does not exist!");
    }

    public KanbanCardDoesNotExistException(String string) {
        super(string);
    }

}
