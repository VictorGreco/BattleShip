package com.codePenguin.codePenguin;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long turnNumber;
    private int shotsPerTurn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="salvo_Locations")
    private List<SingleShot> salvoLocations = new ArrayList<>();

    public Salvo() {
        this.shotsPerTurn = 1;
    }

    public Salvo(long turnNumber, GamePlayer gamePlayer, List<SingleShot> salvoLocations) {
        this.turnNumber = turnNumber;
        this.gamePlayer = gamePlayer;
        this.salvoLocations = salvoLocations;
        gamePlayer.addSalvo(this);
        this.shotsPerTurn = 1;
    }

    //getter && setters
    public long getId() {
        return id;
    }

    public long getTurnNumber() {
        return turnNumber;
    }

    public List<SingleShot> getSalvoLocations() {
        return salvoLocations;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getShotsPerTurn() {
        return shotsPerTurn;
    }

    public void setShotsPerTurn(int shotsPerTurn) {
        this.shotsPerTurn = shotsPerTurn;
    }
}
