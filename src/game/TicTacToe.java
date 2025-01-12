package game;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicTacToe extends Application{
	// 2d array for the tictactoe board
	private char[][] board = new char[3][3];
	private String gameMode;
	

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
        primaryStage.setMaximized(true);
        primaryStage.show();
		
	}
	
	
	
	private void startGame(Stage primaryStage) {
		
		
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
	
	
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
