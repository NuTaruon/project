package com.game.repository;

import com.game.entity.Player;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private final SessionFactory sessionFactory;


    public PlayerRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/rpg?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "Bielik1989");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.FORMAT_SQL, "true");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(Player.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 0) throw new IllegalArgumentException("pageNumber must be >= 0");
        if (pageSize < 1)   throw new IllegalArgumentException("pageSize must be >= 1");

        int offset = pageNumber * pageSize;
        String sql = "select * from player order by id limit :limit offset :offset ";
        try (Session session = sessionFactory.openSession()) {
            NativeQuery<Player> nativeQuery = session.createNativeQuery(sql, Player.class);
            nativeQuery.setParameter("limit", pageSize);
            nativeQuery.setParameter("offset", pageNumber);
            return nativeQuery.list();
        }
    }

    @Override
    public int getAllCount() {
        try (Session session = sessionFactory.openSession()) {
            Long singleResult = session.createNamedQuery("Player.GetAllCount", Long.class).getSingleResult();
            return singleResult.intValue();
        }
    }

    @Override
    public Player save(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
            return player;
        } catch(Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    @Override
    public Player update(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(player);
            transaction.commit();
            return player;
        } catch(Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Player> query = session.createQuery("from Player where id = :id", Player.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.uniqueResult());
        }
    }

    @Override
    public void delete(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(player);
            transaction.commit();
        } catch(Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}