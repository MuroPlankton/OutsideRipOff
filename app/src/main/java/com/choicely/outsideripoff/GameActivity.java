package com.choicely.outsideripoff;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.choicely.outsideripoff.db.RealmHelper;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class GameActivity extends AppCompatActivity {

    private Button saveButton, clickButton, powerUpButton, challengeButton;
    private TextView scoreText;
    private ProgressBar powerUpProgress;
    private CountDownTimer powerUpButtonCountDown, challengeButtonCountDown, challengeCountDown;
    private ProgressBar challengeProgress;
    private int score = 0, scoreAdder = 0, challenger = 0;
    private long id = 0;
    Realm realm = Realm.getDefaultInstance();

    private final static String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "GameActivity started with onCreate method");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        saveButton = findViewById(R.id.activity_game_save_game);
        clickButton = findViewById(R.id.activity_game_click);
        scoreText = findViewById(R.id.activity_game_score);
        powerUpButton = findViewById(R.id.activity_game_powerup_button);
        powerUpProgress = findViewById(R.id.activity_game_powerup_progress);
        challengeButton = findViewById(R.id.activity_game_challenge_button);
        challengeProgress = findViewById(R.id.activity_game_challenge_progress);

        id = getIntent().getLongExtra("intent_game_id", -1);
        if (id == -1) {
            GameData lastGame = realm.where(GameData.class).sort("id", Sort.DESCENDING).findFirst();
            if (lastGame != null) {
                id = lastGame.getId() + 1;
            }
            Log.d(TAG, "created game with id: " + id);
        } else {
            loadGame();
            Log.d(TAG, "loaded game with id: " + id);
        }
        Log.d(TAG, "game opened with id: " + id);

        clickButton.setOnClickListener(v -> {
            score++;
            scoreText.setText("" + score);
        });

        saveButton.setOnClickListener(v -> {
            realm.beginTransaction();
            GameData game = new GameData();
            game.setId(id);
            game.setGameScore(score);
            game.setScoreAdder(scoreAdder);
            game.setScoreChallenger(challenger);
            Log.d(TAG, "going to save a game with score: " + score);
            realm.insertOrUpdate(game);
            realm.commitTransaction();
        });

        powerUpTimer();
        challengeTimer();
    }

    private void loadGame() {
        GameData game = realm.where(GameData.class).equalTo("id", id).findFirst();
        score = (int) game.getGameScore();
        scoreAdder = (int) game.getScoreAdder();
        challenger = (int) game.getScoreChallenger();
        scoreText.setText("" + score);
        powerUpCounter();
        challengeCounter();
    }

    private void powerUpTimer() {
        Log.d(TAG, "poweruptimer started");
        powerUpProgress.setProgress(0);
        powerUpButton.setEnabled(false);

        powerUpButtonCountDown = new CountDownTimer(30000, 30) {
            @Override
            public void onTick(long millisUntilFinished) {
                powerUpProgress.setProgress((int) millisUntilFinished / 30);
            }

            @Override
            public void onFinish() {
                powerUpButton.setEnabled(true);
            }
        };
        powerUpButtonCountDown.start();

        powerUpButton.setOnClickListener(v -> {
            if (scoreAdder < 1) {
                powerUpCounter();
            }
            scoreAdder += 1;
            powerUpTimer();
        });
    }

    private void powerUpCounter() {
        Log.d(TAG, "powerupcounter started");

        TimerTask powerUpTask = new TimerTask() {
            @Override
            public void run() {
                score += scoreAdder;
            }
        };

        Timer taskTimer = new Timer();
        taskTimer.scheduleAtFixedRate(powerUpTask, 10000, 10000);
    }

    private void challengeTimer() {
        Log.d(TAG, "challengetimer started");
        challengeProgress.setProgress(0);
        challengeButton.setEnabled(false);
        challengeButtonCountDown = new CountDownTimer(60000, 60) {
            @Override
            public void onTick(long millisUntilFinished) {
                challengeProgress.setProgress((int) millisUntilFinished / 60);
            }

            @Override
            public void onFinish() {
                challengeButton.setEnabled(true);
            }
        };
        challengeButtonCountDown.start();
        challengeButton.setOnClickListener(v -> {
            if (challenger < 1)
                challengeCounter();
            challenger += 2;
            challengeTimer();
        });
    }

    private void challengeCounter() {
        Log.d(TAG, "challengecounter started");
        TimerTask powerUpTask = new TimerTask() {
            @Override
            public void run() {
                score -= challenger;
            }
        };

        Timer taskTimer = new Timer();
        taskTimer.scheduleAtFixedRate(powerUpTask, 20000, 20000);
    }
}
