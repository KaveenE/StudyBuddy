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
public class SchoolDoesNotExistException extends DoesNotExistException{

    public SchoolDoesNotExistException() {
        this("School does not exist!");
    }

    public SchoolDoesNotExistException(String string) {
        super(string);
    }
    
}