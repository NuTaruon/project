package com.game.repository;

import com.game.entity.Player;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
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
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "malishka");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.HBM2DDL_AUTO, "update");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Player.class)
                .addProperties(properties)
                .buildSessionFactory();

    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("select * from rpg.player limit :l offset :o", Player.class)
                    .setParameter("o", pageNumber * pageSize)
                    .setParameter("l", pageSize)
                    .list();
        }
    }

    @Override
    public int getAllCount() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("player.getCount").getFirstResult();
        }
    }

    @Override
    public Player save(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(player);
                session.flush();
                transaction.commit();
                return player;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException("Ошибка при сохранении игрока", e);
            }
        }
    }

    @Override
    public Player update(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Player mergedPlayer = (Player) session.merge(player);
                session.update(mergedPlayer);
                transaction.commit();
                return mergedPlayer;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException("Ошибка при обновлении игрока", e);
            }
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Player player = session.get(Player.class, id);
                transaction.commit();
                return Optional.ofNullable(player);
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException("Ошибка при поиске игрока", e);
            }
        }
    }

    @Override
    public void delete(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                if (player == null) {
                    throw new IllegalArgumentException("Игрок не может быть null");
                }
                if (player.getId() == null) {
                    throw new IllegalArgumentException("ID игрока не может быть null для удаления");
                }
                Player existingPlayer = session.get(Player.class, player.getId());

                if (existingPlayer == null) {
                    throw new EntityNotFoundException("Игрок с ID " + player.getId() + " не найден");
                }

                session.delete(existingPlayer);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException("Ошибка при удалении игрока", e);
            }
        }
    }

    @PreDestroy
    public void beforeStop() {
        try(Session session = sessionFactory.openSession()) {
            session.close();
        }
    }
}