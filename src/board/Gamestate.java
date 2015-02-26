package board;

public class GameState {

	private boolean whiteToMove, whiteCastleKing, whiteCastleQueen,
			blackCastleKing, blackCastleQueen;
	private long enPassantSquare = 0;
	private int numberOfFullMoves = 1;
	private int numberOfHalfMoves = 0;
	private char[][] currentBoard;

	public GameState(char[][] currentBoard, boolean whiteToMove,
			boolean whiteCastleKing, boolean whiteCastleQueen,
			boolean blackCastleKing, boolean blackCastleQueen,
			long enPassantSquare, int numberOfFullMoves, int numberOfHalfMoves) {

		this.whiteToMove = whiteToMove;
		this.whiteCastleKing = whiteCastleKing;
		this.whiteCastleQueen = whiteCastleQueen;
		this.blackCastleKing = blackCastleKing;
		this.blackCastleQueen = blackCastleQueen;

		this.enPassantSquare = enPassantSquare;
		this.numberOfFullMoves = numberOfFullMoves;
		this.numberOfHalfMoves = numberOfHalfMoves;

		this.currentBoard = currentBoard;
	}

	public boolean isWhiteToMove() {
		return whiteToMove;
	}

	public boolean getWhiteCastleKing() {
		return whiteCastleKing;
	}

	public boolean isWhiteCastleQueen() {
		return whiteCastleQueen;
	}

	public boolean isBlackCastleKing() {
		return blackCastleKing;
	}

	public boolean isBlackCastleQueen() {
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

}
