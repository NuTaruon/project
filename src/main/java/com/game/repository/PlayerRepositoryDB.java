package com.game.repository;

import com.game.entity.Player;
import com.mysql.cj.jdbc.Driver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() throws SQLException {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/rpg") ;
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");
        properties.put(Environment.HBM2DDL_AUTO, "update");

        sessionFactory = new Configuration().addAnnotatedClass(Player.class)
                .addProperties(properties)
                .buildSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try(Session session = sessionFactory.openSession()){
            NativeQuery<Player> nativeQuery = session.createNativeQuery("SELECT * FROM rpg.player", Player.class);
            nativeQuery.setFirstResult(pageNumber * pageSize);
            nativeQuery.setMaxResults(pageSize);
            return nativeQuery.list();
        }
    }

    @Override
    public int getAllCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> playerGetAllCount = session.createNamedQuery("player_getAllCount", Long.class);
            return Math.toIntExact(playerGetAllCount.uniqueResult());
        }
    }

    @Override
    public Player save(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(player);
        transaction.commit();
        session.close();
        return player;
    }

    @Override
    public Player update(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(player);
        transaction.commit();
        session.close();
        return player;
    }

    @Override
    public Optional<Player> findById(long id) {
        Session session = sessionFactory.openSession();
        Player player = session.find(Player.class, id);
        return Optional.of(player);
    }

    @Override
    public void delete(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(player);
        transaction.commit();
        session.close();
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}