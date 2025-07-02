package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import org.hibernate.cfg.Environment;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository("db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private static PlayerRepositoryDB instance;
    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/rpg");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "Burduzann123");

        this.sessionFactory = new Configuration()
                .addAnnotatedClass(Player.class)
                .setProperties(properties)
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (instance == null) {
            instance = new PlayerRepositoryDB();
        }
        return instance.sessionFactory;
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try (Session session = getSessionFactory().openSession()) {
            NativeQuery<Player> query = session.createNativeQuery("select * from rpg.player", Player.class);
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        }
    }

    @Override
    public int getAllCount() {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createNamedQuery("player.getAllCount", Long.class);
            return Math.toIntExact(query.uniqueResult());
        }
    }

    @Override
    public Player save(Player player) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save player", e);
        }
    }

    @Override
    public Player update(Player player) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update player", e);
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Player.class, id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to find player by ID: " + id, e);
        }
    }

    @Override
    public void delete(Player player) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(player);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete player", e);
        }
    }

    @PreDestroy
    public void beforeStop() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
