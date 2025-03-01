package com.choicely.outsideripoff;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GameData extends RealmObject {

    @PrimaryKey
    private String id;
    private long gameScore;
    private long scoreAdder;
    private long scoreChallenger;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getGameScore() {
        return gameScore;
    }

    public void setGameScore(long gameScore) {
        this.gameScore = gameScore;
    }

    public long getScoreAdder() {
        return scoreAdder;
    }

    public void setScoreAdder(long scoreAdder) {
        this.scoreAdder = scoreAdder;
    }

    public long getScoreChallenger() {
        return scoreChallenger;
    }

    public void setScoreChallenger(long scoreChallenger) {
        this.scoreChallenger = scoreChallenger;
    }
}
