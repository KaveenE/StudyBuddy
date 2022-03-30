/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entities.AbstractRepository;
import javax.ejb.Stateless;
import entities.TaskEntity;
import java.util.List;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author wenting
 */
@Stateless
public class TaskSessionBean extends AbstractRepository <TaskEntity, Long> implements TaskSessionBeanLocal {

    @PersistenceContext(unitName = "StudyBuddy-ejbPU")
    private EntityManager em;

    @Override
    public List<TaskEntity> findByStatus(TaskEntity.Status status) {
        Objects.requireNonNull(status, "task status can not be null");
        
        return this.stream()
                .filter(t -> t.getStatus() == status)
                .sorted(TaskEntity.DEFAULT_COMPARATOR)
                .collect(toList());
    }

    @Override
    protected EntityManager entityManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
