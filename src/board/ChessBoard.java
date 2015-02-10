package board;

import java.util.Vector;

import ai.evaluation.Evaluator;

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
	// private char[][] currentBoard = {
	// { 'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r' },
	// { 'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n' },
	// { 'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b' },
	// { 'Q', 'P', ' ', ' ', ' ', ' ', 'p', 'q' },
	// { 'K', 'P', ' ', ' ', ' ', ' ', 'p', 'k' },
	// { 'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b' },
	// { 'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n' },
	// { 'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r' } };;

	private char[][] currentBoard = {
			{ 'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r' },
			{ ' ', 'P', ' ', ' ', ' ', ' ', 'p', 'n' },
			{ ' ', 'P', 'N', ' ', ' ', ' ', 'p', 'b' },
			{ 'Q', ' ', 'B', 'P', ' ', ' ', 'p', 'q' },
			{ 'K', ' ', 'B', 'P', ' ', ' ', 'p', 'k' },
			{ ' ', 'P', 'N', ' ', ' ', ' ', 'p', 'b' },
			{ ' ', 'P', ' ', ' ', ' ', ' ', 'p', 'n' },
			{ 'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r' } };;

	public ChessBoard() {
		long time = System.currentTimeMillis();
		long timeNano = System.nanoTime();

		updateBitboards();
		printBoard();

		System.out.println(evaluatePosition());
		
		testMethod();

		System.out.println("That took :" + (System.currentTimeMillis() - time)
				+ "ms");
		System.out.println("That took :"
				+ ((System.nanoTime() - timeNano) / 1000) + " micro seconds");

	}

	private double evaluatePosition() {
		return Evaluator.evaluatePosition(whitePawns, whiteRooks, whiteKnights,
				whiteBishops, whiteQueens, whiteKing, blackPawns, blackRooks,
				blackKnights, blackBishops, blackQueens, blackKing,
				currentBoard, getWhiteAttackingSquares(),
				getBlackAttackingSquares());
	}

	private Vector<char[][]> generateWhiteLegalMoves() {

		return MoveGenerator.generateWhiteLegalMoves(currentBoard, whitePawns,
				whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
				getBlackPieces(), getWhitePieces(), getBlackAttackingSquares(),
				blackPawns, blackRooks, blackKnights, blackBishops,
				blackQueens, blackKing);
	}

	private Vector<char[][]> generateBlackLegalMoves() {
		return MoveGenerator.generateBlackLegalMoves(currentBoard, whitePawns,
				whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
				getBlackPieces(), getWhitePieces(), getWhiteAttackingSquares(),
				blackPawns, blackRooks, blackKnights, blackBishops,
				blackQueens, blackKing);
	}

	private boolean areEqual(String[][] board1, String[][] board2) {

		for (int i = 0; i < board2.length; i++) {
			if (!board1[i % 8][i / 8].equals(board2[i % 8][i / 8])) {
				System.out.println("Comparing");
				System.out.println(board1[i % 8][i / 8] + " ");
				System.out.print(board2[i % 8][i / 8]);
				return false;
			}
		}
		printBoard(board1);
		printBoard(board2);
		return true;

	}

	public void newGame() {

		currentBoard[0][0] = 'R';
		currentBoard[1][0] = 'N';
		currentBoard[2][0] = 'B';
		currentBoard[3][0] = 'Q';
		currentBoard[4][0] = 'K';
		currentBoard[5][0] = 'B';
		currentBoard[6][0] = 'N';
		currentBoard[7][0] = 'R';

		currentBoard[0][7] = 'r';
		currentBoard[1][7] = 'n';
		currentBoard[2][7] = 'b';
		currentBoard[3][7] = 'q';
		currentBoard[4][7] = 'k';
		currentBoard[5][7] = 'b';
		currentBoard[6][7] = 'n';
		currentBoard[7][7] = 'r';

		for (int x = 0; x < 7; x++) {
			currentBoard[x][1] = 'P';
		}

		for (int x = 0; x < 7; x++) {
			currentBoard[x][6] = 'p';
		}

		for (int y = 2; y < 6; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = ' ';
			}
		}

		updateBitboards();

	}

	/*
	 * fromSquare and toSquare should be between 0 and 63 inclusive
	 */
	public boolean makeMove(long fromSquare, long toSquare) {

		int x = (int) (fromSquare % 8);
		int y = (int) (fromSquare / 8);

		long fromBitboard = BitboardOperations.getPositionBitboard(fromSquare);
		long toBitboard = BitboardOperations.getPositionBitboard(toSquare);
		char[][] tempBoard = copyCurrentBoard();

		// check to see if a piece exists at the from coordinate
		// check to see if the piece has a move possible
		if (moveIsPossible(fromBitboard, fromSquare, toBitboard)) {

			// move the piece on the temporary board
			tempBoard[(int) toSquare % 8][(int) toSquare / 8] = tempBoard[(int) x][(int) y];
			tempBoard[x][y] = ' ';

			// next check if tempBoard puts the same colour king in check and
			// if its all good then currentBoard = tempBoard;
			boolean isValid;
			if (Character.isUpperCase(tempBoard[x][y])) {
				isValid = BoardManager.IsSelfCheck(tempBoard, true);
			} else {
				isValid = BoardManager.IsSelfCheck(tempBoard, false);
			}

			return isValid;
			// TODO: Add en passant check.
			// if (isValid) {
			// currentBoard = tempBoard;
			// updateBitboards();
			// return true;
			// // return isValid;
			// } else {
			// return false;
			// // return isValid;
			// }
		}

		return false;
	}

	/**
	 * 
	 * Checks to see if a move is possible by seeing if the piece exists and if
	 * one of its possible moves exists at the coordinate stated.
	 * 
	 * @param fromBitboard
	 *            - The bitboard of the piece
	 * @param fromSquare
	 *            - The coordinate of the piece being moved
	 * @param toBitboard
	 *            - The bitboard of the destination square
	 * @return
	 */
	private boolean moveIsPossible(long fromBitboard, long fromSquare,
			long toBitboard) {

		// checks if a piece exists at that coordinate
		long pieceExists = fromBitboard & getOccupiedSquares();
		// checks to see if a move exists
		long moveExists = generatePieceMoves(fromSquare, fromBitboard)
				& toBitboard;

		return pieceExists != 0 && moveExists != 0;
	}

	/*
	 * Returns a bitboard of the generated moves for a single coordinate
	 */
	/**
	 * 
	 * @param fromSquare
	 *            - The coordinate of the piece
	 * @param fromBitboard
	 *            - The bitboard of the piece
	 * @return - The bitboard of all possible moves
	 */
	private long generatePieceMoves(long fromSquare, long fromBitboard) {
		switch (currentBoard[(int) fromSquare % 8][(int) fromSquare / 8]) {
		case 'P':
			return WhitePieces.getPawnMoves(fromBitboard, getOccupiedSquares(),
					getBlackPieces());
		case 'R':
			return WhitePieces.getRookMoves(fromBitboard, getOccupiedSquares(),
					getWhitePieces());
		case 'N':
			return WhitePieces.getKnightMoves(fromBitboard, getWhitePieces());
		case 'B':
			return WhitePieces.getBishopMoves(fromBitboard,
					getOccupiedSquares(), getWhitePieces());
		case 'Q':
			return WhitePieces.getQueenMoves(fromBitboard,
					getOccupiedSquares(), getWhitePieces());
		case 'K':
			return WhitePieces.getKingMoves(fromBitboard, getWhitePieces(),
					getBlackAttackingSquares());
		case 'p':
			return BlackPieces.getPawnMoves(fromBitboard, getOccupiedSquares(),
					getWhitePieces());
		case 'r':
			return BlackPieces.getRookMoves(fromBitboard, getOccupiedSquares(),
					getBlackPieces());
		case 'n':
			return BlackPieces.getKnightMoves(fromSquare, getBlackPieces());
		case 'b':
			return BlackPieces.getBishopMoves(fromBitboard,
					getOccupiedSquares(), getBlackPieces());
		case 'q':
			return BlackPieces.getQueenMoves(fromBitboard,
					getOccupiedSquares(), getBlackPieces());
		case 'k':
			return BlackPieces.getKingMoves(fromBitboard, getBlackPieces(),
					getWhiteAttackingSquares());
		}
		return 0L;
	}

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
				currentBoard[x][y] = ' ';
			}
		}
	}

	private void updateBitboards() {
		resetBitboards();
		long currentPiece = 1;
		for (int i = 0; i < 64; i++) {
			switch (currentBoard[i % 8][i / 8]) {
			case 'P':
				whitePawns = whitePawns | currentPiece;
				break;
			case 'R':
				whiteRooks = whiteRooks | currentPiece;
				break;
			case 'N':
				whiteKnights = whiteKnights | currentPiece;
				break;
			case 'B':
				whiteBishops = whiteBishops | currentPiece;
				break;
			case 'Q':
				whiteQueens = whiteQueens | currentPiece;
				break;
			case 'K':
				whiteKing = whiteKing | currentPiece;
				break;
			case 'p':
				blackPawns = blackPawns | currentPiece;
				break;
			case 'r':
				blackRooks = blackRooks | currentPiece;
				break;
			case 'n':
				blackKnights = blackKnights | currentPiece;
				break;
			case 'b':
				blackBishops = blackBishops | currentPiece;
				break;
			case 'q':
				blackQueens = blackQueens | currentPiece;
				break;
			case 'k':
				blackKing = blackKing | currentPiece;
				break;
			default:
				break;
			}
			currentPiece = currentPiece << 1;
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
				char temp = currentBoard[x][y];
				if (temp == ' ') {
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

	private char[][] copyCurrentBoard() {
		char[][] temp = new char[currentBoard.length][currentBoard.length];

		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < temp.length; y++) {
				temp[x][y] = currentBoard[x][y];
			}
		}
		return temp;
	}

	private void printAllBitboards() {
		System.out.println("White Pawns");
		printBitboard(whitePawns);
		System.out.println("White Knights");
		printBitboard(whiteKnights);
		System.out.println("White Rooks");
		printBitboard(whiteRooks);
		System.out.println("White Bishops");
		printBitboard(whiteBishops);
		System.out.println("White Queens");
		printBitboard(whiteQueens);
		System.out.println("White King");
		printBitboard(whiteKing);

		System.out.println("Black Pawns");
		printBitboard(blackPawns);
		System.out.println("Black Knights");
		printBitboard(blackKnights);
		System.out.println("Black Rooks");
		printBitboard(blackRooks);
		System.out.println("Black Bishops");
		printBitboard(blackBishops);
		System.out.println("Black Queens");
		printBitboard(blackQueens);
		System.out.println("Black King");
		printBitboard(blackKing);

	}

	private void testMethod() {
		Vector<char[][]> firstMove = generateWhiteLegalMoves();
		Vector<char[][]> secondMove = new Vector<char[][]>();
		Vector<char[][]> thirdMove = new Vector<char[][]>();
		Vector<char[][]> fourthMove = new Vector<char[][]>();

		// int firstSize, secondSize, thirdSize, fourthSize;
		//
		// firstSize = firstMove.size();
		// while (!firstMove.isEmpty()) {
		// currentBoard = firstMove.get(0);
		// updateBitboards();
		// printBoard();
		// System.out.println(evaluatePosition());
		// System.out.println();
		// System.out.println();
		// firstMove.remove(0);
		// }

		// secondSize = secondMove.size();
		// while (!secondMove.isEmpty()) {
		// currentBoard = secondMove.get(0);
		// thirdMove.addAll(generateWhiteLegalMoves());
		// updateBitboards();
		// printBoard();
		// System.out.println(evaluatePosition());
		// secondMove.remove(0);
		// }
		//
		// thirdSize = thirdMove.size();
		// while (!thirdMove.isEmpty()) {
		// currentBoard = thirdMove.get(0);
		// fourthMove.addAll(generateBlackLegalMoves());
		// updateBitboards();
		// // printBoard();
		// thirdMove.remove(0);
		// }
		//
		// fourthSize = fourthMove.size();
		// while (!fourthMove.isEmpty()) {
		// currentBoard = fourthMove.get(0);
		// updateBitboards();
		// // printBoard();
		// fourthMove.remove(0);
		// }
		// System.out.println(firstSize);
		// System.out.println(secondSize);
		// System.out.println(thirdSize);
		// System.out.println(fourthSize);
	}

}
