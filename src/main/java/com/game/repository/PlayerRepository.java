package com.game.repository;
import com.game.entity.Player;
import java.util.List;

public interface PlayerRepository {
    List<Player> getAll(int pageNumber, int pageSize);
    int getAllCount();
    Player save(Player player);
    Player update(Player player);
    Player getById(Long id);
    void delete(Player player);
}