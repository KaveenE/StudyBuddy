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
public class SchoolAlreadyExistsException extends AlreadyExistsException{

    public SchoolAlreadyExistsException() {
        this("School already exists!");
    }

    public SchoolAlreadyExistsException(String msg) {
        super(msg);
    }
    
}
