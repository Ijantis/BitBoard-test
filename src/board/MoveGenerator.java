package board;

import java.util.Vector;

public class MoveGenerator {

	protected static void generateWhiteLegalMoves(String[][] currentBoard,
			long whitePawns, long whiteRooks, long whiteKnights,
			long whiteBishops, long whiteQueens, long whiteKing,
			long blackPieces, long whitePieces) {

		Vector<String[][]> possibleStates = new Vector<String[][]>(20, 20);
		printBitboard(whitePawns);

		while (whitePawns != 0) {

			long nextPawn;
			String nextPawnString = "1";

			for (int i = 0; i < Long.toBinaryString(whitePawns).length() - 1; i++) {
				nextPawnString += "0";
			}

			nextPawn = Long.parseLong(nextPawnString, 2);
			System.out.println("The next piece is: ");
			printBitboard(nextPawn);
			System.out.println("With possible moves: ");
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					whitePieces | blackPieces, blackPieces);
			printBitboard(bitboardOfMoves);

			possibleStates.addAll(generateNextMoves(nextPawn, bitboardOfMoves,
					copyCurrentBoard(currentBoard)));

			String whitePawnsString = Long.toBinaryString(whitePawns);
			whitePawns = Long.parseLong(whitePawnsString.substring(1), 2);
		}
		
		
		System.out.println(possibleStates.size());
		

	}

	private static Vector<String[][]> generateNextMoves(long nextPawn,
			long bitboardOfMoves, String[][] currentBoard) {

		System.out.println("generating possible states here");
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		while (bitboardOfMoves != 0) {
			String[][] tempBoard = copyCurrentBoard(currentBoard);
			long nextMove;
			String nextMoveString = "1";

			for (int i = 0; i < Long.toBinaryString(bitboardOfMoves).length() - 1; i++) {
				nextMoveString += "0";
			}
			nextMove = Long.parseLong(nextMoveString, 2);
			printBitboard(nextMove);

			String bitboardOfMovesString = Long.toBinaryString(bitboardOfMoves);
			bitboardOfMoves = Long.parseLong(
					bitboardOfMovesString.substring(1), 2);

			int fromSquare = Long.toBinaryString(nextPawn).length() - 1;
			int toSquare = Long.toBinaryString(nextMove).length() - 1;
			System.out.println("From square: " + fromSquare);
			System.out.println("To square: " + toSquare);

			System.out.println("x: " + (fromSquare % 8) + " y: "
					+ (fromSquare / 8));
			System.out
					.println("x: " + (toSquare % 8) + " y: " + (toSquare / 8));
			tempBoard[toSquare % 8][toSquare / 8] = "P";
			tempBoard[fromSquare % 8][fromSquare / 8] = " ";

			boolean moveIsValid = BoardManager.IsSelfCheck(
					tempBoard,
					tempBoard[toSquare % 8][toSquare / 8].toUpperCase().equals(
							tempBoard[toSquare % 8][toSquare / 8]));

			if (moveIsValid) {
				listOfMoves.add(tempBoard);
				printBoard(tempBoard);
			}
		}

		printBoard(currentBoard);
		return listOfMoves;
	}

	private static String[][] copyCurrentBoard(String[][] currentBoard) {
		String[][] temp = new String[currentBoard.length][currentBoard.length];

		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < temp.length; y++) {
				temp[x][y] = new String(currentBoard[x][y]);
			}
		}
		return temp;
	}

	private static void printBitboard(long bitBoard) {
		String stringBitBoard = Long.toBinaryString(bitBoard);
		System.out.println("Value : " + stringBitBoard);
		while (stringBitBoard.length() != 64) {
			stringBitBoard = "0" + stringBitBoard;
		}

		for (int i = 0; i < 8; i++) {
			StringBuilder stringReverser = new StringBuilder(
					stringBitBoard.substring(i * 8, ((i + 1) * 8)));
			stringReverser.reverse();
			for (int j = 0; j < stringReverser.toString().length(); j++) {
				System.out.print(stringReverser.toString().charAt(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public static void printBoard(String[][] board) {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < board.length; x++) {
				String temp = board[x][y];
				if (temp.equals(" ")) {
					System.out.print(", ");
				} else {
					System.out.print(board[x][y] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
