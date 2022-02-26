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
public class GroupEntityDoesNotExistException extends DoesNotExistException{

    public GroupEntityDoesNotExistException() {
        this("Group does not exist!");
    }

    public GroupEntityDoesNotExistException(String string) {
        super(string);
    }
    
}
