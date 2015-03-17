package movegen;

import java.util.ArrayList;

import bitboards.BitboardOperations;
import board.BoardManager;
import board.FullGameState;

public class WhiteLegalMoves {

	private static final long bottomLeftCorner = Long.parseLong("1", 2);
	private static final long bottomRightCorner = Long.parseLong("10000000", 2);

	protected static ArrayList<FullGameState> whiteKingMoves(
			long nextKingBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

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

					listOfMoves
							.add(new FullGameState(
									myGamestate.getWhitePawns(),
									rookBitboard,
									myGamestate.getWhiteKnights(),
									myGamestate.getWhiteBishops(),
									myGamestate.getWhiteQueens(),
									possiblePieceMoveBitboard,
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
									myGamestate.getBlackKing(), false, false,
									false, myGamestate.getBlackCastleKing(),
									myGamestate.getBlackCastleQueen(), 0,
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

	protected static ArrayList<FullGameState> whitePawnMoves(
			long nextPawnBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

		long nextMove;
		boolean isValid = false;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			long possiblePieceMoveBitboard = (nextPawnBitboard ^ nextMove)
					^ myGamestate.getWhitePawns();

			// If the piece is not under attack we dont need to check whether
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
				int fromCoord = Long.toBinaryString(nextPawnBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				boolean enPassantMade = false;
				if (toCoord == myGamestate.getEnPassantSquare()) {
					enPassantMade = true;
				}

				long enPassantSquare;
				if (fromCoord == toCoord - 16) {
					enPassantSquare = toCoord - 8;
				} else {
					enPassantSquare = 100;
				}

				if (enPassantMade) {
					listOfMoves
							.add(new FullGameState(
									possiblePieceMoveBitboard,
									myGamestate.getWhiteRooks(),
									myGamestate.getWhiteKnights(),
									myGamestate.getWhiteBishops(),
									myGamestate.getWhiteQueens(),
									myGamestate.getWhiteKing(),
									BitboardOperations
											.getPositionBitboard(myGamestate
													.getEnPassantSquare()) >>> 8
											^ myGamestate.getBlackPawns()
											& myGamestate.getBlackPawns(),
									(myGamestate.getBlackRooks() ^ possiblePieceMoveBitboard)
											& myGamestate.getBlackRooks(),
									(myGamestate.getBlackKnights() ^ possiblePieceMoveBitboard)
											& myGamestate.getBlackKnights(),
									(myGamestate.getBlackBishops() ^ possiblePieceMoveBitboard)
											& myGamestate.getBlackBishops(),
									(myGamestate.getBlackQueens() ^ possiblePieceMoveBitboard)
											& myGamestate.getBlackQueens(),
									myGamestate.getBlackKing(), false,
									myGamestate.getWhiteCastleKing(),
									myGamestate.getWhiteCastleQueen(),
									myGamestate.getBlackCastleKing(),
									myGamestate.getBlackCastleQueen(),
									enPassantSquare, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));
				} else if (toCoord >= 56) {

					listOfMoves
							.add(new FullGameState(
									possiblePieceMoveBitboard
											& BitboardOperations.clearRank(8),
									myGamestate.getWhiteRooks(),
									myGamestate.getWhiteKnights(),
									myGamestate.getWhiteBishops(),
									myGamestate.getWhiteQueens()
											| (Long.parseLong("1", 2) << toCoord),
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
									myGamestate.getBlackKing(), false,
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
									possiblePieceMoveBitboard
											& BitboardOperations.clearRank(8),
									myGamestate.getWhiteRooks(),
									myGamestate.getWhiteKnights()
											| (Long.parseLong("1", 2) << toCoord),
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
									myGamestate.getBlackKing(), false,
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
									possiblePieceMoveBitboard
											& BitboardOperations.clearRank(8),
									myGamestate.getWhiteRooks(),
									myGamestate.getWhiteKnights(),
									myGamestate.getWhiteBishops()
											| (Long.parseLong("1", 2) << toCoord),
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
									myGamestate.getBlackKing(), false,
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
									possiblePieceMoveBitboard
											& BitboardOperations.clearRank(8),
									myGamestate.getWhiteRooks()
											| (Long.parseLong("1", 2) << toCoord),
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
									myGamestate.getBlackKing(), false,
									myGamestate.getWhiteCastleKing(),
									myGamestate.getWhiteCastleQueen(),
									myGamestate.getBlackCastleKing(),
									myGamestate.getBlackCastleQueen(),
									enPassantSquare, myGamestate
											.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));

				} else {

					listOfMoves
							.add(new FullGameState(
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
									myGamestate.getBlackKing(), false,
									myGamestate.getWhiteCastleKing(),
									myGamestate.getWhiteCastleQueen(),
									myGamestate.getBlackCastleKing(),
									myGamestate.getBlackCastleQueen(),
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

	protected static ArrayList<FullGameState> whiteKnightMoves(
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
				int fromCoord = Long.toBinaryString(nextKnightBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				listOfMoves
						.add(new FullGameState(
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
								myGamestate.getBlackKing(), false, myGamestate
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

	protected static ArrayList<FullGameState> whiteBishopMoves(
			long nextBishopBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

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
				int fromCoord = Long.toBinaryString(nextBishopBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				listOfMoves
						.add(new FullGameState(
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
								myGamestate.getBlackKing(), false, myGamestate
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

	protected static ArrayList<FullGameState> whiteQueenMoves(
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
				int fromCoord = Long.toBinaryString(nextQueenBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				listOfMoves.add(new FullGameState(myGamestate.getWhitePawns(),
						myGamestate.getWhiteRooks(), myGamestate
								.getWhiteKnights(), myGamestate
								.getWhiteBishops(), possiblePieceMoveBitboard,
						myGamestate.getWhiteKing(), (myGamestate
								.getBlackPawns() ^ possiblePieceMoveBitboard)
								& myGamestate.getBlackPawns(), (myGamestate
								.getBlackRooks() ^ possiblePieceMoveBitboard)
								& myGamestate.getBlackRooks(), (myGamestate
								.getBlackKnights() ^ possiblePieceMoveBitboard)
								& myGamestate.getBlackKnights(), (myGamestate
								.getBlackBishops() ^ possiblePieceMoveBitboard)
								& myGamestate.getBlackBishops(), (myGamestate
								.getBlackQueens() ^ possiblePieceMoveBitboard)
								& myGamestate.getBlackQueens(), myGamestate
								.getBlackKing(), false, myGamestate
								.getWhiteCastleKing(), myGamestate
								.getWhiteCastleQueen(), myGamestate
								.getBlackCastleKing(), myGamestate
								.getBlackCastleQueen(), 0, myGamestate
								.getNumberOfFullMoves(), myGamestate
								.getNumberOfHalfMoves(), fromCoord, toCoord));

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static ArrayList<FullGameState> whiteRookMoves(
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
				int fromCoord = Long.toBinaryString(nextRookBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				if (fromCoord == 0) {
					listOfMoves
							.add(new FullGameState(
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
									myGamestate.getBlackKing(), false,
									myGamestate.getWhiteCastleKing(), false,
									myGamestate.getBlackCastleKing(),
									myGamestate.getBlackCastleQueen(), 0,
									myGamestate.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));
				} else if (fromCoord == 7) {
					listOfMoves
							.add(new FullGameState(
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
									myGamestate.getBlackKing(), false, false,
									myGamestate.getWhiteCastleQueen(),
									myGamestate.getBlackCastleKing(),
									myGamestate.getBlackCastleQueen(), 0,
									myGamestate.getNumberOfFullMoves(),
									myGamestate.getNumberOfHalfMoves(),
									fromCoord, toCoord));
				} else {
					listOfMoves
							.add(new FullGameState(
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
									myGamestate.getBlackKing(), false,
									myGamestate.getWhiteCastleKing(),
									myGamestate.getWhiteCastleQueen(),
									myGamestate.getBlackCastleKing(),
									myGamestate.getBlackCastleQueen(), 0,
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

}
