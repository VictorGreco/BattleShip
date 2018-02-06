package com.codePenguin.codePenguin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
// adding /api for all the paths.
@RequestMapping("/api")
public class CodePenguinController {

//[START AUTOWIRED REPOSITORIES] -- 5 Autowired
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

// 1-- [START REQUESTS MAPPINGS]

    // 1.1 -- POST method that receive an auth object, create a new game and return HTTP response.
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
    // 1.2 -- POST method that receive an auth object, and gameId from path and if HTTP response is OK, join an existing game.
    @RequestMapping(path = "/games/{gameId}/players", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,Object>> joinGame(Authentication authentication, @PathVariable long gameId) {
        if(authentication != null){
            Game wantedGame = gameRespository.findOne(gameId);
            Player user = playerRepository.findByUserName(authentication.getName());
            if(wantedGame == null){
                return new ResponseEntity<>(makeResponseEntityMap("error", "Doesn't Exist"), HttpStatus.FORBIDDEN);
            }else{
                if (wantedGame.getGamePlayers().size() == 1){
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
    // 1.3 -- POST method that receive an auth object, id from path and a list of objects and create one ship for each object and return HTTP response
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
                        shipList.forEach(gamePlayer::addShip);
                        shipList.forEach(shipRepository::save);
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
    // 1.4 -- POST method that receive an auth object, an ID from path and a list of strings, then create a singleShot 4 each string and 1 turn, return HTTP response.
    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postSalvo(Authentication authentication,
                                                         @PathVariable long gamePlayerId,
                                                         @RequestBody List<String> singShotsLoc) {
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
                for (String shotLoc: singShotsLoc
                     ) {
                    SingleShot newShot = new SingleShot(shotLoc);
                    singleShotList.add(newShot);
                }
                long currentTurn = ownerGp.getSalvoes().size() + 1;
                Salvo newSalvo = new Salvo(currentTurn, ownerGp, singleShotList);
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

    // 1.5 -- POST method that receive an auth object, an ID from the path and a string, create a singleMessage and add it to the list of messages in the game, return HTTP response.
    //Temporal method for the chat, in the future i'll implement websockets.
    @RequestMapping(path = "/games/players/{gamePlayerId}/chat", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postMessage(Authentication authentication,
                                                           @PathVariable long gamePlayerId,
                                                           @RequestBody String messageBody) {
        if (authentication != null){
            if(gamePlayerRepository.findOne(gamePlayerId) != null){
                if(messageBody != null){
                    GamePlayer currentGamePlayer = gamePlayerRepository.findOne(gamePlayerId);
                    String writerName = currentGamePlayer.getPlayer().getUserName();
                    Game currentGame = currentGamePlayer.getGame();
                    SingleMessage newSingleMessage = new SingleMessage(writerName, messageBody);
                    currentGame.getAllChatMessages().add(newSingleMessage);
                    gameRespository.save(currentGame);
                    return new ResponseEntity<>(makeResponseEntityMap("OK", ""), HttpStatus.CREATED);
                }else{
                    return new ResponseEntity<>(makeResponseEntityMap("error", "empty message"), HttpStatus.UNAUTHORIZED);

                }
            }else{
                return new ResponseEntity<>(makeResponseEntityMap("error", "Your're Not the Owner"), HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>(makeResponseEntityMap("error", "Not Logged User"), HttpStatus.UNAUTHORIZED);
        }
    }
    // 1.6 -- POST method that receive 2 strings that are requested and create a new user returning a HTTP response.
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

    // 1.7 -- GET method that receive an auth object and returns a JSON with the logged user, gallGames and ranking.
    @RequestMapping("/games")
    public Map<String, Object> jsonGamesAndUser(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();
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
    // 1.8 -- GET method that receive an auth object and ID from path and return a JSON for the game view for the current player at the current game.
    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> makeGameViewDTO(@PathVariable long gamePlayerId, Authentication authentication){

        Player user = playerRepository.findByUserName(authentication.getName());
        GamePlayer myGamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Game myGame = myGamePlayer.getGame();
        Map<String, Object> dto = new LinkedHashMap<>();

        List<Salvo> AllSalvoOfGame = new ArrayList<>();

        for (GamePlayer gameplayer: myGame.getGamePlayers()
             ) {
//            for (Salvo salvo: gameplayer.getSalvoes()
//                 ) {
                AllSalvoOfGame.addAll(gameplayer.getSalvoes());
//            }
        }
        AllSalvoOfGame.sort(Comparator.comparing(Salvo::getTurnNumber));
        if(myGamePlayer.getPlayer().equals(user)){
            dto.put("id", myGamePlayer.getId());
            dto.put("create", myGame.getDate());
            dto.put("GamePlayers", myGame.getGamePlayers()
                    .stream()
                    .map(this::makeGamePlayDTO)
                    .collect(Collectors.toList()));
            dto.put("Ships", myGamePlayer.getShips()
                    .stream()
                    .map(this::makeShipDTO)
                    .collect(Collectors.toList()));
            dto.put("history", AllSalvoOfGame.stream()
                    .map(salvo -> makeTurnDTO(salvo, user))
                    .collect(Collectors.toList()));
            dto.put("chat", myGame.getAllChatMessages()
                    .stream()
                    .map(this::makeSingleMessageDTO)
                    .collect(Collectors.toList()));
            dto.put("gameStatus", makeGameState(myGamePlayer, user));
            return new ResponseEntity<>(makeResponseEntityMap("OK", dto), HttpStatus.CREATED);
        }else{
            dto.put("ERROR", "Your're Not the Owner");
            return new ResponseEntity<>(makeResponseEntityMap("error", dto), HttpStatus.UNAUTHORIZED);
        }
    }
//[ENDS REQUEST MAPPINGS]

// 2 -- [START GENERAL FUNCTIONS]
    private long getResult(Set<Score> scoreSet, int filter){
        return scoreSet
                .stream()
                .filter(score -> score.getScore() == filter)
                .count();
    }
    // 2.1 -- receive a string and an object and make a map for the response entity.
    private Map<String, Object> makeResponseEntityMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    // 2.2 -- method that receive the logged user and a gamePlayer and return the satus of an ongoing game, used to hold the turns status.
    private String makeGameState(GamePlayer currentGamePlayer, Player user){
        GamePlayer ownerGp = null;
        GamePlayer otherGp = null;
        for (GamePlayer gamePlayer:  currentGamePlayer.getGame().getGamePlayers()
                ) {
            if(gamePlayer.getPlayer().getUserName().equals(user.getUserName())){
                ownerGp = gamePlayer;
            }else{
                otherGp = gamePlayer;
            }
        }
        if(ownerGp.getShips().size() < 5){
            return "w84UrShips";
        }
        if(ownerGp.getGame().getGamePlayers().size() < 2){
            return "w84Opp";

        }
        if(otherGp.getShips().size() < 5){
            return "w84OppShips";
        }
        if(ownerGp.getId() < otherGp.getId()){
            if(ownerGp.getSalvoes().size() == otherGp.getSalvoes().size()){
                return "youPlay";

            }else{
                return "OppPlay";
            }
        }else{
            if(ownerGp.getSalvoes().size() != otherGp.getSalvoes().size()){
                return "youPlay";

            }else{
                return "OppPlay";
            }
        }
    }
    // 2.3 -- method that return a map for each player in the repository.
    private List<Map<String, Object>> getLeaderBoard(){
        return playerRepository
                .findAll()
                .stream()
                .map(this::makePlayerScoreDTO)
                .collect(Collectors.toList());
    }
    // 2.4 -- method that return a map for each player in the repository.
    private Map<String, Object> makeSingleMessageDTO(SingleMessage currentMessage){
        Map<String, Object> dtoChat = new LinkedHashMap<>();

        dtoChat.put("writer", currentMessage.getWriterId());
        dtoChat.put("message", currentMessage.getMessageBody());

        return dtoChat;
    }
    // 2.5 -- method that return a map for all the games.
    private List<Map<String, Object>> getAllGames() {
        return gameRespository
                .findAll()
                .stream()
                .map(this::makeGameDTO)
                .collect(Collectors.toList());
    }
    // 2.6 -- method that receive a player and return a map of that playerScores.
    private Map<String, Object> makePlayerScoreDTO(Player currentPlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("pId", currentPlayer.getId());
        dto.put("name", currentPlayer.getUserName());
        dto.put("WinGames", getResult(currentPlayer.getScores(), 2));
        dto.put("LostGames", getResult(currentPlayer.getScores(), 0));
        dto.put("DrawGames", getResult(currentPlayer.getScores(), 1));

        return dto;
    }
    // 2.7 -- method that receive a game and return a map of that game.
    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gameId", game.getId());
        dto.put("create", game.getDate());
        dto.put("GamePlayers", game.getGamePlayers()
                .stream()
                .map(this::makeGamePlayDTO)
                .collect(Collectors.toList()));
        dto.put("Scores", game.getScores()
                .stream()
                .map(this::makeScoreDTO)
                .collect(Collectors.toList()));
        return dto;
    }
    // 2.8 -- method that receive a gamePlayer and return a map of that gamePlayer.
    private Map<String, Object> makeGamePlayDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gpId", gamePlayer.getId());
        dto.put("Player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }
    // 2.9 -- method that receive a player and return a map of that player.
    private Map<String, Object> makePlayerDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("pId", player.getId());
        dto.put("name", player.getUserName());
        return dto;
    }
    // 2.10 -- method that receive a score and return a map of that score.
    private Map<String, Object> makeScoreDTO(Score score){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("pId", score.getPlayer().getId());
        dto.put("name", score.getPlayer().getUserName());
        dto.put("Points", score.getScore());
        return dto;
    }
    // 2.11 -- method that receive a ship and return a map of that ship.
    private Map<String, Object> makeShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", ship.getType());
        dto.put("location", ship.getShipPositions());
        dto.put("shipHits",ship.getHitTimes());
        dto.put("shipStatus",ship.getShipStatus());
        return dto;
    }
    // 2.12 -- method that receive a salvo and return a map of that salvo.
    private List<SingleShot> listOfSingShotsFromSalvo(Salvo salvo){
        List<SingleShot> listOfSingShots = new ArrayList<>();
        listOfSingShots.addAll(salvo.getSalvoLocations());
        return listOfSingShots;
    }
    // 2.13 -- method that receive a salvo and a gamePlayer and update the status of that gamePlayer.
    private void updateStatus(Salvo currentSalvo, GamePlayer gamePlayer){
        gamePlayer.getShips().forEach(ship -> ship.getShipPositions().forEach(string -> currentSalvo.getSalvoLocations().forEach(singleShot -> {
            if(singleShot.getLocation().equals(string)){
                int hitTimes = ship.getHitTimes();
                hitTimes ++;
                ship.setHitTimes(hitTimes);
                singleShot.setStatus(true);
            }
        })));
    }
    // 2.14 -- method that receive a salvo and the logged user and return a map of the player turns.
    private Map<String, Object> makeTurnDTO(Salvo currentSalvo, Player userLogged){
        Map<String, Object> dtoTurn = new LinkedHashMap<>();
        GamePlayer ownerGp = null;
        GamePlayer enemyGp = null;
        List<SingleShot> myShots;
        List<SingleShot> enemyShots;
        for (GamePlayer gamePlayer: currentSalvo.getGamePlayer().getGame().getGamePlayers()
                ) {
            if(gamePlayer.getPlayer().getUserName().equals(userLogged.getUserName())){
                ownerGp = gamePlayer;
            }else{
                enemyGp = gamePlayer;
            }
        }
        dtoTurn.put("turnNumber", currentSalvo.getTurnNumber());
        if(currentSalvo.getGamePlayer().getPlayer().equals(userLogged)){
            updateStatus(currentSalvo, enemyGp);
            dtoTurn.put("playerTurn", "me");
            myShots = listOfSingShotsFromSalvo(currentSalvo);
            dtoTurn.put("shots", myShots
                    .stream()
                    .map(this::makeSingleShotDTO)
                    .collect(Collectors.toList()));
        }else{
            updateStatus(currentSalvo, ownerGp);
            dtoTurn.put("playerTurn", "enemy");
            enemyShots = listOfSingShotsFromSalvo(currentSalvo);
            dtoTurn.put("shots", enemyShots
                    .stream()
                    .map(this::makeSingleShotDTO)
                    .collect(Collectors.toList()));
        }
        dtoTurn.put("myShipStatus", ownerGp.getShips()
                .stream()
                .map(this::verifyShipStatus)
                .collect(Collectors.toList()));
        dtoTurn.put("enemyShipStatus", enemyGp.getShips()
                .stream()
                .map(this::verifyShipStatus)
                .collect(Collectors.toList()));

        return dtoTurn;
    }
    // 2.15 -- method that receive a ship and return a map veryfing the status of that ship.
    private Map<String, Object> verifyShipStatus(Ship ship){
        Map<String, Object> dtoShip = new LinkedHashMap<>();
        dtoShip.put("ShipType", ship.getType());
        dtoShip.put("ShipHit", ship.getHitTimes());
        dtoShip.put("ShipStatus", ship.getShipStatus());
        return dtoShip;
    }
    // 2.16 -- method that receive a single shot and return a map of that shot.
    private Map<String, Object> makeSingleShotDTO(SingleShot singleShot){
        Map<String, Object> dtoSingleShot = new LinkedHashMap<>();
        dtoSingleShot.put("location", singleShot.getLocation());
        dtoSingleShot.put("status",singleShot.getStatus());
        return dtoSingleShot;
    }
//[ENDS GENERAL FUNCTIONS]
}