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
public class AccountDoesNotExistException extends DoesNotExistException{

    public AccountDoesNotExistException() {
        this("Account does not exist!");
    }

    public AccountDoesNotExistException(String string) {
        super(string);
    }
    
}
