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
public class ReportAlreadyExistsException extends AlreadyExistsException {

    public ReportAlreadyExistsException() {
        this("Report already exists!");
    }

    public ReportAlreadyExistsException(String msg) {
        super(msg);
    }

}
