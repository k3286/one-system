package com.github.k3286.one_system.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import com.github.k3286.one_system.dao.UserDao;
import com.github.k3286.one_system.model.User;

public class UserDaoImpl implements UserDao {

    @Override
    public User getById(String id) {
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("brycen");
        EntityManager em = fac.createEntityManager();
        return em.find(User.class, id);
    }

    @Override
    public List<User> getUserListByName(String name) {
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("brycen");
        EntityManager em = fac.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        EntityType<User> type = em.getMetamodel().entity(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.where(//
                builder.like(root.get(//
                        type.getDeclaredSingularAttribute("name", String.class)), "%" + name + "%"));
        return em.createQuery(criteria).getResultList();
    }

    @Override
    public List<User> getUserList() {
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("brycen");
        EntityManager em = fac.createEntityManager();
        CriteriaQuery<User> q = em.getCriteriaBuilder().createQuery(User.class);
        return em.createQuery(q.select(q.from(User.class))).getResultList();
    }

    @Override
    public void add(User user) {
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("brycen");
        EntityManager em = fac.createEntityManager();
        try {
            em.getTransaction().begin();
            // ないことを確認
            if (getById(user.getId()) == null) {
                em.persist(user);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    @Override
    public void update(User user) {
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("brycen");
        EntityManager em = fac.createEntityManager();
        try {
            em.getTransaction().begin();
            if (getById(user.getId()) != null) {
                em.merge(user);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    @Override
    public void delete(String id) {
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("brycen");
        EntityManager em = fac.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = getById(id);
            if (user != null) {
                em.remove(em.merge(user));
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

}
