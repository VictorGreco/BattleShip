package com.codePenguin.codePenguin;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int turnNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="salvo_Locations")
    private List<String> salvoLocations = new ArrayList<>();

    public Salvo() {}

    public Salvo(int turnNumber, GamePlayer gamePlayer, List<String> salvoLocations) {
        this.turnNumber = turnNumber;
        this.gamePlayer = gamePlayer;
        this.salvoLocations = salvoLocations;
    }

    //getter && setters
    public long getId() {
        return id;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

}
