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
public class KanbanListAlreadyExistsException extends AlreadyExistsException{

    public KanbanListAlreadyExistsException() {
        this("Kanban list already exists!");
    }

    public KanbanListAlreadyExistsException(String msg) {
        super(msg);
    }
    
    
}
