package com.example.miguelf03kai.musicplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {

    Button play,next,previous;
    TextView time,currentTime,artist,music;
    SeekBar duration;
    ImageView albumart;

    static MediaPlayer musicPlayer;

    public static ArrayList<Songs> arrayList;

    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_activity);

        //hide the actionbar
        //getSupportActionBar().hide();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Songs song = (Songs)getIntent().getSerializableExtra("song");

        play = (Button)findViewById(R.id.button);
        time = (TextView)findViewById(R.id.textView2);
        currentTime = (TextView)findViewById(R.id.textView);
        artist = (TextView)findViewById(R.id.textView4);
        music = (TextView)findViewById(R.id.textView3);
        duration = (SeekBar)findViewById(R.id.seekBar);
        albumart = (ImageView)findViewById(R.id.imageView);
        next = (Button)findViewById(R.id.button2);
        previous = (Button)findViewById(R.id.button3);

        //does work
        /*if(musicPlayer.isPlaying()){
            Toast.makeText(getApplicationContext(),"Cached", Toast.LENGTH_LONG).show();
        }*/

        setView(song);

        currentIndex = song.getIndex();

        //Bitmap bm = BitmapFactory.decodeFile(song.getAlbumArt());

        //albumart.setImageBitmap(bm);
        //Glide.with(MusicPlayerActivity.this).load(song.getAlbumArt()).placeholder(R.drawable.album).into(albumart);
        //Glide.with(MusicPlayerActivity.this).load(song.getAlbumArt()).into(albumart);

        //Toast.makeText(getApplicationContext(),"current "+currentIndex+" total "+ListActivity.listViewSize, Toast.LENGTH_LONG).show();
        //musicPlayer = MediaPlayer.create(this,R.raw.heaven_help_us);
        musicPlayer = new MediaPlayer();

        playMusicPrepare(song);

        duration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

         new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer != null){
                    if(musicPlayer.isPlaying()){
                        try{
                            final double current = musicPlayer.getCurrentPosition();
                            final String elapsedTime = millisceondsToString((int) current);

                            /**
                             * Runs the specified action on the UI thread. If the current thread is the UI thread, then the action is
                             * executed immediately. If the current thread is not the UI thread, the action is posted to the event queue
                             * of the UI thread.
                             * */
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    currentTime.setText(elapsedTime);
                                    duration.setProgress((int) current);
                                }
                            });

                            Thread.sleep(1000);
                        }catch(InterruptedException e){}
                    }
                }
            }
         }).start();

        musicPlayer.start();
        play.setBackgroundResource(R.drawable.pause);

        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(currentIndex >=0  && currentIndex < (arrayList.size()-1)) {
                    musicPlayer.reset();
                    Songs song = arrayList.get(currentIndex += 1);
                    setView(song);
                    playMusicPrepare(song);
                    musicPlayer.start();
                }
                else{
                    currentTime.setText("0:00");
                    duration.setProgress(0);
                    play.setBackgroundResource(R.drawable.play);
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicPlayer.isPlaying()) {
                    musicPlayer.pause();
                    play.setBackgroundResource(R.drawable.play);
                } else {
                    musicPlayer.start();
                    play.setBackgroundResource(R.drawable.pause);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentIndex >=0  && currentIndex < (arrayList.size()-1)) {

                    Songs song = arrayList.get(currentIndex += 1);

                    setView(song);

                    try {
                        musicPlayer.reset();
                        playMusicPrepare(song);

                        musicPlayer.start();

                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"Error "+e, Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Last Song", Toast.LENGTH_LONG).show();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentIndex > 0 && currentIndex <= (arrayList.size()-1)){

                    Songs song = arrayList.get(currentIndex -= 1);

                    setView(song);

                    try {
                        musicPlayer.reset();
                        playMusicPrepare(song);

                        musicPlayer.start();
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"Error "+e, Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"First Song", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setView(Songs song){
        music.setText(song.getSong().length() > 30 ? song.getSong().substring(0, 30) + "..." : song.getSong());
        artist.setText(song.getArtist().length() > 30 ? song.getArtist().substring(0, 30) + "..." : song.getArtist());

        if(GetImage(song.getPath()) == null){
            albumart.setImageResource(R.drawable.album);
        }
        else{
            albumart.setImageBitmap(GetImage(song.getPath()));
        }
    }

    public void playMusicPrepare(Songs song){

        try {
            musicPlayer.setDataSource(song.getPath());
            musicPlayer.prepare();

            duration.setMax(musicPlayer.getDuration());
            duration.setProgress(0);

            time.setText(millisceondsToString(musicPlayer.getDuration()));

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Invalid path" , Toast.LENGTH_LONG).show();
        }

        musicPlayer.setLooping(false); //looping
        musicPlayer.seekTo(0);
    }

    public Bitmap GetImage(String filepath)              //filepath is path of music file
    {
        Bitmap image;

        MediaMetadataRetriever mData=new MediaMetadataRetriever();
        mData.setDataSource(filepath);
        try{
            byte art[]=mData.getEmbeddedPicture();
            /*
            * decodeByteArray(byte[] data, int offset, int length)
            * Decode an immutable bitmap from the specified byte array.
            * */
            image=BitmapFactory.decodeByteArray(art, 0, art.length);
        }
        catch(Exception e)
        {
            image=null;
        }

        return image;
    }

    public String millisceondsToString(int time){
        String elapsedTime = "";

        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;

        elapsedTime = minutes+":";

        if(seconds<10){
            elapsedTime+="0";
        }
        elapsedTime += seconds;

        return elapsedTime;
    }

    //back up button result
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            //finish();//finishes the activity will destroy it
            /*if(musicPlayer.isPlaying()){
                musicPlayer.stop();
            }*/
            startActivity(new Intent(this,ListActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
