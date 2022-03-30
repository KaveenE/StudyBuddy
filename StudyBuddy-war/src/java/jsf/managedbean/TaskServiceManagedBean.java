/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.TaskRepositorySessionBean;
import entities.TaskEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import util.exception.TaskNotFoundException;
/**
 *
 * @author wenting
 */

@Named(value = "taskServiceManagedBean")
@ApplicationScoped
public class TaskServiceManagedBean {

    @EJB
    private TaskRepositorySessionBean tasks;

//    @Inject
//    TaskRepository tasks;


    public TaskEntity findById(Long id) throws TaskNotFoundException {
        return this.tasks.findOptionalById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<TaskEntity> findByStatus(TaskEntity.Status status) {
        return this.tasks.findByStatus(status);
    }

    public TaskEntity save(TaskEntity data) {
        return this.tasks.save(data);
    }

    public TaskEntity update(Long id, TaskEntity data) throws TaskNotFoundException {
        TaskEntity _existed = findById(id);
        _existed.setName(data.getName());
        _existed.setDescription(data.getDescription());
        return this.tasks.save(_existed);
    }

    public void deleteById(Long id) throws TaskNotFoundException {
        TaskEntity _existed = findById(id);
        this.tasks.delete(_existed);
    }

    public void updateStatus(Long id, TaskEntity.Status status) throws TaskNotFoundException {
        TaskEntity _existed = findById(id);
        _existed.setStatus(status);
        this.tasks.save(_existed);
    }

}