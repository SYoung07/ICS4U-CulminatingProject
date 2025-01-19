package game;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
/**
 * This class is the main logic and visuals of a Tic-Tac-Toe game.
 * It provides methods to initialize the game board, check for wins, and reset the game.
 * It also handles interactions with the user through a buttons and labels using JavaFX.
 * 
 * The game supports both singleplayer (against an AI) and multiplayer modes. It includes
 * functionality to display a winning line when a player wins, and allows players to restart the game
 * or return to the main menu.
 * 
 * The program uses the minimax algorithm to implement the AI player for the singleplayer mode.
 * 
 * @author Stacey Young, Nathan Barran
 */
public class TicTacToe extends Application{
	// declaring/initializing variables and objects
	private char[][] board = new char[3][3]; // 2d array for the tictactoe board
	private Button[][] buttons = new Button[3][3]; // 2d array for storing the buttons so the ai can do the right moves visually
	private String gameMode;
	private Label statusLabel = new Label();
	private boolean player1Turn;
	GridPane gridPane = new GridPane();
	private BorderPane gameLayout = new BorderPane();
	private VBox buttonBox = new VBox(10);
	private int player1Score;
	private int player2Score;
	private int draws = 0;
	private Label player1ScoreLabel;
	private Label player2ScoreLabel;
	private Label drawsLabel;
	private boolean gameOver;
	private MediaPlayer mediaPlayer;
	VBox centerLayout;
	/**
	 * Starts the JavaFX application and displays the main menu.
	 * 
	 * @param primaryStage The primary stage for this application.
	 */
	@Override
	public void start(Stage primaryStage){
		showMainMenu(primaryStage);
	}
	/**
	 * Displays the main menu allowing the user to select the game mode.
	 * 
	 * @param primaryStage The primary stage for this application. 
	 */
	private void showMainMenu(Stage primaryStage) {
		// resets scores to 0 when going to the main menu instead of restarting the same gamemode
		player1Score = 0;
		player2Score = 0;
		Label titleLabel = new Label("Select Game Mode");
		Button singlePlayerButton = new Button("Single Player");
		Button twoPlayerButton = new Button("Two Player");
		singlePlayerButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px; -fx-pref-height: 100px;");
		twoPlayerButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px; -fx-pref-height: 100px;");
		titleLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: black;");
		primaryStage.setTitle("Main Menu");

		try {
			Media media = new Media(new File("src/game/MenuMusic.mp3").toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
		} catch(Exception e) {
			e.printStackTrace();
		}  
		primaryStage.setTitle("Tic-Tac-Toe: Main Menu");

		singlePlayerButton.setOnAction(e -> {
			showDifficultySelector(primaryStage);
		});
		twoPlayerButton.setOnAction(e -> {
			gameMode = "twoPlayer";
			primaryStage.setTitle("Tic-Tac-Toe: Player 1 vs Player 2");

			startGame(primaryStage);
		});
		VBox modeSelection = new VBox(30, titleLabel, singlePlayerButton,twoPlayerButton);
		modeSelection.setAlignment(Pos.CENTER);

		Scene mainMenu = new Scene(modeSelection, 400, 300);
		primaryStage.setScene(mainMenu);

		primaryStage.setMaximized(false);
		primaryStage.setMaximized(true);
		primaryStage.show();

	}
	/**
	 * Displays the difficulty selector for singleplayer mode.
	 * 
	 * @param primaryStage The primary stage for this application. 
	 */
	private void showDifficultySelector(Stage primaryStage) {
		// create labels and buttons
		primaryStage.setTitle("Tic-Tac-Toe: Difficulty Selector");

		Label titleLabel = new Label("Choose the Difficulty");
		Button easyModeButton = new Button("Easy");
		Button hardModeButton = new Button("Hard");

		easyModeButton.setOnAction(e -> {
			gameMode = "singlePlayerEasy";
			primaryStage.setTitle("Tic-Tac-Toe: Player vs Computer (Easy)");
			startGame(primaryStage);
		});
		hardModeButton.setOnAction(e -> {
			gameMode = "singlePlayerHard";
			primaryStage.setTitle("Tic-Tac-Toe: Player vs Computer (Hard)");
			startGame(primaryStage);
		});
		HBox buttonBox = new HBox(600, easyModeButton, hardModeButton);
		buttonBox.setAlignment(Pos.CENTER);
		VBox screenLayout = new VBox(500, titleLabel, buttonBox);
		screenLayout.setAlignment(Pos.TOP_CENTER);

		// making text/buttons bigger
		easyModeButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px; -fx-pref-height: 100px;");
		hardModeButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px; -fx-pref-height: 100px;");
		titleLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: black;");
		// add a (cracked?)line down the center to separate easy and hard modes with different backgrounds for each (damjan sketch) - NATHANS JOB
		// maybe add a unique background for each side (damjan sketch) maybe nathans job if he can figure it out

		Scene mainMenu = new Scene(screenLayout, 400, 300);
		primaryStage.setScene(mainMenu);
		primaryStage.setMaximized(false);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
	/**
	 * Initializes and starts a new game based on the selected game mode.
	 * 
	 * @param primaryStage The primary stage for this application.
	 */
	private void startGame(Stage primaryStage) {
		// reset to player 1's turn and resets the board
		resetBoard();
		player1Turn = true; 
		statusLabel.setText("Player 1's Turn (X)");
		gameOver = false;

		// different names for players depending on game mode
		if(gameMode.equals("twoPlayer")) {
			player1ScoreLabel = new Label("Player 1: " + player1Score);
			player2ScoreLabel = new Label("Player 2: " + player2Score);
			drawsLabel = new Label("Draws: " + draws);
		} 
		// singlePlayerEasy or singlePlayerHard
		else { 

			//						player1ScoreLabel = new Label("Player: " + player1Score);
			//						player2ScoreLabel = new Label("Computer: " + player2Score);
			//						drawsLabel = new Label("Draws: " + draws);

			// attempts to align them better by using string.format to reserve space because player is shorter than computer
			player1ScoreLabel = new Label(String.format("%11s %d", "Player:", player1Score));
			player2ScoreLabel = new Label(String.format("%11s %d", "Computer:", player2Score));
			drawsLabel = new Label(String.format("%11s %d", "Draws:", draws));

		}


		gridPane = new GridPane();
		gameLayout = new BorderPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setGridLinesVisible(true);
		gridPane.setStyle("-fx-font-size: 115px;"); // makes the text of the grid bigger (the X's and O's)
		statusLabel.setStyle("-fx-font-size: 25px;"); // makes the text of the statusLabel bigger

		//		Image gameBackgroundImage = new Image("src/game/XObackground");
		//		ImageView background = new ImageView(gameBackgroundImage);

		// creates the buttons
		for (int i = 0; i<3; i++) {
			for (int j = 0; j<3; j++) {
				// for some reason there were errors if i used row and column as variables in the for loops
				int row = i;
				int column = j;
				Button square = new Button();
				square.setPrefSize(250, 250); // 250x250px button
				buttons[row][column] = square; // the buttons[][] array is for the AI to make the right move visually
				gridPane.add(square, column, row); // gridpane.add uses (column, row) instead of the usual (row, column)
				//				gridPane.add(square, row, column);
				try {
					square.setOnAction(e ->{
						if(gameMode.equals("twoPlayer")) {
							// test case, prints the row and column of the button clicked
							twoPlayerMove(square, row, column, primaryStage);

							Media media = new Media(new File("src/game/CartoonClickSound.mp3").toURI().toString());
							mediaPlayer = new MediaPlayer(media);
							mediaPlayer.play();
						}else {
							// test case, prints the row and column of the button clicked
							singlePlayerMove(square,row,column,primaryStage);

							Media media = new Media(new File("src/game/CartoonClickSound.mp3").toURI().toString());
							mediaPlayer = new MediaPlayer(media);
							mediaPlayer.play();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// reserve space for the button box on the left to prevent it from moving the board when shown later
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setStyle("-fx-padding: 10;"); // spacing from the wall
		buttonBox.setPrefWidth(250); // reserve space on the left
		buttonBox.getChildren().clear(); // empty the left side 

		// this allows the board to be in the exact center of the screen since there is now a 200 px reserve on both sides from the buttonBox and this spacer
		VBox spacer = new VBox(10);
		spacer.setAlignment(Pos.CENTER_RIGHT);
		spacer.setPrefWidth(250);
		spacer.setStyle("-fx-padding: 10;"); // spacing from the wall
		spacer.getChildren().clear(); // empty the right side 
		// add score labels at the top and change the colours of them
		Label scoreText = new Label("SCORES");
		player1ScoreLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: blue;");
		player2ScoreLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: red;");
		drawsLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: gray;");
		scoreText.setStyle("-fx-font-size: 35px; -fx-text-fill: black;");

		HBox scoreBox = new HBox(100, player1ScoreLabel, drawsLabel, player2ScoreLabel);
		scoreBox.setAlignment(Pos.TOP_CENTER);
		scoreText.setAlignment(Pos.TOP_CENTER);
		scoreBox.setStyle("-fx-font-size: 35px;");

		// this is to have the SCORE text along with player score aligned at the top
		VBox scoreLayout = new VBox(scoreText, scoreBox);
		scoreLayout.setAlignment(Pos.CENTER);
		centerLayout = new VBox(gridPane, statusLabel);

		centerLayout.setAlignment(Pos.CENTER);

		// adds all VBox's/HBox's to a BorderPane for organization
		gameLayout.setTop(scoreLayout);
		gameLayout.setCenter(centerLayout);
		gameLayout.setLeft(buttonBox);
		gameLayout.setRight(spacer);

		Scene gameScene = new Scene(gameLayout);
		primaryStage.setScene(gameScene);
		primaryStage.setMaximized(false);
		primaryStage.setMaximized(true);
	}
	/**
	 * Handles a player's move in single-player mode.
	 * The player is always 'X', and after the move is made, it checks if the player has won or if the board is full.
	 * If neither condition is met, the computer takes its turn.
	 *
	 * @param square       The button representing the square where the player made the move.
	 * @param row          The row index where the move was made.
	 * @param column       The column index where the move was made.
	 * @param primaryStage The primary stage for this application.
	 */
	public void singlePlayerMove(Button square, int row, int column, Stage primaryStage) {	

		if (board[row][column] == ' ' && !gameOver) {
			board[row][column] = 'X'; // player move is always X
			square.setText("X");
			square.setStyle("-fx-text-fill: blue;");
			System.out.println("Player: 'X' placed at (" + row + "," + column +").");


			if (checkWin('X', false)) {
				statusLabel.setText("Player Wins!");
				player1Score++;
				player1ScoreLabel.setText(String.format("%11s %d", "Player:", player1Score));
				gameOver = true;
				try {
					Media media = new Media(new File("src/game/WinnerSoundEffect.mp3").toURI().toString());
					mediaPlayer = new MediaPlayer(media);
					mediaPlayer.play();
				} catch (Exception e) {
					e.printStackTrace();
				}
				showRestart(primaryStage);
				return;  // exits the method early to not run the computers turn
			} else if (isBoardFull(false)) {
				statusLabel.setText("It's a Draw!");
				draws++;
				drawsLabel.setText(String.format("%11s %d", "Draws:", draws));
				gameOver = true;
				showRestart(primaryStage);
				return; // exits the method early to not run the computers turn
			}
			// computer's turn
			computerMove(primaryStage);
		}
	}

	/**
	 * Handles the computer's move.
	 * If the game mode is "singlePlayerEasy", it selects a random empty square.
	 * If the game mode is "singlePlayerHard", it uses the minimax algorithm to select the best move.
	 *
	 * @param primaryStage The primary stage for this application. 
	 */
	private void computerMove(Stage primaryStage) {
		if (!gameOver) {
			int row, col;
			// easy mode
			if(gameMode.equals("singlePlayerEasy")) {
				// keeps randomly choosing a row and column until it finds an empty square 
				do {
					row = (int) (Math.random() * 3);
					col = (int) (Math.random() * 3);
				} while (board[row][col] != ' '); // keeps searching until an empty square is found
				// hard mode, finds the best move using the minimax algorithm
			} else {
				int[] bestMove = findBestMove(); // index 0 of the array is row, index 1 is column
				row = bestMove[0];
				col = bestMove[1];
			}
			// place the computer's move ('O') in the selected square
			board[row][col] = 'O';
			// makes a local variable to hold the button at (row,col) in the grid which allows us to edit it
			Button square = buttons[row][col];
			square.setText("O");
			square.setStyle("-fx-text-fill: red;");

			// test case
			System.out.println("Computer: 'O' placed at (" + row + "," + col + ").");

			// check if the computer wins after making the move
			if (checkWin('O', false)) {
				statusLabel.setText("Computer Wins!");
				player2Score++;
				player2ScoreLabel.setText(String.format("%11s %d", "Computer:", player2Score));
				gameOver = true;
				showRestart(primaryStage);
			} else if (isBoardFull(false)) {
				statusLabel.setText("It's a Draw!");
				draws++;
				drawsLabel.setText(String.format("%11s %d", "Draws:", draws));
				gameOver = true;
				showRestart(primaryStage);
			} else {
				statusLabel.setText("Player's Turn (X)");
			}
		}
	}
	/**
	 * Implements the minimax algorithm to evaluate the best move for the computer.
	 * This method is called recursively to explore all possible moves and returns the score for each move.
	 *
	 * @param moves         The number of moves made so far.
	 * @param computerTurn  True if it's the computer's turn, false if it's the player's turn.
	 * @return              The score for the current move based on the game state.
	 */
	private int minimax(int moves, boolean computerTurn) {
		// The minimax algorithm evaluates all possible moves and returns the move that maximizes the AI's chances of winning while minimizing the player's chance

		// base cases
		if (checkWin('O', true)) return 10 - moves; // AI wins, prefers faster wins by subtracting the amount of moves
		if (checkWin('X', true)) return moves - 10; // player wins, prefers slower losses by adding the number of moves
		if (isBoardFull(true)) return 0; // draw

		if (computerTurn) {
			int bestScore = -1000; // worst possible score for the AI
			// goes through all possible moves
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					if (board[row][col] == ' ') {
						board[row][col] = 'O';
						// AI is trying to maximize its score by recursively calling minimax to evaluate the score after this move
						int score = minimax(moves + 1, false); // swaps to the opponent players turn
						board[row][col] = ' '; // undos the move once the recursion above is fully completed
						bestScore = Math.max(bestScore, score); // the ai wants to maximize its score
					}
				}
			}
			return bestScore; // returns the best score for the AI
		} else {
			int bestScore = 1000; // worst possible score for the player
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					if (board[row][col] == ' ') {
						board[row][col] = 'X';
						int score = minimax(moves + 1, true); // after the player makes a move, it goes back to the AI's turn
						board[row][col] = ' '; // undos the move once the recursion above is fully completed
						bestScore = Math.min(bestScore, score); // player wants to minimize the AI's score
					}
				}
			}
			return bestScore; // returns the best score for the player
		}
	}
	/**
	 * Finds the best move for the computer using the minimax algorithm.
	 * This method evaluates all empty squares and returns the row and column for the best move.
	 *
	 * @return An array where index 0 is the row and index 1 is the column of the best move.
	 */
	private int[] findBestMove() {
		int bestScore = -1000; //   worst possible score for the AI
		int[] bestMove = new int[2]; // array to store the best move (row, column)

		// Loop through all squares on the board
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (board[row][col] == ' ') { // checks for empty squares
					board[row][col] = 'O'; // places a temporary move for the minimax algorithm
					// evaluate the move using the minimax algorithm
					int score = minimax(0, false); // 0 for moves, false because it's the player's turn next
					board[row][col] = ' '; // undo the move after recursion

					// if this move has a better score, update the bestScore and store the move
					if (score > bestScore) {
						bestScore = score;
						bestMove[0] = row;
						bestMove[1] = col;
					}
				}
			}
		}

		return bestMove; // returns the best move found with index 0 being[row] and index 1 being [col]
	}
	/**
	 * Handles a move in two-player mode. Alternates between Player 1 ('X') and Player 2 ('O') turns.
	 * Checks if either player has won or if the board is full, along with updating the status and scores.
	 *
	 * @param square       The button representing the square where the move is made.
	 * @param row          The row index where the move was made.
	 * @param column       The column index where the move was made.
	 * @param primaryStage The primary stage for this application. 
	 */
	public void twoPlayerMove(Button square, int row, int column, Stage primaryStage) {
		// if the square is empty and the game isn't over
		if(board[row][column] == ' ' && !gameOver) {
			char currentPlayer;
			// if its player 1's turn
			if(player1Turn) {
				currentPlayer = 'X';
				square.setStyle("-fx-text-fill: blue;");  // set the color for X to blue
			} else { // player 2's turn
				currentPlayer = 'O';
				square.setStyle("-fx-text-fill: red;");   // set the color for O to red
			}

			// adds the move to the 2d array which will be used to check who wins later
			board[row][column] = currentPlayer;
			// changes the visible grid to the player move
			square.setText(String.valueOf(currentPlayer)); // string.valueof converts the char to a string because .setText needs a string

			// test case
			System.out.println("'" + currentPlayer + "' placed at (" + row + "," + column +").");


			if(checkWin(currentPlayer, false)) {
				if(player1Turn) {
					statusLabel.setText("Player 1 Wins!");
					// updates score
					player1Score++;
					player1ScoreLabel.setText("Player 1: " + player1Score);
					Media media = new Media(new File("src/game/WinnerSoundEffect.mp3").toURI().toString());
					mediaPlayer = new MediaPlayer(media);
					mediaPlayer.play();
					gameOver = true;
				} else {
					statusLabel.setText("Player 2 Wins!");
					// updates score
					player2Score++;
					player2ScoreLabel.setText("Player 2: " + player2Score);
					Media media = new Media(new File("src/game/WinnerSoundEffect.mp3").toURI().toString());
					mediaPlayer = new MediaPlayer(media);
					mediaPlayer.play();
					gameOver = true;
				}
				showRestart(primaryStage);
			} else if(isBoardFull(false)) {
				statusLabel.setText("It's a Draw!");
				draws++;
				drawsLabel.setText("Draws: " + draws);
				gameOver = true;
				showRestart(primaryStage);

			} else { // if there isn't any wins/draws continue and display whos turn it is
				player1Turn =! player1Turn; // switches turns
				if(player1Turn) {
					statusLabel.setText("Player 1's Turn (X)");
				} else {
					statusLabel.setText("Player 2's Turn (O)");
				}
			}
		}
	}
	/**
	 * Checks if the specified player has won the game.
	 * This method checks all possible winning conditions (rows, columns, diagonals) for the given player.
	 * If the player wins, it will draw the winning line unless the method is called by the minimax algorithm.
	 *
	 * @param player    The character representing the player ('X' or 'O').
	 * @param minimax   A boolean indicating whether the method is being called by the minimax algorithm.
	 *                  If true, no winning line will be drawn.
	 * @return          True if the player has won, false otherwise.
	 */
	public boolean checkWin(char player, boolean minimax) {

		for (int i = 0; i<3; i++) {

			// rows
			if(board[i][0] == player && board[i][1] == player && board [i][2] == player ) {
				// if it isnt being called by the minimax algorithm, draw the winning line
				if(!minimax) { 
					drawWinningLine(i,-1, player); 
					// test case
					System.out.println("'" + player + "' wins on row index " + i);
				}
				return true;
			}
			// columns
			if(board[0][i] == player && board[1][i] == player && board [2][i] == player ) {
				// if it isnt being called by the minimax algorithm, draw the winning line
				if(!minimax) {
					drawWinningLine(-1,i, player);
					// test case
					System.out.println("'" + player + "' wins on column index " + i);
				}
				return true;
			}



			// diagonal
			if(board[0][0] == player && board[1][1] == player && board[2][2] == player) {
				// if it isnt being called by the minimax algorithm, draw the winning line
				if(!minimax) {
					drawWinningLine(0, 0, player); 
					// test case
					System.out.println("'" + player + "' wins on the main diagonal (top left to bottom right)");
				}
				return true;
			}
			if(board[0][2] == player && board[1][1] == player && board[2][0] == player) {
				// if it isnt being called by the minimax algorithm, draw the winning line
				if(!minimax) { 
					drawWinningLine(0, 2, player); 
					// test case
					System.out.println("'" + player + "' wins on the anti-diagonal (top right to bottom left)");
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * Checks if the board is full.
	 * This method checks every square on the board to determine if there are any empty spaces left.
	 *
	 * @param minimax   A boolean indicating whether the method is being called by the minimax algorithm.
	 *                  If true, the test case wont run.
	 * @return True if the board is full, false otherwise.
	 */
	public boolean isBoardFull(boolean minimax) {
		// checks the entire board for any empty square, returns false if it finds one
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j<3; j++) {
				if(board[i][j] == ' ') {
					return false;
				}
			}
		}
		// test case, only print if it isnt called by the minimax algoritm
		if(!minimax) System.out.println("The board is full (no possible moves).");
		return true;
	}

	/**
	 * Resets the board to its initial state, clearing all squares.
	 * This method is called when restarting the game.
	 */
	public void resetBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j<3; j++) {
				board[i][j] = ' ';
			}
		}
	}
	/**
	 * Displays the restart menu, allowing the player to either return to the main menu, restart the game, or exit.
	 * This method creates buttons for each option, and adds the logic.
	 *
	 * @param primaryStage The primary stage for this application. 
	 */
	public void showRestart(Stage primaryStage) {

		Button mainMenuButton = new Button("Main Menu");
		Button restartButton = new Button("Restart");
		Button exitButton = new Button("Exit");


		mainMenuButton.setOnAction(e -> {
			resetBoard();
			showMainMenu(primaryStage); // back to main menu
		});

		restartButton.setOnAction(e -> {
			resetBoard();
			startGame(primaryStage);
		});

		exitButton.setOnAction(e -> {
			primaryStage.close();
		});

		// bigger font, not touching the wall
		mainMenuButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px; -fx-pref-height: 100px;");
		restartButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px;-fx-pref-height: 100px;");
		exitButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px;-fx-pref-height: 100px;");



		// add buttons to the left side
		buttonBox.getChildren().clear(); // makes sure it doesn't overflow if the method is called multiple times
		buttonBox.getChildren().addAll(mainMenuButton, restartButton, exitButton);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setSpacing(100); // add spacing between buttons
		buttonBox.setStyle("-fx-padding: 30px;"); // add spacing from the side

	}
	/**
	 * Draws a line on the board to show the winning combination.
	 * The line will be drawn across a row, column, or diagonal where the player has won.
	 * 
	 * @param row      The row index of the winning line. A value of -1 indicates a column.
	 * @param column   The column index of the winning line. A value of -1 indicates a row.
	 * @param player   The character representing the player ('X' or 'O') who won.
	 */
	public void drawWinningLine(int row, int column, char player) {
		Line line = new Line();
		// rows
		if(row == 0 && column == -1) {
			line = new Line(500, 290, 1425, 290);
		}
		if(row == 1 && column == -1) {
			line = new Line(500, 540, 1425, 540);
		}
		if(row == 2 && column == -1) {
			line = new Line(500, 790, 1425, 790);
		}

		// columns
		if(row == -1 && column == 0) {
			line = new Line(710, 125, 710, 965);
		}

		if(row == -1 && column == 1) {
			line = new Line(960, 125, 960, 965);
		}

		if(row == -1 && column == 2) {
			line = new Line(1210, 125, 1210, 965);
		}

		// diagonals

		// main diagonal (top left to bottom right)
		if(row == 0 && column == 0) {
			line = new Line(585, 165, 1335, 915);	
		}
		// anti-diagonal (top right to bottom left)
		if(row == 0 && column == 2) {
			line = new Line(1335, 165, 585, 915);
		}

		line.setStrokeWidth(10); // thicker line
		// changes colour of the line depending on who won
		if(player == 'X') line.setStroke(Color.DODGERBLUE);
		if(player == 'O') line.setStroke(Color.INDIANRED);
		gameLayout.getChildren().add(line);
	}
	/**
	 * The main entry point for the application. It launches the JavaFX application.
	 * This method is called when the program is run, and it calls the "launch" method 
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
