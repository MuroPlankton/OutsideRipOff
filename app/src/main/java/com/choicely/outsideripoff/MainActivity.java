package com.choicely.outsideripoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startNew;
    private Button loadGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startNew = findViewById(R.id.activity_main_new_game);
        loadGame = findViewById(R.id.activity_main_load_game);

        startNew.setOnClickListener(v -> {
            startActivity(new Intent(this, GameActivity.class));
        });

        loadGame.setOnClickListener(v -> {
            startActivity(new Intent(this, LoadActivity.class));
        });
    }
}