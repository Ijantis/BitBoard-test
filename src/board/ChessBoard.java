package board;

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
			{ "R", " ", " ", " ", " ", " ", "p", "r" },
			{ "N", " ", " ", " ", " ", " ", "p", "n" },
			{ "B", " ", " ", " ", " ", " ", "p", "b" },
			{ "Q", " ", " ", " ", " ", " ", "p", "q" },
			{ "K", " ", " ", " ", " ", " ", "p", "k" },
			{ "B", " ", " ", " ", " ", " ", "p", "b" },
			{ "N", " ", " ", " ", " ", " ", "p", "n" },
			{ "R", " ", " ", " ", " ", " ", "p", "r" } };;

	public ChessBoard() {
		long time = System.currentTimeMillis();
		long timeNano = System.nanoTime();

		updateBitboards();

		printBoard();
		updateBitboards();

		MoveGenerator.generateWhiteLegalMoves(currentBoard, whitePawns,
				whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
				getBlackPieces(), getWhitePieces(), getBlackAttackingSquares());

//		MoveGenerator.generateBlackLegalMoves(currentBoard, blackPawns,
//				blackRooks, blackKnights, blackBishops, blackQueens, blackKing,
//				getWhitePieces(), getBlackPieces(), getWhiteAttackingSquares());

		System.out.println("That took :" + (System.currentTimeMillis() - time)
				+ "ms");
		System.out.println("That took :"
				+ ((System.nanoTime() - timeNano) / 1000) + " micro seconds");
	}

	public void newGame() {

		currentBoard[0][0] = "R";
		currentBoard[1][0] = "N";
		currentBoard[2][0] = "B";
		currentBoard[3][0] = "Q";
		currentBoard[4][0] = "K";
		currentBoard[5][0] = "B";
		currentBoard[6][0] = "N";
		currentBoard[7][0] = "R";

		currentBoard[0][7] = "r";
		currentBoard[1][7] = "n";
		currentBoard[2][7] = "b";
		currentBoard[3][7] = "q";
		currentBoard[4][7] = "k";
		currentBoard[5][7] = "b";
		currentBoard[6][7] = "n";
		currentBoard[7][7] = "r";

		for (int x = 0; x < 7; x++) {
			currentBoard[x][1] = "P";
		}

		for (int x = 0; x < 7; x++) {
			currentBoard[x][6] = "p";
		}

		for (int y = 2; y < 6; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = " ";
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
		String[][] tempBoard = copyCurrentBoard();

		// check to see if a piece exists at the from coordinate
		// check to see if the piece has a move possible
		if (moveIsPossible(fromBitboard, fromSquare, toBitboard)) {

			// move the piece on the temporary board
			tempBoard[(int) toSquare % 8][(int) toSquare / 8] = tempBoard[(int) x][(int) y];
			tempBoard[x][y] = " ";

			// next check if tempBoard puts the same colour king in check and
			// if its all good then currentBoard = tempBoard;
			boolean isValid;
			if (tempBoard[x][y].toUpperCase().equals(tempBoard[x][y])) {
				isValid = BoardManager.IsSelfCheck(tempBoard, true);
			} else {
				isValid = BoardManager.IsSelfCheck(tempBoard, false);
			}

			// TODO: Add en passant check.
			if (isValid) {
				currentBoard = tempBoard;
				updateBitboards();
				return true;
				// return isValid;
			} else {
				return false;
				// return isValid;
			}
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

	private void printAllBitboards() {

		printBitboard(whitePawns);
		printBitboard(whiteKnights);
		printBitboard(whiteRooks);
		printBitboard(whiteBishops);
		printBitboard(whiteQueens);
		printBitboard(whiteKing);

		printBitboard(blackPawns);
		printBitboard(blackKnights);
		printBitboard(blackRooks);
		printBitboard(blackBishops);
		printBitboard(blackQueens);
		printBitboard(blackKing);

	}

}
