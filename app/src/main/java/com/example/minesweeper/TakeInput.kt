package com.example.minesweeper

import android.content.Intent
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

data class Pair(val x: Int, val y: Int)

/**
 * @author Vishal Singhal 12 - 2 - 21
 * @taskNo_1_4 = task1 - step -> 4      (1.4) -> TODO - DONE
 * @work this activity takes input from the user and populate mines by returning minX and minY as
 *          this activity is called by startActivityForResult()
 *
 * step - 1 -> this activity collects rowcount(m) and column(n) count from user so that we can show
 *              grid of m*n
 * step - 2 -> now we collect ArrayList<Int> minesX and minesY and set into minX and minY
 * step - 3 -> as m and n are variables we have to show dynamic grid and for that set gridView into
 *              take_input.xml
 * step - 4 => set customAdapter linked with this gridView and make a grid of m*n
 *          -> make data array of size m*n
 *          -> @param currentContext , minX , minY , dataArray(size = m*n) , n
 *          -> set numColumns - n to gridView and arrayAdapter = customAdapter
 * step - 5 -> now On click save these ArrayList (minX , minY) get returned to calling activity
 *             using new Intent and setResult() with finish() method
 */


public class TakeInput : AppCompatActivity() {

    var mediaPlayer : MediaPlayer? = MediaPlayer();
    lateinit var adapter : customAdapter ;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this activity attached to take_input layout (GridView)
        setContentView(R.layout.take_input)

        //prompt user to select locations
        Toast.makeText(this, "Enter Location of Mines not more than Grid/4", Toast.LENGTH_SHORT).show()

        //1.4.1
        var intent : Intent = getIntent();
        var n : Int = intent.getIntExtra("ROW_COUNT", 0);
        var m : Int = intent.getIntExtra("COLUMN_COUNT", 0);
        //1.4.2
        val minX : java.util.ArrayList<Int?>? = intent.getIntegerArrayListExtra("MINE_X")
        val minY : java.util.ArrayList<Int?>? = intent.getIntegerArrayListExtra("MINE_Y")

//      1.4.4.1
        val arr : Array<String> = Array(n * m){""};
//      1.4.4.2  now set an array adapter at the takeIntput layout
        adapter  = customAdapter(this, minX, minY, arr, n , mediaPlayer)
//      1.4.4.3  take gridView and set arrayAdapter
        val view : GridView = findViewById(R.id.take_Input_View);
        view.numColumns = n;
        view.adapter = adapter;

//      1.4.5
        var saveButton : Button = findViewById(R.id.save_Input);
        saveButton.setOnClickListener{
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, R.raw.on_btn_)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.release();
            }
            val inte : Intent = Intent(this, MainActivity::class.java)
            inte.putIntegerArrayListExtra("MINE_X", minX);
            inte.putIntegerArrayListExtra("MINE_Y", minY);
//            this sets the values that to returned to calling activity using intent
//            parameter --> (status , intent)
            setResult(RESULT_OK, inte);
//            finish this activity that means return intent
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        adapter.destroyMP()

    }

//    override fun onPause() {
//        super.onPause()
//        Log.i("VISHAL" , "here")
//        mediaPlayer.stop()
//        mediaPlayer.release()
//        Log.i("VISHAL" , "released")
//    }


}