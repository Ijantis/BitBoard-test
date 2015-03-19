package search.alphabeta;

import java.util.ArrayList;
import java.util.TreeMap;

import movegen.MoveGenerator;
import evaluation.Evaluator;
import board.FullGameState;

public class AlphaBetaThread extends Thread {

	private long positionScore;
	private FullGameState currentState;
	private int depth;
	private long alpha;
	private long beta;
	private boolean whiteToMove;
	private int shouldOrder;

	public AlphaBetaThread(FullGameState currentGameState, int depth,
			long alpha, long beta, boolean whiteToMove, int shouldOrder) {

		this.currentState = currentGameState;
		this.depth = depth;
		this.alpha = alpha;
		this.beta = beta;
		this.whiteToMove = whiteToMove;
		this.shouldOrder = shouldOrder;

	}

	public long getScore() {
		return this.positionScore;
	}

	@Override
	public void run() {
		positionScore = alphaBeta(this.currentState, this.depth, this.alpha,
				this.beta, this.whiteToMove, this.shouldOrder);
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
	 * @param shouldOrder
	 *            - Whether or not this call should order the list of moves or
	 *            not.
	 * @return - The evaluation of the position to a given depth.
	 */
	public static long alphaBeta(FullGameState currentGameState, int depth,
			long alpha, long beta, boolean whiteToMove, int shouldOrder) {

		if (depth == 0) {
			return Evaluator.evaluatePosition(currentGameState);
		}

		if (whiteToMove) {
			if (shouldOrder > 0) {
				ArrayList<FullGameState> nextDepth = MoveGenerator
						.generateWhiteLegalMoves(currentGameState);
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
							alphaBeta(orderedNextDepth.get(orderedNextDepth
									.lastKey()), depth - 1, alpha, beta,
									!whiteToMove, shouldOrder - 1));
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
				if (nextDepth.size() == 0) {
					return Evaluator.evaluatePosition(currentGameState);
				}
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
			if (shouldOrder > 0) {
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
							alphaBeta(orderedNextDepth.get(orderedNextDepth
									.firstKey()), depth - 1, alpha, beta,
									!whiteToMove, shouldOrder - 1));
					orderedNextDepth.remove(orderedNextDepth.firstKey());
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
