package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.repository.PlayerRepositoryDB;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class PlayerRestController extends HttpServlet {
    private PlayerRepository repository;

    @Override
    public void init() {
        repository = new PlayerRepositoryDB(); //Specific implementation
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            handleGetAllPlayers(request, response);
        } else {
            handleGetPlayer(pathInfo, request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleCreatePlayer(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        handleUpdatePlayer(pathInfo, request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        handleDeletePlayer(pathInfo, request, response);
    }

    private void handleGetAllPlayers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().print("[]");
    }

    private void handleGetPlayer(String pathInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = extractIdFromPath(pathInfo);
        if (id != null) {
            Player player = repository.getById(id);
            if (player != null) {
                response.setContentType("application/json");
                response.getWriter().print(playerToJson(player));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handleCreatePlayer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Player player = new Player();
        player.setName("New Player");
        player.setTitle("New Title");
        player.setRace(Race.HUMAN);
        player.setProfession(Profession.WARRIOR);
        player.setExperience(0);
        player.setLevel(1);
        player.setUntilNextLevel(100);
        player.setBirthdate(new Date());


        player.setBanned(false);

        Player savedPlayer = repository.save(player);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().print(playerToJson(savedPlayer));
    }

    private void handleUpdatePlayer(String pathInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = extractIdFromPath(pathInfo);
        if (id != null) {
            Player existingPlayer = repository.getById(id);
            if (existingPlayer != null) {
                existingPlayer.setName("Updated Player");
                Player updatedPlayer = repository.update(existingPlayer);
                response.setContentType("application/json");
                response.getWriter().print(playerToJson(updatedPlayer));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handleDeletePlayer(String pathInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = extractIdFromPath(pathInfo);
        if (id != null) {
            Player player = repository.getById(id);
            if (player != null) {
                repository.delete(player);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private Long extractIdFromPath(String pathInfo) {
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                return Long.parseLong(pathInfo.substring(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String playerToJson(Player player) {
        return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"title\":\"%s\",\"race\":\"%s\",\"profession\":\"%s\",\"experience\":%d,\"level\":%d,\"untilNextLevel\":%d,\"birthday\":%d,\"banned\":%b}",
                player.getId(),
                player.getName(),
                player.getTitle(),
                player.getRace(),
                player.getProfession(),
                player.getExperience(),
                player.getLevel(),
                player.getUntilNextLevel(),
                player.getBirthdate().getTime(), //  getBirthdate
                player.getBanned()
        );
    }
}