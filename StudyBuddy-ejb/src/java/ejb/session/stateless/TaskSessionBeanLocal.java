/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.TaskEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wenting
 */
@Local
public interface TaskSessionBeanLocal {

    public List<TaskEntity> findByStatus(TaskEntity.Status status);
    
}
