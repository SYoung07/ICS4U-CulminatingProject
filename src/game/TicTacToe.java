package game;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicTacToe extends Application{
	// 2d array for the tictactoe board
	private char[][] board = new char[3][3];
	private String gameMode;
	private Label statusLabel = new Label();
	Scene gameScene;
	private boolean player1Turn;
	GridPane gridPane = new GridPane();

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
		VBox mainLayout = new VBox(gridPane, statusLabel);
		mainLayout.setAlignment(Pos.CENTER);
		gameScene = new Scene(mainLayout, 400, 300);
		primaryStage.setScene(gameScene);
		primaryStage.setMaximized(false);
		primaryStage.setMaximized(true);
		

	}
	
	public void twoPlayerMove(Button square, int row, int column, Stage primaryStage) {
		// if the square is empty
		if(board[row][column] == ' ') {
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
			square.setText(String.valueOf(currentPlayer)); // string.valueof converts the char to a string because 
			
			if(checkWin(currentPlayer)) {
				if(player1Turn) {
					statusLabel.setText("Player 1 Wins!");
				} else {
					statusLabel.setText("Player 2 Wins!");
				}
				showRestart(primaryStage);
			} else if(isBoardFull()) {
				statusLabel.setText("It's a Draw!");
				showRestart(primaryStage);
			
			} else { // if there isnt any wins/draws continue and display whos turn it is
				player1Turn =! player1Turn; // flips the turn from player1 to not player 1(player 2)
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
		
		mainMenuButton.setOnAction(e ->{
			resetBoard();
			showMainMenu(primaryStage);
		});
		
		restartButton.setOnAction(e ->{
			resetBoard();
			startGame(primaryStage);
		});
		
		
		exitButton.setOnAction(e ->{
			primaryStage.close();
		});
		VBox layout = new VBox(10, mainMenuButton, restartButton, exitButton);
		gameScene.setRoot(layout);
		primaryStage.setScene(gameScene);
	}
	
	
	public static void scoreCounter () {
		Label 
	}
	



	public static void main(String[] args) {
		launch(args);
	}
}
