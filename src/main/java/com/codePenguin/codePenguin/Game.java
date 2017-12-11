package com.codePenguin.codePenguin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

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

    //other methods

//    @JsonIgnore
//    public List<Player> getPlayers() {
//        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
//    }

}
