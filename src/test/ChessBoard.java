package test;

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

		initialiseBoard();
		printBitBoard(getBlackKnightMoves());

	}

	private void clearChessBoard() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = " ";
			}
		}
	}

	private long getWhiteKnightMoves() {

		// variable names are first direction then second direction

		long upRight = (whiteKnights & clearFile(8)) << 17;
		long upLeft = (whiteKnights & clearFile(1)) << 15;

		long rightUp = (clearFile(8) & clearFile(7) & whiteKnights) << 10;
		long rightDown = (clearFile(8) & clearFile(7) & whiteKnights) >>> 6;

		long downRight = (whiteKnights & clearFile(8)) >>> 15;
		long downLeft = (whiteKnights & clearFile(1)) >>> 17;

		long leftUp = (clearFile(1) & clearFile(2) & whiteKnights) << 6;
		long leftDown = (clearFile(1) & clearFile(2) & whiteKnights) >>> 10;

		long possibleMoves = upRight | upLeft | rightUp | rightDown | downRight
				| downLeft | leftUp | leftDown;

		return possibleMoves & ~getWhitePieces();// & ~getWhitePieces();
	}

	private long getBlackKnightMoves() {

		// variable names are first direction then second direction

		long upRight = (blackKnights & clearFile(8)) << 17;
		long upLeft = (blackKnights & clearFile(1)) << 15;

		long rightUp = (clearFile(8) & clearFile(7) & blackKnights) << 10;
		long rightDown = (clearFile(8) & clearFile(7) & blackKnights) >>> 6;

		long downRight = (blackKnights & clearFile(8)) >>> 15;
		long downLeft = (blackKnights & clearFile(1)) >>> 17;

		long leftUp = (clearFile(1) & clearFile(2) & blackKnights) << 6;
		long leftDown = (clearFile(1) & clearFile(2) & blackKnights) >>> 10;

		long possibleMoves = upRight | upLeft | rightUp | rightDown | downRight
				| downLeft | leftUp | leftDown;

		return possibleMoves & ~getBlackPieces();// & ~getWhitePieces();
	}

	/*
	 * Generate white pawn moves for attacking a piece
	 */
	private long getWhitePawnMovesDiagonal() {

		long upLeft = ((whitePawns & clearFile(1)) << 7);
		long upRight = ((whitePawns & clearFile(8)) << 9);
		long attackingAPiece = (upLeft | upRight) & getBlackPieces();

		printBitBoard(upLeft);

		return attackingAPiece;
	}

	/*
	 * Generate black pawn moves when attacking a piece
	 */
	private long getBlackPawnMovesDiagonal() {

		long downRight = ((blackPawns & clearFile(8)) >>> 7);
		long downLeft = ((blackPawns & clearFile(1)) >>> 9);
		long attackingAPiece = (downRight | downLeft) & getWhitePieces();

		printBitBoard(attackingAPiece);

		return 0;
	}

	/*
	 * Generate white king moves.
	 */
	private long getWhiteKingMoves() {

		// vertical movement 8 bit shift
		long up = whiteKing << 8;
		long down = whiteKing >>> 8;

		// clear file stops move generation to the left if on the 1st file
		long left = ((whiteKing & clearFile(1)) >>> 1);
		long upLeft = (whiteKing & clearFile(1)) << 7;
		long downLeft = (whiteKing & clearFile(1)) >>> 9;

		// clear file stops move generation to the right if on the 8th file
		long right = ((whiteKing & clearFile(8)) << 1);
		long upRight = ((whiteKing & clearFile(8)) << 9);
		long downRight = ((whiteKing & clearFile(8)) >>> 7);

		long possibleMoves = (up | down | left | right | upRight | downRight
				| upLeft | downLeft);

		// TODO: Add another check to not let the king move onto a square under
		// attack by the opposite colour.
		return possibleMoves & ~getWhitePieces();
	}

	/*
	 * Generate black king moves.
	 */
	private long getBlackKingMoves() {

		// vertical movement 8 bit shift
		long up = blackKing << 8;
		long down = blackKing >>> 8;

		// clear file stops move generation to the left if on the 1st file
		long left = ((blackKing & clearFile(1)) >>> 1);
		long upLeft = (blackKing & clearFile(1)) << 7;
		long downLeft = (blackKing & clearFile(1)) >>> 9;

		// clear file stops move generation to the right if on the 8th file
		long right = ((blackKing & clearFile(8)) << 1);
		long upRight = ((blackKing & clearFile(8)) << 9);
		long downRight = ((blackKing & clearFile(8)) >>> 7);

		long possibleMoves = (up | down | left | right | upRight | downRight
				| upLeft | downLeft);

		// TODO: Add another check to not let the king move onto a square under
		// attack by the opposite colour.
		return possibleMoves & ~getBlackPieces();
	}

	private long getWhitePawnMovesVertical() {

		// Vertical single rank
		long upOne = (whitePawns << 8) & ~getOccupiedSquares();

		// Moving up 2 spaces from 2nd rank
		long secondRankPawns = whitePawns & maskRank(2);
		long pawnsNotBlocked = (((secondRankPawns << 8) & ~getOccupiedSquares()) >>> 8);
		long upTwo = pawnsNotBlocked << 16 & ~getOccupiedSquares();

		return upOne | upTwo;
	}

	private long getBlackPawnMovesVertical() {

		// Vertical single rank
		long downOne = (blackPawns >>> 8) & ~getOccupiedSquares();

		// Moving down 2 ranks from 7th rank
		long seventhRankPawns = blackPawns & maskRank(7);
		long pawnsNotBlocked = (((seventhRankPawns >>> 8) & ~getOccupiedSquares()) << 8);
		long downTwo = pawnsNotBlocked >>> 16 & ~getOccupiedSquares();

		return downOne | downTwo;
	}

	private void initialiseBoard() {
		initialiseBitBoards();
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

	private void initialiseBitBoards() {
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
	public void printBoard() {
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

	private long clearRank(int rankToClear) {
		return ~maskRank(rankToClear);
	}

	private long clearFile(int fileToClear) {
		return ~maskFile(fileToClear);
	}

	private long maskRank(int rankToMask) {
		String temp = "0000000000000000000000000000000000000000000000000000000011111111";
		long maskedRank = Long.parseLong(temp, 2);
		maskedRank = maskedRank << (8 * (rankToMask - 1));
		return maskedRank;
	}

	private long maskFile(int fileToMask) {

		String temp = "0000000100000001000000010000000100000001000000010000000100000001";
		long maskedFile = Long.parseLong(temp, 2);
		maskedFile = maskedFile << (fileToMask - 1);
		return maskedFile;
	}

	private void printBitBoard(long bitBoard) {
		String stringBitBoard = Long.toBinaryString(bitBoard);
		System.out.println("Value : " + stringBitBoard);
		while (stringBitBoard.length() != 64) {
			stringBitBoard = "0" + stringBitBoard;
		}

		for (int i = 0; i < 8; i++) {
			StringBuilder stringReverser = new StringBuilder(
					stringBitBoard.substring(i * 8, ((i + 1) * 8)));
			stringReverser.reverse();
			System.out.println(stringReverser.toString());
		}
		System.out.println();
	}
}
