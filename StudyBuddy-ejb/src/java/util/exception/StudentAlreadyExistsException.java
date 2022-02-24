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
public class StudentAlreadyExistsException extends AlreadyExistsException{

    public StudentAlreadyExistsException() {
        this("Student already exists!");
    }

    public StudentAlreadyExistsException(String msg) {
        super(msg);
    }
    
}
