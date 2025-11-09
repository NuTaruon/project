package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/rpg");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "12345678");
        properties.put(Environment.HBM2DDL_AUTO, "update");

        this.sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(Player.class)
                .buildSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        List<Player> players = new ArrayList<>();
        try (Session session = sessionFactory.openSession()){
            Query<Player> query = session.createNativeQuery("select * from player", Player.class);
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
            players = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    @Override
    public int getAllCount() {
        int count = 0;
        try (Session session = sessionFactory.openSession()){
            Query<Long> query = session.createNamedQuery("Player_getAllCount", Long.class);
            count = query.getSingleResult().intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Player save(Player player) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return player;
    }

    @Override
    public Player update(Player player) {
        Player updatedPlayer = null;
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            updatedPlayer = (Player) session.merge(player);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedPlayer;
    }

    @Override
    public Optional<Player> findById(long id) {
        Optional<Player> player = Optional.empty();
        try (Session session = sessionFactory.openSession()){
            Query<Player> query = session.createQuery("select p from Player p where id=:id", Player.class);
            query.setParameter("id", id);
            player = query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return player;
    }

    @Override
    public void delete(Player player) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(player);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}