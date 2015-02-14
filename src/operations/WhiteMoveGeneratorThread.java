package operations;

import java.util.Vector;

import operations.pieces.WhitePieces;

public class WhiteMoveGeneratorThread implements Runnable {

	private char[][] currentBoard;
	private long whitePawns;
	private long whiteRooks;
	private long whiteKnights;
	private long whiteBishops;
	private long whiteQueens;
	private long whiteKing;
	private long blackPieces;
	private long whitePieces;
	private long blackAttackingSquares;
	private long blackPawns;
	private long blackRooks;
	private long blackKnights;
	private long blackBishops;
	private long blackQueens;
	private long blackKing;
	private static Vector<char[][]> possibleStates = new Vector<>(40, 10);

	public WhiteMoveGeneratorThread(char[][] currentBoard, long whitePawns,
			long whiteRooks, long whiteKnights, long whiteBishops,
			long whiteQueens, long whiteKing, long blackPieces,
			long whitePieces, long blackAttackingSquares, long blackPawns,
			long blackRooks, long blackKnights, long blackBishops,
			long blackQueens, long blackKing) {

		this.whitePawns = whitePawns;
		this.whiteRooks = whiteRooks;
		this.whiteKnights = whiteKnights;
		this.whiteBishops = whiteBishops;
		this.whiteQueens = whiteQueens;
		this.whiteKing = whiteKing;
		this.blackPieces = blackPieces;
		this.whitePieces = whitePieces;
		this.blackAttackingSquares = blackAttackingSquares;
		this.blackPawns = blackPawns;
		this.blackRooks = blackRooks;
		this.blackKnights = blackKnights;
		this.blackBishops = blackBishops;
		this.blackQueens = blackQueens;
		this.blackKing = blackKing;
		this.currentBoard = currentBoard;

	}

	@Override
	public void run() {

		long temp;

		temp = whitePawns;
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					whitePieces | blackPieces, blackPieces);
			addStates(MoveGenerator.whitePawnMoves(nextPawn, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = whiteKnights;
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getKnightMoves(nextKnight,
					whitePieces);
			addStates(MoveGenerator.whiteKnightMoves(nextKnight,
					bitboardOfMoves, whitePawns, whiteRooks, whiteKnights,
					whiteBishops, whiteQueens, blackPawns, blackRooks,
					blackKnights, blackBishops, blackQueens, whiteKing,
					blackKing, currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = whiteBishops;
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getBishopMoves(nextBishop,
					whitePieces | blackPieces, whitePieces);
			addStates(MoveGenerator.whiteBishopMoves(nextBishop,
					bitboardOfMoves, whitePawns, whiteRooks, whiteKnights,
					whiteBishops, whiteQueens, blackPawns, blackRooks,
					blackKnights, blackBishops, blackQueens, whiteKing,
					blackKing, currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = whiteQueens;
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(temp, whitePieces
					| blackPieces, whitePieces);
			addStates(MoveGenerator.whiteQueenMoves(nextQueen, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = (Long.highestOneBit(whiteQueens) - 1) & temp;
		}

		temp = whiteRooks;
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getRookMoves(nextRook,
					whitePieces | blackPieces, whitePieces);
			addStates(MoveGenerator.whiteRookMoves(nextRook, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = WhitePieces.getKingMoves(whiteKing,
				whitePieces, blackAttackingSquares);
		addStates(MoveGenerator.whiteKingMoves(whiteKing, kingMovesBitboard,
				whitePawns, whiteRooks, whiteKnights, whiteBishops,
				whiteQueens, blackPawns, blackRooks, blackKnights,
				blackBishops, blackQueens, whiteKing, blackKing, currentBoard));

	}

	private synchronized void addStates(Vector<char[][]> arg0) {
		if (!arg0.isEmpty()) {
			possibleStates.addAll(arg0);
		}
	}

	/*
	 * This method should only be called when you are finished generating moves
	 * at a certain depth.
	 */
	public static Vector<char[][]> getStates() {
		Vector<char[][]> temp = (Vector<char[][]>) possibleStates.clone();
		possibleStates.removeAllElements();
		return temp;
	}
}
