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

		whiteScore += evaluateMaterial(whitePawns, whiteRooks,
				whiteKnights, whiteBishops, whiteQueens, whiteKing);
		blackScore += evaluateMaterial(blackPawns, blackRooks,
				blackKnights, blackBishops, blackQueens, blackKing);

		System.out.println("white score " + whiteScore);
		System.out.println("black score " + blackScore);
		
		whiteScore -= evaluateDoubledPawns(whitePawns);
		blackScore -= evaluateDoubledPawns(blackPawns);
		
		System.out.println("white score " + whiteScore);
		System.out.println("black score " + blackScore);
		
		// problems with precision so this is needed
		return Math.round((whiteScore - blackScore) * 100) / 100d;
	}

	private static double evaluateDoubledPawns(long pawns) {
		double score = 0;
		for (int file = 1; file < 9; file++) {
			int pawnsInFile = 0;
			pawnsInFile = Long.bitCount(pawns & BitboardOperations.maskFile(file));
			System.out.println("Found " + pawnsInFile + " in file " + file);
			if (pawnsInFile > 1) {
				score -= (pawnsInFile * 0.2) * (pawnsInFile - 1);
			}
			System.out.println("Score is " + score);
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