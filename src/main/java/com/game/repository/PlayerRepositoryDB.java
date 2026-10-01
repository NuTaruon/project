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
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private final SessionFactory sessionFactory;
    public PlayerRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/rpg");
        properties.put(Environment.USER, "root"); // Укажите ваш логин от Workbench
        properties.put(Environment.PASS, "Evoruf27"); // Укажите ваш пароль от Workbench
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.HBM2DDL_AUTO, "update");


        this.sessionFactory = new Configuration().setProperties(properties).addAnnotatedClass(Player.class).buildSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try (Session session = this.sessionFactory.openSession()) {
            NativeQuery <Player> query = session.createNativeQuery("select * from player", Player.class);
            query.setFirstResult(pageNumber);
            query.setMaxResults(pageSize);
            return query.list();
        }

    }

    @Override
    public int getAllCount() {
        try (Session session = this.sessionFactory.openSession()) {
         Query query = session.createNativeQuery("SELECT COUNT(*) FROM player", Player.class);
         return ((Long) query.uniqueResult()).intValue();
        }

    }

    @Override
    public Player save(Player player) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
            return player;

        }
    }

    @Override
    public Player update(Player player) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return player;
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = this.sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Player.class, id));
        }
        }

    @Override
    public void delete(Player player) {
      try (Session session = this.sessionFactory.openSession()) {
          Transaction transaction = session.beginTransaction();
          session.remove(player);
          session.flush();
          session.clear();
          transaction.commit();
      }
    }

    @PreDestroy
    public void beforeStop() {
       this.sessionFactory.close();
    }
}

