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
public class RatingAlreadyExistsException extends AlreadyExistsException{

    public RatingAlreadyExistsException() {
        this("Rating already exists!");
    }

    public RatingAlreadyExistsException(String msg) {
        super(msg);
    }
    
}