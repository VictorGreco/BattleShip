package com.codePenguin.codePenguin;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String userName;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    public Player() {
    }

    public Player(String userName) {
        this.userName = userName;
    }

    // voiders!
    public void addGamePlay(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }



    //      getter && setter methods
    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    //other
//    public List<Game> getGames() {
//        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
//    }

}
