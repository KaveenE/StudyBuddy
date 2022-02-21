/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author PP42
 */
public class DoesNotExistException extends Exception {

    public DoesNotExistException() {
    }

    public DoesNotExistException(String string) {
        super(string);
    }

}
