package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Properties;

@WebListener
public class PlayerRepositoryDB implements PlayerRepository, ServletContextListener {

    private static SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Properties properties = new Properties();
            properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
            properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/rpg?useSSL=false&serverTimezone=UTC");
            properties.put(Environment.USER, "root");
            properties.put(Environment.PASS, "261508arnLAR!");
            properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
            properties.put(Environment.SHOW_SQL, "true");
            properties.put(Environment.HBM2DDL_AUTO, "update");
            properties.put(Environment.C3P0_MIN_SIZE, "5");
            properties.put(Environment.C3P0_MAX_SIZE, "20");
            properties.put(Environment.C3P0_TIMEOUT, "300");

            sessionFactory = new Configuration()
                    .setProperties(properties)
                    .addAnnotatedClass(Player.class)
                    .buildSessionFactory();

            System.out.println("Hibernate SessionFactory created successfully");

            // Test connection
            testConnection();

        } catch (Exception e) {
            System.err.println("Failed to create Hibernate SessionFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void testConnection() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            // A simple test query
            Long count = session.createQuery("SELECT COUNT(p) FROM Player p", Long.class)
                    .getSingleResult();
            System.out.println("Test connection successful. Players in DB: " + count);
        } catch (Exception e) {
            System.err.println("Test connection failed: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory closed");
        }
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        Session session = sessionFactory.openSession();
        try {
            Query<Player> query = session.createQuery("FROM Player p", Player.class);
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public int getAllCount() {
        Session session = sessionFactory.openSession();
        try {
            return ((Long) session.createNamedQuery("Player.getAllCount")
                    .getSingleResult()).intValue();
        } catch (Exception e) {
            System.err.println("Error in getAllCount: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Player save(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error in save: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Player update(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error in update: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Player getById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Player.class, id);
        } catch (Exception e) {
            System.err.println("Error in getById: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(player);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error in delete: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }
    // Static method for getting the repository
    public static PlayerRepositoryDB getInstance() {
        return new PlayerRepositoryDB();
    }
}