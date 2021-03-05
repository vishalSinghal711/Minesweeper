package com.example.minesweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.media.MediaBrowserCompatUtils;

import java.util.ArrayList;

/**
 * @helper_Gameplay_Activity : this adapter fills the grid according to current state of the game and manages user
 *           action to update the game and update the grid which is shown by Gameplay Activity
 * @taskID_2_Implementation
 *
 *
 */

public class customGameAdapter extends ArrayAdapter<String> {
//  make fields to store constructor parameter so that it can be accessed by all member Functions
    public Minesweeper game = null;
    int count = 0;
    MediaPlayer mediaPlayer = null;
//      made custom Constructor according to needs
    public customGameAdapter(@NonNull Context context, @NonNull String[] objects , int n ,
                             Minesweeper gamePara , MediaPlayer mp) {
//        put 0 in place of layout as we are setting custom layout into getView
        super(context,  0 , objects);
        count = n;
        game = gamePara;
        mediaPlayer = mp;
    }
//      overrided getView Function of ArrayAdapter<String> to customAdapter coz. ArrayAdapter returns
//      only textView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View gridItemView = convertView;
        if(gridItemView == null){
//            only changed field here is grid_Item_layout(custom) instead of android defined
//            layout Eg - simple_item_1
//            params : customLayout , viewGrp parameter , false
            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_layout, parent , false);
        }
//        take button and image view from frameLayout(grid_item) and set image to clickable so that flag works fine
        Button b = (Button) gridItemView.findViewById(R.id.grid_item);
        ImageView img = (ImageView) gridItemView.findViewById(R.id.hello);
        img.setClickable(true);

//        get co-ordinates for current position
        int x = position/count;
        int y = position%count;

//        print grid on gridView according to board[x][y]
        if(game.getBoard()[x][y].isMarked() && game.getStatus() == Status.ONGOINING){
            b.setVisibility(View.INVISIBLE);
            img.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.flag);
        }
        if (game.getBoard()[x][y].isRevealed() && game.getStatus() == Status.ONGOINING){
            int value = game.getBoard()[x][y].getValue();
            if(value == 0){
                b.setVisibility(View.INVISIBLE);
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.zero);
            }else{
                if (value == 1){
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.one);
                }else if (value == 2){
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.two);
                }else if(value == 3){
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.three);
                }else if(value == 4){
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.four);
                }else if(value == 5){
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.five);
                }else if(value == 6){
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.six);
                }else if(value == 7){
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.seven);
                }else if(value == 3){
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.eight);
                }else {}
            }
        }
        if(game.getStatus() == Status.LOST){
            if (game.getBoard()[x][y].getValue() == Minesweeper.MINE){
                b.setVisibility(View.INVISIBLE);
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.bomb2);
            }else if (game.getBoard()[x][y].isRevealed()) {
                int value = game.getBoard()[x][y].getValue();
                if (value == 0) {
                    b.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.zero);
                } else {
                    if (value == 1) {
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.one);
                    } else if (value == 2) {
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.two);
                    } else if (value == 3) {
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.three);
                    } else if (value == 4) {
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.four);
                    } else if (value == 5) {
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.five);
                    } else if (value == 6) {
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.six);
                    } else if (value == 7) {
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.seven);
                    } else if (value == 3) {
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.eight);
                    } else {
                    }
                }
            }
            else {
                b.setVisibility(View.INVISIBLE);
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.lost);
            }
        }
        if (game.getStatus() == Status.WON){
            b.setVisibility(View.INVISIBLE);
            img.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.win);
        }

//          single clicks represent  -> open
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                move in game with choice - 1(move) with current x,y (x,y -> co-ordinates of current clicked)
                boolean isAns = game.move(1 , position/count , position%count);
//                if move was valid game will return true
//                if true update gridView by call Static Minesweeper function which commands to fill grid view
                if (isAns){
                    if (game.getStatus() == Status.LOST){
                        mediaPlayer = MediaPlayer.create(getContext() , R.raw.lost);
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });
                    }else if(game.getStatus() == Status.WON){
                        mediaPlayer = MediaPlayer.create(getContext() , R.raw.won);
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });
                    }
                        else{
                        mediaPlayer = MediaPlayer.create(getContext() , R.raw.onsuccess);
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });
                    }
                    GamePlay.Companion.update(count , game);
                }
            }
        });
//        long click represent -> flag
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean ans = game.move(2 , position/count , position%count);
                if (ans){
                        b.setVisibility(View.INVISIBLE);
                        img.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.flag);
                        mediaPlayer = MediaPlayer.create(getContext() , R.raw.flag);
                        mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                        GamePlay.Companion.updateMines(count , game);
                }
//                return true to maintain the state of onlong
                return true;
            }
        });
//        long click image -> unflag (if status ONGOING)
        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean ans = game.move(2 , position/count , position%count);
                if (ans && game.getStatus() == Status.ONGOINING){
                    b.setVisibility(View.VISIBLE);
                    img.setVisibility(View.INVISIBLE);
                    img.setImageResource(R.drawable.flag);
                    //innovation
                    mediaPlayer = MediaPlayer.create(getContext() , R.raw.unflag);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    //after evaluation - to update flags in game
                    GamePlay.Companion.updateMines(count , game);
                }
                return true;
            }
        });
        return gridItemView;
    }
    public void  destroyMP(){
        mediaPlayer.release();
    }

}
