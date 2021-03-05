 package com.example.minesweeper

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.util.Log.INFO
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.fragment.app.FragmentTransaction
import java.util.*
import java.util.logging.Level.INFO


 /**
 * @author : Vishal Singhal (11 - 2 - 2021)
 *
 * this is the main activity
 * holds mines locations as x and y in java arrayList<Int> into minesX and minesY which initially is empty
 *
 * TODO : task-1_DONE -> populate mines location -> DONE
 * step - 1 -> on proceed click into activity_main call a function populate -> DONE
 * step - 2 -> in that function check for radio button and custom input options -> DONE
 *          -> if both given - toast choose one option
 *          -> if only difficulty fiels call -> diffPop()
 *          -> if custom call -> customPop()
 *
 * Step - 3 -> diffPop()
 *          -> gets radio button value
 *          -> and using when place random mines for easy - 1/6 , m,n = 9
 *                                               for med - 1/5  , mn = 11
 *                                               for diff - 1/4   mn = 13
 *          ->this can be done as populate arraylist of mines and assign to mines
 *
 *step- 4 -> customPop() -> DONE
 *        -> takes fiels m and n
 *        -> call new Activity getInput using intent (result callback -> read IMP_NOTE) and put
 *           extra data (m,n and minesX , minesY)
 *           *(m , n) to set grid in that activity
 *           * Note :we can make new similar minesX and minesY in takeInput and return here but
 *                  for more readability i used to pass empty minesX and minesY and populate them and
 *                  return here
 *   @IMP_Note : the above process can be Achieved used startActivityForResult() and overriding
 *               onActivityResult() -> task1 : helper
 *        -> in that activity user selects the location of mines not more that 1/4 and popuplate mines
 */

/**
 * TODO : task - 2 -> now we have mines location and we have to play the game on click play -> DONE
 * step -1 when user press play -> move to newActivity Gameplay and pass minesX and minesY with sizes
 *         using intent and startActivity for result to get scores(getting will be done by overriding onActivityResult) and update lastMatchTime
 *
 * TODO : task - 3 keep track of best using shared preferences
 *
 *        step - 1 -> onResultCallBack -> loadPreferences and get Value
 *                                     -> compare newVal and loaded val
 *                                     -> if new is less clear old and save new
 *        Step - 2 -> in onCreate get the texr id for best and set Best : ${loadPref}
 */
class MainActivity : AppCompatActivity() {

    //    task1 needs
    public var minesX: ArrayList<Int> = ArrayList<Int>();
    public var minesY: ArrayList<Int> = ArrayList<Int>();
    public val ROW_COUNT: String = "ROW_COUNT";
    public val COLUMN_COUNT: String = "COLUMN_COUNT";
    var boardsize: Int = 0;
    var mediaPlayer: MediaPlayer? = MediaPlayer();
//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        task 1 - step 1
        val submit: Button = findViewById(R.id.submit);
        submit.setOnClickListener {
            var mediaPlayer = MediaPlayer()
            mediaPlayer = MediaPlayer.create(this, R.raw.on_btn_)
            mediaPlayer.start()
            minesX.clear()
            minesY.clear()
            populate();
        }
//        task 2 - step 2
//      task3
        val oldBest = loadBest();
        val bestView = findViewById<TextView>(R.id.prevMax)
        bestView.setText("Best : ${String.format("%.2f", oldBest - 0.5)} seconds")

    }

    override fun onStop() {
        super.onStop()
            mediaPlayer!!.release();
    }


    //    task 1  : step 2
    private fun populate() {

        val radGrp: RadioGroup = findViewById(R.id.radioGrp);
        val checkId: Int = radGrp.checkedRadioButtonId;
        var radBtn: RadioButton? = null;
        if (checkId != -1) {
            radBtn = findViewById(checkId);
        }
        val rows: EditText = findViewById(R.id.rows)
        val rowVal: String = rows.text.toString();
        val columns: EditText = findViewById(R.id.columns)
        val colVal: String = columns.text.toString();

//    if user is not giving any input and proceed (task1 -> 2.1)
        if ((radBtn == null || radBtn.text.toString()
                .trim() == "None") && (rowVal.length == 0 || colVal.length == 0)
        ) {
            Toast.makeText(this, "Provide Inputs", Toast.LENGTH_SHORT).show()
            return
        }

//        if checked and inputs both are given -> toast (task 1 - step 2.2)
        if (radBtn != null && radBtn.text.toString()
                .trim() != "None" && (rowVal.length > 0 || colVal.length > 0)
        ) {
            // task 1 - step 2.2
            Toast.makeText(
                this,
                "Choose Either Dificulty level or Custom Input",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

//        if difficulty inputs (task1 -> step 2.3)
        if (radBtn != null && radBtn.text.toString()
                .trim() != "None" && (rowVal.length == 0 && colVal.length == 0)
        ) {
            //task 1 -> case 2.3
            val diffLevel = radBtn.text;
            when (diffLevel.toString().trim()) {
                "Easy" -> {
                    Toast.makeText(this, "Easy Selected", Toast.LENGTH_SHORT).show()
                    boardsize = 9;
                    minesX.clear()
                    minesY.clear();
                    diffPop("Easy")
                }
                "Medium" -> {
                    Toast.makeText(this, "Medium Selected", Toast.LENGTH_SHORT).show()
                    boardsize = 11
                    minesX.clear()
                    minesY.clear();
                    diffPop("Medium")
                }
                "Hard" -> {
                    Toast.makeText(this, "Hard Selected", Toast.LENGTH_SHORT).show()
                    boardsize = 13
                    minesX.clear()
                    minesY.clear();
                    diffPop("Hard")
                }
            }
        }

//      if custom inputs (task1 -> step 2.4)
        // not provided equal size grid
        if ((radBtn == null || radBtn.text.toString()
                .trim() == "None") && (rowVal.length > 0 && colVal.length > 0) && rowVal.toString() != colVal.toString()
        ) {
            Toast.makeText(this, "Provide Equal Size Grid", Toast.LENGTH_SHORT).show()
            return
        }
        //provided equal inputs
        if ((radBtn == null || radBtn.text.toString()
                .trim() == "None") && (rowVal.length > 0 && colVal.length > 0) && rowVal.toString() == colVal.toString()
        ) {
            //task1 -> case 2.4 custom populate
            if (Integer.valueOf(rowVal) > 14) {
                Toast.makeText(this, "Put inputs smaller than 15", Toast.LENGTH_SHORT).show()
                return
            } else {
                customPop(Integer.valueOf(rowVal), Integer.valueOf(colVal))
                boardsize = Integer.valueOf(rowVal);
                return
            }
        }

    }

    //    task 1 : step 3
    private fun diffPop(s: String) {
        when (s) {
            "Easy" -> {
                val size = 9;
                val noOfMines = 81 / 6;
                diffPopHelper(size, noOfMines);
            }
            "Medium" -> {
                val size = 11;
                val noOfMines = 121 / 5;
                diffPopHelper(size, noOfMines);
            }
            "Hard" -> {
                val size = 13;
                val noOfMines = 169 / 4;
                diffPopHelper(size, noOfMines);
            }
        }
    }

    //    task 1 : step 4
    private fun customPop(rows: Int, columns: Int) {
        // make intent and pass rows and columns with minesX and minesY so that takeInput activity
        // populates there copy and return using resultCallBack
        val intent: Intent = Intent(this, TakeInput::class.java)
        intent.putIntegerArrayListExtra("MINE_X", minesX);
        intent.putIntegerArrayListExtra("MINE_Y", minesY);
        intent.putExtra(ROW_COUNT, rows);
        intent.putExtra(COLUMN_COUNT, columns);
//          when we need to get some data from called activity just after it done its work we uses
//          startActivity for result Instead of startActivity
        startActivityForResult(intent, 12);
        return
    }


    //    task 2 part
    fun play(view: View) {
        mediaPlayer!!.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.on_btn_)
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener {
            mediaPlayer!!.release();
        }
        //if input not given correctly
        if (boardsize == 0) {
            Toast.makeText(this, "First Select Difficulty or Put Custom Mines", Toast.LENGTH_SHORT)
                .show()
        }
        if (minesX.size <= 0) {
            Toast.makeText(this, "First Select Difficulty or Put Custom Mines", Toast.LENGTH_SHORT)
                .show()
            return
        }
//        //task 2.1
        val intent: Intent = Intent(this, GamePlay::class.java);
        intent.putIntegerArrayListExtra("MINE_X", minesX)
        intent.putIntegerArrayListExtra("MINE_Y", minesY)
        intent.putExtra("BOARD_SIZE", boardsize);
        startActivityForResult(intent, 13);
    }


    //    helper (task1.4 -> @work : sets the populated returned X and Y locations)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 12
        // request code is identification for the result we passed during startActivityForResult
        // E.g -> 1startActForRes(reqCode , intent) -> TargetActivity sets Result -> then this function
        //        checks for reqCode to be sure that this result is came for 1 or not -> if yes do work on result else leave
        if (requestCode == 12) {
            val minX: java.util.ArrayList<Int?>? = data!!.getIntegerArrayListExtra("MINE_X")
            val minY: java.util.ArrayList<Int?>? = data!!.getIntegerArrayListExtra("MINE_Y")
            minesX = minX as ArrayList<Int>;
            minesY = minY as ArrayList<Int>;
        } else if (requestCode == 13) {
            val time: Float = data!!.getFloatExtra("TIME", 0.0f);
            val result: String? = data!!.getStringExtra("RESULT");
            val v: TextView = findViewById(R.id.prevMinTime)
            v.setText("Last Game Time : ${String.format("%.2f", time - 0.5)} seconds")

            //task3
            if (result == "won") {
                val oldBest: Float = loadBest();
                //if new is less than old save
                if (time < oldBest) {
                    saveBest(time)
                }
                val oldBes = loadBest();
                val bestView = findViewById<TextView>(R.id.prevMax)
                bestView.setText("Best : ${String.format("%.2f", oldBes - 0.5)} seconds")
            }

        } else {
        }

    }

    //task 3
    private fun saveBest(newBest: Float) {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat("BEST", newBest)
            commit()
        }
    }

    // task 3
    private fun loadBest(): Float {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val bestScore = sharedPreferences.getFloat("BEST", 600000.5f);
        return bestScore
    }

    //    helper (task 1.3) -> sets minesX and minesY on the basis of size and noOfMines
    private fun diffPopHelper(size: Int, noOfMines: Int) {
//        we have size of the populating mines and minesCount
//        while noOfMines != 0
//        get random val from size and add it into the minX and minY simultaneously if not preExisted
        var hSet: java.util.HashSet<Int> = java.util.HashSet<Int>();
        var counter = noOfMines;
        var currRandom: Int = 0;
        var currX: Int = 0;
        var currY: Int = 0;
        while (counter != 0) {
            currRandom = ((Math.random().toDouble()) * (size * size)).toInt()
            if (!hSet.contains(currRandom)) {
                hSet.add(currRandom);
                currX = currRandom / size;
                currY = currRandom % size;
                minesX.add(currX);
                minesY.add(currY);
                counter = counter - 1;
            }
        }
        return
    }
}