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
public class RatingDoesNotExistException extends DoesNotExistException{

    public RatingDoesNotExistException() {
        this("Rating does not exist!");
    }

    public RatingDoesNotExistException(String string) {
        super(string);
    }
    
}