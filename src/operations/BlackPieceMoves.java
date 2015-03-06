package operations;

import java.util.ArrayList;

import board.BoardManager;
import board.FullGameState;

public class BlackPieceMoves {

	private static final long topLeftCorner = Long.parseLong(
			"100000000000000000000000000000000000000000000000000000000", 2);
	private static final long topRightCorner = Long.parseLong(
			"100000000000000000000000000000000000000000000000000000000000000",
			2) << 1;
	private static final long bottomLeftCorner = Long.parseLong("1", 2);
	private static final long bottomRightCorner = Long.parseLong("10000000", 2);

	protected static ArrayList<FullGameState> BlackKingMoves(
			long nextKingBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

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

					listOfMoves
							.add(new FullGameState(
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
									myGamestate.getWhiteKing(), myGamestate
											.getBlackPawns(), rookBitboard,
									myGamestate.getBlackKnights(), myGamestate
											.getBlackBishops(), myGamestate
											.getBlackQueens(),
									possiblePieceMoveBitboard, true,
									myGamestate.getWhiteCastleKing(),
									myGamestate.getWhiteCastleQueen(), false,
									false, 0, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));

				}
			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}
		return listOfMoves;
	}

	protected static ArrayList<FullGameState> BlackPawnMoves(
			long nextPawnBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

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
				int fromCoord = Long.toBinaryString(nextPawnBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				boolean enPassantMade = false;
				if (toCoord == myGamestate.getEnPassantSquare()) {
					enPassantMade = true;
				}

				long enPassantSquare;
				if (fromCoord == toCoord + 16) {
					enPassantSquare = toCoord + 8;
				} else {
					enPassantSquare = 100;
				}
				if (enPassantMade) {
					listOfMoves
							.add(new FullGameState(
									BitboardOperations
											.getPositionBitboard(myGamestate
													.getEnPassantSquare()) << 8
											^ myGamestate.getWhitePawns(),
									(myGamestate.getWhiteRooks() ^ possiblePieceMoveBitboard)
											& myGamestate.getWhiteRooks(),
									(myGamestate.getWhiteKnights() ^ possiblePieceMoveBitboard)
											& myGamestate.getWhiteKnights(),
									(myGamestate.getWhiteBishops() ^ possiblePieceMoveBitboard)
											& myGamestate.getWhiteBishops(),
									(myGamestate.getWhiteQueens() ^ possiblePieceMoveBitboard)
											& myGamestate.getWhiteQueens(),
									myGamestate.getWhiteKing(),
									possiblePieceMoveBitboard, myGamestate
											.getBlackRooks(), myGamestate
											.getBlackKnights(), myGamestate
											.getBlackBishops(), myGamestate
											.getBlackQueens(), myGamestate
											.getBlackKing(), true, myGamestate
											.getWhiteCastleKing(), myGamestate
											.getWhiteCastleQueen(), myGamestate
											.getBlackCastleKing(), myGamestate
											.getBlackCastleQueen(),
									enPassantSquare, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));
				}

				else if (toCoord <= 7) {
					listOfMoves
							.add(new FullGameState(
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
									possiblePieceMoveBitboard
											& BitboardOperations.clearRank(1),
									myGamestate.getBlackRooks(),
									myGamestate.getBlackKnights(),
									myGamestate.getBlackBishops(),
									myGamestate.getBlackQueens()
											| (Long.parseLong("1", 2) << toCoord),
									myGamestate.getBlackKing(), true,
									myGamestate.getWhiteCastleKing(),
									myGamestate.getWhiteCastleQueen(),
									myGamestate.getBlackCastleKing(),
									myGamestate.getBlackCastleQueen(),
									enPassantSquare, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));

					listOfMoves
							.add(new FullGameState(
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
									possiblePieceMoveBitboard
											& BitboardOperations.clearRank(1),
									myGamestate.getBlackRooks(),
									myGamestate.getBlackKnights()
											| (Long.parseLong("1", 2) << toCoord),
									myGamestate.getBlackBishops(), myGamestate
											.getBlackQueens(), myGamestate
											.getBlackKing(), true, myGamestate
											.getWhiteCastleKing(), myGamestate
											.getWhiteCastleQueen(), myGamestate
											.getBlackCastleKing(), myGamestate
											.getBlackCastleQueen(),
									enPassantSquare, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));

					listOfMoves
							.add(new FullGameState(
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
									possiblePieceMoveBitboard
											& BitboardOperations.clearRank(1),
									myGamestate.getBlackRooks(),
									myGamestate.getBlackKnights(),
									myGamestate.getBlackBishops()
											| (Long.parseLong("1", 2) << toCoord),
									myGamestate.getBlackQueens(), myGamestate
											.getBlackKing(), true, myGamestate
											.getWhiteCastleKing(), myGamestate
											.getWhiteCastleQueen(), myGamestate
											.getBlackCastleKing(), myGamestate
											.getBlackCastleQueen(),
									enPassantSquare, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));

					listOfMoves
							.add(new FullGameState(
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
									possiblePieceMoveBitboard
											& BitboardOperations.clearRank(1),
									myGamestate.getBlackRooks()
											| (Long.parseLong("1", 2) << toCoord),
									myGamestate.getBlackKnights(), myGamestate
											.getBlackBishops(), myGamestate
											.getBlackQueens(), myGamestate
											.getBlackKing(), true, myGamestate
											.getWhiteCastleKing(), myGamestate
											.getWhiteCastleQueen(), myGamestate
											.getBlackCastleKing(), myGamestate
											.getBlackCastleQueen(),
									enPassantSquare, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));

				} else {

					listOfMoves
							.add(new FullGameState(
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
									possiblePieceMoveBitboard, myGamestate
											.getBlackRooks(), myGamestate
											.getBlackKnights(), myGamestate
											.getBlackBishops(), myGamestate
											.getBlackQueens(), myGamestate
											.getBlackKing(), true, myGamestate
											.getWhiteCastleKing(), myGamestate
											.getWhiteCastleQueen(), myGamestate
											.getBlackCastleKing(), myGamestate
											.getBlackCastleQueen(),
									enPassantSquare, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));

				}
			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static ArrayList<FullGameState> BlackKnightMoves(
			long nextKnightBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

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
				int fromCoord = Long.toBinaryString(nextKnightBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				listOfMoves
						.add(new FullGameState(
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
								myGamestate.getWhiteKing(), myGamestate
										.getBlackPawns(), myGamestate
										.getBlackRooks(),
								possiblePieceMoveBitboard, myGamestate
										.getBlackBishops(), myGamestate
										.getBlackQueens(), myGamestate
										.getBlackKing(), true, myGamestate
										.getWhiteCastleKing(), myGamestate
										.getWhiteCastleQueen(), myGamestate
										.getBlackCastleKing(), myGamestate
										.getBlackCastleQueen(), 0, myGamestate
										.getNumberOfFullMoves(), myGamestate
										.getNumberOfHalfMoves(), fromCoord,
								toCoord));

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static ArrayList<FullGameState> BlackBishopMoves(
			long nextBishopBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

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
				int fromCoord = Long.toBinaryString(nextBishopBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				listOfMoves
						.add(new FullGameState(
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
								myGamestate.getWhiteKing(), myGamestate
										.getBlackPawns(), myGamestate
										.getBlackRooks(), myGamestate
										.getBlackKnights(),
								possiblePieceMoveBitboard, myGamestate
										.getBlackQueens(), myGamestate
										.getBlackKing(), true, myGamestate
										.getWhiteCastleKing(), myGamestate
										.getWhiteCastleQueen(), myGamestate
										.getBlackCastleKing(), myGamestate
										.getBlackCastleQueen(), 0, myGamestate
										.getNumberOfFullMoves(), myGamestate
										.getNumberOfHalfMoves(), fromCoord,
								toCoord));

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static ArrayList<FullGameState> BlackQueenMoves(
			long nextQueenBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

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
				int fromCoord = Long.toBinaryString(nextQueenBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				listOfMoves
						.add(new FullGameState(
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
								myGamestate.getWhiteKing(), myGamestate
										.getBlackPawns(), myGamestate
										.getBlackRooks(), myGamestate
										.getBlackKnights(), myGamestate
										.getBlackBishops(),
								possiblePieceMoveBitboard, myGamestate
										.getBlackKing(), true, myGamestate
										.getWhiteCastleKing(), myGamestate
										.getWhiteCastleQueen(), myGamestate
										.getBlackCastleKing(), myGamestate
										.getBlackCastleQueen(), 0, myGamestate
										.getNumberOfFullMoves(), myGamestate
										.getNumberOfHalfMoves(), fromCoord,
								toCoord));

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static ArrayList<FullGameState> BlackRookMoves(
			long nextRookBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

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
				int fromCoord = Long.toBinaryString(nextRookBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				if (fromCoord == 56) {
					listOfMoves
							.add(new FullGameState(
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
									myGamestate.getWhiteKing(), myGamestate
											.getBlackPawns(),
									possiblePieceMoveBitboard, myGamestate
											.getBlackKnights(), myGamestate
											.getBlackBishops(), myGamestate
											.getBlackQueens(), myGamestate
											.getBlackKing(), true, myGamestate
											.getWhiteCastleKing(), myGamestate
											.getWhiteCastleQueen(), myGamestate
											.getBlackCastleKing(), false, 0,
									myGamestate.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));
				} else if (fromCoord == 63) {
					listOfMoves
							.add(new FullGameState(
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
									myGamestate.getWhiteKing(), myGamestate
											.getBlackPawns(),
									possiblePieceMoveBitboard, myGamestate
											.getBlackKnights(), myGamestate
											.getBlackBishops(), myGamestate
											.getBlackQueens(), myGamestate
											.getBlackKing(), true, myGamestate
											.getWhiteCastleKing(), myGamestate
											.getWhiteCastleQueen(), false,
									myGamestate.getBlackCastleQueen(), 0,
									myGamestate.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));
				} else {
					listOfMoves
							.add(new FullGameState(
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
									myGamestate.getWhiteKing(), myGamestate
											.getBlackPawns(),
									possiblePieceMoveBitboard, myGamestate
											.getBlackKnights(), myGamestate
											.getBlackBishops(), myGamestate
											.getBlackQueens(), myGamestate
											.getBlackKing(), true, myGamestate
											.getWhiteCastleKing(), myGamestate
											.getWhiteCastleQueen(), myGamestate
											.getBlackCastleKing(), myGamestate
											.getBlackCastleQueen(), 0,
									myGamestate.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));
				}
			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	private static char[][] copyCurrentBoard(char[][] currentBoard) {
		char[][] temp = new char[currentBoard.length][currentBoard.length];

		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < temp.length; y++) {
				temp[x][y] = currentBoard[x][y];
			}
		}
		return temp;
	}

}
