package com.example.minesweeper

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.min

/**
 * @task_2_1 - TODO - DONE
 * @work - this is Gameplay Activity which shows a grid to user to interact and manages user event --> DONE
 *          according to MinesWeeper and and shows results back to user
 *        - this activity also shows reset button which resets the game for same mines location --> DONE
 *        - we also need timer which we will show in the time section
 *
 * @id_task_2_1_1 TODO - DONE
 * Step - 1 : get mines locations and size of board
 * step - 2 : Make a UI for user using custom Adapter
 *            -> call setGame()
 *            -> why this function? -> reduces copyPaste for reset and oncreate
 * step - 3 -> make reset function which is linked with reset button to restart the game with same
 *              size and set of mines
 * step - 4 -> setGame() : this function calls the static (companion object in kotlin) function
 *               update() and setContext() which sets the grid using customAdapter(helper)
 *          -> param : minX , minY , size
 *          -> 1)make minesWeeper game object according to params
 *          -> 2)setMines
 *          -> collect Grid View
 *          -> setContext(this , gridView)
 *          -> update(size , Game)
 *          -> updateTimer(Timer Restart)
 * step - 5 -> make companion object
 *          -> make setContext()
 *             params: context(Gameplay Activity) , gridView (collected)
 *          -> make update() : this function sets the adapter for parameters received
 * @NOTE : why this function static (companion)
 * @ANS : we are setting griditems into gridView using this
 *        1) At first oncreate
 *        2) At Every Change -> onClick or onLongClick
 *        so, to access this function from both locations
 *            we link this work to class instead of object
 *
 *
 *@task_2.1.2 - set timer TODO - DONE
 *      step - 1 - get textView minTime
 *      step - 2 - make countdownTimer for 600sec (10min) @param - 600,000 , 1000
 *               - now override onTick function (done some work at every second)
 *                  -if game is ongoing -> update the current time into the text view
 *                  -else return this data to main activity and finish
 *                      -> call goback fun which takes time of end and return to Mainactivity as this activity call by startActivityForResult
 *               -on timer end update game status to false and give command to print the grid
 *
 *
 */
class GamePlay : AppCompatActivity() {

    var size : Int? = null
    var minX : java.util.ArrayList<Int?>? = null;
    var minY : java.util.ArrayList<Int?>? = null;
    var game : Minesweeper? = null;
    var timer : CountDownTimer? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.the_game)
        //task 2.1.1.1
        val intent = getIntent()
        size = intent.getIntExtra("BOARD_SIZE", 0);
        minX = intent.getIntegerArrayListExtra("MINE_X")
        minY = intent.getIntegerArrayListExtra("MINE_Y")

        //task 2.1.1.2.1
        setGame(minX, minY, size!!)

        //task 2.1.2.1
        val view : TextView = findViewById(R.id.timer);
        updateTimer(this, view, game, size!!)

//        set Mines
        val mines : TextView = findViewById(R.id.noOfMines)
        mines.setText("${minX!!.size}")
    }

    //    task 2.1.1.3.1
    fun reset(view: View){
        var mediaPlayer = MediaPlayer()
        mediaPlayer = MediaPlayer.create(this, R.raw.on_btn_)
        mediaPlayer.start()
        setGame(minX, minY, size!!);
        val view2 : TextView = findViewById(R.id.timer);
        updateTimer(this, view2, game, size!!)
    }

//    task 2.1.1.4.1
    fun setGame(minX: java.util.ArrayList<Int?>?, minY: java.util.ArrayList<Int?>?, size: Int){
        //2.1.4.1
        game = Minesweeper(size);
        //2.1.4.2
        for (i in 0 until minX!!.size){
            game?.setMine(minX?.get(i) as Int, minY?.get(i) as Int);
        }
        //2.1.4.3
        val view : GridView = findViewById(R.id.the_game_view);
        //2.1.4.4
        //this is after evaluation work to update mines as user flagged an item
        val mines : TextView = findViewById(R.id.noOfMines)
        setContext(this, view , mines , minX!!.size);
        //2.1.4.5
        update(size, game!!);
    }

//    taks 2.1.1.5.1
    companion object{
        var conti : Context? = null
        var viewR : GridView? = null
        var mines : TextView? = null
        var totalMines : Int? = null;
        //2.1.5.1
        fun setContext(con: Context, viewPara: GridView , mineView : TextView , mine : Int){
            conti = con;
            viewR = viewPara
            mines = mineView;
            totalMines = mine;
        }
        //2.1.5.2
        fun update(size: Int, game: Minesweeper){
            val arr : Array<String> = Array(size * size){""};
//      1.4.4.2  now set an array adapter at the takeIntput layout
            val adapter : customGameAdapter = customGameAdapter(conti!!, arr, size, game)
//      1.4.4.3  take gridView and set arrayAdapter
            viewR!!.numColumns = size;
            viewR!!.adapter = adapter;
        }
//    update mines as the user flags the view
        fun updateMines(size: Int, game: Minesweeper){
            var flagged = totalMines!! - game.flagged;
            if (flagged >= 0){
                mines!!.setText("$flagged")
            }else{
                mines!!.setText("$flagged")
                Toast.makeText(conti, "Exceeding Mine Count", Toast.LENGTH_SHORT).show()
            }

        }
    }

//  2.1.2
    private fun updateTimer(gamePlay: GamePlay, view: TextView, game: Minesweeper?, size: Int) {
        var time : Float = 0.0f;
        view.setText("0.00");
        if (timer != null){
            timer!!.cancel();
        }
        timer = object: CountDownTimer(600000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (game!!.status == Status.ONGOINING){
                    view.setText("${(600000.toDouble() - millisUntilFinished.toDouble()) / 1000}")
                }else if(game!!.status == Status.WON){
                    time = (600000.toFloat() - millisUntilFinished.toFloat())/1000;
                    cancel()
                    goBack(time, "won")
                }
                else{
                    time = (600000.toFloat() - millisUntilFinished.toFloat())/1000;
                    cancel()
                    goBack(time, "lost")
                }
            }

            override fun onFinish() {
                game!!.status = Status.LOST;
                update(size, game);
            }
        }
        timer!!.start()

    }
    private fun goBack(time: Float, result: String){
        val inte : Intent = Intent(this, MainActivity::class.java)
        inte.putExtra("TIME", time);
        inte.putExtra("RESULT", result);
        setResult(RESULT_OK, inte);
        conti = null;
        viewR = null;
    }


}