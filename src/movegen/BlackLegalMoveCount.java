package movegen;

import board.BoardManager;
import board.FullGameState;

public class BlackLegalMoveCount {

	private static final long topLeftCorner = Long.parseLong(
			"100000000000000000000000000000000000000000000000000000000", 2);
	private static final long topRightCorner = Long.parseLong(
			"100000000000000000000000000000000000000000000000000000000000000",
			2) << 1;

	protected static long BlackKingMoves(long nextKingBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {
		long count = 0;

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKingBitboard ^ nextMove)
					^ myGamestate.getBlackKing();
			boolean isValid = BoardManager.IsSelfCheck(
					(myGamestate.getWhitePawns() ^ possiblePieceMoveBitboard)
							& myGamestate.getWhitePawns(),
					(myGamestate.getWhiteRooks() ^ possiblePieceMoveBitboard)
							& myGamestate.getWhiteRooks(),
					(myGamestate.getWhiteKnights() ^ possiblePieceMoveBitboard)
							& myGamestate.getWhiteKnights(),
					(myGamestate.getWhiteBishops() ^ possiblePieceMoveBitboard)
							& myGamestate.getWhiteBishops(),
					(myGamestate.getWhiteQueens() ^ possiblePieceMoveBitboard)
							& myGamestate.getWhiteQueens(),
					myGamestate.getWhiteKing(), myGamestate.getBlackPawns(),
					myGamestate.getBlackRooks(), myGamestate.getBlackKnights(),
					myGamestate.getBlackBishops(),
					myGamestate.getBlackQueens(), possiblePieceMoveBitboard,
					false);
			if (isValid) {
				int fromCoord = Long.toBinaryString(nextKingBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				boolean canMove = true;
				long rookBitboard = myGamestate.getBlackRooks();

				// king side
				if (fromCoord == 60 && toCoord == 62) {
					if ((rookBitboard & topRightCorner) == 0) {
						canMove = false;
					} else {
						rookBitboard = (~topRightCorner & rookBitboard)
								| topRightCorner >>> 2;
					}
				}
				// queen side
				else if (fromCoord == 60 && toCoord == 58) {
					if ((rookBitboard & topLeftCorner) == 0) {
						canMove = false;
					} else {
						rookBitboard = (~topLeftCorner & rookBitboard)
								| topLeftCorner << 3;
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

	protected static long BlackPawnMoves(long nextPawnBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {
		long count = 0;

		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextPawnBitboard ^ nextMove)
					^ myGamestate.getBlackPawns();
			if ((nextPawnBitboard & myGamestate.getWhiteAttackingSquares()) == 0
					&& (myGamestate.getBlackKing() & myGamestate
							.getWhiteAttackingSquares()) == 0) {
				isValid = true;
			} else {

				isValid = BoardManager
						.IsSelfCheck(
								(myGamestate.getWhitePawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhitePawns(),
								(myGamestate.getWhiteRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteRooks(),
								(myGamestate.getWhiteKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteKnights(),
								(myGamestate.getWhiteBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteBishops(),
								(myGamestate.getWhiteQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								possiblePieceMoveBitboard,
								myGamestate.getBlackRooks(),
								myGamestate.getBlackKnights(),
								myGamestate.getBlackBishops(),
								myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), false);
			}
			if (isValid) {
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				boolean enPassantMade = false;
				if (toCoord == myGamestate.getEnPassantSquare()) {
					enPassantMade = true;
				}

				if (enPassantMade) {
					count++;
				}

				else if (toCoord <= 7) {
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

	protected static long BlackKnightMoves(long nextKnightBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {
		long count = 0;

		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKnightBitboard ^ nextMove)
					^ myGamestate.getBlackKnights();

			if ((nextKnightBitboard & myGamestate.getWhiteAttackingSquares()) == 0
					&& (myGamestate.getBlackKing() & myGamestate
							.getWhiteAttackingSquares()) == 0) {
				isValid = true;
			} else {
				isValid = BoardManager
						.IsSelfCheck(
								(myGamestate.getWhitePawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhitePawns(),
								(myGamestate.getWhiteRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteRooks(),
								(myGamestate.getWhiteKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteKnights(),
								(myGamestate.getWhiteBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteBishops(),
								(myGamestate.getWhiteQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								myGamestate.getBlackPawns(),
								myGamestate.getBlackRooks(),
								possiblePieceMoveBitboard,
								myGamestate.getBlackBishops(),
								myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), false);
			}
			if (isValid) {
				count++;

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return count;
	}

	protected static long BlackBishopMoves(long nextBishopBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {
		long count = 0;

		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextBishopBitboard ^ nextMove)
					^ myGamestate.getBlackBishops();

			if ((nextBishopBitboard & myGamestate.getWhiteAttackingSquares()) == 0
					&& (myGamestate.getBlackKing() & myGamestate
							.getWhiteAttackingSquares()) == 0) {
				isValid = true;
			} else {
				isValid = BoardManager
						.IsSelfCheck(
								(myGamestate.getWhitePawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhitePawns(),
								(myGamestate.getWhiteRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteRooks(),
								(myGamestate.getWhiteKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteKnights(),
								(myGamestate.getWhiteBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteBishops(),
								(myGamestate.getWhiteQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								myGamestate.getBlackPawns(),
								myGamestate.getBlackRooks(),
								myGamestate.getBlackKnights(),
								possiblePieceMoveBitboard,
								myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), false);
			}

			if (isValid) {
				count++;

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return count;
	}

	protected static long BlackQueenMoves(long nextQueenBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {
		long count = 0;

		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextQueenBitboard ^ nextMove)
					^ myGamestate.getBlackQueens();
			if ((nextQueenBitboard & myGamestate.getWhiteAttackingSquares()) == 0
					&& (myGamestate.getBlackKing() & myGamestate
							.getWhiteAttackingSquares()) == 0) {
				isValid = true;
			} else {
				isValid = BoardManager
						.IsSelfCheck(
								(myGamestate.getWhitePawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhitePawns(),
								(myGamestate.getWhiteRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteRooks(),
								(myGamestate.getWhiteKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteKnights(),
								(myGamestate.getWhiteBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteBishops(),
								(myGamestate.getWhiteQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								myGamestate.getBlackPawns(),
								myGamestate.getBlackRooks(),
								myGamestate.getBlackKnights(),
								myGamestate.getBlackBishops(),
								possiblePieceMoveBitboard,
								myGamestate.getBlackKing(), false);
			}

			if (isValid) {

				count++;
			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return count;
	}

	protected static long BlackRookMoves(long nextRookBitboard,
			long possibleMovesBitboard, FullGameState myGamestate) {
		long count = 0;

		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextRookBitboard ^ nextMove)
					^ myGamestate.getBlackRooks();
			if ((nextRookBitboard & myGamestate.getWhiteAttackingSquares()) == 0
					&& (myGamestate.getBlackKing() & myGamestate
							.getWhiteAttackingSquares()) == 0) {
				isValid = true;
			} else {
				isValid = BoardManager
						.IsSelfCheck(
								(myGamestate.getWhitePawns() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhitePawns(),
								(myGamestate.getWhiteRooks() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteRooks(),
								(myGamestate.getWhiteKnights() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteKnights(),
								(myGamestate.getWhiteBishops() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteBishops(),
								(myGamestate.getWhiteQueens() ^ possiblePieceMoveBitboard)
										& myGamestate.getWhiteQueens(),
								myGamestate.getWhiteKing(),
								myGamestate.getBlackPawns(),
								possiblePieceMoveBitboard,
								myGamestate.getBlackKnights(),
								myGamestate.getBlackBishops(),
								myGamestate.getBlackQueens(),
								myGamestate.getBlackKing(), false);
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
