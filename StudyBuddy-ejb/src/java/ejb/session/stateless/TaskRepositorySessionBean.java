/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AbstractRepository;
import entities.TaskEntity;
import java.util.List;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wenting
 */
@Stateless
@LocalBean


public class TaskRepositorySessionBean extends AbstractRepository <TaskEntity, Long> implements TaskRepositorySessionBeanLocal {

    @PersistenceContext
    EntityManager em;

    public List<TaskEntity> findByStatus(TaskEntity.Status status) {
        Objects.requireNonNull(status, "task status can not be null");
        
        return this.stream()
                .filter(t -> t.getStatus() == status)
                .sorted(TaskEntity.DEFAULT_COMPARATOR)
                .collect(toList());
    }

    @Override
    protected EntityManager entityManager() {
        return em;
    }

}