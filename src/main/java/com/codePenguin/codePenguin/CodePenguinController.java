package com.codePenguin.codePenguin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;
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
        dto.put("leader_board", getLeaderBoard());
        return dto;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,Object>> createNewGame(Authentication authentication) {
        if (authentication != null) {
            Player user = playerRepository.findByUserName(authentication.getName());
            Game newGame = new Game();
            gameRespository.save(newGame);
            GamePlayer newGamePlayer = new GamePlayer(newGame, user);
            gamePlayerRepository.save(newGamePlayer);
            return new ResponseEntity<>(makeResponseEntityMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(makeResponseEntityMap("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
        }
    }
    private Map<String, Object> makeResponseEntityMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/games/{gameId}/players", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,Object>> joinGame(Authentication authentication, @PathVariable long gameId) {
        if(authentication != null){
            Game wantedGame = gameRespository.findOne(gameId);
            Player user = playerRepository.findByUserName(authentication.getName());
            if(wantedGame == null){
                return new ResponseEntity<>(makeResponseEntityMap("error", "Doesn't Exist"), HttpStatus.FORBIDDEN);
            }else{
                if (wantedGame.getGamePlayers().stream().count() == 1){
                    GamePlayer newGamePlayer = new GamePlayer(wantedGame, user);
                    gamePlayerRepository.save(newGamePlayer);
                    return new ResponseEntity<>(makeResponseEntityMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
                }else{
                    return new ResponseEntity<>(makeResponseEntityMap("error", "Game is Full"), HttpStatus.FORBIDDEN);
                }
            }
        }else{
            return new ResponseEntity<>(makeResponseEntityMap("error", "Not Logged User"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> genShips(Authentication authentication,
                                                        @PathVariable long gamePlayerId,
                                                        @RequestBody List<Ship>shipList) {
        if (authentication != null){
            if(gamePlayerRepository.findOne(gamePlayerId) != null){
                GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
                Player user = playerRepository.findByUserName(authentication.getName());
                if(gamePlayer.getPlayer().getId() == user.getId()){
                    if(gamePlayer.getShips().isEmpty()){
                        shipList.stream().forEach(ship -> gamePlayer.addShip(ship));
                        shipList.stream().forEach(ship -> shipRepository.save(ship));
                        return new ResponseEntity<>(makeResponseEntityMap("OK", ""), HttpStatus.CREATED);
                    }else{
                        return new ResponseEntity<>(makeResponseEntityMap("error", "You can't have more Ships"), HttpStatus.FORBIDDEN);
                    }
                }else{
                    return new ResponseEntity<>(makeResponseEntityMap("error", "Your're Not the Owner"), HttpStatus.UNAUTHORIZED);
                }
            }else{
                return new ResponseEntity<>(makeResponseEntityMap("error", "No GamePlayer"), HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>(makeResponseEntityMap("error", "Not Logged User"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> makeGameViewDTO(@PathVariable long gamePlayerId, Authentication authentication){

        Player user = playerRepository.findByUserName(authentication.getName());
        GamePlayer myGamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Game myGame = myGamePlayer.getGame();
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        if(myGamePlayer.getPlayer().equals(user)){
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
        }else{
            dto.put("Error", "ERROR");
        }
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
    private List<Map<String, Object>> getLeaderBoard(){
        return playerRepository
                .findAll()
                .stream()
                .map(player -> makePlayerScoreDTO(player))
                .collect(Collectors.toList());
    }
    private List<Map<String, Object>> getAllGames() {
        return gameRespository
                .findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(Collectors.toList());
    }
    private Map<String, Object> makePlayerScoreDTO(Player currentPlayer){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("pId", currentPlayer.getId());
        dto.put("name", currentPlayer.getUserName());
        dto.put("WinGames", getResult(currentPlayer.getScores(), 2));
        dto.put("LostGames", getResult(currentPlayer.getScores(), 0));
        dto.put("DrawGames", getResult(currentPlayer.getScores(), 1));

        return dto;
    }
    private long getResult(Set<Score> scoreSet, int filter){
        long result = scoreSet
                .stream()
                .filter(score -> score.getScore() == filter)
                .count();
        return result;
    }
    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("gameId", game.getId());
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
        dto.put("gpId", gamePlayer.getId());
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