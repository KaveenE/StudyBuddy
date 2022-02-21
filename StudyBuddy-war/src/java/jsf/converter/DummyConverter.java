/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author enkav
 */
@Named(value = "dummyConverter")
@Dependent
public class DummyConverter {

    /**
     * Creates a new instance of DummyConverter
     */
    public DummyConverter() {
    }
    
}
