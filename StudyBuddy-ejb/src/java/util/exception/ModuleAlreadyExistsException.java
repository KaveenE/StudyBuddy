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
public class ModuleAlreadyExistsException extends AlreadyExistsException{

    public ModuleAlreadyExistsException() {
        this("Module already exists!");
    }

    public ModuleAlreadyExistsException(String msg) {
        super(msg);
    }
    
}
