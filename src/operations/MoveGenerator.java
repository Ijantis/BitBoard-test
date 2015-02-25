package operations;

import java.util.Vector;

import operations.pieces.BlackPieces;
import operations.pieces.WhitePieces;
import board.BoardManager;
import board.Gamestate;

public class MoveGenerator {

	/*
	 * NOTE: Do not return a Vector<char[][]> return squares numbers instead.
	 */
	public static Vector<char[][]> generateWhiteLegalMoves(Gamestate myGamestate) {

		Vector<char[][]> possibleStates = new Vector<char[][]>(20, 20);
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
				myGamestate.canWhiteCastleKing(),
				myGamestate.canWhiteCastleQueen());
		possibleStates.addAll(whiteKingMoves(myGamestate.getWhiteKing(),
				kingMovesBitboard, myGamestate));

		return possibleStates;
	}

	protected static Vector<char[][]> whiteKingMoves(long nextKingBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

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

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}
		return listOfMoves;
	}

	protected static Vector<char[][]> whitePawnMoves(long nextPawnBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

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

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<char[][]> whiteKnightMoves(long nextKnightBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

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

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<char[][]> whiteBishopMoves(long nextBishopBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

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

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<char[][]> whiteQueenMoves(long nextQueenBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

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

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<char[][]> whiteRookMoves(long nextRookBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

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

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	public static Vector<char[][]> generateBlackLegalMoves(Gamestate myGamestate) {

		Vector<char[][]> possibleStates = new Vector<char[][]>(20, 20);
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
				myGamestate.canBlackCastleKing(),
				myGamestate.canBlackCastleQueen());
		possibleStates.addAll(BlackKingMoves(myGamestate.getBlackKing(),
				kingMovesBitboard, myGamestate));

		return possibleStates;
	}

	protected static Vector<char[][]> BlackKingMoves(long nextKingBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKingBitboard ^ nextMove)
					^ myGamestate.getBlackKing();
			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getBlackPawns(), myGamestate.getBlackRooks(),
					myGamestate.getBlackKnights(),
					myGamestate.getBlackBishops(),
					myGamestate.getBlackQueens(), possiblePieceMoveBitboard,
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
					myGamestate.getWhiteKing(), false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextKingBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}
		return listOfMoves;
	}

	protected static Vector<char[][]> BlackPawnMoves(long nextPawnBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextPawnBitboard ^ nextMove)
					^ myGamestate.getBlackPawns();
			boolean isValid = BoardManager.IsSelfCheck(
					possiblePieceMoveBitboard, myGamestate.getBlackRooks(),
					myGamestate.getBlackKnights(),
					myGamestate.getBlackBishops(),
					myGamestate.getBlackQueens(), myGamestate.getBlackKing(),
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
					myGamestate.getWhiteKing(), false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextPawnBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<char[][]> BlackKnightMoves(long nextKnightBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKnightBitboard ^ nextMove)
					^ myGamestate.getBlackKnights();
			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getBlackPawns(), myGamestate.getBlackRooks(),
					possiblePieceMoveBitboard, myGamestate.getBlackBishops(),
					myGamestate.getBlackQueens(), myGamestate.getBlackKing(),
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
					myGamestate.getWhiteKing(), false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextKnightBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<char[][]> BlackBishopMoves(long nextBishopBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextBishopBitboard ^ nextMove)
					^ myGamestate.getBlackBishops();
			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getBlackPawns(), myGamestate.getBlackRooks(),
					myGamestate.getBlackKnights(), possiblePieceMoveBitboard,
					myGamestate.getBlackQueens(), myGamestate.getBlackKing(),
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
					myGamestate.getWhiteKing(), false);

			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextBishopBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<char[][]> BlackQueenMoves(long nextQueenBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextQueenBitboard ^ nextMove)
					^ myGamestate.getBlackQueens();
			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getBlackPawns(), myGamestate.getBlackRooks(),
					myGamestate.getBlackKnights(),
					myGamestate.getBlackBishops(), possiblePieceMoveBitboard,
					myGamestate.getBlackKing(),
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
					myGamestate.getWhiteKing(), false);

			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextQueenBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(temp);

			}

			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<char[][]> BlackRookMoves(long nextRookBitboard,
			long possibleMovesBitboard, Gamestate myGamestate) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextRookBitboard ^ nextMove)
					^ myGamestate.getBlackRooks();

			boolean isValid = BoardManager.IsSelfCheck(
					myGamestate.getBlackPawns(), possiblePieceMoveBitboard,
					myGamestate.getBlackKnights(),
					myGamestate.getBlackBishops(),
					myGamestate.getBlackQueens(), myGamestate.getBlackKing(),
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
					myGamestate.getWhiteKing(), false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(myGamestate.getCurrentBoard());
				int fromCoord = Long.toBinaryString(nextRookBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove).length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = ' ';

				listOfMoves.add(temp);

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
