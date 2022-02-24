/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author wenting
 */

public class StudentDoesNotExistException extends DoesNotExistException{

    public StudentDoesNotExistException() {
        this("Student does not exist!");
    }

    public StudentDoesNotExistException(String string) {
        super(string);
    }
    
}
