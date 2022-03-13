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
public class GroupAccessRightsException extends AccessRightsException {

    public GroupAccessRightsException() {
        this("Insufficient access rights!");
    }

    public GroupAccessRightsException(String msg) {
        super(msg);
    }
}
