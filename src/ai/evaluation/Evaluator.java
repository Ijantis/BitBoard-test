package ai.evaluation;

public class Evaluator {

	public static double evaluatePosition(long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing) {

		double whiteScore = 0;
		double blackScore = 0;

		whiteScore += evaluateWhiteMaterial(whitePawns, whiteRooks,
				whiteKnights, whiteBishops, whiteQueens, whiteKing);
		blackScore += evaluateBlackMaterial(blackPawns, blackRooks,
				blackKnights, blackBishops, blackQueens, blackKing);

		return whiteScore - blackScore;
	}

	private static double evaluateBlackMaterial(long blackPawns,
			long blackRooks, long blackKnights, long blackBishops,
			long blackQueens, long blackKing) {

		int score = 0;

		score += Long.bitCount(blackPawns);
		score += Long.bitCount(blackRooks) * 5;
		score += Long.bitCount(blackBishops) * 3;
		score += Long.bitCount(blackKnights) * 3;
		score += Long.bitCount(blackQueens) * 9;

		return score;
	}

	private static double evaluateWhiteMaterial(long whitePawns,
			long whiteRooks, long whiteKnights, long whiteBishops,
			long whiteQueens, long whiteKing) {
		int score = 0;

		score += Long.bitCount(whitePawns);
		score += Long.bitCount(whiteRooks) * 5;
		score += Long.bitCount(whiteBishops) * 3;
		score += Long.bitCount(whiteKnights) * 3;
		score += Long.bitCount(whiteQueens) * 9;

		return score;
	}

}
