package movegen;

import java.util.ArrayList;

import movegen.legal.BlackLegalMoveCount;
import movegen.legal.BlackLegalMoves;
import movegen.legal.WhiteLegalMoveCount;
import movegen.legal.WhiteLegalMoves;
import movegen.pseudo.WhitePseudoMoves;
import bitboards.BlackPieces;
import bitboards.WhitePieces;
import board.FullGameState;

public class MoveGenerator {

	public static long counter = 0;

	/*
	 * NOTE: Do not return a ArrayList<FullGameState> return squares numbers
	 * instead.
	 */
	public static ArrayList<FullGameState> generateWhiteLegalMoves(
			FullGameState currentGameState) {

		ArrayList<FullGameState> possibleStates = new ArrayList<FullGameState>();
		long temp;

		temp = currentGameState.getWhitePawns();
		while (temp != 0) {
			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces(),
					currentGameState.getEnPassantSquare());
			possibleStates.addAll(WhiteLegalMoves.whitePawnMoves(nextPawn,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getKnightMoves(nextKnight,
					currentGameState.getWhitePieces());
			possibleStates.addAll(WhiteLegalMoves.whiteKnightMoves(nextKnight,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getBishopMoves(nextBishop,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			possibleStates.addAll(WhiteLegalMoves.whiteBishopMoves(nextBishop,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(nextQueen,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			possibleStates.addAll(WhiteLegalMoves.whiteQueenMoves(nextQueen,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getRookMoves(nextRook,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			possibleStates.addAll(WhiteLegalMoves.whiteRookMoves(nextRook,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = WhitePieces.getKingMoves(
				currentGameState.getWhiteKing(),
				currentGameState.getWhitePieces(),
				currentGameState.getBlackAttackingSquares(),
				currentGameState.getWhiteCastleKing(),
				currentGameState.getWhiteCastleQueen(),
				currentGameState.getBlackPieces());
		possibleStates.addAll(WhiteLegalMoves.whiteKingMoves(
				currentGameState.getWhiteKing(), kingMovesBitboard,
				currentGameState));

		return possibleStates;
	}

	public static ArrayList<FullGameState> generateBlackLegalMoves(
			FullGameState currentGameState) {

		ArrayList<FullGameState> possibleStates = new ArrayList<FullGameState>();
		long temp;

		temp = currentGameState.getBlackPawns();
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getPawnMoves(nextPawn,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces(),
					currentGameState.getEnPassantSquare());
			possibleStates.addAll(BlackLegalMoves.BlackPawnMoves(nextPawn,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getKnightMoves(nextKnight,
					currentGameState.getBlackPieces());
			possibleStates.addAll(BlackLegalMoves.BlackKnightMoves(nextKnight,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getBishopMoves(nextBishop,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			possibleStates.addAll(BlackLegalMoves.BlackBishopMoves(nextBishop,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getQueenMoves(nextQueen,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			possibleStates.addAll(BlackLegalMoves.BlackQueenMoves(nextQueen,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getRookMoves(nextRook,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			possibleStates.addAll(BlackLegalMoves.BlackRookMoves(nextRook,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = BlackPieces.getKingMoves(
				currentGameState.getBlackKing(),
				currentGameState.getBlackPieces(),
				currentGameState.getWhiteAttackingSquares(),
				currentGameState.getBlackCastleKing(),
				currentGameState.getBlackCastleQueen(),
				currentGameState.getWhitePieces());
		possibleStates.addAll(BlackLegalMoves.BlackKingMoves(
				currentGameState.getBlackKing(), kingMovesBitboard,
				currentGameState));

		return possibleStates;
	}

	public static ArrayList<FullGameState> generateWhitePseudoMoves(
			FullGameState currentGameState) {
		ArrayList<FullGameState> possibleStates = new ArrayList<FullGameState>();

		long temp;

		temp = currentGameState.getWhitePawns();
		while (temp != 0) {
			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces(),
					currentGameState.getEnPassantSquare());
			possibleStates.addAll(WhitePseudoMoves.whitePawnMoves(nextPawn,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getKnightMoves(nextKnight,
					currentGameState.getWhitePieces());
			possibleStates.addAll(WhiteLegalMoves.whiteKnightMoves(nextKnight,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getBishopMoves(nextBishop,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			possibleStates.addAll(WhiteLegalMoves.whiteBishopMoves(nextBishop,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(nextQueen,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			possibleStates.addAll(WhiteLegalMoves.whiteQueenMoves(nextQueen,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getRookMoves(nextRook,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			possibleStates.addAll(WhiteLegalMoves.whiteRookMoves(nextRook,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = WhitePieces.getKingMoves(
				currentGameState.getWhiteKing(),
				currentGameState.getWhitePieces(),
				currentGameState.getBlackAttackingSquares(),
				currentGameState.getWhiteCastleKing(),
				currentGameState.getWhiteCastleQueen(),
				currentGameState.getBlackPieces());
		possibleStates.addAll(WhiteLegalMoves.whiteKingMoves(
				currentGameState.getWhiteKing(), kingMovesBitboard,
				currentGameState));

		return possibleStates;
	}

	public static ArrayList<FullGameState> generateBlackPseudoMoves(
			FullGameState currentGameState) {
		ArrayList<FullGameState> possibleStates = new ArrayList<FullGameState>();

		long temp;

		temp = currentGameState.getBlackPawns();
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getPawnMoves(nextPawn,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces(),
					currentGameState.getEnPassantSquare());
			possibleStates.addAll(BlackLegalMoves.BlackPawnMoves(nextPawn,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getKnightMoves(nextKnight,
					currentGameState.getBlackPieces());
			possibleStates.addAll(BlackLegalMoves.BlackKnightMoves(nextKnight,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getBishopMoves(nextBishop,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			possibleStates.addAll(BlackLegalMoves.BlackBishopMoves(nextBishop,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getQueenMoves(nextQueen,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			possibleStates.addAll(BlackLegalMoves.BlackQueenMoves(nextQueen,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getRookMoves(nextRook,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			possibleStates.addAll(BlackLegalMoves.BlackRookMoves(nextRook,
					bitboardOfMoves, currentGameState));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = BlackPieces.getKingMoves(
				currentGameState.getBlackKing(),
				currentGameState.getBlackPieces(),
				currentGameState.getWhiteAttackingSquares(),
				currentGameState.getBlackCastleKing(),
				currentGameState.getBlackCastleQueen(),
				currentGameState.getWhitePieces());
		possibleStates.addAll(BlackLegalMoves.BlackKingMoves(
				currentGameState.getBlackKing(), kingMovesBitboard,
				currentGameState));

		return possibleStates;
	}

	public static long generateWhiteLegalMoveCount(
			FullGameState currentGameState) {
		long count = 0;

		long temp;

		temp = currentGameState.getWhitePawns();
		while (temp != 0) {
			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces(),
					currentGameState.getEnPassantSquare());
			count += WhiteLegalMoveCount.whitePawnMoves(nextPawn,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getKnightMoves(nextKnight,
					currentGameState.getWhitePieces());
			count += WhiteLegalMoveCount.whiteKnightMoves(nextKnight,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getBishopMoves(nextBishop,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			count += WhiteLegalMoveCount.whiteBishopMoves(nextBishop,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(nextQueen,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			count += WhiteLegalMoveCount.whiteQueenMoves(nextQueen,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getWhiteRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getRookMoves(nextRook,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces());
			count += WhiteLegalMoveCount.whiteRookMoves(nextRook,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = WhitePieces.getKingMoves(
				currentGameState.getWhiteKing(),
				currentGameState.getWhitePieces(),
				currentGameState.getBlackAttackingSquares(),
				currentGameState.getWhiteCastleKing(),
				currentGameState.getWhiteCastleQueen(),
				currentGameState.getBlackPieces());
		count += WhiteLegalMoveCount.whiteKingMoves(
				currentGameState.getWhiteKing(), kingMovesBitboard,
				currentGameState);

		counter += count;

		return count;
	}

	public static long generateBlackLegalMoveCount(
			FullGameState currentGameState) {
		long count = 0;

		long temp;

		temp = currentGameState.getBlackPawns();
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getPawnMoves(nextPawn,
					currentGameState.getOccupiedSquares(),
					currentGameState.getWhitePieces(),
					currentGameState.getEnPassantSquare());
			count += BlackLegalMoveCount.BlackPawnMoves(nextPawn,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getKnightMoves(nextKnight,
					currentGameState.getBlackPieces());
			count += BlackLegalMoveCount.BlackKnightMoves(nextKnight,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getBishopMoves(nextBishop,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			count += BlackLegalMoveCount.BlackBishopMoves(nextBishop,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getQueenMoves(nextQueen,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			count += BlackLegalMoveCount.BlackQueenMoves(nextQueen,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = currentGameState.getBlackRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getRookMoves(nextRook,
					currentGameState.getOccupiedSquares(),
					currentGameState.getBlackPieces());
			count += BlackLegalMoveCount.BlackRookMoves(nextRook,
					bitboardOfMoves, currentGameState);

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = BlackPieces.getKingMoves(
				currentGameState.getBlackKing(),
				currentGameState.getBlackPieces(),
				currentGameState.getWhiteAttackingSquares(),
				currentGameState.getBlackCastleKing(),
				currentGameState.getBlackCastleQueen(),
				currentGameState.getWhitePieces());
		count += BlackLegalMoveCount.BlackKingMoves(
				currentGameState.getBlackKing(), kingMovesBitboard,
				currentGameState);

		counter += count;
		return count;
	}

}
