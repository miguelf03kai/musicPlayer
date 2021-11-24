package com.example.miguelf03kai.musicplayer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class SongAdapter extends ArrayAdapter<Songs>{

    public SongAdapter(@Nullable Context context,@Nullable List<Songs> objects) {
        super(context, 0, objects);
    }

    @Nullable
    @Override
    public View getView(int position,@Nullable View convertView, @Nullable ViewGroup parent){
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,null);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvArtist = convertView.findViewById(R.id.tvArtist);

        Songs song = getItem(position);
        song.setIndex(position);
        tvTitle.setText(song.getSong());
        tvArtist.setText(song.getArtist());

        return convertView;
    }
}