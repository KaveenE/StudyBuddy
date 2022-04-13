/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.passwordHelper;

import entities.StudentEntity;

public class PasswordHelper {

    private StudentEntity studentEntityToChangePassword;
    private String newPassword;

    public PasswordHelper() {
    }

    public PasswordHelper(StudentEntity studentEntityToChangePassword, String newPassword) {
        this.studentEntityToChangePassword = studentEntityToChangePassword;
        this.newPassword = newPassword;
    }

    public StudentEntity getStudentEntityToChangePassword() {
        return studentEntityToChangePassword;
    }

    public void setStudentEntityToChangePassword(StudentEntity studentEntityToChangePassword) {
        this.studentEntityToChangePassword = studentEntityToChangePassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
