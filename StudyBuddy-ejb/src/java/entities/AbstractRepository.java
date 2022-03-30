/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public abstract class AbstractRepository<T extends AbstractEntity, ID> {

    protected abstract EntityManager entityManager();

    private Class<T> entityClass() {
        @SuppressWarnings("unchecked")
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    public List<T> findAll() {

        CriteriaBuilder cb = this.entityManager().getCriteriaBuilder();

        CriteriaQuery<T> q = cb.createQuery(entityClass());
        Root<T> c = q.from(entityClass());

        return entityManager().createQuery(q).getResultList();
    }

    public T save(T entity) {
        if (entity.getId() == null) {
            entityManager().persist(entity);

            return entity;
        } else {
            return entityManager().merge(entity);
        }
    }

    public T findById(ID id) {
        return entityManager().find(entityClass(), id);
    }

    public void delete(T entity) {
        T _entity = entityManager().merge(entity);
        entityManager().remove(_entity);
    }

    public void deleteById(ID id) {
        T _entity = findById(id);
        entityManager().remove(_entity);
    }

    public Stream<T> stream() {
        CriteriaBuilder cb = this.entityManager().getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityClass());
        Root<T> c = q.from(entityClass());

        return entityManager().createQuery(q).getResultStream();
    }

    public Optional<T> findOptionalById(ID id) {
        return Optional.ofNullable(findById(id));
    }

}