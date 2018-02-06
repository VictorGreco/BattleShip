package com.codePenguin.codePenguin;

import javax.persistence.*;
import java.util.*;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date creationDate;

    @ElementCollection
    @Column(name="chat_Messages")
    private List<SingleMessage> allChatMessages = new ArrayList<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();


    public Game(){this.creationDate = new Date();}

    public Game (Date currentDate){
        this.creationDate = currentDate;
    }

    // voids!!
    public void addGamePlay(GamePlayer gamePlayer) {
//        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public void addScore(Score score) {
//        score.setGame(this);
        scores.add(score);
    }

    //      getter && setter methods
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

    public long getId() {
        return id;
    }

    public Date getDate() {
        return creationDate;
    }

    public List<SingleMessage> getAllChatMessages() {
        return allChatMessages;
    }

    public void setAllChatMessages(List<SingleMessage> allChatMessages) {
        this.allChatMessages = allChatMessages;
    }

}
