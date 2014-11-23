package board;

public class BlackPieces {

	private static void printBitBoard(long bitBoard) {
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

	protected static long getPawnMoves(long blackPawns,
			long occupiedSquares, long whitePieces) {
		return getPawnMovesVertical(blackPawns, occupiedSquares)
				| getPawnMovesDiagonal(blackPawns, whitePieces);
	}

	protected static long getPawnMovesVertical(long blackPawns,
			long occupiedSquares) {

		// Vertical single rank
		long upOne = (blackPawns >>> 8) & ~occupiedSquares;

		// Moving up 2 spaces from 2nd rank
		long seventhRankPawns = blackPawns & BitboardOperations.maskRank(7);
		long pawnsNotBlocked = (((seventhRankPawns << 8) & ~occupiedSquares) << 8);
		long upTwo = pawnsNotBlocked >>> 16 & ~occupiedSquares;

		System.out.println("Exit");
		return upOne | upTwo;
	}

	/*
	 * Generate black pawn moves for attacking a piece
	 */
	protected static long getPawnMovesDiagonal(long blackPawns,
			long whitePieces) {
		return getPawnAttackingSquares(blackPawns) & whitePieces;
	}

	/*
	 * Generate squares under attack diagonally by a black pawn
	 */
	protected static long getPawnAttackingSquares(long blackPawns) {
		long upLeft = ((blackPawns & BitboardOperations.clearFile(1)) >>> 7);
		long upRight = ((blackPawns & BitboardOperations.clearFile(8)) >>> 9);
		long attackingAPiece = (upLeft | upRight);

		return attackingAPiece;
	}

	protected static long getKnightMoves(long blackKnights,
			long blackPieces) {
		return getKnightAttackingSquares(blackKnights) & ~blackPieces;// &
																			// ~getblackPieces();
	}

	protected static long getKnightAttackingSquares(long blackKnights) {
		// variable names are first direction then second direction

		long upRight = (blackKnights & BitboardOperations.clearFile(8)) << 17;
		long upLeft = (blackKnights & BitboardOperations.clearFile(1)) << 15;

		long rightUp = (BitboardOperations.clearFile(8)
				& BitboardOperations.clearFile(7) & blackKnights) << 10;
		long rightDown = (BitboardOperations.clearFile(8)
				& BitboardOperations.clearFile(7) & blackKnights) >>> 6;

		long downRight = (blackKnights & BitboardOperations.clearFile(8)) >>> 15;
		long downLeft = (blackKnights & BitboardOperations.clearFile(1)) >>> 17;

		long leftUp = (BitboardOperations.clearFile(1)
				& BitboardOperations.clearFile(2) & blackKnights) << 6;
		long leftDown = (BitboardOperations.clearFile(1)
				& BitboardOperations.clearFile(2) & blackKnights) >>> 10;

		long possibleMoves = upRight | upLeft | rightUp | rightDown | downRight
				| downLeft | leftUp | leftDown;

		return possibleMoves;
	}

	protected static long getRookMoves(long rookBitboardToCalculate,
			long occupiedSquares, long blackPieces) {
		return getRookAttackingSquares(rookBitboardToCalculate,
				occupiedSquares) & ~blackPieces;
	}

	protected static long getRookAttackingSquares(
			long rookBitboardToCalculate, long occupiedSquares) {

		long nextRook = 0L;
		long allRookMoves = 0L;

		while (rookBitboardToCalculate != 0) {
			nextRook = Long.parseLong("1", 2);
			for (int i = 0; i < Long.toBinaryString(rookBitboardToCalculate)
					.length() - 1; i++) {
				if (Long.toBinaryString(nextRook).length() == 63) {
					nextRook = nextRook * 2;
				} else {
					nextRook = Long.parseLong(Long.toBinaryString(nextRook)
							+ "0", 2);
				}
			}
			if (rookBitboardToCalculate == 1) {
				allRookMoves = allRookMoves
						| getSingleRookMove(nextRook, occupiedSquares);
				break;
			} else {
				rookBitboardToCalculate = Long.parseLong(
						Long.toBinaryString(rookBitboardToCalculate).substring(
								1), 2);
				allRookMoves = allRookMoves
						| getSingleRookMove(nextRook, occupiedSquares);
			}
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

		long right_moves = BitboardOperations.getRightSquares(rookToMove)
				& occupiedSquares;
		right_moves = (right_moves << 1) | (right_moves << 2)
				| (right_moves << 3) | (right_moves << 4) | (right_moves << 5)
				| (right_moves << 6);
		right_moves = right_moves
				& BitboardOperations.getRightSquares(rookToMove);
		right_moves = right_moves
				^ BitboardOperations.getRightSquares(rookToMove);
		// right_moves = right_moves & ~blackPieces;

		long left_moves = BitboardOperations.getLeftSquares(rookToMove)
				& occupiedSquares;
		left_moves = (left_moves >>> 1) | (left_moves >>> 2)
				| (left_moves >>> 3) | (left_moves >>> 4) | (left_moves >>> 5)
				| (left_moves >>> 6);
		left_moves = left_moves & BitboardOperations.getLeftSquares(rookToMove);
		left_moves = left_moves ^ BitboardOperations.getLeftSquares(rookToMove);
		// left_moves = left_moves & ~blackPieces;

		long up_moves = BitboardOperations.getUpSquares(rookToMove)
				& occupiedSquares;
		up_moves = (up_moves << 8) | (up_moves << 16) | (up_moves << 24)
				| (up_moves << 32) | (up_moves << 40) | (up_moves << 48);
		up_moves = up_moves & BitboardOperations.getUpSquares(rookToMove);
		up_moves = up_moves ^ BitboardOperations.getUpSquares(rookToMove);
		// up_moves = up_moves & ~blackPieces;

		long down_moves = BitboardOperations.getDownSquares(rookToMove)
				& occupiedSquares;
		down_moves = (down_moves >>> 8) | (down_moves >>> 16)
				| (down_moves >>> 24) | (down_moves >>> 32)
				| (down_moves >>> 40) | (down_moves >>> 48);
		down_moves = down_moves & BitboardOperations.getDownSquares(rookToMove);
		down_moves = down_moves ^ BitboardOperations.getDownSquares(rookToMove);
		// down_moves = down_moves & ~blackPieces;

		return left_moves | right_moves | up_moves | down_moves;
	}

	protected static long getBishopMoves(long blackBishops,
			long occupiedSquares, long blackPieces) {
		return getBishopAttackingSquares(blackBishops, occupiedSquares)
				& ~blackPieces;

	}

	protected static long getBishopAttackingSquares(long blackBishops,
			long occupiedSquares) {

		long topRight = BitboardOperations.getTopRightSquares(blackBishops)
				& occupiedSquares;
		topRight = (topRight << 9) | (topRight << 18) | (topRight << 27)
				| (topRight << 36) | (topRight << 45) | (topRight << 54);
		topRight = topRight
				& BitboardOperations.getTopRightSquares(blackBishops);
		topRight = topRight
				^ BitboardOperations.getTopRightSquares(blackBishops);
		// topRight = topRight & ~getblackPieces();

		long bottomLeft = BitboardOperations.getBottomLeftSquares(blackBishops)
				& occupiedSquares;
		bottomLeft = (bottomLeft >>> 9) | (bottomLeft >>> 18)
				| (bottomLeft >>> 27) | (bottomLeft >>> 36)
				| (bottomLeft >>> 45) | (bottomLeft >>> 54);
		bottomLeft = bottomLeft
				& BitboardOperations.getBottomLeftSquares(blackBishops);
		bottomLeft = bottomLeft
				^ BitboardOperations.getBottomLeftSquares(blackBishops);
		// bottomLeft = bottomLeft & ~getblackPieces();

		long bottomRight = BitboardOperations
				.getBottomRightSquares(blackBishops) & occupiedSquares;
		bottomRight = (bottomRight >>> 7) | (bottomRight >>> 14)
				| (bottomRight >>> 21) | (bottomRight >>> 28)
				| (bottomRight >>> 35) | (bottomRight >>> 42);
		bottomRight = bottomRight
				& BitboardOperations.getBottomRightSquares(blackBishops);
		bottomRight = bottomRight
				^ BitboardOperations.getBottomRightSquares(blackBishops);
		// bottomRight = bottomRight & ~getblackPieces();

		long topLeft = BitboardOperations.getTopLeftSquares(blackBishops)
				& occupiedSquares;
		topLeft = (topLeft << 7) | (topLeft << 14) | (topLeft << 21)
				| (topLeft << 28) | (topLeft << 35) | (topLeft << 42);
		topLeft = topLeft & BitboardOperations.getTopLeftSquares(blackBishops);
		topLeft = topLeft ^ BitboardOperations.getTopLeftSquares(blackBishops);
		// topLeft = topLeft & ~getblackPieces();

		return topRight | topLeft | bottomRight | bottomLeft;
	}

	protected static long getQueenAttackingSquares(long blackQueens,
			long occupiedSquares) {
		return getBishopAttackingSquares(blackQueens, occupiedSquares)
				| getRookAttackingSquares(blackQueens, occupiedSquares);
	}

	protected static long getQueenMoves(long blackQueens,
			long occupiedSquares, long blackPieces) {
		return getQueenAttackingSquares(blackQueens, occupiedSquares)
				& ~blackPieces;
	}

	/*
	 * Generate black king moves.
	 */
	protected static long getKingAttackingSquares(long blackKing,
			long blackPieces) {

		// vertical movement 8 bit shift
		long up = blackKing << 8;
		long down = blackKing >>> 8;

		// clear file stops move generation to the left if on the 1st file
		long left = ((blackKing & BitboardOperations.clearFile(1)) >>> 1);
		long upLeft = (blackKing & BitboardOperations.clearFile(1)) << 7;
		long downLeft = (blackKing & BitboardOperations.clearFile(1)) >>> 9;

		// clear file stops move generation to the right if on the 8th file
		long right = ((blackKing & BitboardOperations.clearFile(8)) << 1);
		long upRight = ((blackKing & BitboardOperations.clearFile(8)) << 9);
		long downRight = ((blackKing & BitboardOperations.clearFile(8)) >>> 7);

		long possibleMoves = (up | down | left | right | upRight | downRight
				| upLeft | downLeft);

		// TODO: Add another check to not let the king move onto a square under
		// attack by the opposite colour.
		return (possibleMoves & ~blackPieces);
	}

	protected static long getKingMoves(long blackKing, long blackPieces,
			long whiteAttackingSquares) {
		return getKingAttackingSquares(blackKing, blackPieces)
				& ~whiteAttackingSquares;
	}
}
