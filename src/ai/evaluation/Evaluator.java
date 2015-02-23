package ai.evaluation;

import java.util.Vector;

import operations.BitboardOperations;
import operations.MoveGenerator;

public class Evaluator {

	public static double evaluatePosition(long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing, char[][] currentBoard, long whiteAttackingSquares,
			long blackAttackingSquares, boolean whiteCastleKing,
			boolean whiteCastleQueen, boolean blackCastleKing,
			boolean blackCastleQueen) {

		double whiteScore = 0;
		double blackScore = 0;

		whiteScore += evaluateMaterial(whitePawns, whiteRooks, whiteKnights,
				whiteBishops, whiteQueens, whiteKing);
		blackScore += evaluateMaterial(blackPawns, blackRooks, blackKnights,
				blackBishops, blackQueens, blackKing);

		whiteScore += evaluateDoubledPawns(whitePawns);
		blackScore += evaluateDoubledPawns(blackPawns);

		whiteScore += evaluateIsolatedPawns(whitePawns);
		blackScore += evaluateIsolatedPawns(blackPawns);

		whiteScore += evaluateHangingPieces(whitePawns | whiteRooks
				| whiteKnights | whiteBishops | whiteQueens,
				whiteAttackingSquares);
		blackScore += evaluateHangingPieces(blackPawns | blackRooks
				| blackKnights | blackBishops | blackQueens,
				blackAttackingSquares);

		whiteScore += MoveGenerator.generateWhiteLegalMoves(
				currentBoard,
				whitePawns,
				whiteRooks,
				whiteKnights,
				whiteBishops,
				whiteQueens,
				whiteKing,
				(blackBishops | blackKing | blackKnights | blackPawns
						| blackQueens | blackRooks),
				(whiteBishops | whiteKing | whiteKnights | whitePawns
						| whiteQueens | whiteRooks), blackAttackingSquares,
				blackPawns, blackRooks, blackKnights, blackBishops,
				blackQueens, blackKing, whiteCastleKing, whiteCastleQueen)
				.size() * 0.1;

		blackScore += MoveGenerator.generateBlackLegalMoves(
				currentBoard,
				whitePawns,
				whiteRooks,
				whiteKnights,
				whiteBishops,
				whiteQueens,
				whiteKing,
				(blackBishops | blackKing | blackKnights | blackPawns
						| blackQueens | blackRooks),
				(whiteBishops | whiteKing | whiteKnights | whitePawns
						| whiteQueens | whiteRooks), whiteAttackingSquares,
				blackPawns, blackRooks, blackKnights, blackBishops,
				blackQueens, blackKing, blackCastleKing, blackCastleQueen)
				.size() * 0.1;

		// problems with precision so this is needed
		return Math.round((whiteScore - blackScore) * 100) / 100d;
	}

	/*
	 * Make this stronger. Count number of rooks etc. and evaluate based on
	 * worth of the piece. Hanging queen worse than hanging pawn.
	 */
	private static double evaluateHangingPieces(long pieces,
			long attackingSquares) {

		long protectedPieces = pieces & attackingSquares;
		long hangingPieces = protectedPieces ^ pieces;

		double score = 0;

		score += Long.bitCount(protectedPieces) * 0.1;
		score -= Long.bitCount(hangingPieces) * 0.2;

		return score;
	}

	// maybe use an array to keep track of the masked files
	// first check if there are pawns in a file then check if adjacent files are
	// empty
	private static double evaluateIsolatedPawns(long pawns) {
		double score = 0;

		// evaluating the first file
		int pawnsInFile = Long.bitCount(pawns & BitboardOperations.maskFile(1));
		if (pawnsInFile != 0 && (pawns & BitboardOperations.maskFile(2)) == 0) {
			score -= 0.2 * pawnsInFile;
			System.out.println("isolated in 1");
		}

		for (int file = 2; file < 8; file++) {
			if ((BitboardOperations.maskFile(file) & pawns) == 0) {
				continue;
			} else {
				if ((BitboardOperations.maskFile(file - 1) & pawns) == 0
						&& (BitboardOperations.maskFile(file + 1) & pawns) == 0) {
					score -= 0.2 * Long.bitCount(BitboardOperations
							.maskFile(file) & pawns);
					System.out.println("Isolated pawn in file " + file);
				}
			}
		}

		// evaluating the eighth file
		pawnsInFile = Long.bitCount(pawns & BitboardOperations.maskFile(8));
		if (pawnsInFile != 0 && (pawns & BitboardOperations.maskFile(7)) == 0) {
			score -= 0.2 * pawnsInFile;
			System.out.println("isolated in 8");
		}
		return score;
	}

	private static double evaluateDoubledPawns(long pawns) {
		double score = 0;
		for (int file = 1; file < 9; file++) {
			int pawnsInFile = 0;
			pawnsInFile = Long.bitCount(pawns
					& BitboardOperations.maskFile(file));
			if (pawnsInFile > 1) {
				score -= (pawnsInFile * 0.2) * (pawnsInFile - 1);
			}
		}
		return score;
	}

	private static double evaluateMaterial(long pawns, long rooks,
			long knights, long bishops, long queens, long king) {

		double score = 0;

		score += Long.bitCount(pawns);
		score += Long.bitCount(rooks) * 5;
		score += Long.bitCount(bishops) * 3;
		score += Long.bitCount(knights) * 3;
		score += Long.bitCount(queens) * 9;
		return score;
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

}