package search.iterative;

import java.util.ArrayList;

import search.alphabeta.AlphaBetaSearch;
import movegen.MoveGenerator;
import board.FullGameState;

public class IterativeAlphaBeta {

	public static FullGameState iterativeAlphaBeta(
			FullGameState currentGameState, boolean whiteToMove) {

		ArrayList<FullGameState> nextDepth;

		if (whiteToMove) {
			nextDepth = MoveGenerator.generateWhiteLegalMoves(currentGameState);
		} else {
			nextDepth = MoveGenerator.generateBlackLegalMoves(currentGameState);
		}

		if (whiteToMove) {
			long bestValue = Integer.MIN_VALUE;
			int bestIndex = 0;
			long currentScore;
			int depth = 0;
			long currentTime = System.currentTimeMillis();

			while ((System.currentTimeMillis() - currentTime) < 5000) {

				System.out.println();
				System.out.println("Attempting depth " + depth);
				for (int i = 0; i < nextDepth.size(); i++) {

					if ((System.currentTimeMillis() - currentTime) > 5000) {
						break;
					}
					currentScore = AlphaBetaSearch.alphaBeta(nextDepth.get(i),
							depth, Integer.MIN_VALUE, Integer.MAX_VALUE,
							whiteToMove, depth - 2);
					if (currentScore > 100000) {
						System.out.println("Checkmate found at depth " + depth);
						System.out.println("Terminating search...");
						return nextDepth.get(i);
					}

					if (currentScore > bestValue) {
						bestValue = currentScore;
						bestIndex = i;
					}
				}

				System.out.println("Finished calculating depth " + depth);
				System.out.println("Best index at depth " + depth + " is "
						+ bestIndex);
				depth++;
			}
			return nextDepth.get(bestIndex);

		} else {
			long bestValue = Integer.MAX_VALUE;
			int bestIndex = 0;
			long currentScore;
			int depth = 0;
			long currentTime = System.currentTimeMillis();

			while ((System.currentTimeMillis() - currentTime) < 5000) {

				System.out.println();
				System.out.println("Attempting depth " + depth);
				for (int i = 0; i < nextDepth.size(); i++) {

					if ((System.currentTimeMillis() - currentTime) > 5000) {
						break;
					}
					currentScore = AlphaBetaSearch.alphaBeta(nextDepth.get(i),
							depth, Integer.MAX_VALUE, Integer.MIN_VALUE,
							whiteToMove, depth - 2);
					if (currentScore < -100000) {
						System.out.println("Checkmate found at depth " + depth);
						System.out.println("Terminating search...");
						return nextDepth.get(i);
					}

					if (currentScore < bestValue) {
						bestValue = currentScore;
						bestIndex = i;
					}
				}

				System.out.println("Finished calculating depth " + depth);
				System.out.println("Best index at depth " + depth + " is "
						+ bestIndex);
				depth++;
			}
			return nextDepth.get(bestIndex);
		}

	}

}
