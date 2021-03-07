package com.choicely.outsideripoff;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.realm.Realm;
import io.realm.Sort;

public class GameActivity extends AppCompatActivity {

    private Button clickButton, powerUpButton, challengeButton;
    private TextView scoreText;
    private ProgressBar powerUpProgress;
    private CountDownTimer powerUpButtonCountDown, challengeButtonCountDown, challengeCountDown;
    private ProgressBar challengeProgress;
    private long score = 0;
    private long scoreAdder = 0;
    private long challenger = 0;
    private Realm realm = Realm.getDefaultInstance();
    private GameData currentGame;

    private final static String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "GameActivity started with onCreate method");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        clickButton = findViewById(R.id.activity_game_click);
        scoreText = findViewById(R.id.activity_game_score);
        powerUpButton = findViewById(R.id.activity_game_powerup_button);
        powerUpProgress = findViewById(R.id.activity_game_powerup_progress);
        challengeButton = findViewById(R.id.activity_game_challenge_button);
        challengeProgress = findViewById(R.id.activity_game_challenge_progress);

        loadGame();

        clickButton.setOnClickListener(v -> {
            score++;
            scoreText.setText("" + score);
        });

        powerUpTimer();
        challengeTimer();
        scoreText.setText(String.format("%d", score));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.game_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_translation_menu_save) {
            saveGame();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        saveGame();
        super.onBackPressed();
    }

    private void saveGame() {
        realm.beginTransaction();
        currentGame.setGameScore(score);
        currentGame.setScoreAdder(scoreAdder);
        currentGame.setScoreChallenger(challenger);
        Log.d(TAG, "going to save a game with score: " + score);
        realm.insertOrUpdate(currentGame);
        realm.commitTransaction();
    }

    private void loadGame() {
        if (getIntent().getStringExtra("intent_game_id") == null
                || getIntent().getStringExtra("intent_game_id").isEmpty()) {
            currentGame = new GameData();
            currentGame.setId(UUID.randomUUID().toString());
        } else {
            currentGame = realm.where(GameData.class).equalTo("id", getIntent().getStringExtra("intent_game_id")).findFirst();
            score = currentGame.getGameScore();
            scoreAdder = currentGame.getScoreAdder();
            challenger = currentGame.getScoreChallenger();
            powerUpCounter();
            challengeCounter();
        }
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
                runOnUiThread(() -> {
                    score += scoreAdder;
                    scoreText.setText(String.format("%d", score));
                });
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
                runOnUiThread(() -> {
                    score -= challenger;
                    scoreText.setText(String.format("%d", score));
                });
            }
        };

        Timer taskTimer = new Timer();
        taskTimer.scheduleAtFixedRate(powerUpTask, 20000, 20000);
    }
}
