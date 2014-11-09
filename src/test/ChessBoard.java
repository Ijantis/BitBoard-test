package test;

import java.util.function.BinaryOperator;

public class ChessBoard {

	public static void main(String[] args) {

		new ChessBoard();
	}

	private long whitePawns = 0L, whiteRooks = 0L, whiteKnights = 0L,
			whiteBishops = 0L, whiteQueen = 0L, whiteKing = 0L;
	private long blackPawns = 0L, blackRooks = 0L, blackKnights = 0L,
			blackBishops = 0L, blackQueen = 0L, blackKing = 0L;
	// Upper case for WHITE
	// Lower case for BLACK
	// 0,0 is top left 0,7 is top right 7,7 bottom right
	private String[][] currentBoard = {
			{ "R", "P", " ", " ", " ", " ", "p", "r" },
			{ "N", "P", " ", " ", " ", " ", "p", "n" },
			{ "B", "P", " ", " ", " ", " ", "p", "b" },
			{ "Q", "P", " ", " ", " ", " ", "p", "q" },
			{ "K", "P", " ", " ", " ", " ", "p", "k" },
			{ "B", "P", " ", " ", " ", " ", "p", "b" },
			{ "N", "P", " ", " ", " ", " ", "p", "n" },
			{ "R", "P", " ", " ", " ", " ", "p", "r" } };;

	public ChessBoard() {

		initialiseBoard();

		// printBoard();
	}

	private void initialiseBoard() {

		String binaryLong;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				binaryLong = "0000000000000000000000000000000000000000000000000000000000000000";

			}
		}
	}

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public void printBoard() {

		System.out.println();
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < currentBoard.length; x++) {
				String temp = currentBoard[x][y];
				if (temp.equals(" ")) {
					System.out.print(",");
				} else {
					System.out.print(currentBoard[x][y]);
				}
			}
			System.out.println();
		}
	}
}
