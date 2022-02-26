/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author enkav
 */
@Entity
public class AdminEntity extends AccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public AdminEntity() {
        super();
    }

    public AdminEntity(String email, String username, String password) {
        super(email, username, password);
    }
}
