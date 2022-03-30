/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.TaskRepositorySessionBean;
import entities.TaskEntity;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.NotNull;
import util.exception.TaskNotFoundException;

/**
 *
 * @author wenting
 */
@Named("viewTaskAction")
@ViewScoped()
public class ViewTaskDetailsAction implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //@Inject
    private static final Logger log = Logger.getLogger(ViewTaskDetailsAction.class.getName());

        @EJB
    private TaskRepositorySessionBean taskRepository;

    @NotNull
    private Long taskId;

    private TaskEntity task;

    public void init() throws TaskNotFoundException {

        log.log(Level.INFO, " get task of id @" + taskId);

        task = taskRepository.findById(taskId);

        if (task == null) {
            throw new TaskNotFoundException(taskId);
        }

    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public TaskEntity getTask() {
        return task;
    }

}

