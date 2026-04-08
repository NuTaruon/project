package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        Properties properties = new Properties();

        // DB connection
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/rpg");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");

        // Hibernate settings
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.SHOW_SQL, "true");

        // Авто создание/обновление таблиц
        properties.put(Environment.HBM2DDL_AUTO, "update");

        // Optional
        properties.put(Environment.FORMAT_SQL, "true");

        // Configuration
        Configuration configuration = new Configuration();
        configuration.setProperties(properties);

        // Добавляем Entity
        configuration.addAnnotatedClass(Player.class);

        // Создаем SessionFactory
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            NativeQuery<Player> query = session.createNativeQuery(
                    "SELECT * FROM player", Player.class
            );
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }

    @Override
    public int getAllCount() {
        return 0;
    }

    @Override
    public Player save(Player player) {
        return null;
    }

    @Override
    public Player update(Player player) {
        return null;
    }

    @Override
    public Optional<Player> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Player player) {

    }

    @PreDestroy
    public void beforeStop() {

    }
}