package game;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
	private Label player1ScoreLabel = new Label("Player 1: " + player1Score);
	private Label player2ScoreLabel = new Label("Player 2: " + player2Score);
	private boolean gameOver;
	@Override
	public void start(Stage primaryStage) throws Exception {
		resetBoard();
		showMainMenu(primaryStage);


		
	}

	private void showMainMenu(Stage primaryStage) {
		Label titleLabel = new Label("Select Game Mode");
		Button singlePlayerButton = new Button("Single Player");
		Button twoPlayerButton = new Button("Two Player");

		singlePlayerButton.setOnAction(e -> {
			gameMode = "singlePlayer";
			// choice between easy and hard. MAKE A NEW METHOD LATER
		});
		twoPlayerButton.setOnAction(e -> {
			gameMode = "twoPlayer";
			startGame(primaryStage);
		});
		VBox modeSelection = new VBox(titleLabel, singlePlayerButton,twoPlayerButton);
		modeSelection.setAlignment(Pos.CENTER);
		Scene mainMenu = new Scene(modeSelection, 400, 300);
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
						// single player move
					}
				});
				gridPane.add(square, row, column);

			}

		}

		// reserve space for the button box on the left to prevent it from moving the board when shown later
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setStyle("-fx-padding: 10;"); // spacing from the wall
		buttonBox.setPrefWidth(200); // reserve space on the left
		buttonBox.getChildren().clear(); // empty the left side 

		// this allows the board to be in the exact center of the screen since there is now a 200 px reserve on both sides from the buttonBox and this spacer
		VBox spacer = new VBox(10);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		spacer.setPrefWidth(200);
		spacer.setStyle("-fx-padding: 10;"); // spacing from the wall
		spacer.getChildren().clear(); // empty the right side 
		// Add score labels at the top
		Label scoreText = new Label("SCORE");
		player1ScoreLabel.setStyle("-fx-font-size: 25px;");
		player2ScoreLabel.setStyle("-fx-font-size: 25px;");
		scoreText.setStyle("-fx-font-size: 25px;");
		HBox scoreBox = new HBox(10, player1ScoreLabel, scoreText, player2ScoreLabel);
		scoreBox.setAlignment(Pos.TOP_CENTER);
		scoreBox.setStyle("-fx-padding: 10; -fx-font-size: 20px;");


		VBox centerLayout = new VBox(scoreBox, gridPane, statusLabel);

		centerLayout.setAlignment(Pos.CENTER);

		gameLayout.setTop(scoreBox);
		gameLayout.setCenter(centerLayout);
		gameLayout.setLeft(buttonBox);
		gameLayout.setRight(spacer);
		Scene gameScene = new Scene(gameLayout);

		primaryStage.setScene(gameScene);
		primaryStage.setMaximized(false);
		primaryStage.setMaximized(true);


	}

	public void twoPlayerMove(Button square, int row, int column, Stage primaryStage) {
		// if the square is empty and the game isn't over
		if(board[row][column] == ' ' && !gameOver) {
			char currentPlayer;
			// if its player 1's turn
			if(player1Turn) {
				currentPlayer = 'X';
			} else { // player 2's turn
				currentPlayer = 'O';
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
			if(board[i][0] == player && board[i][1] == player && board [i][2] == player ) return true;
			// columns
			if(board[0][i] == player && board[1][i] == player && board [2][i] == player ) return true;
		}
		// diagonal
		if(board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
		if(board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;

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
		mainMenuButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");
		restartButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");
		exitButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");

		// add buttons to the left side
		buttonBox.getChildren().clear(); // makes sure it doesnt overflow if the method is called multiple times
		buttonBox.getChildren().addAll(mainMenuButton, restartButton, exitButton);
		buttonBox.setAlignment(Pos.TOP_CENTER);
		buttonBox.setSpacing(20); // add spacing between buttons
		buttonBox.setStyle("-fx-padding: 20px;"); // add spacing from the side

	}





	public static void main(String[] args) {
		launch(args);
	}
}
