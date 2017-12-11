package com.codePenguin.codePenguin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CodePenguinController {

    @Autowired
    private GameRepository myGameRespository;


//    public List<Game> getAll() {
//        return myGameRespository.findAll();
//    }
    @RequestMapping("/games")
//    public Set<Long> getBillingTypes() {
//        return getAll().stream().map(b -> b.getId()).collect(toSet());}

    public List<Map<String, Object>> getAllGames() {
        return myGameRespository
                .findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(Collectors.toList());
    }

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("create", game.getDate());
        dto.put("GamePlayers", game.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayDTO(gamePlayer))
                .collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeGamePlayDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("Player", gamePlayer.getPlayer());
        return dto;
    }
}