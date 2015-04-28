package search.iterative;

import java.util.ArrayList;

import search.alphabeta.AlphaBetaSearch;
import movegen.MoveGenerator;
import board.FullGameState;

public class IterativeAlphaBeta {

	private static long timeLimit = 2000;

	public static FullGameState iterativeAlphaBeta(
			FullGameState currentGameState, boolean whiteToMove) {

		ArrayList<FullGameState> nextDepth;

		if (whiteToMove) {
			nextDepth = MoveGenerator.generateWhiteLegalMoves(currentGameState);
		} else {
			nextDepth = MoveGenerator.generateBlackLegalMoves(currentGameState);
		}

		// maximising player
		if (whiteToMove) {

			int currentDepth = 0;
			int bestIndex = 0;
			System.out.println("Initialising iterative deepening for white");
			long timeAtStart = System.currentTimeMillis();

			// The begining of the iterative deepening
			while ((System.currentTimeMillis() - timeAtStart) < timeLimit) {
				System.out.println();
				System.out.println("Starting search at depth " + currentDepth);
				long bestScore = Integer.MIN_VALUE;
				int currentBestIndex = 0;
				long currentScore;
				boolean terminatedEarly = false;

				// looping through
				for (int i = 0; i < nextDepth.size(); i++) {
					// this checks to see if we run out of time during the
					// current depth
					if ((System.currentTimeMillis() - timeAtStart) > timeLimit) {
						System.out
								.println("Search terminated early due to time constraint");
						terminatedEarly = true;
						break;
					}
					currentScore = AlphaBetaSearch.alphaBeta(nextDepth.get(i),
							currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE,
							!whiteToMove, currentDepth - 2);
					// we check to see if a forced mate is found.
					if (currentScore > 100000) {
						System.out.println("Checkmate found for white at index " + i);
						return nextDepth.get(i);
					}

					// if the current evaluation is better update the index
					if (currentScore > bestScore) {
						System.out.println("New best index " + i + " at depth "
								+ currentDepth);
						currentBestIndex = i;
						bestScore = currentScore;
					}
				}
				// end of current search at this depth

				// if the search was terminated early we disregard the
				// currentIndex
				if (terminatedEarly) {
					System.out
							.println("Search was terminated early with current best index "
									+ currentBestIndex);
					System.out.println("Best index from previous depth is "
							+ bestIndex);
					return nextDepth.get(bestIndex);
				} else {
					bestIndex = currentBestIndex;
				}
				System.out.println("Finished search at depth " + currentDepth);
				currentDepth++;
			}
			// once time has run out this is run
			return nextDepth.get(bestIndex);

			// minimizing player
		} else {

			int currentDepth = 0;
			int bestIndex = 0;
			System.out.println("Initialising iterative deepening for black");
			long timeAtStart = System.currentTimeMillis();

			// The begining of the iterative deepening
			while ((System.currentTimeMillis() - timeAtStart) < timeLimit) {
				System.out.println();
				System.out.println("Starting search at depth " + currentDepth);
				long bestScore = Integer.MAX_VALUE;
				int currentBestIndex = 0;
				long currentScore;
				boolean terminatedEarly = false;

				// looping through
				for (int i = 0; i < nextDepth.size(); i++) {
					// this checks to see if we run out of time during the
					// current depth
					if ((System.currentTimeMillis() - timeAtStart) > timeLimit) {
						System.out
								.println("Search terminated early due to time constraint");
						terminatedEarly = true;
						break;
					}
					currentScore = AlphaBetaSearch.alphaBeta(nextDepth.get(i),
							currentDepth, Integer.MAX_VALUE, Integer.MIN_VALUE,
							!whiteToMove, currentDepth - 2);
					// we check to see if a forced mate is found.
					if (currentScore < -100000) {
						System.out.println("Checkmate found for black at index " + i);
						return nextDepth.get(i);
					}

					// if the current evaluation is better update the index
					if (currentScore < bestScore) {
						System.out.println("New best index " + i + " at depth "
								+ currentDepth);
						currentBestIndex = i;
						bestScore = currentScore;
					}
				}
				// end of current search at this depth

				// if the search was terminated early we disregard the
				// currentIndex
				if (terminatedEarly) {
					System.out
							.println("Search was terminated early with current best index "
									+ currentBestIndex);
					System.out.println("Best index from previous depth is "
							+ bestIndex);
					return nextDepth.get(bestIndex);
				} else {
					bestIndex = currentBestIndex;
				}
				System.out.println("Finished search at depth " + currentDepth);
				currentDepth++;
			}
			// once time has run out this is run
			return nextDepth.get(bestIndex);

		}
	}

}

























































