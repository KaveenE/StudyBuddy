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
public class StudentPremiumAlreadyExistsException extends AlreadyExistsException {

    public StudentPremiumAlreadyExistsException() {
         this("Student is already a premium buddy!");
    }

    public StudentPremiumAlreadyExistsException(String msg) {
        super(msg);
    }
}
