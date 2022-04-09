/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author larby
 */
public class SystemException extends DoesNotExistException{

    public SystemException() {
        this("Unknown System Error");
    }

    public SystemException(String string) {
        super(string);
    }
    
}