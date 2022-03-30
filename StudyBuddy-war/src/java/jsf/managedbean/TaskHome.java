/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.TaskRepositorySessionBean;
import entities.TaskEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import util.exception.TaskNotFoundException;

@Named("taskHome")
@ViewScoped()
public class TaskHome implements Serializable {

    //@Inject
    private static final Logger log = Logger.getLogger(TaskHome.class.getName());

    @Inject
    private TaskServiceManagedBean taskService;

    private List<TaskDetails> todotasks = new ArrayList<>();

    private List<TaskDetails> doingtasks = new ArrayList<>();

    private List<TaskDetails> donetasks = new ArrayList<>();

    public List<TaskDetails> getTodotasks() {
        return todotasks;
    }

    public List<TaskDetails> getDoingtasks() {
        return doingtasks;
    }

    public List<TaskDetails> getDonetasks() {
        return donetasks;
    }

    public void init() {
        log.log(Level.INFO, "initalizing TaskHome...");
        retrieveAllTasks();
    }

    private void retrieveAllTasks() {
        log.log(Level.INFO, "retriveing all tasks...");
        this.todotasks = findTasksByStatus(TaskEntity.Status.TODO);
        this.doingtasks = findTasksByStatus(TaskEntity.Status.DOING);
        this.donetasks = findTasksByStatus(TaskEntity.Status.DONE);
    }

    private List<TaskDetails> findTasksByStatus(TaskEntity.Status status) {
        List<TaskDetails> taskList = new ArrayList<>();
        List<TaskEntity> tasks = taskService.findByStatus(status);

        tasks.stream().map((task) -> {
            TaskDetails details = new TaskDetails();
            details.setId(task.getId());
            details.setName(task.getName());
            details.setDescription(task.getDescription());
            details.setCreatedDate(task.getCreatedDate());
            details.setLastModifiedDate(task.getLastModifiedDate());
            return details;
        }).forEach((details) -> {
            taskList.add(details);
        });

        return taskList;
    }

    public void deleteTask(Long id) throws TaskNotFoundException {

        log.log(Level.INFO, "delete task of id@{0}", id);

        TaskEntity task = taskService.findById(id);
        taskService.deleteById(id);

        // retrieve all tasks
        retrieveAllTasks();

        FacesMessage deleteInfo = new FacesMessage(FacesMessage.SEVERITY_WARN, "Task is deleted!", "Task is deleted!");
        FacesContext.getCurrentInstance().addMessage(null, deleteInfo);
    }

    public void markTaskDoing(Long id) throws TaskNotFoundException {
        log.log(Level.INFO, "changing task DONG @{0}", id);

        taskService.updateStatus(id, TaskEntity.Status.DOING);

        // retrieve all tasks
        retrieveAllTasks();
    }

    public void markTaskDone(Long id) throws TaskNotFoundException {
        log.log(Level.INFO, "changing task DONE @{0}", id);


        taskService.updateStatus(id, TaskEntity.Status.DONE);

        // retrieve all tasks
        retrieveAllTasks();

    }

}