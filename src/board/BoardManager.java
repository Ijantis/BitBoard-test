package board;

public class BoardChecker {

	private static long whitePawns, whiteRooks, whiteKnights, whiteBishops,
			whiteQueens, whiteKing;
	private static long blackPawns, blackRooks, blackKnights, blackBishops,
			blackQueens, blackKing;

	protected static boolean IsSelfCheck(String[][] boardToCheck,
			boolean checkForWhite) {

		updateBitboards(boardToCheck);

		if (checkForWhite) {
			return (whiteKing & getBlackAttackingSquares()) == 0;
		} else {
			return (blackKing & getWhiteAttackingSquares()) == 0;
		}
	}

	private static void updateBitboards(String[][] boardToCheck) {
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
				switch (boardToCheck[x][y]) {
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

	private static void resetBitboards() {
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

	private static long convertStringToBinary(String binaryLong) {
		if (binaryLong.charAt(0) == '0') {
			return Long.parseLong(binaryLong, 2);
		} else {
			return Long.parseLong("1" + binaryLong.substring(2), 2) * 2;
		}
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

	protected static boolean isWhiteKingInCheck() {

		long blackMoves = getBlackAttackingSquares();

		if ((blackMoves & whiteKing) == 0) {
			return false;
		} else {
			return true;
		}
	}

	protected static boolean isBlackKingInCheck() {

		long whiteMoves = getWhiteAttackingSquares();

		if ((whiteMoves & blackKing) == 0) {
			return false;
		} else {
			return true;
		}
	}

	// move this method to WhitePieces.java
	protected static long getWhiteAttackingSquares() {
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
	protected static long getBlackAttackingSquares() {

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

	protected static long getWhitePieces() {
		return (whiteBishops | whiteKing | whiteKnights | whitePawns
				| whiteQueens | whiteRooks);
	}

	protected static long getBlackPieces() {
		return (blackBishops | blackKing | blackKnights | blackPawns
				| blackQueens | blackRooks);
	}

	protected static long getOccupiedSquares() {
		return (getWhitePieces() | getBlackPieces());
	}
}
