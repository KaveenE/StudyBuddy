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
public class AdvertisementDoesNotExistException extends DoesNotExistException{

    public AdvertisementDoesNotExistException() {
        this("Advertisement does not exist!");
    }

    public AdvertisementDoesNotExistException(String string) {
        super(string);
    }
    
}
