package board;

import java.lang.reflect.Array;

public class ChessBoard {

	public static void main(String[] args) {

		new ChessBoard();

	}

	private long whitePawns, whiteRooks, whiteKnights, whiteBishops,
			whiteQueens, whiteKing;
	private long blackPawns, blackRooks, blackKnights, blackBishops,
			blackQueens, blackKing;
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
		long time = System.currentTimeMillis();
		long timeNano = System.nanoTime();

		newGame();
		clearChessBoard();
		currentBoard[7][0] = "K";
		currentBoard[6][0] = "R";
		currentBoard[3][0] = "q";
		updateBitboards();
		makeMove(6, 14);
		printBoard();

		System.out.println("That took :" + (System.currentTimeMillis() - time)
				+ "ms");
		System.out.println("That took :"
				+ ((System.nanoTime() - timeNano) / 1000) + " micro seconds");
	}

	public void newGame() {

		updateBitboards();

	}

	/*
	 * fromSquare and toSquare should be between 0 and 63 inclusive
	 */
	public boolean makeMove(long fromSquare, long toSquare) {

		int x = (int) (fromSquare % 8);
		int y = (int) (fromSquare / 8);

		System.out.println("Attempting to move a piece from (" + x + "," + y
				+ ")");

		long fromBitboard = BitboardOperations.getPositionBitboard(fromSquare);
		long toBitboard = BitboardOperations.getPositionBitboard(toSquare);
		String[][] tempBoard = copyCurrentBoard();

		// check to see if there is a piece there
		// check to see if the move is possible

		if (moveIsPossible(fromBitboard, fromSquare, toBitboard)) {
			System.out.println("Piece exists at (" + x + "," + y
					+ ") and move is possible to (" + (toSquare % 8) + ","
					+ (toSquare / 8) + ")");
			// moving the piece
			tempBoard[(int) toSquare % 8][(int) toSquare / 8] = tempBoard[(int) x][(int) y];
			tempBoard[x][y] = " ";

			// next check if tempBoard puts the same colour king in check and if
			// its in check then check for checkmate. Then check for any draws
			// etc.
			// if its all good then currentBoard = tempBoard;
			System.out.println("Current state");
			printBoard();
			boolean isValid;
			if (tempBoard[x][y].toUpperCase().equals(tempBoard[x][y])) {
				isValid = BoardChecker.IsSelfCheck(tempBoard, true);
			} else {
				isValid = BoardChecker.IsSelfCheck(tempBoard, false);
			}

			if (isValid) {
				currentBoard = tempBoard;
				return isValid;
			} else {
				return isValid;
			}
		}

		return false;
	}

	private boolean moveIsPossible(long fromBitboard, long fromSquare,
			long toBitboard) {

		// checks if a piece exists at that coordinate
		long pieceExists = fromBitboard & getOccupiedSquares();
		printBitboard(fromBitboard);
		printBitboard(getOccupiedSquares());
		// System.out.println("piece exists:");
		// printBitboard(pieceExists);
		// checks to see if a move exists
		long moveExists = generatePieceMoves(fromSquare, fromBitboard)
				& toBitboard;
		// System.out.println("move exists");
		// printBitboard(moveExists);

		return pieceExists != 0 && moveExists != 0;
	}

	/*
	 * Returns a bitboard of the generated moves for a particular coordinate
	 */
	private long generatePieceMoves(long fromSquare, long fromBitboard) {
		switch (currentBoard[(int) fromSquare % 8][(int) fromSquare / 8]) {
		case "P":
			return WhitePieces.getPawnMoves(fromBitboard, getOccupiedSquares(),
					getBlackPieces());
		case "R":
			return WhitePieces.getRookMoves(fromBitboard, getOccupiedSquares(),
					getWhitePieces());
		case "N":
			return WhitePieces.getKnightMoves(fromBitboard, getWhitePieces());
		case "B":
			return WhitePieces.getBishopMoves(fromBitboard,
					getOccupiedSquares(), getWhitePieces());
		case "Q":
			return WhitePieces.getQueenMoves(fromBitboard,
					getOccupiedSquares(), getWhitePieces());
		case "K":
			return WhitePieces.getKingMoves(fromBitboard, getWhitePieces(),
					getBlackAttackingSquares());
		case "p":
			return BlackPieces.getPawnMoves(fromBitboard, getOccupiedSquares(),
					getWhitePieces());
		case "r":
			return BlackPieces.getRookMoves(fromBitboard, getOccupiedSquares(),
					getBlackPieces());
		case "n":
			return BlackPieces.getKnightMoves(fromSquare, getBlackPieces());
		case "b":
			return BlackPieces.getBishopMoves(fromBitboard,
					getOccupiedSquares(), getBlackPieces());
		case "q":
			return BlackPieces.getQueenMoves(fromBitboard,
					getOccupiedSquares(), getBlackPieces());
		case "k":
			return BlackPieces.getKingMoves(fromBitboard, getBlackPieces(),
					getWhiteAttackingSquares());
		}
		return 0L;
	}

	private boolean isWhiteKingInCheck() {

		long blackMoves = getBlackAttackingSquares();

		if ((blackMoves & whiteKing) == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isBlackKingInCheck() {

		long whiteMoves = getWhiteAttackingSquares();

		if ((whiteMoves & blackKing) == 0) {
			return false;
		} else {
			return true;
		}
	}

	// move this method to WhitePieces.java
	private long getWhiteAttackingSquares() {
		long attackedSquares = 0L;

		attackedSquares = attackedSquares
				| WhitePieces.getPawnAttackingSquares(whitePawns)
				| WhitePieces.getRookAttackingSquares(whiteRooks,
						getOccupiedSquares())
				| WhitePieces.getKnightAttackingSquares(whiteKnights)
				| WhitePieces.getBishopAttackingSquares(whiteBishops,
						getOccupiedSquares())
				| WhitePieces.getQueenAttackingSquares(whiteQueens,
						getOccupiedSquares())
				| WhitePieces.getKingAttackingSquares(whiteKing,
						getWhitePieces());

		return attackedSquares;
	}

	// move this method to BlackPieces.java
	private long getBlackAttackingSquares() {

		long attackedSquares = 0L;

		attackedSquares = attackedSquares
				| BlackPieces.getPawnAttackingSquares(blackPawns)
				| BlackPieces.getRookAttackingSquares(blackRooks,
						getOccupiedSquares())
				| BlackPieces.getKnightAttackingSquares(blackKnights)
				| BlackPieces.getBishopAttackingSquares(blackBishops,
						getOccupiedSquares())
				| BlackPieces.getQueenAttackingSquares(blackQueens,
						getOccupiedSquares())
				| BlackPieces.getKingAttackingSquares(blackKing,
						getBlackPieces());

		return attackedSquares;
	}

	private void clearChessBoard() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = " ";
			}
		}
	}

	private void updateBitboards() {
		resetBitboards();
		String binaryLong;
		int currentIndex;
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				// 64 bits and currentIndex is the square being looked at
				binaryLong = "0000000000000000000000000000000000000000000000000000000000000000";
				currentIndex = (y * 8) + x;
				binaryLong = binaryLong.substring(currentIndex,
						binaryLong.length() - 1)
						+ "1" + binaryLong.substring(0, currentIndex);
				switch (currentBoard[x][y]) {
				case "P":
					whitePawns += convertStringToBinary(binaryLong);
					break;
				case "R":
					whiteRooks += convertStringToBinary(binaryLong);
					break;
				case "N":
					whiteKnights += convertStringToBinary(binaryLong);
					break;
				case "B":
					whiteBishops += convertStringToBinary(binaryLong);
					break;
				case "Q":
					whiteQueens += convertStringToBinary(binaryLong);
					break;
				case "K":
					whiteKing += convertStringToBinary(binaryLong);
					break;
				case "p":
					blackPawns += convertStringToBinary(binaryLong);
					break;
				case "r":
					blackRooks += convertStringToBinary(binaryLong);
					break;
				case "n":
					blackKnights += convertStringToBinary(binaryLong);
					break;
				case "b":
					blackBishops += convertStringToBinary(binaryLong);
					break;
				case "q":
					blackQueens += convertStringToBinary(binaryLong);
					break;
				case "k":
					blackKing += convertStringToBinary(binaryLong);
					break;
				default:
					break;
				}
			}
		}
	}

	private void resetBitboards() {
		whitePawns = 0L;
		whiteRooks = 0L;
		whiteKnights = 0L;
		whiteBishops = 0L;
		whiteQueens = 0L;
		whiteKing = 0L;
		blackPawns = 0L;
		blackRooks = 0L;
		blackKnights = 0L;
		blackBishops = 0L;
		blackQueens = 0L;
		blackKing = 0L;
	}

	private long convertStringToBinary(String binaryLong) {
		if (binaryLong.charAt(0) == '0') {
			return Long.parseLong(binaryLong, 2);
		} else {
			return Long.parseLong("1" + binaryLong.substring(2), 2) * 2;
		}
	}

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public void printBoard(String[][] board) {
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

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public void printBoard() {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < currentBoard.length; x++) {
				String temp = currentBoard[x][y];
				if (temp.equals(" ")) {
					System.out.print(", ");
				} else {
					System.out.print(currentBoard[x][y] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private long getWhitePieces() {
		return (whiteBishops | whiteKing | whiteKnights | whitePawns
				| whiteQueens | whiteRooks);
	}

	private long getBlackPieces() {
		return (blackBishops | blackKing | blackKnights | blackPawns
				| blackQueens | blackRooks);
	}

	private long getOccupiedSquares() {
		return (getWhitePieces() | getBlackPieces());
	}

	private void printBitboard(long bitBoard) {
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

	private String[][] copyCurrentBoard() {
		String[][] temp = new String[currentBoard.length][currentBoard.length];

		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < temp.length; y++) {
				temp[x][y] = new String(currentBoard[x][y]);
			}
		}
		return temp;
	}
}
