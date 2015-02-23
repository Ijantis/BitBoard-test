package operations.pieces;

import operations.BitboardOperations;

public class BlackPieces {

	private final static long blackCastleKingSquares = Long.parseLong(
			"110000000000000000000000000000000000000000000000000000000000000",
			2);
	private final static long blackCastleQueenSquares = Long.parseLong(
			"110000000000000000000000000000000000000000000000000000000000", 2);

	public static long getPawnMoves(long blackPawns, long occupiedSquares,
			long whitePieces, long enPassantSquare) {
		return getPawnMovesVertical(blackPawns, occupiedSquares,
				enPassantSquare)
				| getPawnMovesDiagonal(blackPawns, whitePieces);
	}

	public static long getPawnMovesVertical(long blackPawns,
			long occupiedSquares, long enPassantSquare) {

		long enPassantBitboard = 0;
		// en passant
		if (enPassantSquare >= 16 && enPassantSquare <= 23) {
			enPassantBitboard = Long.parseLong("1", 2);
			enPassantBitboard = enPassantBitboard << enPassantSquare;
		}

		// Vertical single rank
		long upOne = (blackPawns >>> 8) & ~occupiedSquares;
		// Moving up 2 spaces from 2nd rank
		long seventhRankPawns = blackPawns & BitboardOperations.maskRank(7);
		long pawnsNotBlocked = (((seventhRankPawns >>> 8) & ~occupiedSquares) << 8);
		long upTwo = pawnsNotBlocked >>> 16 & ~occupiedSquares;

		return upOne | upTwo | enPassantBitboard;
	}

	/*
	 * Generate black pawn moves for attacking a piece
	 */
	public static long getPawnMovesDiagonal(long blackPawns, long whitePieces) {
		return getPawnAttackingSquares(blackPawns) & whitePieces;
	}

	/*
	 * Generate squares under attack diagonally by a black pawn
	 */
	public static long getPawnAttackingSquares(long blackPawns) {
		long upLeft = ((blackPawns & BitboardOperations.clearFile(1)) >>> 7);
		long upRight = ((blackPawns & BitboardOperations.clearFile(8)) >>> 9);
		long attackingAPiece = (upLeft | upRight);

		return attackingAPiece;
	}

	public static long getKnightMoves(long blackKnights, long blackPieces) {
		return getKnightAttackingSquares(blackKnights) & ~blackPieces;// &
																		// ~getblackPieces();
	}

	public static long getKnightAttackingSquares(long blackKnights) {
		// variable names are first direction then second direction

		long firstFile = BitboardOperations.clearFile(1);
		long secondFile = BitboardOperations.clearFile(2);
		long seventhFile = BitboardOperations.clearFile(7);
		long eigthFile = BitboardOperations.clearFile(8);

		long upRight = (blackKnights & eigthFile) << 17;
		long upLeft = (blackKnights & firstFile) << 15;

		long rightUp = (BitboardOperations.clearFile(8) & seventhFile & blackKnights) << 10;
		long rightDown = (BitboardOperations.clearFile(8) & seventhFile & blackKnights) >>> 6;

		long downRight = (blackKnights & eigthFile) >>> 15;
		long downLeft = (blackKnights & firstFile) >>> 17;

		long leftUp = (firstFile & secondFile & blackKnights) << 6;
		long leftDown = (firstFile & secondFile & blackKnights) >>> 10;

		long possibleMoves = upRight | upLeft | rightUp | rightDown | downRight
				| downLeft | leftUp | leftDown;

		return possibleMoves;
	}

	public static long getRookMoves(long rookBitboardToCalculate,
			long occupiedSquares, long blackPieces) {
		return getRookAttackingSquares(rookBitboardToCalculate, occupiedSquares)
				& ~blackPieces;
	}

	public static long getRookAttackingSquares(long rookBitboardToCalculate,
			long occupiedSquares) {

		long nextRook = 0L;
		long allRookMoves = 0L;

		while (rookBitboardToCalculate != 0) {
			nextRook = Long.highestOneBit(rookBitboardToCalculate);
			allRookMoves = allRookMoves
					| getSingleRookMove(nextRook, occupiedSquares);

			String rookBitboard = Long.toBinaryString(rookBitboardToCalculate);
			if (rookBitboard.length() == 1) {
				break;
			}
			rookBitboardToCalculate = Long.parseLong(rookBitboard.substring(1),
					2);
		}
		return allRookMoves;
	}

	/*
	 * Returns the bitboard of possible moves for a single rook.
	 */
	/**
	 * Takes in a bitboard with a single piece on it and outputs the moves
	 * associated with that rook.
	 * 
	 * @param rookToMove
	 *            - The bitboard of a rook to compute the moves for.
	 * @return - The bitboard of possible moves for the inputed rook.
	 */
	private static long getSingleRookMove(long rookToMove, long occupiedSquares) {

		long rightSquares = BitboardOperations.getRightSquares(rookToMove);
		long right_moves = rightSquares & occupiedSquares;
		right_moves = (right_moves << 1) | (right_moves << 2)
				| (right_moves << 3) | (right_moves << 4) | (right_moves << 5)
				| (right_moves << 6);
		right_moves = right_moves & rightSquares;
		right_moves = right_moves ^ rightSquares;
		// right_moves = right_moves & ~whitePieces;

		long leftSquares = BitboardOperations.getLeftSquares(rookToMove);
		long left_moves = leftSquares & occupiedSquares;
		left_moves = (left_moves >>> 1) | (left_moves >>> 2)
				| (left_moves >>> 3) | (left_moves >>> 4) | (left_moves >>> 5)
				| (left_moves >>> 6);
		left_moves = left_moves & leftSquares;
		left_moves = left_moves ^ leftSquares;
		// left_moves = left_moves & ~whitePieces;

		long upSquares = BitboardOperations.getUpSquares(rookToMove);
		long up_moves = upSquares & occupiedSquares;
		up_moves = (up_moves << 8) | (up_moves << 16) | (up_moves << 24)
				| (up_moves << 32) | (up_moves << 40) | (up_moves << 48);
		up_moves = up_moves & upSquares;
		up_moves = up_moves ^ upSquares;
		// up_moves = up_moves & ~whitePieces;

		long downSquares = BitboardOperations.getDownSquares(rookToMove);
		long down_moves = downSquares & occupiedSquares;
		down_moves = (down_moves >>> 8) | (down_moves >>> 16)
				| (down_moves >>> 24) | (down_moves >>> 32)
				| (down_moves >>> 40) | (down_moves >>> 48);
		down_moves = down_moves & downSquares;
		down_moves = down_moves ^ downSquares;
		// down_moves = down_moves & ~whitePieces;

		return left_moves | right_moves | up_moves | down_moves;
	}

	public static long getBishopMoves(long blackBishops, long occupiedSquares,
			long blackPieces) {
		return getBishopAttackingSquares(blackBishops, occupiedSquares)
				& ~blackPieces;

	}

	public static long getBishopAttackingSquares(long blackBishops,
			long occupiedSquares) {

		long topRightSquares = BitboardOperations
				.getTopRightSquares(blackBishops);
		long topRight = topRightSquares & occupiedSquares;
		topRight = (topRight << 9) | (topRight << 18) | (topRight << 27)
				| (topRight << 36) | (topRight << 45) | (topRight << 54);
		topRight = topRight & topRightSquares;
		topRight = topRight ^ topRightSquares;

		long bottomLeftSquares = BitboardOperations
				.getBottomLeftSquares(blackBishops);
		long bottomLeft = bottomLeftSquares & occupiedSquares;
		bottomLeft = (bottomLeft >>> 9) | (bottomLeft >>> 18)
				| (bottomLeft >>> 27) | (bottomLeft >>> 36)
				| (bottomLeft >>> 45) | (bottomLeft >>> 54);
		bottomLeft = bottomLeft & bottomLeftSquares;
		bottomLeft = bottomLeft ^ bottomLeftSquares;

		long bottomRightSquares = BitboardOperations
				.getBottomRightSquares(blackBishops);
		long bottomRight = bottomRightSquares & occupiedSquares;
		bottomRight = (bottomRight >>> 7) | (bottomRight >>> 14)
				| (bottomRight >>> 21) | (bottomRight >>> 28)
				| (bottomRight >>> 35) | (bottomRight >>> 42);
		bottomRight = bottomRight & bottomRightSquares;
		bottomRight = bottomRight ^ bottomRightSquares;

		long topLeftSquares = BitboardOperations
				.getTopLeftSquares(blackBishops);
		long topLeft = topLeftSquares & occupiedSquares;
		topLeft = (topLeft << 7) | (topLeft << 14) | (topLeft << 21)
				| (topLeft << 28) | (topLeft << 35) | (topLeft << 42);
		topLeft = topLeft & topLeftSquares;
		topLeft = topLeft ^ topLeftSquares;

		return topRight | topLeft | bottomRight | bottomLeft;
	}

	public static long getQueenAttackingSquares(long blackQueens,
			long occupiedSquares) {
		return getBishopAttackingSquares(blackQueens, occupiedSquares)
				| getRookAttackingSquares(blackQueens, occupiedSquares);
	}

	public static long getQueenMoves(long blackQueens, long occupiedSquares,
			long blackPieces) {
		return getQueenAttackingSquares(blackQueens, occupiedSquares)
				& ~blackPieces;
	}

	/*
	 * Generate black king moves.
	 */
	public static long getKingAttackingSquares(long blackKing, long blackPieces) {

		// vertical movement 8 bit shift
		long up = blackKing << 8;
		long down = blackKing >>> 8;

		long firstFile = BitboardOperations.clearFile(1);
		long eigthFile = BitboardOperations.clearFile(8);

		// clear file stops move generation to the left if on the 1st file
		long left = ((blackKing & firstFile) >>> 1);
		long upLeft = (blackKing & firstFile) << 7;
		long downLeft = (blackKing & firstFile) >>> 9;

		// clear file stops move generation to the right if on the 8th file
		long right = ((blackKing & eigthFile) << 1);
		long upRight = ((blackKing & eigthFile) << 9);
		long downRight = ((blackKing & eigthFile) >>> 7);

		long possibleMoves = (up | down | left | right | upRight | downRight
				| upLeft | downLeft);

		// TODO: Add another check to not let the king move onto a square under
		// attack by the opposite colour.
		return (possibleMoves);
	}

	public static long getKingMoves(long blackKing, long blackPieces,
			long whiteAttackingSquares, boolean blackCastleKing,
			boolean blackCastleQueen) {

		long castled = 0;

		if (blackCastleKing
				&& ((blackCastleKingSquares & whiteAttackingSquares) == 0)
				&& ((blackCastleKingSquares & blackPieces) == 0)) {
			castled = castled | blackKing << 2;
		}

		if (blackCastleQueen
				&& ((blackCastleQueenSquares & whiteAttackingSquares) == 0)
				&& ((blackCastleQueenSquares & blackPieces) == 0)) {
			castled = castled | blackKing >>> 2;
		}

		return ((getKingAttackingSquares(blackKing, blackPieces) & ~whiteAttackingSquares) | castled)
				& ~blackPieces;
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
}
