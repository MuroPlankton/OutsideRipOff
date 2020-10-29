package com.choicely.outsideripoff;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoadActivity extends AppCompatActivity {
    private static final String TAG = "LoadActivity";

    private RecyclerView recyclerView;
    private LoadListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "GameActivity started with onCreate method");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        recyclerView = findViewById(R.id.activity_load_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LoadListAdapter(this);
        recyclerView.setAdapter(adapter);

        updateContent();
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
