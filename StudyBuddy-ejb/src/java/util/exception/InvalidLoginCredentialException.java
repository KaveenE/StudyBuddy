/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author SCXY
 */
public class InvalidLoginCredentialException extends Exception {

    public InvalidLoginCredentialException() {
        super("Username does not exist or invalid password!");
    }

    public InvalidLoginCredentialException(String msg) {
        super(msg);
    }
}
