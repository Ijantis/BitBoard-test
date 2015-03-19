package search.fixed;

import java.util.ArrayList;

import movegen.MoveGenerator;
import board.FullGameState;
import evaluation.Evaluator;

public class StaticSearch {

	public static FullGameState makeStaticEvaluatedMove(boolean playingWhite,
			FullGameState currentGameState) {

		ArrayList<FullGameState> depthOne = new ArrayList<FullGameState>();

		if (playingWhite) {

			depthOne = MoveGenerator.generateWhiteLegalMoves(currentGameState);

			long bestScore = Long.MIN_VALUE;
			long currentScore;
			int bestIndex = 0;

			for (int i = 0; i < depthOne.size(); i++) {
				ArrayList<FullGameState> depthTwo = new ArrayList<FullGameState>();
				depthTwo = MoveGenerator.generateBlackLegalMoves(depthOne
						.get(i));
				if (depthTwo.size() == 0) {
					currentScore = Evaluator.evaluatePosition(depthOne.get(i));
					if (currentScore > bestScore) {
						bestScore = currentScore;
						bestIndex = i;
					}
				}
				for (int j = 0; j < depthTwo.size(); j++) {
					currentScore = Evaluator.evaluatePosition(depthTwo.get(j));
					if (currentScore > bestScore) {
						bestScore = currentScore;
						bestIndex = i;
					}
				}

			}
			return depthOne.get(bestIndex);
		} else {

			depthOne = MoveGenerator.generateBlackLegalMoves(currentGameState);

			long bestScore = Long.MAX_VALUE;
			long currentScore;
			int bestIndex = 0;

			for (int i = 0; i < depthOne.size(); i++) {
				ArrayList<FullGameState> depthTwo = new ArrayList<FullGameState>();
				depthTwo = MoveGenerator.generateWhiteLegalMoves(depthOne
						.get(i));
				if (depthTwo.size() == 0) {
					currentScore = Evaluator.evaluatePosition(depthOne.get(i));
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestIndex = i;
					}
				}
				for (int j = 0; j < depthTwo.size(); j++) {
					currentScore = Evaluator.evaluatePosition(depthTwo.get(j));
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestIndex = i;
					}
				}

			}
			return depthOne.get(bestIndex);
		}
	}

}
