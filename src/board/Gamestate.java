package board;

/**
 * Keeps track of the en passant square and castling rights etc.
 * 
 * 
 * @author Philip
 * 
 */
public class Gamestate {

	private boolean whiteToMove, whiteCastleKing, whiteCastleQueen,
			blackCastleKing, blackCastleQueen;
	private long enPassantSquare = 0;
	private int numberOfFullMoves = 1;
	private int numberOfHalfMoves = 0;
	private char[][] currentBoard;

	private long whiteAttackingSquares, blackAttackingSquares;

	private long whitePawns, whiteRooks, whiteKnights, whiteBishops,
			whiteQueens, whiteKing;
	private long blackPawns, blackRooks, blackKnights, blackBishops,
			blackQueens, blackKing;

	public Gamestate(char[][] currentBoard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing, boolean whiteToMove, boolean whiteCastleKing,
			boolean whiteCastleQueen, boolean blackCastleKing,
			boolean blackCastleQueen, long enPassantSquare,
			int numberOfFullMoves, int numberOfHalfMoves,
			long whiteAttackingSquares, long blackAttackingSquares) {

		this.whitePawns = whitePawns;
		this.whiteRooks = whiteRooks;
		this.whiteKnights = whiteKnights;
		this.whiteBishops = whiteBishops;
		this.whiteQueens = whiteQueens;
		this.whiteKing = whiteKing;

		this.blackPawns = blackPawns;
		this.blackRooks = blackRooks;
		this.blackKnights = blackKnights;
		this.blackBishops = blackBishops;
		this.blackQueens = blackQueens;
		this.blackKing = blackKing;

		this.whiteToMove = whiteToMove;
		this.whiteCastleKing = whiteCastleKing;
		this.whiteCastleQueen = whiteCastleQueen;
		this.blackCastleKing = blackCastleKing;
		this.blackCastleQueen = blackCastleQueen;

		this.enPassantSquare = enPassantSquare;
		this.numberOfFullMoves = numberOfFullMoves;
		this.numberOfHalfMoves = numberOfHalfMoves;

		this.whiteAttackingSquares = whiteAttackingSquares;
		this.blackAttackingSquares = blackAttackingSquares;

		this.currentBoard = currentBoard;
	}

	public long getWhitePawns() {
		return whitePawns;
	}

	public long getWhiteRooks() {
		return whiteRooks;
	}

	public long getWhiteKnights() {
		return whiteKnights;
	}

	public long getWhiteBishops() {
		return whiteBishops;
	}

	public long getWhiteQueens() {
		return whiteQueens;
	}

	public long getWhiteKing() {
		return whiteKing;
	}

	public long getBlackPawns() {
		return blackPawns;
	}

	public long getBlackRooks() {
		return blackRooks;
	}

	public long getBlackKnights() {
		return blackKnights;
	}

	public long getBlackBishops() {
		return blackBishops;
	}

	public long getBlackQueens() {
		return blackQueens;
	}

	public long getBlackKing() {
		return blackKing;
	}

	public boolean isWhiteToMove() {
		return whiteToMove;
	}

	public boolean canWhiteCastleKing() {
		return whiteCastleKing;
	}

	public boolean canWhiteCastleQueen() {
		return whiteCastleQueen;
	}

	public boolean canBlackCastleKing() {
		return blackCastleKing;
	}

	public boolean canBlackCastleQueen() {
		return blackCastleQueen;
	}

	public long getEnPassantSquare() {
		return enPassantSquare;
	}

	public int getNumberOfFullMoves() {
		return numberOfFullMoves;
	}

	public int getNumberOfHalfMoves() {
		return numberOfHalfMoves;
	}

	public char[][] getCurrentBoard() {
		return currentBoard;
	}

	public long getBlackPieces() {
		return (blackBishops | blackKing | blackKnights | blackPawns
				| blackQueens | blackRooks);
	}

	public long getWhitePieces() {
		return (whiteBishops | whiteKing | whiteKnights | whitePawns
				| whiteQueens | whiteRooks);
	}

	public long getBlackAttackingSquares() {
		return blackAttackingSquares;
	}

	public long getWhiteAttackingSquares() {
		return whiteAttackingSquares;
	}

	public long getOccupiedSquares() {
		return getWhitePieces() | getBlackPieces();
	}
}
