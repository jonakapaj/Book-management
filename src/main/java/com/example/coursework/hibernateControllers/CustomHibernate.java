package com.example.coursework.hibernateControllers;

import com.example.coursework.model.Client;
import com.example.coursework.model.Comment;
import com.example.coursework.model.Publication;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.PublicationStatus;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class CustomHibernate extends GenericHibernate {
    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCredentials(String login, String password) {
        try {
            entityManager = createEntityManager(entityManagerFactory);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> queryUser = cb.createQuery(User.class);
            Root<User> root = queryUser.from(User.class);
            queryUser.select(root).where(cb.and(cb.equal(root.get("login"), login),
                    cb.equal(root.get("password"), password)));

            Query query = entityManager.createQuery(queryUser);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Publication> getAvailablePublications() {
        try {
            entityManager = createEntityManager(entityManagerFactory);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Publication> q = cb.createQuery(Publication.class);
            Root<Publication> root = q.from(Publication.class);
            q.select(root).where(cb.equal(root.get("publicationStatus"), PublicationStatus.AVAILABLE));

            Query query = entityManager.createQuery(q);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<Publication> getPublicationsByUser(Client client) {
        try {
            entityManager = createEntityManager(entityManagerFactory);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Publication> q = cb.createQuery(Publication.class);
            Root<Publication> root = q.from(Publication.class);
            q.select(root).where(cb.equal(root.get("owner"), client));

            Query query = entityManager.createQuery(q);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<Publication> getPublicationsByStatus(PublicationStatus status) {
        try {
            entityManager = createEntityManager(entityManagerFactory);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Publication> q = cb.createQuery(Publication.class);
            Root<Publication> root = q.from(Publication.class);
            q.select(root).where(cb.equal(root.get("publicationStatus"), status));

            Query query = entityManager.createQuery(q);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    //getCommentsByPublication
    public List<Comment> getCommentsByPublication(Publication publication) {
        try {
            entityManager = createEntityManager(entityManagerFactory);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Comment> q = cb.createQuery(Comment.class);
            Root<Comment> root = q.from(Comment.class);
            q.select(root).where(cb.equal(root.get("publication"), publication));

            Query query = entityManager.createQuery(q);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
