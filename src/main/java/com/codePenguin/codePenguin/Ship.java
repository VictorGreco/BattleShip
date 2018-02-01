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
    private boolean shipStatus;
    private int hitTimes;

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
        gamePlayer.addShip(this);
        this.hitTimes = 0;
        this.shipStatus = false;

    }

    public Ship(String type, List<String> shipPositions) {
        this.type = type;
        this.shipPositions = shipPositions;
        this.hitTimes = 0;
        this.shipStatus = false;
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

    public boolean getShipStatus() {
        return shipStatus;
    }

    public int getHitTimes() {
        return hitTimes;
    }

    public void setHitTimes(int hitTimes) {
        this.hitTimes = hitTimes;
        if(hitTimes == shipPositions.size()){
            this.shipStatus = true;
        }
//        if(type == "Battleship" && hitTimes == shipPositions.size()){
//            this.shipStatus = true;
//        }
//        if(type == "Submarine"  && hitTimes == shipPositions.size()){
//            this.shipStatus = true;
//        }
//        if(type == "Destroyer" && hitTimes == shipPositions.size()){
//            this.shipStatus = true;
//        }
//        if(type == "Patrol Boat" && hitTimes == shipPositions.size()){
//            this.shipStatus = true;
//        }
    }
    // voids!!
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
