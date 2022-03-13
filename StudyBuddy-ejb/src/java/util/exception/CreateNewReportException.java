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
public class CreateNewReportException extends CreateNewEntityException {

    public CreateNewReportException() {
        this("An exception happened when creating a new report!");
    }

    public CreateNewReportException(String msg) {
        super(msg);
    }
}
