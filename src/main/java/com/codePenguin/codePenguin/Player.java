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
    private
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private
    Set<Score> scores = new HashSet<>();

    public Player() {
    }

    Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // voiders!
    void addGamePlay(GamePlayer gamePlayer) {
        //gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    void addScore(Score score) {
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

    Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
