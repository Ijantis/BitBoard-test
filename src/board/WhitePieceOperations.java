package board;

public class WhitePieceOperations {

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

	protected static long getWhitePawnMoves(long whitePawns,
			long occupiedSquares, long blackPieces) {
		return getWhitePawnMovesVertical(whitePawns, occupiedSquares)
				| getWhitePawnMovesDiagonal(whitePawns, blackPieces);
	}

	protected static long getWhitePawnMovesVertical(long whitePawns,
			long occupiedSquares) {

		// Vertical single rank
		long upOne = (whitePawns << 8) & ~occupiedSquares;

		// Moving up 2 spaces from 2nd rank
		long secondRankPawns = whitePawns & BitboardOperations.maskRank(2);
		long pawnsNotBlocked = (((secondRankPawns << 8) & ~occupiedSquares) >>> 8);
		long upTwo = pawnsNotBlocked << 16 & ~occupiedSquares;

		System.out.println("Exit");
		return upOne | upTwo;
	}

	/*
	 * Generate white pawn moves for attacking a piece
	 */
	protected static long getWhitePawnMovesDiagonal(long whitePawns,
			long blackPieces) {
		return getWhitePawnAttackingSquares(whitePawns) & blackPieces;
	}

	/*
	 * Generate squares under attack diagonally by a white pawn
	 */
	protected static long getWhitePawnAttackingSquares(long whitePawns) {
		long upLeft = ((whitePawns & BitboardOperations.clearFile(1)) << 7);
		long upRight = ((whitePawns & BitboardOperations.clearFile(8)) << 9);
		long attackingAPiece = (upLeft | upRight);

		return attackingAPiece;
	}

	protected static long getWhiteKnightMoves(long whiteKnights,
			long whitePieces) {
		return getWhiteKnightAttackingSquares(whiteKnights) & ~whitePieces;// &
																			// ~getWhitePieces();
	}

	protected static long getWhiteKnightAttackingSquares(long whiteKnights) {
		// variable names are first direction then second direction

		long upRight = (whiteKnights & BitboardOperations.clearFile(8)) << 17;
		long upLeft = (whiteKnights & BitboardOperations.clearFile(1)) << 15;

		long rightUp = (BitboardOperations.clearFile(8)
				& BitboardOperations.clearFile(7) & whiteKnights) << 10;
		long rightDown = (BitboardOperations.clearFile(8)
				& BitboardOperations.clearFile(7) & whiteKnights) >>> 6;

		long downRight = (whiteKnights & BitboardOperations.clearFile(8)) >>> 15;
		long downLeft = (whiteKnights & BitboardOperations.clearFile(1)) >>> 17;

		long leftUp = (BitboardOperations.clearFile(1)
				& BitboardOperations.clearFile(2) & whiteKnights) << 6;
		long leftDown = (BitboardOperations.clearFile(1)
				& BitboardOperations.clearFile(2) & whiteKnights) >>> 10;

		long possibleMoves = upRight | upLeft | rightUp | rightDown | downRight
				| downLeft | leftUp | leftDown;

		return possibleMoves;
	}

	protected static long getWhiteRookMoves(long rookBitboardToCalculate,
			long occupiedSquares, long whitePieces) {
		return getWhiteRookAttackingSquares(rookBitboardToCalculate,
				occupiedSquares) & ~whitePieces;
	}

	protected static long getWhiteRookAttackingSquares(
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
		// right_moves = right_moves & ~whitePieces;

		long left_moves = BitboardOperations.getLeftSquares(rookToMove)
				& occupiedSquares;
		left_moves = (left_moves >>> 1) | (left_moves >>> 2)
				| (left_moves >>> 3) | (left_moves >>> 4) | (left_moves >>> 5)
				| (left_moves >>> 6);
		left_moves = left_moves & BitboardOperations.getLeftSquares(rookToMove);
		left_moves = left_moves ^ BitboardOperations.getLeftSquares(rookToMove);
		// left_moves = left_moves & ~whitePieces;

		long up_moves = BitboardOperations.getUpSquares(rookToMove)
				& occupiedSquares;
		up_moves = (up_moves << 8) | (up_moves << 16) | (up_moves << 24)
				| (up_moves << 32) | (up_moves << 40) | (up_moves << 48);
		up_moves = up_moves & BitboardOperations.getUpSquares(rookToMove);
		up_moves = up_moves ^ BitboardOperations.getUpSquares(rookToMove);
		// up_moves = up_moves & ~whitePieces;

		long down_moves = BitboardOperations.getDownSquares(rookToMove)
				& occupiedSquares;
		down_moves = (down_moves >>> 8) | (down_moves >>> 16)
				| (down_moves >>> 24) | (down_moves >>> 32)
				| (down_moves >>> 40) | (down_moves >>> 48);
		down_moves = down_moves & BitboardOperations.getDownSquares(rookToMove);
		down_moves = down_moves ^ BitboardOperations.getDownSquares(rookToMove);
		// down_moves = down_moves & ~whitePieces;

		return left_moves | right_moves | up_moves | down_moves;
	}

	protected static long getWhiteBishopMoves(long whiteBishops,
			long occupiedSquares, long whitePieces) {
		return getWhiteBishopAttackingSquares(whiteBishops, occupiedSquares)
				& ~whitePieces;

	}

	protected static long getWhiteBishopAttackingSquares(long whiteBishops,
			long occupiedSquares) {

		long topRight = BitboardOperations.getTopRightSquares(whiteBishops)
				& occupiedSquares;
		topRight = (topRight << 9) | (topRight << 18) | (topRight << 27)
				| (topRight << 36) | (topRight << 45) | (topRight << 54);
		topRight = topRight
				& BitboardOperations.getTopRightSquares(whiteBishops);
		topRight = topRight
				^ BitboardOperations.getTopRightSquares(whiteBishops);
		// topRight = topRight & ~getWhitePieces();

		long bottomLeft = BitboardOperations.getBottomLeftSquares(whiteBishops)
				& occupiedSquares;
		bottomLeft = (bottomLeft >>> 9) | (bottomLeft >>> 18)
				| (bottomLeft >>> 27) | (bottomLeft >>> 36)
				| (bottomLeft >>> 45) | (bottomLeft >>> 54);
		bottomLeft = bottomLeft
				& BitboardOperations.getBottomLeftSquares(whiteBishops);
		bottomLeft = bottomLeft
				^ BitboardOperations.getBottomLeftSquares(whiteBishops);
		// bottomLeft = bottomLeft & ~getWhitePieces();

		long bottomRight = BitboardOperations
				.getBottomRightSquares(whiteBishops) & occupiedSquares;
		bottomRight = (bottomRight >>> 7) | (bottomRight >>> 14)
				| (bottomRight >>> 21) | (bottomRight >>> 28)
				| (bottomRight >>> 35) | (bottomRight >>> 42);
		bottomRight = bottomRight
				& BitboardOperations.getBottomRightSquares(whiteBishops);
		bottomRight = bottomRight
				^ BitboardOperations.getBottomRightSquares(whiteBishops);
		// bottomRight = bottomRight & ~getWhitePieces();

		long topLeft = BitboardOperations.getTopLeftSquares(whiteBishops)
				& occupiedSquares;
		topLeft = (topLeft << 7) | (topLeft << 14) | (topLeft << 21)
				| (topLeft << 28) | (topLeft << 35) | (topLeft << 42);
		topLeft = topLeft & BitboardOperations.getTopLeftSquares(whiteBishops);
		topLeft = topLeft ^ BitboardOperations.getTopLeftSquares(whiteBishops);
		// topLeft = topLeft & ~getWhitePieces();

		return topRight | topLeft | bottomRight | bottomLeft;
	}

	protected static long getWhiteQueenAttackingSquares(long whiteQueens,
			long occupiedSquares) {
		return getWhiteBishopAttackingSquares(whiteQueens, occupiedSquares)
				| getWhiteRookAttackingSquares(whiteQueens, occupiedSquares);
	}

	protected static long getWhiteQueenMoves(long whiteQueens,
			long occupiedSquares, long whitePieces) {
		return getWhiteQueenAttackingSquares(whiteQueens, occupiedSquares)
				& ~whitePieces;
	}

	/*
	 * Generate white king moves.
	 */
	private long getWhiteKingMoves(long whiteKing, long whitePieces,
			long blackAttackingSquares) {

		// vertical movement 8 bit shift
		long up = whiteKing << 8;
		long down = whiteKing >>> 8;

		// clear file stops move generation to the left if on the 1st file
		long left = ((whiteKing & BitboardOperations.clearFile(1)) >>> 1);
		long upLeft = (whiteKing & BitboardOperations.clearFile(1)) << 7;
		long downLeft = (whiteKing & BitboardOperations.clearFile(1)) >>> 9;

		// clear file stops move generation to the right if on the 8th file
		long right = ((whiteKing & BitboardOperations.clearFile(8)) << 1);
		long upRight = ((whiteKing & BitboardOperations.clearFile(8)) << 9);
		long downRight = ((whiteKing & BitboardOperations.clearFile(8)) >>> 7);

		long possibleMoves = (up | down | left | right | upRight | downRight
				| upLeft | downLeft);

		// TODO: Add another check to not let the king move onto a square under
		// attack by the opposite colour.
		return (possibleMoves & ~whitePieces) & ~blackAttackingSquares;
	}
}
