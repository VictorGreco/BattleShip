package com.codePenguin.codePenguin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class CodePenguinApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodePenguinApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                        GameRepository gameRepository,
                                        GamePlayerRepository gamePlayerRepository)
    {
        return (args) -> {
            Player player1 = new Player("victorgreco263@gmail.com");
            Player player2 = new Player("victor@gmail.com");

            Game Game1 = new Game(LocalDateTime.now());
            Game Game2 = new Game(LocalDateTime.now());
            // save a couple of players
            playerRepository.save(player1);
            playerRepository.save(player2);

            gameRepository.save(Game1);
            gameRepository.save(Game2);

            gamePlayerRepository.save(new GamePlayer(Game1, player1, LocalDateTime.now()));
            gamePlayerRepository.save(new GamePlayer(Game1, player2, LocalDateTime.now()));
            gamePlayerRepository.save(new GamePlayer(Game2, player2, LocalDateTime.now()));
        };
    }
}
