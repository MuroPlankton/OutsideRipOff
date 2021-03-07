package com.choicely.outsideripoff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private LoadListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "GameActivity started with onCreate method");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.activity_load_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LoadListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_translation_menu_save) {
            startActivity(new Intent(this, GameActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateContent() {
        adapter.clear();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<GameData> games = realm.where(GameData.class).findAll();
        for (GameData game : games) {
            Log.d(TAG, "added game with id: " + game.getId() + " and score: " + game.getGameScore());
            adapter.add(game);
        }

        adapter.notifyDataSetChanged();
    }
}
