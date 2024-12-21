package org.example.projet4dx.model.dao;

import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Represents a generic Data Access Object (DAO) for entity T using JPA.
 *
 * @param <T> the entity type that this DAO operates on
 */
public abstract class Dao<T> {
    protected EntityManager em;

    /**
     * Constructs a new Dao with the given EntityManager.
     *
     * @param em the EntityManager to be used for database operations
     */
    public Dao(EntityManager em) {
        this.em = em;
    }
    /**
     * Persists a new entity of type T using the EntityManager in a transactional manner.
     *
     * @param entity the entity to be created and persisted
     */
    public void create(T entity){
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
    /**
     * Updates the specified entity in the database using JPA merge operation.
     *
     * @param entity the entity to be updated
     */
    public void update(T entity){
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
    /**
     * Deletes the specified entity from the database.
     *
     * @param entity the entity to be deleted
     */
    public void delete(T entity){
        try {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
    /**
     * Retrieves an entity of type T by its ID from the database.
     *
     * @param entityClass the entity instance used to determine the class type for lookup
     * @param id the ID of the entity to retrieve
     * @return the entity instance with the given ID, or null if not found
     */
    public T getById(Class<T> entityClass,long id){
        return em.find(entityClass, id);
    }
    /**
     * Retrieves all entities of type T from the database.
     *
     * @return list of all entities of type T
     */
    public abstract List<T> getAll();
}
