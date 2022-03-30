/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author wenting
 */
public class TaskNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>TaskNotFoundException</code> without
     * detail message.
     */


	public TaskNotFoundException(Long taskId) {
		super(String.format("task id:%s not found!", taskId));
	}
}
