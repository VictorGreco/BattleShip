package com.codePenguin.codePenguin;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships;

    private LocalDateTime joinDate;

    public GamePlayer(){}

    public GamePlayer(Game game, Player player ,LocalDateTime joinDate) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
    }

    //voiders!!
    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    //      getter && setter methods
    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public long getId() {
        return id;
    }


}
