package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;
    ListView listView;
    ImageView imagePlayButton ;
    SeekBar seekBar;
    String flag = "play";
    ArrayList<String> arrayList;
    EditText editText;
    int lastIndex = 3;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Integer> musicList;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicList = new ArrayList<>();
        seekBar = findViewById(R.id.seekBar);
        editText = findViewById(R.id.editTextText);
        musicList.add(0,R.raw.sitdown);
        ImageView done = findViewById(R.id.done);
        musicList.add(1,R.raw.flirt);
        musicList.add(2,R.raw.backofcar);
        imagePlayButton = findViewById(R.id.playImage);
        listView = findViewById(R.id.listView);
         arrayList = new ArrayList<>();
       player = createSong(currentIndex);

        arrayList.add(0,"SitDown");
        arrayList.add(1,"Flirt");
        arrayList.add(2,"BackOfTheCar");

         arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentIndex = i;
                player.release();
                player = createSong(currentIndex);
                setSeekBar(player);
                Toast.makeText(getApplicationContext(),getMusicName(),Toast.LENGTH_LONG).show();
                playButton(view);
            }
        });

       new Timer().scheduleAtFixedRate(new TimerTask() {
           @Override
           public void run() {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       seekBar.setProgress(player.getCurrentPosition());
                   }
               });
           }
       },0,1000);

       done.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

                   String text = editText.getText().toString();
                   arrayList.add(lastIndex,text);
                   arrayAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1,arrayList);
                   listView.setAdapter(arrayAdapter);
                   lastIndex++;

           }
       });

    }




    public void nextButton(View view){
        player.release();
        imagePlayButton.setImageResource(R.drawable.playbutton);

        if(currentIndex < musicList.size()-1){
            currentIndex++;

        }else{
            currentIndex = 0;
        }
        player = createSong(currentIndex);
        Toast.makeText(getApplicationContext(),getMusicName(),Toast.LENGTH_LONG).show();
        setSeekBar(player);
        playButton(view);
    }

    public MediaPlayer createSong(int curr){
        player = MediaPlayer.create(getApplicationContext(),musicList.get(curr));
        return player;
    }

    public String getMusicName(){
        switch (currentIndex){
            case 0:
                return "Sitdown";
            case 1:
                return "Flirt";
            case 2:
                return "BackOfTheCar";
        }
        return "";
    }



    public void setSeekBar(MediaPlayer p){
        seekBar.setMax(p.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean userFrom) {
                if(userFrom){
                    p.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    public void prevButton(View view){

        if(currentIndex != 0){
            currentIndex--;
        }else{
           currentIndex = musicList.size()-1;
        }
        player.release();
        player = MediaPlayer.create(getApplicationContext(),musicList.get(currentIndex));
        Toast.makeText(getApplicationContext(),getMusicName(),Toast.LENGTH_LONG).show();
        setSeekBar(player);
        playButton(view);
    }



    public void playButton(View view){
        if(!player.isPlaying()){
            player.start();
            setSeekBar(player);

            imagePlayButton.setImageResource(R.drawable.pausebutton);
            flag = "pause";
        }else{
            flag = "play";
            player.pause();
            imagePlayButton.setImageResource(R.drawable.playbutton);
        }

    }
}