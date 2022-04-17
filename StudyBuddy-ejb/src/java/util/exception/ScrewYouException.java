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
public class ScrewYouException extends Exception{

    public ScrewYouException() {
        this("Your account has been disabled until further notice.");
    }

    public ScrewYouException(String msg) {
        super(msg);
    }
    
    
}
