package com.example.miguelf03kai.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<Songs> songsArrayList;
    ListView listView;
    SongAdapter songAdapter;

    public static int listViewSize = 0;

    private static final int PERMISSION_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.list_activity);

        listView = (ListView)findViewById(R.id.listView);

        songsArrayList = new ArrayList<>();

        /*for(int i = 0; i<10;i++)
            songsArrayList.add(new Songs("Artist "+i,"Path "+i,"Title "+i));*/


        songAdapter = new SongAdapter(this,songsArrayList);
        listView.setAdapter(songAdapter);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(ListActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_CODE);
            return;
        }else{
            getSongs();
        }

        MusicPlayerActivity.arrayList = songsArrayList;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                try{
                if(MusicPlayerActivity.musicPlayer != null){
                    if(MusicPlayerActivity.musicPlayer.isPlaying())
                        MusicPlayerActivity.musicPlayer.stop();
                }}
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
                }
                Songs song = songsArrayList.get(i);
                Intent openMusic = new Intent(ListActivity.this,MusicPlayerActivity.class);
                openMusic.putExtra("song",song);
                startActivity(openMusic);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getSongs();
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void getSongs(){
        //read songs from phone
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);

        String ImgFilePath = "";

        if(songCursor !=  null && songCursor.moveToFirst()){
            int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int indexPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            int indexAlbum = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
            int _thumbID = songCursor.getInt(indexAlbum);

            ImgFilePath = "content://media/external/audio/albumart/"+_thumbID;

            do{

                String title = songCursor.getString(indexTitle);
                String artist = songCursor.getString(indexArtist);
                String path = songCursor.getString(indexPath);
                //String album = songCursor.getString(indexAlbum);

                songsArrayList.add(new Songs(artist,path,title,ImgFilePath));

            }while(songCursor.moveToNext());
        }

        listViewSize = songsArrayList.size();

        songAdapter.notifyDataSetChanged();
    }

}
