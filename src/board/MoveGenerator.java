package board;

import java.util.Vector;

public class MoveGenerator {

	public static int count = 0;

	/*
	 * NOTE: Do not return a Vector<String[][]> return squares numbers instead.
	 */
	protected static Vector<String[][]> generateWhiteLegalMoves(
			String[][] currentBoard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPieces, long whitePieces,
			long blackAttackingSquares) {

		Vector<String[][]> possibleStates = new Vector<String[][]>(20, 20);

		while (whitePawns != 0) {

			long nextPawn = Long.highestOneBit(whitePawns);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					whitePieces | blackPieces, blackPieces);

			possibleStates.addAll(generateNextMoves(nextPawn, bitboardOfMoves,
					copyCurrentBoard(currentBoard)));

			whitePawns = (Long.highestOneBit(whitePawns) - 1) & whitePawns;
		}

		while (whiteKnights != 0) {

			long nextKnight = Long.highestOneBit(whiteKnights);
			long bitboardOfMoves = WhitePieces.getKnightMoves(nextKnight,
					whitePieces);

			possibleStates.addAll(generateNextMoves(nextKnight,
					bitboardOfMoves, copyCurrentBoard(currentBoard)));

			whiteKnights = (Long.highestOneBit(whiteKnights) - 1)
					& whiteKnights;
		}

		while (whiteBishops != 0) {
			long nextBishop = Long.highestOneBit(whiteBishops);
			long bitboardOfMoves = WhitePieces.getBishopMoves(nextBishop,
					whitePieces | blackPieces, whitePieces);

			possibleStates.addAll(generateNextMoves(nextBishop,
					bitboardOfMoves, copyCurrentBoard(currentBoard)));

			whiteBishops = (Long.highestOneBit(whiteBishops) - 1)
					& whiteBishops;
		}

		while (whiteQueens != 0) {
			long nextQueen = Long.highestOneBit(whiteQueens);
			long bitboardOfMoves = WhitePieces.getQueenMoves(whiteQueens,
					whitePieces | blackPieces, whitePieces);

			possibleStates.addAll(generateNextMoves(nextQueen, bitboardOfMoves,
					copyCurrentBoard(currentBoard)));

			whiteQueens = (Long.highestOneBit(whiteQueens) - 1) & whiteQueens;
		}

		while (whiteRooks != 0) {
			long nextRook = Long.highestOneBit(whiteRooks);
			long bitboardOfMoves = WhitePieces.getQueenMoves(nextRook,
					whitePieces | blackPieces, whitePieces);
			// printBitboard(bitboardOfMoves);

			possibleStates.addAll(generateNextMoves(nextRook, bitboardOfMoves,
					copyCurrentBoard(currentBoard)));

			whiteRooks = (Long.highestOneBit(whiteRooks) - 1) & whiteRooks;
		}

		long kingMovesBitboard = WhitePieces.getKingMoves(whiteKing,
				whitePieces, blackAttackingSquares);
		possibleStates.addAll(generateNextMoves(whiteKing, kingMovesBitboard,
				currentBoard));

		if (possibleStates.isEmpty()) {
			System.out.println("Checkmate!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			printBoard(currentBoard);
		}
		return possibleStates;
	}

	protected static Vector<String[][]> generateBlackLegalMoves(
			String[][] currentBoard, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing, long whitePieces, long blackPieces,
			long whiteAttackingSquares) {

		Vector<String[][]> possibleStates = new Vector<String[][]>(20, 20);

		while (blackPawns != 0) {
			long nextPawn = Long.highestOneBit(blackPawns);
			long bitboardOfMoves = BlackPieces.getPawnMoves(nextPawn,
					blackPieces | whitePieces, whitePieces);

			possibleStates.addAll(generateNextMoves(nextPawn, bitboardOfMoves,
					copyCurrentBoard(currentBoard)));

			blackPawns = (Long.highestOneBit(blackPawns) - 1) & blackPawns;
		}

		while (blackKnights != 0) {

			long nextKnight = Long.highestOneBit(blackKnights);
			long bitboardOfMoves = BlackPieces.getKnightMoves(nextKnight,
					blackPieces);

			possibleStates.addAll(generateNextMoves(nextKnight,
					bitboardOfMoves, copyCurrentBoard(currentBoard)));

			blackKnights = (Long.highestOneBit(blackKnights) - 1)
					& blackKnights;
		}

		while (blackBishops != 0) {
			long nextBishop = Long.highestOneBit(blackBishops);
			long bitboardOfMoves = BlackPieces.getBishopMoves(nextBishop,
					blackPieces | whitePieces, blackPieces);

			possibleStates.addAll(generateNextMoves(nextBishop,
					bitboardOfMoves, copyCurrentBoard(currentBoard)));

			blackBishops = (Long.highestOneBit(blackBishops) - 1)
					& blackBishops;
		}

		while (blackQueens != 0) {
			long nextQueen = Long.highestOneBit(blackQueens);
			long bitboardOfMoves = BlackPieces.getQueenMoves(blackQueens,
					blackPieces | whitePieces, blackPieces);

			possibleStates.addAll(generateNextMoves(nextQueen, bitboardOfMoves,
					copyCurrentBoard(currentBoard)));

			blackQueens = (Long.highestOneBit(blackQueens) - 1) & blackQueens;
		}

		while (blackRooks != 0) {
			long nextRook = Long.highestOneBit(blackRooks);
			long bitboardOfMoves = BlackPieces.getRookMoves(nextRook,
					blackPieces | whitePieces, blackPieces);

			possibleStates.addAll(generateNextMoves(nextRook, bitboardOfMoves,
					copyCurrentBoard(currentBoard)));

			blackRooks = (Long.highestOneBit(blackRooks) - 1) & blackRooks;
		}

		long kingMovesBitboard = BlackPieces.getKingMoves(blackKing,
				blackPieces, whiteAttackingSquares);
		possibleStates.addAll(generateNextMoves(blackKing, kingMovesBitboard,
				currentBoard));

		if (possibleStates.isEmpty()) {
			System.out.println("Checkmate!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			printBoard(currentBoard);
		}
		return possibleStates;

	}

	/**
	 * Calculates the possible legal moves from a certain piece given a current
	 * board state, the bitboard of the piece to be moved and the bitboard of
	 * potential moves for that piece.
	 * 
	 * @param nextPieceBitboard
	 *            - The bitboard of the next piece to be moved.
	 * @param bitboardOfMoves
	 *            - The bitboard of every possible move which may or not be
	 *            valid.
	 * @param currentBoard
	 *            - The current board state
	 * @return - The list of valid moves.
	 */
	private static Vector<String[][]> generateNextMoves(long nextPieceBitboard,
			long bitboardOfMoves, String[][] currentBoard) {
		// System.out.println("generating possible states here");
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// working through each bit
		while (bitboardOfMoves != 0) {
			String[][] tempBoard = copyCurrentBoard(currentBoard);
			long nextMove = Long.highestOneBit(bitboardOfMoves);
			// printBitboard(nextMove);

			// removing the leftmost 1 from the possible moves
			String bitboardOfMovesString = Long.toBinaryString(bitboardOfMoves);
			if (bitboardOfMovesString.length() == 1) {
				break;
			}
			bitboardOfMoves = Long.parseLong(
					bitboardOfMovesString.substring(1), 2);

			int fromSquare = Long.toBinaryString(nextPieceBitboard).length() - 1;
			int toSquare = Long.toBinaryString(nextMove).length() - 1;
			// System.out.println("From square: " + fromSquare);
			// System.out.println("To square: " + toSquare);

			// System.out.println("x: " + (fromSquare % 8) + " y: "
			// + (fromSquare / 8));
			// System.out
			// .println("x: " + (toSquare % 8) + " y: " + (toSquare / 8));
			tempBoard[toSquare % 8][toSquare / 8] = tempBoard[fromSquare % 8][fromSquare / 8];
			tempBoard[fromSquare % 8][fromSquare / 8] = " ";

			boolean moveIsValid = BoardManager.IsSelfCheck(
					tempBoard,
					tempBoard[toSquare % 8][toSquare / 8].toUpperCase().equals(
							tempBoard[toSquare % 8][toSquare / 8]));

			if (moveIsValid) {
				listOfMoves.add(tempBoard);
				// printBoard(tempBoard);
			}
		}

		// printBoard(currentBoard);
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

	public static void printCount() {
		System.out.println(count);
	}
}
