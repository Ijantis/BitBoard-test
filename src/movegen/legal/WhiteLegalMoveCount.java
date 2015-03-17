package movegen.legal;

import board.BoardManager;
import board.FullGameState;

public class WhiteLegalMoveCount {

	private static final long bottomLeftCorner = Long.parseLong("1", 2);
	private static final long bottomRightCorner = Long.parseLong("10000000", 2);

	public static long whiteKingMoves(long nextKingBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {

		long count = 0;

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move

			long possiblePieceMoveBitboard = (nextKingBitboard ^ nextMove)
					^ myGamestate.getWhiteKing();

			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getWhitePawns(), myGamestate.getWhiteRooks(),
					myGamestate.getWhiteKnights(),
					myGamestate.getWhiteBishops(),
					myGamestate.getWhiteQueens(), possiblePieceMoveBitboard,
					(myGamestate.getBlackPawns() ^ possiblePieceMoveBitboard)
							& myGamestate.getBlackPawns(),
					(myGamestate.getBlackRooks() ^ possiblePieceMoveBitboard)
							& myGamestate.getBlackRooks(),
					(myGamestate.getBlackKnights() ^ possiblePieceMoveBitboard)
							& myGamestate.getBlackKnights(),
					(myGamestate.getBlackBishops() ^ possiblePieceMoveBitboard)
							& myGamestate.getBlackBishops(),
					(myGamestate.getBlackQueens() ^ possiblePieceMoveBitboard)
							& myGamestate.getBlackQueens(),
					myGamestate.getBlackKing(), true);
			if (isValid) {
				int fromCoord = Long.toBinaryString(nextKingBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				long rookBitboard = myGamestate.getWhiteRooks();
				boolean canMove = true;

				if (fromCoord == 4 && toCoord == 6) {
					if ((rookBitboard & bottomRightCorner) == 0) {
						canMove = false;
					} else {
						rookBitboard = (~bottomRightCorner & rookBitboard)
								| bottomRightCorner >>> 2;
					}
				} else if (fromCoord == 4 && toCoord == 2) {
					if ((rookBitboard & bottomLeftCorner) == 0) {
						canMove = false;
					} else {
						rookBitboard = (~bottomLeftCorner & rookBitboard)
								| bottomLeftCorner << 3;
					}
				}
				if (canMove) {

					count++;
				}
			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}
		return count;
	}

	public static long whitePawnMoves(long nextPawnBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {

		long nextMove;
		long count = 0;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			long possiblePieceMoveBitboard = (nextPawnBitboard ^ nextMove)
					^ myGamestate.getWhitePawns();

			// If the piece is not under attack we don't need to check whether
			// the move puts the same colour king in check
			if ((nextPawnBitboard & myGamestate.getBlackAttackingSquares()) == 0
					&& (myGamestate.getWhiteKing() & myGamestate
							.getBlackAttackingSquares()) == 0) {
				isValid = true;
			} else {

				// this is the bitboard after having moved the piece to the next
				// move
				isValid = BoardManager
						.IsSelfCheck(
								possiblePieceMoveBitboard,
								myGamestate.getWhiteRooks(),
								myGamestate.getWhiteKnights(),
								myGamestate.getWhiteBishops(),
								myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								(myGamestate.getBlackPawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackPawns(),
								(myGamestate.getBlackRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackRooks(),
								(myGamestate.getBlackKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackKnights(),
								(myGamestate.getBlackBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackBishops(),
								(myGamestate.getBlackQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), true);
			}
			if (isValid) {
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				boolean enPassantMade = false;
				if (toCoord == myGamestate.getEnPassantSquare()) {
					enPassantMade = true;
				}

				if (enPassantMade) {
					count++;
				} else if (toCoord >= 56) {

					count += 4;

				} else {

					count++;

				}

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return count;
	}

	public static long whiteKnightMoves(long nextKnightBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {
		long count = 0;

		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKnightBitboard ^ nextMove)
					^ myGamestate.getWhiteKnights();
			if ((nextKnightBitboard & myGamestate.getBlackAttackingSquares()) == 0
					&& (myGamestate.getWhiteKing() & myGamestate
							.getBlackAttackingSquares()) == 0) {
				isValid = true;
			} else {

				isValid = BoardManager
						.IsSelfCheck(
								myGamestate.getWhitePawns(),
								myGamestate.getWhiteRooks(),
								possiblePieceMoveBitboard,
								myGamestate.getWhiteBishops(),
								myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								(myGamestate.getBlackPawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackPawns(),
								(myGamestate.getBlackRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackRooks(),
								(myGamestate.getBlackKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackKnights(),
								(myGamestate.getBlackBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackBishops(),
								(myGamestate.getBlackQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), true);
			}
			if (isValid) {

				count++;

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return count;
	}

	public static long whiteBishopMoves(long nextBishopBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {
		long count = 0;

		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			long possiblePieceMoveBitboard = (nextBishopBitboard ^ nextMove)
					^ myGamestate.getWhiteBishops();

			if ((nextBishopBitboard & myGamestate.getBlackAttackingSquares()) == 0
					&& (myGamestate.getWhiteKing() & myGamestate
							.getBlackAttackingSquares()) == 0) {
				isValid = true;
			} else {

				// this is the bitboard after having moved the piece to the next
				// move
				isValid = BoardManager
						.IsSelfCheck(
								myGamestate.getWhitePawns(),
								myGamestate.getWhiteRooks(),
								myGamestate.getWhiteKnights(),
								possiblePieceMoveBitboard,
								myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								(myGamestate.getBlackPawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackPawns(),
								(myGamestate.getBlackRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackRooks(),
								(myGamestate.getBlackKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackKnights(),
								(myGamestate.getBlackBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackBishops(),
								(myGamestate.getBlackQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), true);
			}

			if (isValid) {

				count++;

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return count;
	}

	public static long whiteQueenMoves(long nextQueenBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {

		long count = 0;
		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextQueenBitboard ^ nextMove)
					^ myGamestate.getWhiteQueens();

			if ((nextQueenBitboard & myGamestate.getBlackAttackingSquares()) == 0
					&& (myGamestate.getWhiteKing() & myGamestate
							.getBlackAttackingSquares()) == 0) {
				isValid = true;
			} else {

				isValid = BoardManager
						.IsSelfCheck(
								myGamestate.getWhitePawns(),
								myGamestate.getWhiteRooks(),
								myGamestate.getWhiteKnights(),
								myGamestate.getWhiteBishops(),
								possiblePieceMoveBitboard,
								myGamestate.getWhiteKing(),
								(myGamestate.getBlackPawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackPawns(),
								(myGamestate.getBlackRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackRooks(),
								(myGamestate.getBlackKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackKnights(),
								(myGamestate.getBlackBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackBishops(),
								(myGamestate.getBlackQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), true);
			}

			if (isValid) {

				count++;

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return count;
	}

	public static long whiteRookMoves(long nextRookBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {

		long count = 0;
		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextRookBitboard ^ nextMove)
					^ myGamestate.getWhiteRooks();
			if ((nextRookBitboard & myGamestate.getBlackAttackingSquares()) == 0
					&& (myGamestate.getWhiteKing() & myGamestate
							.getBlackAttackingSquares()) == 0) {
				isValid = true;
			} else {
				isValid = BoardManager
						.IsSelfCheck(
								myGamestate.getWhitePawns(),
								possiblePieceMoveBitboard,
								myGamestate.getWhiteKnights(),
								myGamestate.getWhiteBishops(),
								myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								(myGamestate.getBlackPawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackPawns(),
								(myGamestate.getBlackRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackRooks(),
								(myGamestate.getBlackKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackKnights(),
								(myGamestate.getBlackBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackBishops(),
								(myGamestate.getBlackQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), true);
			}
			if (isValid) {
				count++;

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return count;
	}

}
