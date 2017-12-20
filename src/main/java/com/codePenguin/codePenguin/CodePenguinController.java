package com.codePenguin.codePenguin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CodePenguinController {

//[START AUTOWIRED REPOSITORIES]
    @Autowired
    private GameRepository gameRespository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository myGamePlayerRespository;
//[ENDS AUTOWIRED REPOSITORIES]

//[START REQUESTS MAPPINGS]
    @RequestMapping("/games")
    public Map<String, Object> jsonGamesAndUser(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if(authentication != null){
            Player user = playerRepository.findByUserName(authentication.getName());
            dto.put("user", makePlayerDTO(user));

        }else{
            dto.put("user", "null");

        }
        dto.put("games", getAllGames());
        return dto;
    }
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
        dto.put("AllSalvOfGame", myGame.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeSalvoesDTO(gamePlayer))
                .collect(Collectors.toList()));
        return dto;
    }
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missed username or password", HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByUserName(username);
        if (player != null) {
            return new ResponseEntity<>("Username already used", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(username, password));
        return new ResponseEntity<>("User added", HttpStatus.CREATED);
    }

//[ENDS REQUEST MAPPINGS]

//[START GENERAL FUNCTIONS]
    private List<Map<String, Object>> getAllGames() {
        return gameRespository
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
        dto.put("Scores", game.getScores()
                .stream()
                .map(score -> makeScoreDTO(score))
                .collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeGamePlayDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("Player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("pId", player.getId());
        dto.put("name", player.getUserName());
        return dto;
    }

    private Map<String, Object> makeScoreDTO(Score score){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("pId", score.getPlayer().getId());
        dto.put("name", score.getPlayer().getUserName());
        dto.put("Points", score.getScore());
        return dto;
    }

    private Map<String, Object> makeShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("location", ship.getShipPositions());
        return dto;
    }

    private Map<String, Object> makeSalvoesDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gamePlayerId", gamePlayer.getId());
        dto.put("Salvoes", gamePlayer.getSalvoes()
                    .stream()
                    .map(salvo -> makeSalvoDTO(salvo))
                    .collect(Collectors.toList()));
        return dto;
    }
    private Map<String, Object> makeSalvoDTO(Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("Turn", salvo.getTurnNumber());
        dto.put("Locations", salvo.getSalvoLocations());
        return dto;
    }
//[ENDS GENERAL FUNCTIONS]
}