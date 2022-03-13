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
public class CreateNewModuleException extends CreateNewEntityException {

    public CreateNewModuleException() {
        this("An exception happened when creating a new module!");
    }

    public CreateNewModuleException(String msg) {
        super(msg);
    }
}
