package board;

import operations.pieces.BlackPieces;
import operations.pieces.WhitePieces;

/**
 * Keeps track of the en passant square and castling rights etc.
 * 
 * 
 * @author Philip
 * 
 */
public class FullGameState {

	private boolean whiteToMove, whiteCastleKing, whiteCastleQueen,
			blackCastleKing, blackCastleQueen;
	private long enPassantSquare = 100;
	private int numberOfFullMoves = 1;
	private int numberOfHalfMoves = 0;

	private long whitePawns, whiteRooks, whiteKnights, whiteBishops,
			whiteQueens, whiteKing;
	private long blackPawns, blackRooks, blackKnights, blackBishops,
			blackQueens, blackKing;

	private long whiteAttackingSquares = 0;
	private long blackAttackingSquares = 0;
	private long toSquare;
	private long fromSquare;

	public long getToSquare() {
		return toSquare;
	}

	public long getFromSquare() {
		return fromSquare;
	}

	public FullGameState(long whitePawns, long whiteRooks, long whiteKnights,
			long whiteBishops, long whiteQueens, long whiteKing,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long blackKing,
			boolean whiteToMove, boolean whiteCastleKing,
			boolean whiteCastleQueen, boolean blackCastleKing,
			boolean blackCastleQueen, long enPassantSquare,
			int numberOfFullMoves, int numberOfHalfMoves, long fromSquare,
			long toSquare) {

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

		this.toSquare = toSquare;
		this.fromSquare = fromSquare;

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

	public boolean getWhiteToMove() {
		return whiteToMove;
	}

	public boolean getWhiteCastleKing() {
		return whiteCastleKing;
	}

	public boolean getWhiteCastleQueen() {
		return whiteCastleQueen;
	}

	public boolean getBlackCastleKing() {
		return blackCastleKing;
	}

	public boolean getBlackCastleQueen() {
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

	public long getBlackPieces() {
		return (blackBishops | blackKing | blackKnights | blackPawns
				| blackQueens | blackRooks);
	}

	public long getWhitePieces() {
		return (whiteBishops | whiteKing | whiteKnights | whitePawns
				| whiteQueens | whiteRooks);
	}

	public long getAllPieces() {
		return getBlackPieces() | getWhitePieces();
	}

	public long getOccupiedSquares() {
		return getWhitePieces() | getBlackPieces();
	}

	public long getBlackAttackingSquares() {

		if (blackAttackingSquares != 0) {
			return blackAttackingSquares;
		} else {

			blackAttackingSquares = blackAttackingSquares
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

			return blackAttackingSquares;
		}
	}

	public long getWhiteAttackingSquares() {

		if (whiteAttackingSquares != 0) {
			return whiteAttackingSquares;
		} else {

			whiteAttackingSquares = whiteAttackingSquares
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

			return whiteAttackingSquares;
		}
	}

}
