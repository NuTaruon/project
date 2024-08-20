package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;


@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {


    public PlayerRepositoryDB() {
    MySessionFactory mySessionFactory = new MySessionFactory();
     mySessionFactory.getSessionFactory();

    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try(Session session = MySessionFactory.getSessionFactory().openSession()) {
            NativeQuery<Player> query = session.createNativeQuery("select* from rpg.player",Player.class);
            query.setFirstResult(pageNumber*pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        }
    }

    @Override
    public int getAllCount() {
        try(Session session = MySessionFactory.getSessionFactory().openSession()) {
            Query<Long> query = session.createNamedQuery("player_getAllCount", Long.class);
            return Math.toIntExact(query.uniqueResult());

        }
    }

    @Override
    public Player save(Player player) {
        try(Session session = MySessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
            return player;

        }
    }

    @Override
    public Player update(Player player) {
        try(Session session = MySessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return player;

        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try(Session session = MySessionFactory.getSessionFactory().openSession()) {
            Player player = session.find(Player.class,id);
            return Optional.of(player);}
    }

    @Override
    public void delete(Player player) {
        try(Session session =MySessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(player);
            transaction.commit();}
    }

    @PreDestroy
    public void beforeStop() {
        MySessionFactory.getSessionFactory().close();
    }
}