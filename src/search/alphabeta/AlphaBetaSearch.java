package search.alphabeta;

import java.util.ArrayList;
import java.util.TreeMap;

import board.FullGameState;
import movegen.MoveGenerator;
import evaluation.Evaluator;

public class AlphaBetaSearch {

	public static FullGameState calculateAlphaBeta(
			FullGameState currentGameState, boolean whiteToMove,
			ArrayList<FullGameState> nextDepth) {
		int depth = 4;
		int moveOrderingDepth = depth - 2;

		if (whiteToMove) {
			int bestIndex = 0;
			long bestValue = Integer.MIN_VALUE;
			long currentScore;

			for (int i = 0; i < nextDepth.size(); i++) {
				currentScore = alphaBeta(nextDepth.get(i), depth,
						Integer.MIN_VALUE, Integer.MAX_VALUE, !whiteToMove,
						moveOrderingDepth);
				if (currentScore > 100000) {
					return nextDepth.get(i);
				}
				if (currentScore > bestValue) {
					bestIndex = i;
					bestValue = currentScore;
				}
			}

			return nextDepth.get(bestIndex);

		} else {
			int bestIndex = 0;
			long bestValue = Integer.MAX_VALUE;
			long currentScore;
			for (int i = 0; i < nextDepth.size(); i++) {
				currentScore = alphaBeta(nextDepth.get(i), depth,
						Integer.MAX_VALUE, Integer.MIN_VALUE, !whiteToMove,
						moveOrderingDepth);
				if (currentScore < -100000) {
					return nextDepth.get(i);
				}
				if (currentScore < bestValue) {
					bestIndex = i;
					bestValue = currentScore;
				}
			}

			return nextDepth.get(bestIndex);
		}
	}

	public static FullGameState calculateAlphaBeta(
			FullGameState currentGameState, boolean whiteToMove) {

		ArrayList<FullGameState> nextDepth;
		if (whiteToMove) {
			nextDepth = MoveGenerator.generateWhiteLegalMoves(currentGameState);
		} else {
			nextDepth = MoveGenerator.generateBlackLegalMoves(currentGameState);
		}

		return calculateAlphaBeta(currentGameState, whiteToMove, nextDepth);
	}

	/**
	 * 
	 * @param currentGameState
	 * @param depth
	 *            - The depth to analyze to. If depth is 0 then the function
	 *            returns immeadiately.
	 * @param alpha
	 * @param beta
	 * @param whiteToMove
	 *            - White to move.
	 * @param moveOrderingDepth
	 *            - Whether or not this call should order the list of moves or
	 *            not.
	 * @return - The evaluation of the position to a given depth.
	 */
	public static long alphaBeta(FullGameState currentGameState, int depth,
			long alpha, long beta, boolean whiteToMove, int moveOrderingDepth) {

		if (depth == 0) {
			return Evaluator.evaluatePosition(currentGameState);
		}

		// maximising player
		if (whiteToMove) {

			// Move ordering for speed up
			if (moveOrderingDepth > 0) {
				ArrayList<FullGameState> nextDepth = MoveGenerator
						.generateWhiteLegalMoves(currentGameState);

				// if the next depth contains no nodes then we just evaluate and
				// return
				if (nextDepth.size() == 0) {
					return Evaluator.evaluatePosition(currentGameState);
				}
				TreeMap<Long, FullGameState> orderedNextDepth = new TreeMap<Long, FullGameState>();

				// sorting the moves in order
				for (int i = 0; i < nextDepth.size(); i++) {
					orderedNextDepth.put(
							Evaluator.evaluatePosition(nextDepth.get(i)),
							nextDepth.get(i));
				}

				long value = Integer.MIN_VALUE;

				// going through each move
				for (int i = 0; i < orderedNextDepth.size(); i++) {
					value = Math.max(
							value,
							alphaBeta(orderedNextDepth.remove(orderedNextDepth
									.lastKey()), depth - 1, alpha, beta,
									!whiteToMove, moveOrderingDepth - 1));
					alpha = Math.max(alpha, value);

					if (alpha >= beta) {
						break;
					}
				}
				return value;

				// without move ordering
			} else {

				long value = Integer.MIN_VALUE;
				ArrayList<FullGameState> nextDepth = MoveGenerator
						.generateWhiteLegalMoves(currentGameState);

				// if the next depth contains no nodes then we just evaluate and
				// return
				if (nextDepth.size() == 0) {
					return Evaluator.evaluatePosition(currentGameState);
				}

				// going through each move
				for (int i = 0; i < nextDepth.size(); i++) {
					value = Math.max(
							value,
							alphaBeta(nextDepth.get(i), depth - 1, alpha, beta,
									!whiteToMove, 0));
					alpha = Math.max(alpha, value);

					if (alpha >= beta) {
						break;
					}
				}
				return value;
			}
		}

		else {
			
			if (moveOrderingDepth > 0) {
				ArrayList<FullGameState> nextDepth = MoveGenerator
						.generateBlackLegalMoves(currentGameState);
				
				if (nextDepth.size() == 0) {
					return Evaluator.evaluatePosition(currentGameState);
				}
				TreeMap<Long, FullGameState> orderedNextDepth = new TreeMap<Long, FullGameState>();
				
				// sorting the moves in order
				for (int i = 0; i < nextDepth.size(); i++) {
					orderedNextDepth.put(
							Evaluator.evaluatePosition(nextDepth.get(i)),
							nextDepth.get(i));
				}
				
				long value = Integer.MAX_VALUE;
				
				// going through each move
				for (int i = 0; i < orderedNextDepth.size(); i++) {
					value = Math.min(
							value,
							alphaBeta(orderedNextDepth.remove(orderedNextDepth
									.firstKey()), depth - 1, alpha, beta,
									!whiteToMove, moveOrderingDepth - 1));
					beta = Math.min(beta, value);
					if (alpha >= beta) {
						break;
					}
				}
				return value;
			} else {

				long value = Integer.MAX_VALUE;
				ArrayList<FullGameState> nextDepth = MoveGenerator
						.generateBlackLegalMoves(currentGameState);
				
				if (nextDepth.size() == 0) {
					return Evaluator.evaluatePosition(currentGameState);
				}
				
				for (int i = 0; i < nextDepth.size(); i++) {
					value = Math.min(
							value,
							alphaBeta(nextDepth.get(i), depth - 1, alpha, beta,
									!whiteToMove, 0));
					beta = Math.min(beta, value);
					
					if (alpha >= beta) {
						break;
					}

				}
				return value;
			}
		}
	}

}



























































