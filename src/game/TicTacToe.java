package game;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class TicTacToe extends Application{
	// 2d array for the tictactoe board
	private char[][] board = new char[3][3];
	private String gameMode;
	private Label statusLabel = new Label();
	private boolean player1Turn;
	GridPane gridPane = new GridPane();
	private BorderPane gameLayout = new BorderPane();
	private VBox buttonBox = new VBox(10);
	private int player1Score = 0;
	private int player2Score = 0;
	private int draws = 0;
	private Label player1ScoreLabel = new Label("Player 1: " + player1Score);
	private Label player2ScoreLabel = new Label("Player 2: " + player2Score);
	private Label drawsLabel = new Label("Draws:" + draws);
	private boolean gameOver;
	private MediaPlayer mediaPlayer;
	@Override
	public void start(Stage primaryStage) throws Exception {
		resetBoard();
		showMainMenu(primaryStage);



	}

	private void showMainMenu(Stage primaryStage) {
		Label titleLabel = new Label("Select Game Mode");
		Button singlePlayerButton = new Button("Single Player");
		Button twoPlayerButton = new Button("Two Player");
		singlePlayerButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px; -fx-pref-height: 100px;");
		twoPlayerButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-pref-width: 250px; -fx-pref-height: 100px;");
		titleLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: black;");


		try {
		Media media = new Media(new File("src/game/MenuMusic.mp3").toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		} catch(Exception e) {
			
		}
		// mediaPlayer.setAutoPlay(true);  
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
		// add a (cracked?)line doown the center to seperate easy and hard modes with different backgrounds for each (damjan sketch) NATHANS JOB
		// maybe add a unique background for each side (damjan sketch) maybe nathans job if he can figure it out

		Scene mainMenu = new Scene(screenLayout, 400, 300);
		primaryStage.setScene(mainMenu);

		primaryStage.setMaximized(false);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	private void startGame(Stage primaryStage) {
		// reset to player 1's turn
		player1Turn = true; 
		statusLabel.setText("Player 1's Turn (X)");
		gameOver = false;

		gridPane = new GridPane();
		gameLayout = new BorderPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setGridLinesVisible(true);
		gridPane.setStyle("-fx-font-size: 75px;"); // makes the text of the grid bigger (the X's and O's)
		statusLabel.setStyle("-fx-font-size: 25px;"); // makes the text of the statusLabel bigger
		for (int i = 0; i<3; i++) {
			for (int j = 0; j<3; j++) {
				Button square = new Button();
				square.setPrefSize(250, 250);
				int row = i;
				int column = j;

				square.setOnAction(e ->{
					if(gameMode.equals("twoPlayer")) {
						twoPlayerMove(square, row, column, primaryStage);
					}else {
						singlePlayerMove(square,row,column,primaryStage);
					}
				});
				gridPane.add(square, row, column);
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
		scoreText.setStyle("-fx-font-size: 35px; -fx-text-fill: black;");
		drawsLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: gray;");

		HBox scoreBox = new HBox(100, player1ScoreLabel, drawsLabel, player2ScoreLabel);
		scoreBox.setAlignment(Pos.TOP_CENTER);
		scoreText.setAlignment(Pos.TOP_CENTER);
		scoreBox.setStyle("-fx-font-size: 35px;");

		// this is to have the SCORE text along with player score aligned at the top
		VBox scoreLayout = new VBox(scoreText, scoreBox);
		scoreLayout.setAlignment(Pos.CENTER);
		VBox centerLayout = new VBox(gridPane, statusLabel);

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
				player1ScoreLabel.setText("Player 1: " + player1Score);
				gameOver = true;
				showRestart(primaryStage);
				return;  // exits the method early to not run the computers turn
			} else if (isBoardFull()) {
				statusLabel.setText("It's a Draw!");
				draws++;
				drawsLabel.setText("Draws: " + draws);
				gameOver = true;
				showRestart(primaryStage);
				return; // exits the method early to not run the computers turn
			}

			// computer's turn
			if (gameMode.equals("singlePlayerEasy")) {
				easyComputerMove(square, primaryStage);
			} else if (gameMode.equals("singlePlayerHard")) {
				// hardComputerMove(primaryStage);
			}
		}

	}


	private void easyComputerMove(Button square, Stage primaryStage) {
		if (!gameOver) {
			int row, col;
			// keeps randomly choosing a row and column until it finds an empty square 
			do {
				row = (int) (Math.random() * 3);
				col = (int) (Math.random() * 3);
			} while (board[row][col] != ' ');

			board[row][col] = 'O';
			square.setText("O");
			square.setStyle("-fx-text-fill: red;");
			// ISNT SWAPPING X's AND O's FIX
			if(checkWin('O')) {
				statusLabel.setText("Player 2 (Computer) Wins!");
				// updates score
				player1Score++;
				player1ScoreLabel.setText("Player 2 (Computer): " + player2Score);
				gameOver = true;
			} else if (isBoardFull()) {
				statusLabel.setText("It's a Draw!");
				draws++;
				drawsLabel.setText("Draws: " + draws);
				gameOver = true;
				showRestart(primaryStage);
			}  else {
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
					gameOver = true;
				} else {
					statusLabel.setText("Player 2 Wins!");
					// updates score
					player2Score++;
					player2ScoreLabel.setText("Player 2: " + player2Score);
					gameOver = true;
				}
				showRestart(primaryStage);
			} else if(isBoardFull()) {
				statusLabel.setText("It's a Draw!");
				draws++;
				drawsLabel.setText("Draws: " + draws);
				gameOver = true;
				showRestart(primaryStage);

			} else { // if there isnt any wins/draws continue and display whos turn it is
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
			if(board[0][i] == player && board[1][i] == player && board [2][i] == player ) {
				drawWinningLine(i,-1);
				return true;
			}
			// columns
			if(board[i][0] == player && board[i][1] == player && board [i][2] == player ) {
				drawWinningLine(-1,0);
				return true;
			}
			// 0,0  0,1

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
		buttonBox.getChildren().clear(); // makes sure it doesnt overflow if the method is called multiple times
		buttonBox.getChildren().addAll(mainMenuButton, restartButton, exitButton);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setSpacing(100); // add spacing between buttons
		buttonBox.setStyle("-fx-padding: 30px;"); // add spacing from the side

	}
	public void drawWinningLine(int row, int column) {



		if(row == 0) {
			Line line = new Line(500, 290, 1425, 290);
			line.setStrokeWidth(10);
			gameLayout.getChildren().add(line);			
		}
		if(row == 1) {
			Line line = new Line(00, 540, 10000, 540);
			line.setStrokeWidth(10);
			gameLayout.getChildren().add(line);			
		}
		if(row == 2) {
			Line line = new Line(00, 770, 10000, 790);
			line.setStrokeWidth(10);
			gameLayout.getChildren().add(line);			
		}

	}


	public static void main(String[] args) {
		launch(args);
	}
}
