package com.example.minesweeper;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * @helper : this Adapter used for TakeInput to set grids into activity of dynamic tyoe which shows
 *           buttons
 * @taskID_1_4_4__3_Implementation
 *
 *
 */

public class customAdapter extends ArrayAdapter<String> {
//  make fields to store constructor parameter so that it can be accessed by all member Functions
    public ArrayList<Integer> mineX;
    public ArrayList<Integer> mineY;
    public int count = 0;
//      made custom Constructor according to needs
    public customAdapter(@NonNull Context context, ArrayList<Integer> resX,ArrayList<Integer> resY ,@NonNull String[] objects , int n) {
//        put 0 in place of layout as we are setting custom layout into getView
        super(context,  0 , objects);
        mineX = resX;
        mineY = resY;
        count = n;
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
        Button b = (Button) gridItemView.findViewById(R.id.grid_item);
        ImageView i = (ImageView) gridItemView.findViewById(R.id.hello);
        b.setVisibility(View.VISIBLE);
//        add listener to every button to add x,y and Invisible the button
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mineX.size() <= (count*count)/4) {

                    //important getting color from resource id and setting it
                    b.setVisibility(View.INVISIBLE);
                    i.setImageResource(R.drawable.bomb);
                    i.setVisibility(View.VISIBLE);
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer = MediaPlayer.create(getContext() , R.raw.set_mine);
                    mediaPlayer.start();
                    mineX.add(position/count);
                    mineY.add(position%count);
                }else{ }
            }
        });
        return gridItemView;
    }
}
