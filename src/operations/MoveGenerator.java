package operations;

import java.util.Vector;

import operations.pieces.BlackPieces;
import operations.pieces.WhitePieces;
import board.BoardManager;
import board.FullGameState;

public class MoveGenerator {

	private static final long topLeftCorner = Long.parseLong(
			"100000000000000000000000000000000000000000000000000000000", 2);
	private static final long topRightCorner = Long.parseLong(
			"100000000000000000000000000000000000000000000000000000000000000",
			2) << 1;
	private static final long bottomLeftCorner = Long.parseLong("1", 2);
	private static final long bottomRightCorner = Long.parseLong("10000000", 2);

	/*
	 * NOTE: Do not return a Vector<FullGameState> return squares numbers
	 * instead.
	 */
	public static Vector<FullGameState> generateWhiteLegalMoves(
			FullGameState myGamestate) {

		Vector<FullGameState> possibleStates = new Vector<FullGameState>(20,
				200);
		long temp;

		temp = myGamestate.getWhitePawns();
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					myGamestate.getOccupiedSquares(),
					myGamestate.getBlackPieces(),
					myGamestate.getEnPassantSquare());
			possibleStates.addAll(whitePawnMoves(nextPawn, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getWhiteKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getKnightMoves(nextKnight,
					myGamestate.getWhitePieces());
			possibleStates.addAll(whiteKnightMoves(nextKnight, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getWhiteBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getBishopMoves(nextBishop,
					myGamestate.getOccupiedSquares(),
					myGamestate.getWhitePieces());
			possibleStates.addAll(whiteBishopMoves(nextBishop, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getWhiteQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(nextQueen,
					myGamestate.getOccupiedSquares(),
					myGamestate.getWhitePieces());
			possibleStates.addAll(whiteQueenMoves(nextQueen, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getWhiteRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getRookMoves(nextRook,
					myGamestate.getOccupiedSquares(),
					myGamestate.getWhitePieces());
			possibleStates.addAll(whiteRookMoves(nextRook, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = WhitePieces.getKingMoves(
				myGamestate.getWhiteKing(), myGamestate.getWhitePieces(),
				myGamestate.getBlackAttackingSquares(),
				myGamestate.getWhiteCastleKing(),
				myGamestate.getWhiteCastleQueen());
		possibleStates.addAll(whiteKingMoves(myGamestate.getWhiteKing(),
				kingMovesBitboard, myGamestate));

		return possibleStates;
	}

	protected static Vector<FullGameState> whiteKingMoves(
			long nextKingBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

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
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextKingBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				boolean canMove = true;
				long rookBitboard = myGamestate.getWhiteRooks();

				if (fromCoord == 4 && toCoord == 6) {
					if (temp[7][0] != 'R') {
						canMove = false;
					} else {
						temp[5][0] = 'R';
						temp[7][0] = ' ';
						rookBitboard = (~bottomRightCorner & rookBitboard)
								| bottomRightCorner >>> 2;

					}

				} else if (fromCoord == 4 && toCoord == 2) {
					if (temp[0][0] != 'R') {
						canMove = false;
					} else {
						temp[3][0] = 'R';
						temp[0][0] = ' ';
						rookBitboard = (~bottomLeftCorner & rookBitboard)
								| bottomLeftCorner << 3;
					}
				}

				if (canMove) {

					listOfMoves
							.add(new FullGameState(
									temp,
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

	protected static Vector<FullGameState> whitePawnMoves(
			long nextPawnBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextPawnBitboard ^ nextMove)
					^ myGamestate.getWhitePawns();
			boolean isValid = BoardManager.IsSelfCheck(
					possiblePieceMoveBitboard, myGamestate.getWhiteRooks(),
					myGamestate.getWhiteKnights(),
					myGamestate.getWhiteBishops(),
					myGamestate.getWhiteQueens(), myGamestate.getWhiteKing(),
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
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextPawnBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				if (toCoord == myGamestate.getEnPassantSquare()) {
					temp[(int) (((myGamestate.getEnPassantSquare() - 8) % 8))][(int) (((myGamestate
							.getEnPassantSquare() - 8) / 8))] = ' ';
				}

				long enPassantSquare;
				if (fromCoord == toCoord - 16) {
					enPassantSquare = toCoord - 8;
				} else {
					enPassantSquare = 0;
				}

				if (toCoord >= 56) {
					temp[toCoord % 8][toCoord / 8] = 'Q';

					listOfMoves
							.add(new FullGameState(
									copyCurrentBoard(temp),
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

					temp[toCoord % 8][toCoord / 8] = 'N';

					listOfMoves
							.add(new FullGameState(
									copyCurrentBoard(temp),
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

					temp[toCoord % 8][toCoord / 8] = 'B';

					listOfMoves
							.add(new FullGameState(
									copyCurrentBoard(temp),
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

					temp[toCoord % 8][toCoord / 8] = 'R';

					listOfMoves
							.add(new FullGameState(
									temp,
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
									temp,
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

	protected static Vector<FullGameState> whiteKnightMoves(
			long nextKnightBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKnightBitboard ^ nextMove)
					^ myGamestate.getWhiteKnights();
			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getWhitePawns(), myGamestate.getWhiteRooks(),
					possiblePieceMoveBitboard, myGamestate.getWhiteBishops(),
					myGamestate.getWhiteQueens(), myGamestate.getWhiteKing(),
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
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextKnightBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves
						.add(new FullGameState(
								temp,
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

	protected static Vector<FullGameState> whiteBishopMoves(
			long nextBishopBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextBishopBitboard ^ nextMove)
					^ myGamestate.getWhiteBishops();
			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getWhitePawns(), myGamestate.getWhiteRooks(),
					myGamestate.getWhiteKnights(), possiblePieceMoveBitboard,
					myGamestate.getWhiteQueens(), myGamestate.getWhiteKing(),
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
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextBishopBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves
						.add(new FullGameState(
								temp,
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

	protected static Vector<FullGameState> whiteQueenMoves(
			long nextQueenBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextQueenBitboard ^ nextMove)
					^ myGamestate.getWhiteQueens();
			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getWhitePawns(), myGamestate.getWhiteRooks(),
					myGamestate.getWhiteKnights(),
					myGamestate.getWhiteBishops(), possiblePieceMoveBitboard,
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

			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextQueenBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(new FullGameState(temp, myGamestate
						.getWhitePawns(), myGamestate.getWhiteRooks(),
						myGamestate.getWhiteKnights(), myGamestate
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

	protected static Vector<FullGameState> whiteRookMoves(
			long nextRookBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextRookBitboard ^ nextMove)
					^ myGamestate.getWhiteRooks();

			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getWhitePawns(), possiblePieceMoveBitboard,
					myGamestate.getWhiteKnights(),
					myGamestate.getWhiteBishops(),
					myGamestate.getWhiteQueens(), myGamestate.getWhiteKing(),
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
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextRookBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				if (fromCoord == 0) {
					listOfMoves
							.add(new FullGameState(
									temp,
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
									temp,
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
									temp,
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

	public static Vector<FullGameState> generateBlackLegalMoves(
			FullGameState myGamestate) {

		Vector<FullGameState> possibleStates = new Vector<FullGameState>(20, 20);
		long temp;

		temp = myGamestate.getBlackPawns();
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getPawnMoves(nextPawn,
					myGamestate.getOccupiedSquares(),
					myGamestate.getWhitePieces(),
					myGamestate.getEnPassantSquare());
			possibleStates.addAll(BlackPawnMoves(nextPawn, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getBlackKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getKnightMoves(nextKnight,
					myGamestate.getBlackPieces());
			possibleStates.addAll(BlackKnightMoves(nextKnight, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getBlackBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getBishopMoves(nextBishop,
					myGamestate.getOccupiedSquares(),
					myGamestate.getBlackPieces());
			possibleStates.addAll(BlackBishopMoves(nextBishop, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getBlackQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getQueenMoves(nextQueen,
					myGamestate.getOccupiedSquares(),
					myGamestate.getBlackPieces());
			possibleStates.addAll(BlackQueenMoves(nextQueen, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getBlackRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getRookMoves(nextRook,
					myGamestate.getOccupiedSquares(),
					myGamestate.getBlackPieces());
			possibleStates.addAll(BlackRookMoves(nextRook, bitboardOfMoves,
					myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = BlackPieces.getKingMoves(
				myGamestate.getBlackKing(), myGamestate.getBlackPieces(),
				myGamestate.getWhiteAttackingSquares(),
				myGamestate.getBlackCastleKing(),
				myGamestate.getBlackCastleQueen());
		possibleStates.addAll(BlackKingMoves(myGamestate.getBlackKing(),
				kingMovesBitboard, myGamestate));

		return possibleStates;
	}

	protected static Vector<FullGameState> BlackKingMoves(
			long nextKingBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

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
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextKingBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				boolean canMove = true;
				long rookBitboard = myGamestate.getBlackRooks();

				// king side
				if (fromCoord == 60 && toCoord == 62) {
					if (temp[7][7] != 'r') {
						canMove = false;
					} else {
						temp[5][7] = 'r';
						temp[7][7] = ' ';
						rookBitboard = (~topRightCorner & rookBitboard)
								| topRightCorner >>> 2;
					}
					// queen side
				} else if (fromCoord == 60 && toCoord == 58) {
					if (temp[0][7] != 'r') {
						canMove = false;
					} else {
						temp[3][7] = 'r';
						temp[0][7] = ' ';
						rookBitboard = (~topLeftCorner & rookBitboard)
								| topLeftCorner << 3;
					}
				}

				if (canMove) {

					listOfMoves
							.add(new FullGameState(
									temp,
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

	protected static Vector<FullGameState> BlackPawnMoves(
			long nextPawnBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextPawnBitboard ^ nextMove)
					^ myGamestate.getBlackPawns();
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
					myGamestate.getWhiteKing(), possiblePieceMoveBitboard,
					myGamestate.getBlackRooks(), myGamestate.getBlackKnights(),
					myGamestate.getBlackBishops(),
					myGamestate.getBlackQueens(), myGamestate.getBlackKing(),
					false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextPawnBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				if (toCoord == myGamestate.getEnPassantSquare()) {
					temp[(int) (((myGamestate.getEnPassantSquare() + 8) % 8))][(int) (((myGamestate
							.getEnPassantSquare() + 8) / 8))] = ' ';
				}

				long enPassantSquare;
				if (fromCoord == toCoord + 16) {
					enPassantSquare = toCoord + 8;
				} else {
					enPassantSquare = 0;
				}

				if (toCoord <= 7) {
					temp[toCoord % 8][toCoord / 8] = 'q';

					listOfMoves
							.add(new FullGameState(
									copyCurrentBoard(temp),
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
											& BitboardOperations.clearRank(8),
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

					temp[toCoord % 8][toCoord / 8] = 'n';

					listOfMoves
							.add(new FullGameState(
									copyCurrentBoard(temp),
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
											& BitboardOperations.clearRank(8),
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

					temp[toCoord % 8][toCoord / 8] = 'b';

					listOfMoves
							.add(new FullGameState(
									copyCurrentBoard(temp),
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
											& BitboardOperations.clearRank(8),
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

					temp[toCoord % 8][toCoord / 8] = 'r';

					listOfMoves
							.add(new FullGameState(
									copyCurrentBoard(temp),
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
											& BitboardOperations.clearRank(8),
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
									temp,
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

	protected static Vector<FullGameState> BlackKnightMoves(
			long nextKnightBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKnightBitboard ^ nextMove)
					^ myGamestate.getBlackKnights();
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
					myGamestate.getBlackRooks(), possiblePieceMoveBitboard,
					myGamestate.getBlackBishops(),
					myGamestate.getBlackQueens(), myGamestate.getBlackKing(),
					false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextKnightBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(new FullGameState(temp, (myGamestate
						.getWhitePawns() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhitePawns(), (myGamestate
						.getWhiteRooks() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteRooks(), (myGamestate
						.getWhiteKnights() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteKnights(), (myGamestate
						.getWhiteBishops() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteBishops(), (myGamestate
						.getWhiteQueens() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteQueens(), myGamestate
						.getWhiteKing(), myGamestate.getBlackPawns(),
						myGamestate.getBlackRooks(), possiblePieceMoveBitboard,
						myGamestate.getBlackBishops(), myGamestate
								.getBlackQueens(), myGamestate.getBlackKing(),
						true, myGamestate.getWhiteCastleKing(), myGamestate
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

	protected static Vector<FullGameState> BlackBishopMoves(
			long nextBishopBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextBishopBitboard ^ nextMove)
					^ myGamestate.getBlackBishops();
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
					possiblePieceMoveBitboard, myGamestate.getBlackQueens(),
					myGamestate.getBlackKing(), false);

			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextBishopBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(new FullGameState(temp, (myGamestate
						.getWhitePawns() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhitePawns(), (myGamestate
						.getWhiteRooks() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteRooks(), (myGamestate
						.getWhiteKnights() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteKnights(), (myGamestate
						.getWhiteBishops() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteBishops(), (myGamestate
						.getWhiteQueens() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteQueens(), myGamestate
						.getWhiteKing(), myGamestate.getBlackPawns(),
						myGamestate.getBlackRooks(), myGamestate
								.getBlackKnights(), possiblePieceMoveBitboard,
						myGamestate.getBlackQueens(), myGamestate
								.getBlackKing(), true, myGamestate
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

	protected static Vector<FullGameState> BlackQueenMoves(
			long nextQueenBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextQueenBitboard ^ nextMove)
					^ myGamestate.getBlackQueens();
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
					myGamestate.getBlackBishops(), possiblePieceMoveBitboard,
					myGamestate.getBlackKing(), false);

			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextQueenBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(new FullGameState(temp, (myGamestate
						.getWhitePawns() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhitePawns(), (myGamestate
						.getWhiteRooks() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteRooks(), (myGamestate
						.getWhiteKnights() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteKnights(), (myGamestate
						.getWhiteBishops() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteBishops(), (myGamestate
						.getWhiteQueens() ^ possiblePieceMoveBitboard)
						& myGamestate.getWhiteQueens(), myGamestate
						.getWhiteKing(), myGamestate.getBlackPawns(),
						myGamestate.getBlackRooks(), myGamestate
								.getBlackKnights(), myGamestate
								.getBlackBishops(), possiblePieceMoveBitboard,
						myGamestate.getBlackKing(), true, myGamestate
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

	protected static Vector<FullGameState> BlackRookMoves(
			long nextRookBitboard, long possibleMovesBitboard,
			FullGameState myGamestate) {
		Vector<FullGameState> listOfMoves = new Vector<FullGameState>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextRookBitboard ^ nextMove)
					^ myGamestate.getBlackRooks();

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
					possiblePieceMoveBitboard, myGamestate.getBlackKnights(),
					myGamestate.getBlackBishops(),
					myGamestate.getBlackQueens(), myGamestate.getBlackKing(),
					false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextRookBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				if (fromCoord == 56) {
					listOfMoves
							.add(new FullGameState(
									temp,
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
									temp,
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
									temp,
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

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public static void printBoard(char[][] board) {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < board.length; x++) {
				char temp = board[x][y];
				if (temp == ' ') {
					System.out.print(", ");
				} else {
					System.out.print(board[x][y] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
