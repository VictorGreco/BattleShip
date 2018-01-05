package com.codePenguin.codePenguin;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    private int score;

    private Date finishDate;

    public Score() {
    }

    public Score( int score, Game game, Player player) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.finishDate = new Date();
        player.addScore(this);
        game.addScore(this);
    }

    //getters && setters
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

    public long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public Date getFinishDate() {
        return finishDate;
    }
}
