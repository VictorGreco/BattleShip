package com.codePenguin.codePenguin;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="ship_Positions")
    private List<String> shipPositions = new ArrayList<>();

    public Ship (){};

    public Ship(String type, List<String> shipPositions, GamePlayer gamePlayer) {
        this.type = type;
        this.shipPositions = shipPositions;
        this.gamePlayer = gamePlayer;
    }
    // getter && setter
    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getShipPositions() {
        return shipPositions;
    }

    // voids!!
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
