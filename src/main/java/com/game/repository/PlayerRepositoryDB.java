package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;


import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/rpg");
        properties.put(Environment.USER, "sa");
        properties.put(Environment.PASS, "sa");
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        sessionFactory = new Configuration()
                .addAnnotatedClass(Player.class)
                .setProperties(properties)
                .buildSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try ( Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Player> query = session.createNativeQuery("select p from player p", Player.class);
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
            List<Player> resultList = query.list();
            transaction.commit();
            return resultList;
        }



    }

    @Override
    public int getAllCount() {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            int allCount = session.createNamedQuery("Player_GetAll", Integer.class).executeUpdate();
            transaction.commit();
            return allCount;
        }
    }

    @Override
    public Player save(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return player;
        }
    }

    @Override
    public Player update(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return player;
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Player> query = session.createQuery("select p from Player p where p.id = :id", Player.class);
            query.setParameter("id", id);
            transaction.commit();
            return Optional.of(query.getSingleResult());
        }

    }

    @Override
    public void delete(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery<Player> nativeQuery = session.createNativeQuery("DELETE FROM rpg.player WHERE id = :playerId", Player.class);
            nativeQuery.setParameter("playerId", player.getId());
            nativeQuery.executeUpdate();
            transaction.commit();
        }
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}