package board;

import operations.pieces.BlackPieces;
import operations.pieces.WhitePieces;

public class BoardManager {

	private static long whitePawns, whiteRooks, whiteKnights, whiteBishops,
			whiteQueens, whiteKing;
	private static long blackPawns, blackRooks, blackKnights, blackBishops,
			blackQueens, blackKing;

	public static boolean IsSelfCheck(long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing, boolean checkForWhite) {

		BoardManager.whitePawns = whitePawns;
		BoardManager.whiteRooks = whiteRooks;
		BoardManager.whiteKnights = whiteKnights;
		BoardManager.whiteBishops = whiteBishops;
		BoardManager.whiteQueens = whiteQueens;
		BoardManager.whiteKing = whiteKing;

		BoardManager.blackPawns = blackPawns;
		BoardManager.blackRooks = blackRooks;
		BoardManager.blackKnights = blackKnights;
		BoardManager.blackBishops = blackBishops;
		BoardManager.blackQueens = blackQueens;
		BoardManager.blackKing = blackKing;

		if (checkForWhite) {
			return (whiteKing & getBlackAttackingSquares()) == 0;
		} else {
			return (blackKing & getWhiteAttackingSquares()) == 0;
		}

	}

	public static boolean IsSelfCheck(char[][] tempBoard, boolean checkForWhite) {

		clearBitboards();
		generateBitBoards(tempBoard);

		if (checkForWhite) {
			return (whiteKing & getBlackAttackingSquares()) == 0;
		} else {
			return (blackKing & getWhiteAttackingSquares()) == 0;
		}

	}

	private static void clearBitboards() {
		BoardManager.whitePawns = 0;
		BoardManager.whiteRooks = 0;
		BoardManager.whiteKnights = 0;
		BoardManager.whiteBishops = 0;
		BoardManager.whiteQueens = 0;
		BoardManager.whiteKing = 0;

		BoardManager.blackPawns = 0;
		BoardManager.blackRooks = 0;
		BoardManager.blackKnights = 0;
		BoardManager.blackBishops = 0;
		BoardManager.blackQueens = 0;
		BoardManager.blackKing = 0;
	}

	private static void generateBitBoards(char[][] boardToCheck) {
		long currentPiece = 1;
		for (int i = 0; i < 64; i++) {
			switch (boardToCheck[i % 8][i / 8]) {
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
