/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AccountSessionBeanLocal;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

/**
 *
 * @author enkav
 */
@Singleton
@LocalBean
public class DataInitSessionBean {

    @EJB
    private AccountSessionBeanLocal accountSessionBean;
    
}
