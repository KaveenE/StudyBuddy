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

public class AdminDoesNotExistException extends DoesNotExistException{

    public AdminDoesNotExistException() {
        this("Admin does not exist!");
    }

    public AdminDoesNotExistException(String string) {
        super(string);
    }
    
}
