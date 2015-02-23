package operations.pieces;

import operations.BitboardOperations;

public class WhitePieces {

	private final static long whiteCastleKingSquares = Long.parseLong(
			"1100000", 2);
	private final static long whiteCastleQueenSquares = Long.parseLong("1100",
			2);

	public static long getPawnMoves(long whitePawns, long occupiedSquares,
			long blackPieces, long enPassantSquare) {
		return getPawnMovesVertical(whitePawns, occupiedSquares,
				enPassantSquare)
				| getPawnMovesDiagonal(whitePawns, blackPieces);
	}

	public static long getPawnMovesVertical(long whitePawns,
			long occupiedSquares, long enPassantSquare) {
		long enPassantBitboard = 0;
		// en passant
		if (enPassantSquare >= 40 && enPassantSquare <= 47) {
			enPassantBitboard = Long.parseLong("1", 2);
			enPassantBitboard = enPassantBitboard << enPassantSquare;
		}

		// Vertical single rank
		long upOne = (whitePawns << 8) & ~occupiedSquares;

		// Moving up 2 spaces from 2nd rank
		long secondRankPawns = whitePawns & BitboardOperations.maskRank(2);
		long pawnsNotBlocked = (((secondRankPawns << 8) & ~occupiedSquares) >>> 8);
		long upTwo = pawnsNotBlocked << 16 & ~occupiedSquares;

		return upOne | upTwo | enPassantBitboard;
	}

	/*
	 * Generate white pawn moves for attacking a piece
	 */
	public static long getPawnMovesDiagonal(long whitePawns, long blackPieces) {
		return getPawnAttackingSquares(whitePawns) & blackPieces;
	}

	/*
	 * Generate squares under attack diagonally by a white pawn
	 */
	public static long getPawnAttackingSquares(long whitePawns) {
		long upLeft = ((whitePawns & BitboardOperations.clearFile(1)) << 7);
		long upRight = ((whitePawns & BitboardOperations.clearFile(8)) << 9);
		long attackingAPiece = (upLeft | upRight);

		return attackingAPiece;
	}

	public static long getKnightMoves(long whiteKnights, long whitePieces) {
		return getKnightAttackingSquares(whiteKnights) & ~whitePieces;// &
																		// ~getWhitePieces();
	}

	public static long getKnightAttackingSquares(long whiteKnights) {
		// variable names are first direction then second direction

		long firstFile = BitboardOperations.clearFile(1);
		long secondFile = BitboardOperations.clearFile(2);
		long seventhFile = BitboardOperations.clearFile(7);
		long eigthFile = BitboardOperations.clearFile(8);

		long upRight = (whiteKnights & eigthFile) << 17;
		long upLeft = (whiteKnights & firstFile) << 15;

		long rightUp = (BitboardOperations.clearFile(8) & seventhFile & whiteKnights) << 10;
		long rightDown = (BitboardOperations.clearFile(8) & seventhFile & whiteKnights) >>> 6;

		long downRight = (whiteKnights & eigthFile) >>> 15;
		long downLeft = (whiteKnights & firstFile) >>> 17;

		long leftUp = (firstFile & secondFile & whiteKnights) << 6;
		long leftDown = (firstFile & secondFile & whiteKnights) >>> 10;

		long possibleMoves = upRight | upLeft | rightUp | rightDown | downRight
				| downLeft | leftUp | leftDown;

		return possibleMoves;
	}

	public static long getRookMoves(long rookBitboardToCalculate,
			long occupiedSquares, long whitePieces) {
		return getRookAttackingSquares(rookBitboardToCalculate, occupiedSquares)
				& ~whitePieces;
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

	public static long getBishopMoves(long whiteBishops, long occupiedSquares,
			long whitePieces) {
		return getBishopAttackingSquares(whiteBishops, occupiedSquares)
				& ~whitePieces;

	}

	public static long getBishopAttackingSquares(long whiteBishops,
			long occupiedSquares) {

		long topRightSquares = BitboardOperations
				.getTopRightSquares(whiteBishops);
		long topRight = topRightSquares & occupiedSquares;
		topRight = (topRight << 9) | (topRight << 18) | (topRight << 27)
				| (topRight << 36) | (topRight << 45) | (topRight << 54);
		topRight = topRight & topRightSquares;
		topRight = topRight ^ topRightSquares;
		// topRight = topRight & ~getWhitePieces();

		long bottomLeftSquares = BitboardOperations
				.getBottomLeftSquares(whiteBishops);
		long bottomLeft = bottomLeftSquares & occupiedSquares;
		bottomLeft = (bottomLeft >>> 9) | (bottomLeft >>> 18)
				| (bottomLeft >>> 27) | (bottomLeft >>> 36)
				| (bottomLeft >>> 45) | (bottomLeft >>> 54);
		bottomLeft = bottomLeft & bottomLeftSquares;
		bottomLeft = bottomLeft ^ bottomLeftSquares;
		// bottomLeft = bottomLeft & ~getWhitePieces();

		long bottomRightSquares = BitboardOperations
				.getBottomRightSquares(whiteBishops);
		long bottomRight = bottomRightSquares & occupiedSquares;
		bottomRight = (bottomRight >>> 7) | (bottomRight >>> 14)
				| (bottomRight >>> 21) | (bottomRight >>> 28)
				| (bottomRight >>> 35) | (bottomRight >>> 42);
		bottomRight = bottomRight & bottomRightSquares;
		bottomRight = bottomRight ^ bottomRightSquares;
		// bottomRight = bottomRight & ~getWhitePieces();

		long topLeftSquares = BitboardOperations
				.getTopLeftSquares(whiteBishops);
		long topLeft = topLeftSquares & occupiedSquares;
		topLeft = (topLeft << 7) | (topLeft << 14) | (topLeft << 21)
				| (topLeft << 28) | (topLeft << 35) | (topLeft << 42);
		topLeft = topLeft & topLeftSquares;
		topLeft = topLeft ^ topLeftSquares;
		// topLeft = topLeft & ~getWhitePieces();

		return topRight | topLeft | bottomRight | bottomLeft;
	}

	public static long getQueenAttackingSquares(long whiteQueens,
			long occupiedSquares) {
		return getBishopAttackingSquares(whiteQueens, occupiedSquares)
				| getRookAttackingSquares(whiteQueens, occupiedSquares);
	}

	public static long getQueenMoves(long whiteQueens, long occupiedSquares,
			long whitePieces) {
		return getQueenAttackingSquares(whiteQueens, occupiedSquares)
				& ~whitePieces;
	}

	/*
	 * Generate white king moves.
	 */
	public static long getKingAttackingSquares(long whiteKing, long whitePieces) {

		// vertical movement 8 bit shift
		long up = whiteKing << 8;
		long down = whiteKing >>> 8;

		long firstFile = BitboardOperations.clearFile(1);
		long eigthFile = BitboardOperations.clearFile(8);

		// clear file stops move generation to the left if on the 1st file
		long left = ((whiteKing & firstFile) >>> 1);
		long upLeft = (whiteKing & firstFile) << 7;
		long downLeft = (whiteKing & firstFile) >>> 9;

		// clear file stops move generation to the right if on the 8th file
		long right = ((whiteKing & eigthFile) << 1);
		long upRight = ((whiteKing & eigthFile) << 9);
		long downRight = ((whiteKing & eigthFile) >>> 7);

		long possibleMoves = (up | down | left | right | upRight | downRight
				| upLeft | downLeft);

		// TODO: Add another check to not let the king move onto a square under
		// attack by the opposite colour.
		return (possibleMoves);
	}

	public static long getKingMoves(long whiteKing, long whitePieces,
			long blackAttackingSquares, boolean whiteCastleKing,
			boolean whiteCastleQueen) {

		long castled = 0;

		if (whiteCastleKing
				&& ((whiteCastleKingSquares & blackAttackingSquares) == 0)
				&& ((whiteCastleKingSquares & whitePieces) == 0)) {
			castled = castled | whiteKing << 2;
		}

		if (whiteCastleQueen
				&& ((whiteCastleQueenSquares & blackAttackingSquares) == 0)
				&& ((whiteCastleQueenSquares & whitePieces) == 0)) {
			castled = castled | whiteKing >>> 2;
		}

		return ((getKingAttackingSquares(whiteKing, whitePieces) & ~blackAttackingSquares) | castled)
				& ~whitePieces;
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
