package board;

import java.util.Collection;
import java.util.Vector;
import java.util.function.LongConsumer;

public class MoveGenerator {

	public static int count = 0;

	/*
	 * NOTE: Do not return a Vector<String[][]> return squares numbers instead.
	 */
	protected static Vector<String[][]> generateWhiteLegalMoves(
			String[][] currentBoard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPieces, long whitePieces,
			long blackAttackingSquares, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing) {

		Vector<String[][]> possibleStates = new Vector<String[][]>(20, 20);
		long temp;

		temp = whitePawns;
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					whitePieces | blackPieces, blackPieces);
			possibleStates.addAll(whitePawnMoves(nextPawn, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));
			// possibleStates.addAll(generateNextMoves(nextPawn,
			// bitboardOfMoves));

			temp = (Long.highestOneBit(temp) - 1) & temp;
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

			temp = (Long.highestOneBit(temp) - 1) & temp;
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
			// possibleStates.addAll(generateNextMoves(nextBishop,
			// bitboardOfMoves));

			temp = (Long.highestOneBit(temp) - 1) & temp;
		}

		temp = whiteQueens;
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(temp, whitePieces
					| blackPieces, whitePieces);
			possibleStates.addAll(whiteQueenMoves(nextQueen, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));
			// possibleStates.addAll(generateNextMoves(nextQueen,
			// bitboardOfMoves));

			temp = (Long.highestOneBit(whiteQueens) - 1) & temp;
		}

		temp = whiteRooks;
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(nextRook,
					whitePieces | blackPieces, whitePieces);
			possibleStates.addAll(whiteRookMoves(nextRook, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));
			// printBitboard(bitboardOfMoves);

			// possibleStates.addAll(generateNextMoves(nextRook,
			// bitboardOfMoves));

			temp = (Long.highestOneBit(temp) - 1) & temp;
		}

		long kingMovesBitboard = WhitePieces.getKingMoves(whiteKing,
				whitePieces, blackAttackingSquares);
		// possibleStates.addAll(generateNextMoves(whiteKing,
		// kingMovesBitboard));

		if (possibleStates.isEmpty()) {
			System.out.println("Checkmate!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		return possibleStates;
	}

	protected static Vector<String[][]> whitePawnMoves(long nextPawnBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextPawnBitboard ^ nextMove)
					^ whitePawns;
			boolean isValid = BoardManager.IsSelfCheck(potentialStateBitboard,
					whiteRooks, whiteKnights, whiteBishops, whiteQueens,
					whiteKing, (blackPawns ^ potentialStateBitboard)
							& blackPawns, (blackRooks ^ potentialStateBitboard)
							& blackRooks,
					(blackKnights ^ potentialStateBitboard) & blackKnights,
					(blackBishops ^ potentialStateBitboard) & blackBishops,
					(blackQueens ^ potentialStateBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextPawnBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<String[][]> whiteKnightMoves(
			long nextKnightBitboard, long possibleMovesBitboard,
			long whitePawns, long whiteRooks, long whiteKnights,
			long whiteBishops, long whiteQueens, long blackPawns,
			long blackRooks, long blackKnights, long blackBishops,
			long blackQueens, long whiteKing, long blackKing,
			String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextKnightBitboard ^ nextMove)
					^ whiteKnights;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns, whiteRooks,
					potentialStateBitboard, whiteBishops, whiteQueens,
					whiteKing, (blackPawns ^ potentialStateBitboard)
							& blackPawns, (blackRooks ^ potentialStateBitboard)
							& blackRooks,
					(blackKnights ^ potentialStateBitboard) & blackKnights,
					(blackBishops ^ potentialStateBitboard) & blackBishops,
					(blackQueens ^ potentialStateBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextKnightBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<String[][]> whiteBishopMoves(
			long nextBishopBitboard, long possibleMovesBitboard,
			long whitePawns, long whiteRooks, long whiteKnights,
			long whiteBishops, long whiteQueens, long blackPawns,
			long blackRooks, long blackKnights, long blackBishops,
			long blackQueens, long whiteKing, long blackKing,
			String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextBishopBitboard ^ nextMove)
					^ whiteBishops;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns, whiteRooks,
					whiteKnights, potentialStateBitboard, whiteQueens,
					whiteKing, (blackPawns ^ potentialStateBitboard)
							& blackPawns, (blackRooks ^ potentialStateBitboard)
							& blackRooks,
					(blackKnights ^ potentialStateBitboard) & blackKnights,
					(blackBishops ^ potentialStateBitboard) & blackBishops,
					(blackQueens ^ potentialStateBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextBishopBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<String[][]> whiteQueenMoves(long nextQueenBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextQueenBitboard ^ nextMove)
					^ whiteQueens;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns, whiteRooks,
					whiteKnights, whiteBishops, potentialStateBitboard,
					whiteKing, (blackPawns ^ potentialStateBitboard)
							& blackPawns, (blackRooks ^ potentialStateBitboard)
							& blackRooks,
					(blackKnights ^ potentialStateBitboard) & blackKnights,
					(blackBishops ^ potentialStateBitboard) & blackBishops,
					(blackQueens ^ potentialStateBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextQueenBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<String[][]> whiteRookMoves(long nextRookBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextRookBitboard ^ nextMove)
					^ whiteRooks;
			boolean isValid = BoardManager.IsSelfCheck(whitePawns,
					potentialStateBitboard, whiteKnights, whiteBishops,
					whiteQueens, whiteKing,
					(blackPawns ^ potentialStateBitboard) & blackPawns,
					(blackRooks ^ potentialStateBitboard) & blackRooks,
					(blackKnights ^ potentialStateBitboard) & blackKnights,
					(blackBishops ^ potentialStateBitboard) & blackBishops,
					(blackQueens ^ potentialStateBitboard) & blackQueens,
					blackKing, true);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextRookBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	protected static Vector<String[][]> generateBlackLegalMoves(
			String[][] currentBoard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPieces, long whitePieces,
			long whiteAttackingSquares, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing) {

		Vector<String[][]> possibleStates = new Vector<String[][]>(20, 20);
		long temp;

		temp = blackPawns;
		while (temp != 0) {
			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getPawnMoves(nextPawn,
					blackPieces | whitePieces, whitePieces);
			possibleStates.addAll(blackPawnMoves(nextPawn, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));

			temp = (Long.highestOneBit(temp) - 1) & temp;
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
			temp = (Long.highestOneBit(temp) - 1) & temp;
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
			temp = (Long.highestOneBit(temp) - 1) & temp;
		}

		temp = blackQueens;
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getQueenMoves(blackQueens,
					blackPieces | whitePieces, blackPieces);
			possibleStates.addAll(blackQueenMoves(nextQueen, bitboardOfMoves,
					whitePawns, whiteRooks, whiteKnights, whiteBishops,
					whiteQueens, blackPawns, blackRooks, blackKnights,
					blackBishops, blackQueens, whiteKing, blackKing,
					currentBoard));
			temp = (Long.highestOneBit(temp) - 1) & temp;
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
			temp = (Long.highestOneBit(temp) - 1) & temp;
		}

		long kingMovesBitboard = BlackPieces.getKingMoves(blackKing,
				blackPieces, whiteAttackingSquares);

		if (possibleStates.isEmpty()) {
			System.out.println("Checkmate!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			printBoard(currentBoard);
		}
		return possibleStates;

	}

	private static Vector<String[][]> blackKnightMoves(long nextKnightBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextKnightBitboard ^ nextMove)
					^ blackKnights;
			boolean isValid = BoardManager.IsSelfCheck(blackPawns, blackRooks,
					potentialStateBitboard, blackBishops, blackQueens,
					blackKing, (whitePawns ^ potentialStateBitboard)
							& whitePawns, (whiteRooks ^ potentialStateBitboard)
							& whiteRooks,
					(whiteKnights ^ potentialStateBitboard) & whiteKnights,
					(whiteBishops ^ potentialStateBitboard) & whiteBishops,
					(whiteQueens ^ potentialStateBitboard) & whiteQueens,
					whiteKing, false);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextKnightBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	private static Vector<String[][]> blackBishopMoves(long nextBishopBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextBishopBitboard ^ nextMove)
					^ blackBishops;
			boolean isValid = BoardManager.IsSelfCheck(blackPawns, blackRooks,
					blackKnights, potentialStateBitboard, blackQueens,
					blackKing, (whitePawns ^ potentialStateBitboard)
							& whitePawns, (whiteRooks ^ potentialStateBitboard)
							& whiteRooks,
					(whiteKnights ^ potentialStateBitboard) & whiteKnights,
					(whiteBishops ^ potentialStateBitboard) & whiteBishops,
					(whiteQueens ^ potentialStateBitboard) & whiteQueens,
					whiteKing, false);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextBishopBitboard)
						.length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	private static Vector<String[][]> blackQueenMoves(long nextQueenBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextQueenBitboard ^ nextMove)
					^ blackQueens;
			boolean isValid = BoardManager.IsSelfCheck(blackPawns, blackRooks,
					blackKnights, blackBishops, potentialStateBitboard,
					blackKing, (whitePawns ^ potentialStateBitboard)
							& whitePawns, (whiteRooks ^ potentialStateBitboard)
							& whiteRooks,
					(whiteKnights ^ potentialStateBitboard) & whiteKnights,
					(whiteBishops ^ potentialStateBitboard) & whiteBishops,
					(whiteQueens ^ potentialStateBitboard) & whiteQueens,
					whiteKing, false);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextQueenBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	private static Vector<String[][]> blackRookMoves(long nextRookBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

		// System.out.println("NEXT PIECE ");
		// printBitboard(nextKnightBitboard);
		// System.out.println("POSSIBLE MOVES");
		// printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextRookBitboard ^ nextMove)
					^ blackRooks;
			boolean isValid = BoardManager.IsSelfCheck(blackPawns,
					potentialStateBitboard, blackKnights, blackBishops,
					blackQueens, blackKing,
					(whitePawns ^ potentialStateBitboard) & whitePawns,
					(whiteRooks ^ potentialStateBitboard) & whiteRooks,
					(whiteKnights ^ potentialStateBitboard) & whiteKnights,
					(whiteBishops ^ potentialStateBitboard) & whiteBishops,
					(whiteQueens ^ potentialStateBitboard) & whiteQueens,
					whiteKing, false);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextRookBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;

				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";

				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	private static Vector<String[][]> blackPawnMoves(long nextPawnBitboard,
			long possibleMovesBitboard, long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long blackPawns, long blackRooks, long blackKnights,
			long blackBishops, long blackQueens, long whiteKing,
			long blackKing, String[][] currentBoard) {
		Vector<String[][]> listOfMoves = new Vector<String[][]>(20, 10);

//		 System.out.println("NEXT PIECE ");
//		 printBitboard(nextPawnBitboard);
//		 System.out.println("POSSIBLE MOVES");
//		 printBitboard(possibleMovesBitboard);

		long nextMove;
		while (possibleMovesBitboard != 0) {
			nextMove = Long.highestOneBit(possibleMovesBitboard);
			// System.out.println("NEXT MOVE");
			// printBitboard(nextMove);

			// this is the bitboard after having moved the knight to the next
			// move
			long potentialStateBitboard = (nextPawnBitboard ^ nextMove)
					^ blackPawns;
			boolean isValid = BoardManager.IsSelfCheck(potentialStateBitboard,
					blackRooks, blackKnights, blackBishops, blackQueens,
					blackKing, (whitePawns ^ potentialStateBitboard)
							& whitePawns, (whiteRooks ^ potentialStateBitboard)
							& whiteRooks,
					(whiteKnights ^ potentialStateBitboard) & whiteKnights,
					(whiteBishops ^ potentialStateBitboard) & whiteBishops,
					(whiteQueens ^ potentialStateBitboard) & whiteQueens,
					whiteKing, false);
			if (isValid) {
				String[][] temp = copyCurrentBoard(currentBoard);
				int fromCoord = Long.toBinaryString(nextPawnBitboard).length() - 1;
				int toCoord = Long.toBinaryString(nextMove)
						.length() - 1;
//				printBitboard(potentialStateBitboard);
//				System.out.println("current board");
//				printBoard(currentBoard);
//				System.out.println("From: " + temp[fromCoord % 8][fromCoord / 8]);
//				System.out.println("x : " + (fromCoord % 8) + " y : " + (fromCoord / 8));
//				System.out.println("To " + temp[toCoord % 8][toCoord / 8]);
//				System.out.println("x : " + (toCoord % 8) + " y : " + (toCoord / 8));
//				System.out.println("Printing temp board before change");
//				printBoard(temp);
				temp[toCoord % 8][toCoord / 8] = temp[fromCoord % 8][fromCoord / 8];
				temp[fromCoord % 8][fromCoord / 8] = " ";
//				System.out.println("printing temp board after change");
				listOfMoves.add(temp);

			}

			// printBitboard((nextKnightBitboard ^ nextMove) ^ whiteKnights);
			possibleMovesBitboard = Long.highestOneBit(possibleMovesBitboard)
					- 1 & possibleMovesBitboard;
		}

		return listOfMoves;
	}

	private static String[][] copyCurrentBoard(String[][] currentBoard) {
		String[][] temp = new String[currentBoard.length][currentBoard.length];

		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < temp.length; y++) {
				temp[x][y] = new String(currentBoard[x][y]);
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
	public static void printBoard(String[][] board) {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < board.length; x++) {
				String temp = board[x][y];
				if (temp.equals(" ")) {
					System.out.print(", ");
				} else {
					System.out.print(board[x][y] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void printCount() {
		System.out.println(count);
	}
}
