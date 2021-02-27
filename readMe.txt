Application : Minesweeper
Created By : Vishal Singhal


Note : Read in wrap content format for proper understanding 

Features : This is a Simple minesweeper game Android Application with features like-
        1) Holds the best Winning Score and Shows it Every Time the game Starts
	2) shows the last played game Time
	3) Has three different Difficulty levels in which mines Shuffles everyTime
	4) Custom Input for user in which user can enter grid Size and save its own mines locations : This helps user to trust the game and check the possibilities 
	5) shows no of mines during gamePlay which helps user to count flags and get Confident
	6) on every game - timer resets to 0 and continously updates itself so that user can compete with himself/herself
	7) on every game - there is feature of reset same game if user accidently not started on time or want to start game from some other end

Working : It consists of three Activity-
	  1) main_activity - main launcher screen (shows and manages feature 1-4)
	  2) take_input - (activity launch on feature 4 )
	  3) gameplay - (connects the UI with Minesweeper game and manages features from 5 - 7)

Note : Game Code is divided into tasks -> makes code more Readable and understandable 

Flow of Code -
1) MainActivity launches from android in which we override function onCreate()
2) setContentView() links the activity_main layout out with this activity
Note: It loads the Best and update it (later discussed) 
3) prompting user to submit inputs using populate() function which fills mines Co-ordinates and handles many Cases like -
	1) user neither selected any difficult level nor put custom inputs and tries to play
	2) user selects both options
	3) user selects  custom and put unequal size of grid
   3.1) this process is Done using-
	-RadioGroup in which 4 RadioButtons Exists with unique ids
	-editText in which user can put its input	
4) now on the basis of inputs populate() Function calls diffPop() or customPop() according to inputs
5) diffPop() takes difficulty level as Input and Fills mines location using random() function 	  
6) customPop() - this opens the new Activity takeInput using Explicit Intent and start activity with startActivityForResult() which expects some return result from called Activity (also sends size of grid using putExtra())

Now we are in TakeInput-

Define properties minX(stores abscissa of user mine location),minY(stores ordinate of user mine location),count(size of grid)
6.0) get size of Grid passed from mainActivity using getIntent() = n
6.1) this activity first links the take_input layout xml file with this activity
6.2) find gridView from take_input using findViewByID()
6.3) make new customAdapter object which takes currentActivityContext , minX , minY , data(empty array of size n*n) , n)
6.4) set this adapter with gridView
6.5) set noOfColumns = n for gridView so that adapter fills it into n columns pattern

customAdapter - this is customAdapter made so that we can user ArrayAdapter functions for custom layout not only restricted to TextViews (uses inheritance)
		-in this first super class(ArrayAdapter) object is made which requires (context,layputPattern,data) l.p = 0 as game using custom layout	 
	      	-override getView() method which uses old made views and updates it according to need and oldView is convertView in Parameter or inflates new views using LayoutInflator() for first time 
		  Note : customLayoutFile passed as parameter (In game grid_Item_Layout)	
		-has method setOnClickListener which adds user clicked button x,y int minX and minY

6.6)has save button which sets result using setResult(STATUS , intent contains extra values) and finishes activity using finish() which returns the screen and flow to mainActivity

Note : Now game is again in MainActivity 

7) override onActivityResult() function which catches the returned intents which are called using startActivityForResult
   - it compares requestCode with which we passed during calling and executes work according to it
   - In game it gets the minX and minY and updates mines Location

Now we have input Ready

8) it has play button where activity navigates to GamePlay activity where actual game is there and passes minX and minY and handles cases of empty inputs (this also called using startActivityForResult as we need to get gameover Time)

Note : now game is again in GamePlay Activity 
8.1) sets the_game layout
8.2) get minX and minY and size using getIntent()
8.3) set no of Mines in mines View
8.4) calls updatetimer() which starts the timer
8.4) calls setGame() which make new Minesweeper Object,sets mines into it, get gridView from layout and call static functions (companion object functions kotlin) setContext() , update()
8.5) setContext() - set activity context and view into static Context field and static view field which received as parameter
8.6) update() - attach customGameAdapter with the view (parameters - context(static) , dataArray(size = n*n), game(Received Parameter) , n)

customGameAdapter - extends ArrayAdapter 
 		  - constructor - make super(context(Paramter), 0, data) object , assign game(parameter) into Minesweeper type field game , assign n into count
	    	  - override getView 
			-if no convertView there all views will make again and for every view there is condition that is according to current game state and user will see current game state		
		  	-has setOnClickListener() which handles user cell open need -> (calls game.move(1,x,y) and if this return true update the grid view by calling static update function made in GamePlay)
			-has setOnLongClickListener() which handles user flag option -> (calls game.move(2,x,y) and if this return true toggles the state by invisibling and visibiling button and image according to state)


8.7) has reset function linked with reset button which calls updateTimer() (resets timer from 0) and setGame() (discussed above and make all things again)
8.8) get timer View and updates it till game is ongoing Every second using onTick() function which is overrided into countDownTimerObject
	-if(game - ongoing) -> update view
	-elseif(won) -> cancel timer and call goBack() with won (param - Result(won or lost) , time)
	-else -> cancel timer and call goBack() with lost
8.9) goBack() - setResult() (passing result and time)

Note : Now we are in mainActivity

9) in onActivityResult make another case checking for request code in which-
   - get result and time
   - update lastGameTime with time
   - if(game won)
10) use sharedPreferences to get Best score and update simultaneously 


									
