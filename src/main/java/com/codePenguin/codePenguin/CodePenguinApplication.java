package com.codePenguin.codePenguin;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CodePenguinApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodePenguinApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      SalvoRepository salvoRepository)
    {
        return (args) -> {
            // PLAYERS
            Player player1 = new Player("victorgreco263@gmail.com");
            Player player2 = new Player("victor@gmail.com");
            Player player3 = new Player("aslamasda@gmail.com");

            //GAMES
            Game Game1 = new Game(LocalDateTime.now());
            Game Game2 = new Game(LocalDateTime.now());

            //GAMEPLAYERS
            GamePlayer gamePlayer1 = new GamePlayer(Game1, player1, LocalDateTime.now());
            GamePlayer gamePlayer2 = new GamePlayer(Game1, player2, LocalDateTime.now());
            GamePlayer gamePlayer3 = new GamePlayer(Game2, player2, LocalDateTime.now());
            GamePlayer gamePlayer4 = new GamePlayer(Game2, player3, LocalDateTime.now());

            //SHIPS POSITIONS
            List<String> patrolBoat_Loc = new ArrayList<>(Arrays.asList("A1", "A2"));
            List<String> destroyer_Loc = new ArrayList<>(Arrays.asList("B1", "B2", "B3"));
            List<String> submarine_Loc = new ArrayList<>(Arrays.asList("C1", "C2", "C3"));
            List<String> battleship_Loc = new ArrayList<>(Arrays.asList("D1", "D2", "D3","D4"));
            List<String> carrier_Loc = new ArrayList<>(Arrays.asList("A7", "B7", "C7","D7","E7"));

            //SHIPS GAMEPLAYER 1
            Ship patrolBoat = new Ship("PatrolBoat", patrolBoat_Loc, gamePlayer1);
            Ship destroyer = new Ship("Destroyer", destroyer_Loc, gamePlayer1);
            Ship submarine = new Ship("Submarine", submarine_Loc, gamePlayer1);
            Ship battleship = new Ship("Battleship", battleship_Loc, gamePlayer1);
            Ship carrier = new Ship("Carrier", carrier_Loc, gamePlayer1);
            //SHIPS GAMEPLAYER 2
            Ship patrolBoat2 = new Ship("PatrolBoat2", patrolBoat_Loc, gamePlayer2);
            Ship carrier2 = new Ship("Carrier2", carrier_Loc, gamePlayer2);

            //SALVOES POSITIONS
            List<String> turn1Player1SalvoPos = new ArrayList<>(Arrays.asList("J1", "C7", "J3"));
            List<String> turn1Player2SalvoPos = new ArrayList<>(Arrays.asList("I1", "C7", "I3"));

            List<String> turn2Player1SalvoPos = new ArrayList<>(Arrays.asList("J4", "J5", "J6"));
            List<String> turn2Player2SalvoPos = new ArrayList<>(Arrays.asList("I4", "I5", "I6"));

            List<String> turn3Player1SalvoPos = new ArrayList<>(Arrays.asList("J8", "J9", "J10"));
            List<String> turn3Player2SalvoPos = new ArrayList<>(Arrays.asList("I8", "I9", "I10"));

            //SALVOES
            Salvo turn1Player1 = new Salvo(1, gamePlayer1, turn1Player1SalvoPos);
            Salvo turn1Player2 = new Salvo(1, gamePlayer2, turn1Player2SalvoPos);

            Salvo turn2Player1 = new Salvo(2, gamePlayer1, turn2Player1SalvoPos);
            Salvo turn2Player2 = new Salvo(2, gamePlayer2, turn2Player2SalvoPos);

            Salvo turn3Player1 = new Salvo(3, gamePlayer1, turn3Player1SalvoPos);
            Salvo turn3Player2 = new Salvo(3, gamePlayer2, turn3Player2SalvoPos);


            // PLAYER REPOSITORY
            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);

            // GAMES REPOSITORY
            gameRepository.save(Game1);
            gameRepository.save(Game2);

            // GAMEPLAYERS REPOSITORY
            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);

            // SHIPS REPOSITORY
            shipRepository.save(patrolBoat);
            shipRepository.save(destroyer);
            shipRepository.save(submarine);
            shipRepository.save(battleship);
            shipRepository.save(carrier);

            shipRepository.save(patrolBoat2);
            shipRepository.save(carrier2);

            // SALVOES REPOSITORY
            salvoRepository.save(turn1Player1);
            salvoRepository.save(turn1Player2);

            salvoRepository.save(turn2Player1);
            salvoRepository.save(turn2Player2);

            salvoRepository.save(turn3Player1);
            salvoRepository.save(turn3Player2);
        };
    }
}
