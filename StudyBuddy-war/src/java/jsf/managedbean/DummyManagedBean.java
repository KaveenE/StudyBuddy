/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author enkav
 */
@Named(value = "dummyManagedBean")
@Dependent
public class DummyManagedBean {

    /**
     * Creates a new instance of DummyManagedBean
     */
    public DummyManagedBean() {
    }
    
}
