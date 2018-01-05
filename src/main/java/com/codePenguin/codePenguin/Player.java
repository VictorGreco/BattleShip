package com.codePenguin.codePenguin;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String userName;
    private String password;


    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    public Player() {
    }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // voiders!
    public void addGamePlay(GamePlayer gamePlayer) {
        //gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public void addScore(Score score) {
//        score.setPlayer(this);
        if(score != null){
            scores.add(score);
        }

    }

    //      getter && setter methods
    public long getId() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
