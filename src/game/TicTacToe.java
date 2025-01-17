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


public class TicTacToe extends Application{
	// 2d array for the tictactoe board
	private char[][] board = new char[3][3];
	private Button[][] buttons = new Button[3][3]; // this is for storing the buttons so the ai can do the right moves visually
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
	private MediaPlayer mediaPlayer; // all music
	VBox centerLayout;
	@Override
	public void start(Stage primaryStage) throws Exception {
		showMainMenu(primaryStage);
		
	}

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
		primaryStage.setTitle("Main Menu");

		singlePlayerButton.setOnAction(e -> {
			showDifficultySelector(primaryStage);
		});
		twoPlayerButton.setOnAction(e -> {
			gameMode = "twoPlayer";
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



	private void showDifficultySelector(Stage primaryStage) {
		// create labels and buttons
		Label titleLabel = new Label("Choose the Difficulty");
		Button easyModeButton = new Button("Easy");
		Button hardModeButton = new Button("Hard");

		easyModeButton.setOnAction(e -> {
			gameMode = "singlePlayerEasy";
			startGame(primaryStage);
		});
		hardModeButton.setOnAction(e -> {
			gameMode = "singlePlayerHard";
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
		// add a (cracked?)line down the center to separate easy and hard modes with different backgrounds for each (damjan sketch) NATHANS JOB
		// maybe add a unique background for each side (damjan sketch) maybe nathans job if he can figure it out

		Scene mainMenu = new Scene(screenLayout, 400, 300);
		primaryStage.setScene(mainMenu);

		primaryStage.setMaximized(false);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

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
		gridPane.setStyle("-fx-font-size: 75px;"); // makes the text of the grid bigger (the X's and O's)
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
				buttons[row][column] = square; // for the AI to make the right move visually
				gridPane.add(square, column, row); // gridpane.add uses (column, row) instead of the usual (row, column)
				//				gridPane.add(square, row, column);


				try {
					square.setOnAction(e ->{
						if(gameMode.equals("twoPlayer")) {
							twoPlayerMove(square, row, column, primaryStage);
							// for testing, prints the row and column of the button clicked
							System.out.println(gridPane.getRowIndex(square) + " " + gridPane.getColumnIndex(square));

							Media media = new Media(new File("src/game/CartoonClickSound.mp3").toURI().toString());
							mediaPlayer = new MediaPlayer(media);
							mediaPlayer.play();
						}else {
							singlePlayerMove(square,row,column,primaryStage);
							// for testing, prints the row and column of the button clicked
							System.out.println(gridPane.getRowIndex(square) + " " + gridPane.getColumnIndex(square));

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

	public void singlePlayerMove(Button square, int row, int column, Stage primaryStage) {	

		if (board[row][column] == ' ' && !gameOver) {

			board[row][column] = 'X'; // player move is always X
			square.setText("X");
			square.setStyle("-fx-text-fill: blue;");

			if (checkWin('X')) {
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
			} else if (isBoardFull()) {
				statusLabel.setText("It's a Draw!");
				draws++;
				drawsLabel.setText(String.format("%11s %d", "Draws:", draws));
				gameOver = true;
				showRestart(primaryStage);
				return; // exits the method early to not run the computers turn
			}

			// computer's turn
			if (gameMode.equals("singlePlayerEasy")) {			
				easyComputerMove(primaryStage);
			} else if (gameMode.equals("singlePlayerHard")) {
				// hardComputerMove(primaryStage);
			}
		}
	}


	private void easyComputerMove(Stage primaryStage) {
		if (!gameOver) {
			int row, col;
			// keeps randomly choosing a row and column until it finds an empty square 
			do {
				row = (int) (Math.random() * 3);
				col = (int) (Math.random() * 3);
			} while (board[row][col] != ' '); // keeps searching until an empty square is found
			// test case


			// place the computer's move ('O') in the selected square
			board[row][col] = 'O';
			// the gridpane buttons are initialized by a nested for loop where it starts at top left (0) and moves right until the top right (2) and then goes down a row to the middle left (3)
			// the line below this accounts for the way the gridpane buttons were added and creates a new button that we can use to edit the moves. formerly i was passing the button in as a parameter
			// but it didn't work and this ended up being better
			Button square = buttons[row][col];

			// test case
			System.out.println("AI Move: Row " + row + ", Column " + col);
			System.out.println("Button: " + square);


			square.setText("O");
			square.setStyle("-fx-text-fill: red;");

			// check if the computer wins after making the move
			if (checkWin('O')) {
				statusLabel.setText("Computer Wins!");
				player2Score++;
				player2ScoreLabel.setText(String.format("%11s %d", "Computer:", player2Score));
				gameOver = true;
				showRestart(primaryStage);
			} else if (isBoardFull()) {
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

			if(checkWin(currentPlayer)) {
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
			} else if(isBoardFull()) {
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

	public boolean checkWin(char player) {

		for (int i = 0; i<3; i++) {

			// rows
			if(board[i][0] == player && board[i][1] == player && board [i][2] == player ) {
				drawWinningLine(i,-1, player);
				return true;
			}
			// columns
			if(board[0][i] == player && board[1][i] == player && board [2][i] == player ) {
				drawWinningLine(-1,i, player);
				return true;
			}



			// diagonal
			if(board[0][0] == player && board[1][1] == player && board[2][2] == player) {
				return true;
			}
			if(board[0][2] == player && board[1][1] == player && board[2][0] == player) {
				return true;
			}
		}
		return false;
	}
	public boolean isBoardFull() {
		// checks the entire board for any empty square, returns false if it finds one
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j<3; j++) {
				if(board[i][j] == ' ') {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Resets the board back to the starting position
	 */
	public void resetBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j<3; j++) {
				board[i][j] = ' ';
			}
		}
	}
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


		line.setStrokeWidth(10);
		if(player == 'X') line.setStroke(Color.BLUE);
		if(player == 'O') line.setStroke(Color.RED);
		gameLayout.getChildren().add(line);
		line.toBack();
		gameLayout.toFront();


	}

	public static void main(String[] args) {
		launch(args);
	}
}
