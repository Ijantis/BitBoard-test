package operations;

import java.util.Vector;

import operations.pieces.BlackPieces;
import operations.pieces.WhitePieces;
import board.BoardManager;

public class MoveGenerator {

	/*
	 * NOTE: Do not return a Vector<char[][]> return squares numbers instead.
	 */
	public static Vector<char[][]> generateWhiteLegalMoves(
			char[][] currentBoard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPieces, long whitePieces,
			long blackAttackingSquares, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing, boolean whiteCastleKing, boolean whiteCastleQueen,
			long enPassantSquare) {

		Vector<char[][]> possibleStates = new Vector<char[][]>(20, 20);
		long temp;

		temp = whitePawns;
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					whitePieces | blackPieces, blackPieces, enPassantSquare);
			possibleStates.addAll(whitePawnMoves(nextPawn, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = whiteKnights;
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getKnightMoves(nextKnight,
					whitePieces);
			possibleStates.addAll(whiteKnightMoves(nextKnight, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = whiteBishops;
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getBishopMoves(nextBishop,
					whitePieces | blackPieces, whitePieces);
			possibleStates.addAll(whiteBishopMoves(nextBishop, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = whiteQueens;
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(nextQueen,
					whitePieces | blackPieces, whitePieces);
			possibleStates.addAll(whiteQueenMoves(nextQueen, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = whiteRooks;
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getRookMoves(nextRook,
					whitePieces | blackPieces, whitePieces);
			possibleStates.addAll(whiteRookMoves(nextRook, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = WhitePieces.getKingMoves(whiteKing,
				whitePieces, blackAttackingSquares, whiteCastleKing,
				whiteCastleQueen);
		possibleStates.addAll(whiteKingMoves(whiteKing, kingMovesBitboard,
				whitePawns, whiteRooks, whiteKnights, whiteBishops,
				whiteQueens, blackPawns, blackRooks, blackKnights,
				blackBishops, blackQueens, whiteKing, blackKing, currentBoard));

		return possibleStates;
	}

	protected static Vector<char[][]> whiteKingMoves(long nextKingBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKingBitboard ^ nextMove)
					^ whiteKing;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns, whiteRooks,
					whiteKnights, whiteBishops, whiteQueens,
					possiblePieceMoveBitboard,
					(blackPawns ^ possiblePieceMoveBitboard) & blackPawns,
					(blackRooks ^ possiblePieceMoveBitboard) & blackRooks,
					(blackKnights ^ possiblePieceMoveBitboard) & blackKnights,
					(blackBishops ^ possiblePieceMoveBitboard) & blackBishops,
					(blackQueens ^ possiblePieceMoveBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextPawnBitboard ^ nextMove)
					^ whitePawns;
			boolean isValid = BoardManager.IsSelfCheck(
					possiblePieceMoveBitboard, whiteRooks, whiteKnights,
					whiteBishops, whiteQueens, whiteKing,
					(blackPawns ^ possiblePieceMoveBitboard) & blackPawns,
					(blackRooks ^ possiblePieceMoveBitboard) & blackRooks,
					(blackKnights ^ possiblePieceMoveBitboard) & blackKnights,
					(blackBishops ^ possiblePieceMoveBitboard) & blackBishops,
					(blackQueens ^ possiblePieceMoveBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKnightBitboard ^ nextMove)
					^ whiteKnights;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns, whiteRooks,
					possiblePieceMoveBitboard, whiteBishops, whiteQueens,
					whiteKing, (blackPawns ^ possiblePieceMoveBitboard)
							& blackPawns,
					(blackRooks ^ possiblePieceMoveBitboard) & blackRooks,
					(blackKnights ^ possiblePieceMoveBitboard) & blackKnights,
					(blackBishops ^ possiblePieceMoveBitboard) & blackBishops,
					(blackQueens ^ possiblePieceMoveBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextBishopBitboard ^ nextMove)
					^ whiteBishops;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns, whiteRooks,
					whiteKnights, possiblePieceMoveBitboard, whiteQueens,
					whiteKing, (blackPawns ^ possiblePieceMoveBitboard)
							& blackPawns,
					(blackRooks ^ possiblePieceMoveBitboard) & blackRooks,
					(blackKnights ^ possiblePieceMoveBitboard) & blackKnights,
					(blackBishops ^ possiblePieceMoveBitboard) & blackBishops,
					(blackQueens ^ possiblePieceMoveBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextQueenBitboard ^ nextMove)
					^ whiteQueens;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns, whiteRooks,
					whiteKnights, whiteBishops, possiblePieceMoveBitboard,
					whiteKing, (blackPawns ^ possiblePieceMoveBitboard)
							& blackPawns,
					(blackRooks ^ possiblePieceMoveBitboard) & blackRooks,
					(blackKnights ^ possiblePieceMoveBitboard) & blackKnights,
					(blackBishops ^ possiblePieceMoveBitboard) & blackBishops,
					(blackQueens ^ possiblePieceMoveBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextRookBitboard ^ nextMove)
					^ whiteRooks;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns,
					possiblePieceMoveBitboard, whiteKnights, whiteBishops,
					whiteQueens, whiteKing,
					(blackPawns ^ possiblePieceMoveBitboard) & blackPawns,
					(blackRooks ^ possiblePieceMoveBitboard) & blackRooks,
					(blackKnights ^ possiblePieceMoveBitboard) & blackKnights,
					(blackBishops ^ possiblePieceMoveBitboard) & blackBishops,
					(blackQueens ^ possiblePieceMoveBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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

	public static Vector<char[][]> generateBlackLegalMoves(
			char[][] currentBoard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPieces, long whitePieces,
			long whiteAttackingSquares, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing, boolean blackCastleKing, boolean blackCastleQueen,
			long enPassantSquare) {

		Vector<char[][]> possibleStates = new Vector<char[][]>(20, 20);
		long temp;

		temp = blackPawns;
		while (temp != 0) {
			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getPawnMoves(nextPawn,
					blackPieces | whitePieces, whitePieces, enPassantSquare);
			possibleStates.addAll(blackPawnMoves(nextPawn, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = blackKnights;
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getKnightMoves(nextKnight,
					blackPieces);
			possibleStates.addAll(blackKnightMoves(nextKnight, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = blackBishops;
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getBishopMoves(nextBishop,
					blackPieces | whitePieces, blackPieces);
			possibleStates.addAll(blackBishopMoves(nextBishop, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = blackQueens;
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getQueenMoves(nextQueen,
					blackPieces | whitePieces, blackPieces);
			possibleStates.addAll(blackQueenMoves(nextQueen, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = blackRooks;
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getRookMoves(nextRook,
					blackPieces | whitePieces, blackPieces);
			possibleStates.addAll(blackRookMoves(nextRook, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));
			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = BlackPieces.getKingMoves(blackKing,
				blackPieces, whiteAttackingSquares, blackCastleKing,
				blackCastleQueen);
		possibleStates.addAll(blackKingMoves(blackKing, kingMovesBitboard,
				whitePawns, whiteRooks, whiteKnights, whiteBishops,
				whiteQueens, blackPawns, blackRooks, blackKnights,
				blackBishops, blackQueens, whiteKing, blackKing, currentBoard));

		return possibleStates;

	}

	private static Vector<char[][]> blackKnightMoves(long nextKnightBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKnightBitboard ^ nextMove)
					^ blackKnights;
			boolean isValid = BoardManager.IsSelfCheck(
					(whitePawns ^ possiblePieceMoveBitboard) & whitePawns,
					(whiteRooks ^ possiblePieceMoveBitboard) & whiteRooks,
					(whiteKnights ^ possiblePieceMoveBitboard) & whiteKnights,
					(whiteBishops ^ possiblePieceMoveBitboard) & whiteBishops,
					(whiteQueens ^ possiblePieceMoveBitboard) & whiteQueens,
					whiteKing, blackPawns, blackRooks,
					possiblePieceMoveBitboard, blackBishops, blackQueens,
					blackKing, false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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

	private static Vector<char[][]> blackBishopMoves(long nextBishopBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextBishopBitboard ^ nextMove)
					^ blackBishops;
			boolean isValid = BoardManager.IsSelfCheck(
					(whitePawns ^ possiblePieceMoveBitboard) & whitePawns,
					(whiteRooks ^ possiblePieceMoveBitboard) & whiteRooks,
					(whiteKnights ^ possiblePieceMoveBitboard) & whiteKnights,
					(whiteBishops ^ possiblePieceMoveBitboard) & whiteBishops,
					(whiteQueens ^ possiblePieceMoveBitboard) & whiteQueens,
					whiteKing, blackPawns, blackRooks, blackKnights,
					possiblePieceMoveBitboard, blackQueens, blackKing, false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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

	private static Vector<char[][]> blackQueenMoves(long nextQueenBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextQueenBitboard ^ nextMove)
					^ blackQueens;
			boolean isValid = BoardManager.IsSelfCheck(
					(whitePawns ^ possiblePieceMoveBitboard) & whitePawns,
					(whiteRooks ^ possiblePieceMoveBitboard) & whiteRooks,
					(whiteKnights ^ possiblePieceMoveBitboard) & whiteKnights,
					(whiteBishops ^ possiblePieceMoveBitboard) & whiteBishops,
					(whiteQueens ^ possiblePieceMoveBitboard) & whiteQueens,
					whiteKing, blackPawns, blackRooks, blackKnights,
					blackBishops, possiblePieceMoveBitboard, blackKing, false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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

	private static Vector<char[][]> blackRookMoves(long nextRookBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextRookBitboard ^ nextMove)
					^ blackRooks;
			boolean isValid = BoardManager.IsSelfCheck(
					(whitePawns ^ possiblePieceMoveBitboard) & whitePawns,
					(whiteRooks ^ possiblePieceMoveBitboard) & whiteRooks,
					(whiteKnights ^ possiblePieceMoveBitboard) & whiteKnights,
					(whiteBishops ^ possiblePieceMoveBitboard) & whiteBishops,
					(whiteQueens ^ possiblePieceMoveBitboard) & whiteQueens,
					whiteKing, blackPawns, possiblePieceMoveBitboard,
					blackKnights, blackBishops, blackQueens, blackKing, false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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

	private static Vector<char[][]> blackPawnMoves(long nextPawnBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextPawnBitboard ^ nextMove)
					^ blackPawns;
			boolean isValid = BoardManager.IsSelfCheck(
					(whitePawns ^ possiblePieceMoveBitboard) & whitePawns,
					(whiteRooks ^ possiblePieceMoveBitboard) & whiteRooks,
					(whiteKnights ^ possiblePieceMoveBitboard) & whiteKnights,
					(whiteBishops ^ possiblePieceMoveBitboard) & whiteBishops,
					(whiteQueens ^ possiblePieceMoveBitboard) & whiteQueens,
					whiteKing, possiblePieceMoveBitboard, blackRooks,
					blackKnights, blackBishops, blackQueens, blackKing, false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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

	private static Vector<char[][]> blackKingMoves(long nextKingBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, char[][] currentBoard) {
		Vector<char[][]> listOfMoves = new Vector<char[][]>(20, 10);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);

			// this is the bitboard after having moved the piece to the next
			// move
			long possiblePieceMoveBitboard = (nextKingBitboard ^ nextMove)
					^ blackKing;
			boolean isValid = BoardManager
					.IsSelfCheck((whitePawns ^ possiblePieceMoveBitboard)
							& whitePawns,
							(whiteRooks ^ possiblePieceMoveBitboard)
									& whiteRooks,
							(whiteKnights ^ possiblePieceMoveBitboard)
									& whiteKnights,
							(whiteBishops ^ possiblePieceMoveBitboard)
									& whiteBishops,
							(whiteQueens ^ possiblePieceMoveBitboard)
									& whiteQueens, whiteKing, blackPawns,
							blackRooks, blackKnights, blackBishops,
							blackQueens, possiblePieceMoveBitboard, false);
			if (isValid) {
				char[][] temp = copyCurrentBoard(currentBoard);
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
					System.out.print(board[x][y] + ' ');
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
