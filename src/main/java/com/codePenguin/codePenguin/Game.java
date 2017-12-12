package com.codePenguin.codePenguin;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;


    public Game(){}

    public Game (LocalDateTime currentDate){
        this.creationDate = currentDate;
    }

    // voids!!
    public void addGamePlay(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    //      getter && setter methods

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return creationDate;
    }
}
