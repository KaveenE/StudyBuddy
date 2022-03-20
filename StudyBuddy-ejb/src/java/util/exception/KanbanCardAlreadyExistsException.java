/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author enkav
 */
public class KanbanCardAlreadyExistsException extends AlreadyExistsException{

    public KanbanCardAlreadyExistsException() {
        this("Kanban card already exists!");
    }

    public KanbanCardAlreadyExistsException(String msg) {
        super(msg);
    }
    
    
}
