/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validator;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author enkav
 */
@Named(value = "dummyValidator")
@Dependent
public class DummyValidator {

    /**
     * Creates a new instance of DummyValidator
     */
    public DummyValidator() {
    }
    
}
