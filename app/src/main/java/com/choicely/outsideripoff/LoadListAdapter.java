package com.choicely.outsideripoff;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LoadListAdapter extends RecyclerView.Adapter<LoadListAdapter.LoadViewHolder> {

    private final static String TAG = "LoadListAdapter";

    private final Context context;
    private final List<GameData> gameList = new ArrayList<>();

    public LoadListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LoadViewHolder(LayoutInflater.from(context).inflate(R.layout.game_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoadViewHolder holder, int position) {
        GameData game = gameList.get(position);

        holder.gameID = game.getId();
        holder.gameIDTextView.setText(game.getId());
        holder.gameScoreTextView.setText(String.format("score: %d", game.getGameScore()));
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public void clear() {
        gameList.clear();
    }

    public void add(GameData gameData) {
        gameList.add(gameData);
        Log.d(TAG, "received game with id:" + gameData.getId());
    }

    public class LoadViewHolder extends RecyclerView.ViewHolder {

        String gameID;
        TextView gameIDTextView, gameScoreTextView;
        public LoadViewHolder(@NonNull View v) {
            super(v);
            v.setOnClickListener(onGameItemClick);
            gameIDTextView = v.findViewById(R.id.activity_load_list_id);
            gameScoreTextView = v.findViewById(R.id.activity_load_list_score);
        }

        private View.OnClickListener onGameItemClick = itemView -> {
            Context ctx = itemView.getContext();
            Intent intent = new Intent(ctx, GameActivity.class);
            intent.putExtra("intent_game_id", gameID);
            ctx.startActivity(intent);
        };
    }
}
