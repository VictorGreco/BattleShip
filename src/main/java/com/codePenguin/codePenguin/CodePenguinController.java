package com.codePenguin.codePenguin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/games")
    public List<Map<String, Object>> getAllGames() {
        return myGameRespository
                .findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(Collectors.toList());
    }

    @Autowired
    private GamePlayerRepository myGamePlayerRespository;

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> makeGameViewDTO(@PathVariable long gamePlayerId){
        GamePlayer myGamePlayer = myGamePlayerRespository.findOne(gamePlayerId);
        Game myGame = myGamePlayer.getGame();

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", myGamePlayer.getId());
        dto.put("create", myGame.getDate());
        dto.put("GamePlayers", myGame.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayDTO(gamePlayer))
                .collect(Collectors.toList()));
        dto.put("Ships", myGamePlayer.getShips()
                .stream()
                .map( ship-> makeShipDTO(ship))
                .collect(Collectors.toList()));
        return dto;
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

    private Map<String, Object> makeShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("location", ship.getShipPositions());
        return dto;
    }
}