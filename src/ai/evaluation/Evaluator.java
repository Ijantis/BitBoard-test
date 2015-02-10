package ai.evaluation;

import board.BitboardOperations;

public class Evaluator {

	public static double evaluatePosition(long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing) {

		double whiteScore = 0;
		double blackScore = 0;

		whiteScore += evaluateMaterial(whitePawns, whiteRooks, whiteKnights,
				whiteBishops, whiteQueens, whiteKing);
		blackScore += evaluateMaterial(blackPawns, blackRooks, blackKnights,
				blackBishops, blackQueens, blackKing);

		System.out.println("white score " + whiteScore);
		System.out.println("black score " + blackScore);

		whiteScore += evaluateDoubledPawns(whitePawns);
		blackScore += evaluateDoubledPawns(blackPawns);

		System.out.println("white score " + whiteScore);
		System.out.println("black score " + blackScore);
		
		whiteScore += evaluateIsolatedPawns(whitePawns);
		blackScore += evaluateIsolatedPawns(blackPawns);

		// problems with precision so this is needed
		return Math.round((whiteScore - blackScore) * 100) / 100d;
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
}