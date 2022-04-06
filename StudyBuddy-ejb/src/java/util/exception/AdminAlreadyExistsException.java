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
public class AdminAlreadyExistsException extends AlreadyExistsException{

    public AdminAlreadyExistsException() {
        this("Admin already exists!");
    }

    public AdminAlreadyExistsException(String msg) {
        super(msg);
    }
    
}
