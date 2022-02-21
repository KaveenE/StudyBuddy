/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.ModuleEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author SCXY
 */
@Local
public interface ModuleEntitySessionBeanLocal {

    public List<ModuleEntity> retrieveAllModules();
    
}
