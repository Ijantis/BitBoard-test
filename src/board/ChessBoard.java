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
			{ "R", "P", " ", " ", " ", " ", "p", "r" },
			{ "N", "P", " ", " ", " ", " ", "p", "n" },
			{ "B", "P", " ", " ", " ", " ", "p", "b" },
			{ "Q", "P", " ", " ", " ", " ", "p", "q" },
			{ "K", "P", " ", " ", " ", " ", "p", "k" },
			{ "B", "P", " ", " ", " ", " ", "p", "b" },
			{ "N", "P", " ", " ", " ", " ", "p", "n" },
			{ "R", "P", " ", " ", " ", " ", "p", "r" } };;

	public ChessBoard() {
		newGame();
	}

	public void newGame() {

		long time = System.currentTimeMillis();
		long timeNano = System.nanoTime();

		updateBitboards();

		System.out.println("That took :" + (System.currentTimeMillis() - time)
				+ "ms");
		System.out.println("That took :"
				+ ((System.nanoTime() - timeNano) / 1000) + " micro seconds");

	}

	public boolean makeMove(int fromSquare, int toSquare) {

		return false;
	}

	private boolean isWhiteKingInCheck() {

		long blackMoves = getBlackAttackingSquaresNonKing();

		if ((blackMoves & whiteKing) == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isBlackKingInCheck() {

		long whiteMoves = getWhiteAttackingSquaresNonKing();

		if ((whiteMoves & blackKing) == 0) {
			return false;
		} else {
			return true;
		}
	}

	private long getWhiteAttackingSquaresNonKing() {
		long attackedSquares = 0L;

		attackedSquares = attackedSquares
				| WhitePieceOperations.getWhitePawnAttackingSquares(whitePawns)
				| WhitePieceOperations.getWhiteRookAttackingSquares(whiteRooks,
						getOccupiedSquares())
				| WhitePieceOperations
						.getWhiteKnightAttackingSquares(whiteKnights)
				| WhitePieceOperations.getWhiteBishopAttackingSquares(
						whiteBishops, getOccupiedSquares())
				| WhitePieceOperations.getWhiteQueenAttackingSquares(
						whiteQueens, getOccupiedSquares());

		return attackedSquares;
	}

	private long getBlackAttackingSquaresNonKing() {

		long attackedSquares = 0L;

		attackedSquares = attackedSquares
				| BlackPieceOperations.getblackPawnAttackingSquares(blackPawns)
				| BlackPieceOperations.getblackRookAttackingSquares(blackRooks,
						getOccupiedSquares())
				| BlackPieceOperations
						.getblackKnightAttackingSquares(blackKnights)
				| BlackPieceOperations.getblackBishopAttackingSquares(
						blackBishops, getOccupiedSquares())
				| BlackPieceOperations.getblackQueenAttackingSquares(
						blackQueens, getOccupiedSquares());

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
			for (int j = 0; j < stringReverser.toString().length(); j++) {
				System.out.print(stringReverser.toString().charAt(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
