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
public class AdvertismentAlreadyExistsException extends AlreadyExistsException{

    public AdvertismentAlreadyExistsException() {
        this("Advertisment already exists!");
    }

    public AdvertismentAlreadyExistsException(String msg) {
        super(msg);
    }
    
}
