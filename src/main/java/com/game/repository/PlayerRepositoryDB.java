package com.game.repository;
import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private static PlayerRepositoryDB instance;
    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/rpg");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "study-mysql");

        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(Player.class)
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
        try(Session session = getSessionFactory().openSession()) {
            return session.createNativeQuery("select * FROM Player", Player.class)
                    .setFirstResult(pageNumber * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all players");
        }
    }

    @Override
    public int getAllCount() {
       try(Session session = PlayerRepositoryDB.getSessionFactory().openSession()) {
           Query<Long> query = session.createNamedQuery("Player.countAll", Long.class);
           return Math.toIntExact(query.uniqueResult());
       }
    }

    @Override
    public Player save(Player player) {
        try (Session session = PlayerRepositoryDB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Player",e);
        }
    }

    @Override
    public Player update(Player player) {
        try(Session session = PlayerRepositoryDB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Player",e);
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try(Session session = PlayerRepositoryDB.getSessionFactory().openSession()) {
            Player player = session.get(Player.class,id);
            return Optional.ofNullable(player);
        } catch (Exception e) {
            throw new RuntimeException("Failed find player by ID: " + id,e);
        }
    }

    @Override
    public void delete(Player player) {
        try(Session session = PlayerRepositoryDB.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.remove(player);
            transaction.commit();
        } catch(Exception e) {
            throw new RuntimeException("Failed to delete player");
        }
    }

    @PreDestroy
    public void beforeStop() {
        if(sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}