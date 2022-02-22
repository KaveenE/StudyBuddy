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
public class AccountAlreadyExistsException extends AlreadyExistsException{

    public AccountAlreadyExistsException() {
        this("Account already exists!");
    }

    public AccountAlreadyExistsException(String msg) {
        super(msg);
    }
    
}
