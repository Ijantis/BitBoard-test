package main;

import board.ChessBoard;

public class GameLoop {

	public static void main(String[] args) {
		new GameLoop();
	}

	public GameLoop() {

		ChessBoard currentGameBoard = new ChessBoard();
		currentGameBoard.newGame();

	}
}
