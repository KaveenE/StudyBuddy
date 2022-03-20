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
public class KanbanBoardAlreadyExistsException extends AlreadyExistsException{

    public KanbanBoardAlreadyExistsException() {
        this("Kanban board already exists!");
    }

    public KanbanBoardAlreadyExistsException(String msg) {
        super(msg);
    }
    
    
}
