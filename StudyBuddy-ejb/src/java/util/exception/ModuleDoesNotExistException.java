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
public class ModuleDoesNotExistException extends DoesNotExistException{

    public ModuleDoesNotExistException() {
        this("Module does not exist!");
    }

    public ModuleDoesNotExistException(String string) {
        super(string);
    }
    
}