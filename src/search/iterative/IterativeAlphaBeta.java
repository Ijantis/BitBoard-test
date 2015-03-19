package search.iterative;

import java.util.ArrayList;

import search.alphabeta.AlphaBetaSearch;
import movegen.MoveGenerator;
import board.FullGameState;

public class IterativeAlphaBeta {

	private static long time = 5000;

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
			boolean forcedMate = true;
			long currentTime = System.currentTimeMillis();
			System.out.println("Playing white");

			while ((System.currentTimeMillis() - currentTime) < time) {
				boolean terminatedEarly = false;
				bestValue = Integer.MIN_VALUE;
				forcedMate = true;
				System.out.println();
				System.out.println("Attempting depth " + depth);
				int currentIndex = 0;

				for (int i = 0; i < nextDepth.size(); i++) {
					if ((System.currentTimeMillis() - currentTime) > time) {
						terminatedEarly = true;
						System.out.println("RAN OUT OF TIME AT DEPTH " + depth);
						break;
					}
					currentScore = AlphaBetaSearch.alphaBeta(nextDepth.get(i),
							depth, Integer.MIN_VALUE, Integer.MAX_VALUE,
							!whiteToMove, depth - 2);
					if (currentScore > -100000) {
						forcedMate = false;
					}
					if (currentScore > 100000) {
						System.out
								.println("Checkmate for white found at depth "
										+ depth);
						System.out.println("Terminating search...");
						return nextDepth.get(i);
					}
					if (currentScore > bestValue) {
						bestValue = currentScore;
						currentIndex = i;
					}
				}
				if (terminatedEarly) {
					return nextDepth.get(bestIndex);
				}
				if (forcedMate) {
					System.out.println("Forced mate found for black");
					break;
				}
				System.out.println("Finished calculating depth " + depth);
				System.out.println("Best index at depth " + depth + " is "
						+ bestIndex);
				depth++;
				bestIndex = currentIndex;
			}
			return nextDepth.get(bestIndex);

		} else {
			long bestValue = Integer.MAX_VALUE;
			int bestIndex = 0;
			long currentScore;
			int depth = 0;
			System.out.println("Playing black");
			long currentTime = System.currentTimeMillis();

			while ((System.currentTimeMillis() - currentTime) < time) {
				boolean terminatedEarly = false;
				bestValue = Integer.MAX_VALUE;
				boolean forcedMate = true;
				System.out.println();
				System.out.println("Attempting depth " + depth);
				int currentIndex = 0;
				for (int i = 0; i < nextDepth.size(); i++) {

					if ((System.currentTimeMillis() - currentTime) > time) {
						terminatedEarly = true;
						System.out.println("RAN OUT OF TIME AT DEPTH " + depth);
						break;
					}
					currentScore = AlphaBetaSearch.alphaBeta(nextDepth.get(i),
							depth, Integer.MAX_VALUE, Integer.MIN_VALUE,
							!whiteToMove, depth - 2);
					if (currentScore < 100000) {
						forcedMate = false;
					}
					if (currentScore < -100000) {
						System.out
								.println("Checkmate for black found at depth "
										+ depth);
						System.out.println("Terminating search...");
						return nextDepth.get(i);
					}

					if (currentScore < bestValue) {
						bestValue = currentScore;
						currentIndex = i;
					}
				}
				if (terminatedEarly) {
					return nextDepth.get(bestIndex);
				}
				if (forcedMate) {
					System.out.println("Forced mate found for white");
					break;
				}
				System.out.println("Finished calculating depth " + depth);
				System.out.println("Best index at depth " + depth + " is "
						+ bestIndex);
				depth++;
				bestIndex = currentIndex;
			}
			return nextDepth.get(bestIndex);
		}

	}

}
