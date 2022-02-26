/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author seanc
 */
public class ReportDoesNotExistException extends DoesNotExistException {

    public ReportDoesNotExistException() {
        this("Report does not exist!");
    }

    public ReportDoesNotExistException(String string) {
        super(string);
    }

}
