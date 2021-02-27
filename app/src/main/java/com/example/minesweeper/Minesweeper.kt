package com.example.minesweeper
import java.util.*
enum class Status{
    WON,
    ONGOINING,
    LOST
}
fun main(args: Array<String>){
    val read = Scanner(System.`in`)
//    boardSize
    val size = read.nextInt()
//    minesCount
    val count = read.nextInt()

    //creates the instance of the board
    val game = Minesweeper(size)
    //sets mines as per the input
    for(i in 1..count)
        game.setMine(read.nextInt(), read.nextInt())

    //iterates input ans prints output
    val n = read.nextInt()
    for(i in 1..n) {
        if (game.move(read.nextInt(), read.nextInt(), read.nextInt())) {
            println("true")
            game.displayBoard()
        } else {
            println("false")
        }
        if(game.status!= Status.ONGOINING)
            break
    }
    println(game.status)
    println(game.board.size);
}

//board class
public class Minesweeper(private val size:Int){
    var board = Array(size) { Array(size) { MineCell() }}
    public var status = Status.ONGOINING
    var boardSize = size*size;
    var flagged = 0;
    var mines = 0;
    var revealed = 0

    //sets up mines
    fun setMine(row: Int, column: Int) : Boolean{
        if(board[row][column].value != MINE) {
            board[row][column].value = MINE
            updateNeighbours(row,column)
            mines++;
            return true
        }
        return false
    }

    //updates the values os the cells neighbouring to the mines
    private fun updateNeighbours(row: Int, column: Int) {
        for (i in movement) {
            for (j in movement) {
                if(((row+i) in 0 until size) && ((column+j) in 0 until size) && board[row+i][column+j].value != MINE)
                    board[row+i][column+j].value++
            }
        }
    }

    fun move(choice: Int, x: Int, y:Int): Boolean {
//        check choice is 1 or 2      1 -> open and 2 -> flag
        // flagging conditions
//        if already opened or marked then can't be flagged
//        if closed can be flagged
        if (status == Status.LOST) {
            displayBoard()
            return false
        }
        if (status == Status.WON) {
            displayBoard()
            return false;
        }
        if (x < 0 || y < 0 || x >= board.size || y >= board.size) {
            return false;
        }
        if (choice == 2) {
            if (board[x][y].isRevealed) {
                return false;
            }else if(board[x][y].isMarked){
                board[x][y].isMarked = false;
                flagged--;
                return true;
            } else{
                board[x][y].isMarked = true
                flagged++;
                return true;
            }

        }
        //opening conditions
//        if already opened or flagged can't be opened
//        if closed then true
        if (choice == 1) {
            if (board[x][y].isRevealed || board[x][y].isMarked) {
                return false
            } else {
                open(x , y);
                if (boardSize - revealed == mines) {
                    status = Status.WON;
                }
                return true;
            }
        }
        return false
    }
    //displays the board
    fun displayBoard() {
        for (i in 0..board.size-1){
            for (j in 0..board.size-1){
                if(board[i][j].isRevealed){
                    var position = i*j;

                }
//                    print("| ${it.value} |")
                else if (board[i][j].isMarked)
                    print("| X |")
                else if (status == Status.LOST && board[i][j].value == MINE)
                    print("| * |")
                else if (status == Status.WON && board[i][j].value == MINE)
                    print("| X |")
                else
                    print("|   |")
            }
            println()
        }
    }

    private fun open(x : Int , y : Int){
//         if out of bounds then return
        if (x >= board.size || y >= board[0].size || x < 0 || y < 0){
            System.out.println("Here")
            return
        }
//        else if current position is no
        if(board[x][y].value != 0 && board[x][y].value != MINE){
            board[x][y].isRevealed = true;
            revealed++;
            return;
        }
//        if current opening position is mine then status = LOST and return
        if (board[x][y].value == MINE){
//            board[x][y].isRevealed = true;
            status = Status.LOST;
            return
        }
//      if flagged return
        if (board[x][y].isMarked){
            return
        }

//        else we have 0 -> now we expand from currLocation and reveal cells till we not encounter any no.
//        can we use recursion -> yes as every mine is surrounded by no so definitely ended up with no
        else{

            board[x][y].isRevealed = true;
            revealed++;
//            open all eight directions
            if((x-1 in 0 until size) && (y-1 in 0 until size)&& !board[x - 1][y - 1].isRevealed && !board[x - 1][y - 1].isMarked){
                open(x - 1, y - 1 )
            }

            if(x-0 in 0 until size && y-1 in 0 until size && !board[x][y - 1].isRevealed && !board[x][y - 1].isMarked)
                open(x - 0 , y - 1)
            if(x+1 in 0 until size && y-1 in 0 until size && !board[x + 1][y - 1].isRevealed && !board[x + 1][y - 1].isMarked)
                open(x + 1 , y - 1)

            if(x-1 in 0 until size && y in 0 until size&& !board[x - 1][y].isRevealed && !board[x - 1][y].isMarked)
                open(x - 1, y  )
            if(x+1 in 0 until size && y in 0 until size&& !board[x + 1][y].isRevealed && !board[x + 1][y].isMarked)
                open(x + 1 , y )

            if(x-1 in 0 until size && y+1 in 0 until size&& !board[x - 1][y + 1].isRevealed && !board[x - 1][y + 1].isMarked)
                open(x - 1, y + 1 )
            if(x in 0 until size && y+1 in 0 until size && !board[x][y + 1].isRevealed && !board[x][y + 1].isMarked)
                open(x - 0 , y + 1)
            if(x+1 in 0 until size && y+1 in 0 until size && !board[x + 1][y + 1].isRevealed && !board[x + 1][y + 1].isMarked)
                open(x + 1 , y + 1)
        }
    }

    companion object{
        const val MINE = -1
        val movement = intArrayOf(-1, 0, 1)
    }
}


//mineCell Data Class
data class MineCell(var value:Int = 0 , var isRevealed: Boolean = false, var isMarked: Boolean = false)
