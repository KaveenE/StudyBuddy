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
public class KanbanListDoesNotExistException extends DoesNotExistException {

    public KanbanListDoesNotExistException() {
    }

    public KanbanListDoesNotExistException(String string) {
        super(string);
    }

}
