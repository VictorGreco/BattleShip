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
                                      ShipRepository shipRepository)
    {
        return (args) -> {
            Player player1 = new Player("victorgreco263@gmail.com");
            Player player2 = new Player("victor@gmail.com");

            Game Game1 = new Game(LocalDateTime.now());
            Game Game2 = new Game(LocalDateTime.now());

            GamePlayer gamePlayer1 = new GamePlayer(Game1, player1, LocalDateTime.now());
            GamePlayer gamePlayer2 = new GamePlayer(Game1, player2, LocalDateTime.now());
            GamePlayer gamePlayer3 = new GamePlayer(Game2, player2, LocalDateTime.now());

            List<String> patrolBoat_Loc = new ArrayList<>(Arrays.asList("A1", "A2"));
            List<String> destroyer_Loc = new ArrayList<>(Arrays.asList("B1", "B2", "B3"));
            List<String> submarine_Loc = new ArrayList<>(Arrays.asList("C1", "C2", "C3"));
            List<String> battleship_Loc = new ArrayList<>(Arrays.asList("D1", "D2", "D3","D4"));
            List<String> carrier_Loc = new ArrayList<>(Arrays.asList("A7", "B7", "C7","D7","E7"));

            Ship patrolBoat = new Ship("PatrolBoat", patrolBoat_Loc, gamePlayer1);
            Ship destroyer = new Ship("Destroyer", destroyer_Loc, gamePlayer1);
            Ship submarine = new Ship("Submarine", submarine_Loc, gamePlayer1);
            Ship battleship = new Ship("Battleship", battleship_Loc, gamePlayer1);
            Ship carrier = new Ship("Carrier", carrier_Loc, gamePlayer1);

            Ship patrolBoat2 = new Ship("PatrolBoat2", patrolBoat_Loc, gamePlayer2);
            Ship carrier2 = new Ship("Carrier2", carrier_Loc, gamePlayer2);



            // save a couple of players
            playerRepository.save(player1);
            playerRepository.save(player2);

            gameRepository.save(Game1);
            gameRepository.save(Game2);

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);

            shipRepository.save(patrolBoat);
            shipRepository.save(destroyer);
            shipRepository.save(submarine);
            shipRepository.save(battleship);
            shipRepository.save(carrier);
            shipRepository.save(patrolBoat2);
        };
    }
}
