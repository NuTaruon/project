package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(@Autowired PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping()
    public List<PlayerInfo> getAll(@RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize) {
        pageNumber = isNull(pageNumber) ? 0 : pageNumber;
        pageSize = isNull(pageSize) ? 3 : pageSize;

        List<Player> players = playerService.getAll(pageNumber, pageSize);

        if (players.isEmpty()) {
            return new ArrayList<>();
        }

        return players.stream().map(PlayerController::toPlayerInfo).collect(Collectors.toList());
    }

    @GetMapping("/count")
    public Integer getAllCount() {
        return playerService.getAllCount();
    }

    @PostMapping
    public ResponseEntity<PlayerInfo> createPlayer(@RequestBody PlayerInfo info) {
        if (StringUtils.isEmpty(info.name) || info.name.length() > 12)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (info.title.length() > 30) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.race)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.profession)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.birthday) || info.birthday < 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (info.level < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        LocalDate localDate = new Date(info.birthday).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int birthYear = localDate.getYear();
        int currentYear = LocalDateTime.now().getYear();
        if (birthYear < currentYear - 100 || birthYear > currentYear + 100)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        boolean banned = !isNull(info.banned) && info.banned;

        Player player = playerService.createPlayer(info.name, info.title, info.race, info.profession, info.birthday, banned, info.level);
        return ResponseEntity.status(HttpStatus.OK).body(toPlayerInfo(player));
    }

    @PostMapping("/{ID}")
    public ResponseEntity<PlayerInfo> updatePlayer(@PathVariable("ID") long id,
                                                   @RequestBody PlayerInfo info) {
        if (id <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (nonNull(info.name) && (info.name.length() > 12 || info.name.isEmpty()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (nonNull(info.title) && info.title.length() > 30)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (nonNull(info.level) && info.level < 1)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (nonNull(info.birthday) && info.birthday < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Date birthday = new Date(info.birthday);
        Player player = playerService.updatePlayer(id, info.name, info.title, info.race, info.profession, info.banned, info.level, birthday);
        if (isNull(player)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(toPlayerInfo(player));
        }
    }

    @DeleteMapping("/{ID}")
    public ResponseEntity delete(@PathVariable("ID") long id) {
        if (id <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Player player = playerService.delete(id);
        if (isNull(player)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    private static PlayerInfo toPlayerInfo(Player player) {
        if (isNull(player)) return null;

        PlayerInfo result = new PlayerInfo();
        result.id = player.getId();
        result.name = player.getName();
        result.title = player.getTitle();
        result.race = player.getRace();
        result.profession = player.getProfession();
        result.birthday = player.getBirthday().getTime();
        result.banned = player.getBanned();
        result.level = player.getLevel();
        return result;
    }
}