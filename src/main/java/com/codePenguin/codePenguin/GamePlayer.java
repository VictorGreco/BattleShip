package com.codePenguin.codePenguin;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
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
    private
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private
    Set<Salvo> salvoes = new HashSet<>();

    private Date joinDate;

    public GamePlayer(){}

    GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.joinDate = new Date();
        player.addGamePlay(this);
        game.addGamePlay(this);
    }

    //voiders!!
    void addShip(Ship ship) {
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    //      getter && setter methods
    public void setGame(Game game) {
        this.game = game;
    }

    Game getGame() {
        return game;
    }

    Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    Set<Ship> getShips() {
        return ships;
    }

    Set<Salvo> getSalvoes() { return salvoes;}

    public long getId() {
        return id;
    }

    public Date getJoinDate() {
        return joinDate;
    }
}
