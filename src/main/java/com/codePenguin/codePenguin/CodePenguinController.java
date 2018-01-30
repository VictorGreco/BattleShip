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

    @Autowired
    private SalvoRepository salvoRepository;
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
    public ResponseEntity<Map<String, Object>> postShips(Authentication authentication,
                                                        @PathVariable long gamePlayerId,
                                                        @RequestBody List<Ship>shipList) {
        if (authentication != null){
            if(gamePlayerRepository.findOne(gamePlayerId) != null){
                GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
                Player user = playerRepository.findByUserName(authentication.getName());
                if(gamePlayer.getPlayer().getId() == user.getId()){
                    if(gamePlayer.getShips().isEmpty() && shipList.size() == 5){
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

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postSalvo(Authentication authentication,
                                                         @PathVariable long gamePlayerId,
                                                         @RequestBody List<String> shotsOfTurn) {
        if (authentication != null){
            if(gamePlayerRepository.findOne(gamePlayerId) != null){
                Game currentGame = gamePlayerRepository.findOne(gamePlayerId).getGame();
                Player user = playerRepository.findByUserName(authentication.getName());
                GamePlayer ownerGp = null;
                GamePlayer otherGp = null;
                List<SingleShot> singleShotList = new ArrayList<>();
                for (GamePlayer gamePlayer: currentGame.getGamePlayers()
                     ) {
                    if(gamePlayer.getPlayer().getUserName().equals(user.getUserName())){
                         ownerGp = gamePlayer;
                    }else{
                         otherGp = gamePlayer;
                    }
                }

                for (String shotLoc: shotsOfTurn
                     ) {
                    SingleShot newShot = new SingleShot(shotLoc);
                    for (Ship ship: otherGp.getShips()
                         ) {
                        if(ship.getShipPositions().contains(newShot.getLocation())){
                            newShot.setStatus(true);
                            int hitTimes =ship.getHitTimes();
                            hitTimes ++;
                            ship.setHitTimes(hitTimes);
                        }
                    }
                    singleShotList.add(newShot);
                }
                Salvo newSalvo = new Salvo(1, ownerGp, singleShotList);
                ownerGp.addSalvo(newSalvo);
                salvoRepository.save(newSalvo);
                    return new ResponseEntity<>(makeResponseEntityMap("OK", ""), HttpStatus.CREATED);
                }else{
                    return new ResponseEntity<>(makeResponseEntityMap("error", "Your're Not the Owner"), HttpStatus.UNAUTHORIZED);
                }
            }else{
            return new ResponseEntity<>(makeResponseEntityMap("error", "Not Logged User"), HttpStatus.UNAUTHORIZED);
            }
        }


    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> makeGameViewDTO(@PathVariable long gamePlayerId, Authentication authentication){

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
            dto.put("history", myGame.getGamePlayers()
                    .stream()
                    .map(gamePlayer -> makeGPsByTurnDTO(gamePlayer, user))
                    .collect(Collectors.toList()));
            return new ResponseEntity<>(makeResponseEntityMap("OK", dto), HttpStatus.CREATED);
        }else{
            dto.put("ERROR", "Your're Not the Owner");
            return new ResponseEntity<>(makeResponseEntityMap("error", dto), HttpStatus.UNAUTHORIZED);
        }

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
        dto.put("shipHits",ship.getHitTimes());
        dto.put("shipStatus",ship.getShipStatus());
        return dto;
    }
    private Map<String, Object> makeGPsByTurnDTO(GamePlayer currentGP, Player userLogged){
        Map<String, Object> dtoGp = new LinkedHashMap<>();
        String currentPlayer;
        if(currentGP.getPlayer().equals(userLogged)){
            currentPlayer = "owner";
        }else{
            currentPlayer = "enemy";
        }

        dtoGp.put(currentPlayer, currentGP.getSalvoes()
                .stream()
                .map(salvo -> makeTurnDTO(salvo, userLogged))
                .collect(Collectors.toList()));

        return dtoGp;
    }
    private Map<String, Object> makeTurnDTO(Salvo currentSalvo, Player userLogged){
        Map<String, Object> dtoTurn = new LinkedHashMap<>();
        GamePlayer ownerGp = null;
        GamePlayer enemyGp = null;
        for (GamePlayer gamePlayer: currentSalvo.getGamePlayer().getGame().getGamePlayers()
             ) {
            if(gamePlayer.getPlayer().getUserName() == userLogged.getUserName()){
                 ownerGp = gamePlayer;
            }else{
                 enemyGp = gamePlayer;
            }
        }
        dtoTurn.put("turn", currentSalvo.getTurnNumber());
        dtoTurn.put("myShots", ownerGp.getSalvoes()
                .stream()
                .filter(salvo -> salvo.getTurnNumber() == currentSalvo.getTurnNumber())
                .map(salvo -> makeShotsDTO(salvo))
                .collect(Collectors.toList()));
        dtoTurn.put("enemyShots", enemyGp.getSalvoes()
                .stream()
                .filter(salvo -> salvo.getTurnNumber() == currentSalvo.getTurnNumber())
                .map(salvo -> makeShotsDTO(salvo))
                .collect(Collectors.toList()));
        dtoTurn.put("myShipStatus", ownerGp.getShips()
                .stream()
                .map(ship -> verifyShipStatus(ship))
                .collect(Collectors.toList()));
        dtoTurn.put("enemyShipStatus", enemyGp.getShips()
                .stream()
                .map(ship -> verifyShipStatus(ship))
                .collect(Collectors.toList()));

        return dtoTurn;
    }

    private Map<String, Object> verifyShipStatus(Ship ship){
        Map<String, Object> dtoShip = new LinkedHashMap<>();
        dtoShip.put("ShipType", ship.getType());
        dtoShip.put("ShipHit", ship.getHitTimes());
        dtoShip.put("ShipStatus", ship.getShipStatus());
        return dtoShip;
    }
    private Map<String, Object> makeShotsDTO(Salvo salvo){
        Map<String, Object> dtoShots = new LinkedHashMap<>();
        dtoShots.put("shots", salvo.getSalvoLocations()
                        .stream()
                        .map(singleShot -> makeSingleShotDTO(singleShot))
                        .collect(Collectors.toList()));
        return dtoShots;
    }

    private Map<String, Object> makeSingleShotDTO(SingleShot singleShot){
        Map<String, Object> dtoSingleShot = new LinkedHashMap<>();
        dtoSingleShot.put("location", singleShot.getLocation());
        dtoSingleShot.put("status",singleShot.getStatus());
        return dtoSingleShot;
    }
//[ENDS GENERAL FUNCTIONS]
}