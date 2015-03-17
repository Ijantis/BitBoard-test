package search;

import hash.ZobristKey;

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import evaluation.Evaluator;
import movegen.MoveGenerator;
import bitboards.BitboardOperations;
import board.ChessBoard;
import board.FullGameState;
import book.OpeningBook;

public class Engine {

	public static final int AI_RANDOM = 0;
	public static final int AI_VERY_EASY = 1;
	public static final int AI_EASY = 2;
	public static final int AI_NORMAL = 3;

	public static FullGameState makeMove(int difficulty, boolean whiteToMove,
			FullGameState currentGameState) {

		switch (difficulty) {
		case AI_RANDOM:
			return makeRandomMove(whiteToMove, currentGameState);
		case AI_VERY_EASY:
			return makeEvaluatedMove(whiteToMove, currentGameState);
		case AI_EASY:
			return calculateAlphaBeta(currentGameState, whiteToMove);
		case AI_NORMAL:
			return makeNormalMove(currentGameState, whiteToMove);
		default:
			return null;
		}

	}

	private static FullGameState makeNormalMove(FullGameState currentGameState,
			boolean whiteToMove) {

		ArrayList<FullGameState> nextDepth;

		if (whiteToMove) {
			nextDepth = MoveGenerator.generateWhiteLegalMoves(currentGameState);
		} else {
			nextDepth = MoveGenerator.generateBlackLegalMoves(currentGameState);
		}

		String nextBookMove = OpeningBook.makeBookMove(ZobristKey
				.getKey(currentGameState));

		if (!nextBookMove.equals("N")) {
			Scanner myScanner = new Scanner(nextBookMove);
			myScanner.useDelimiter("-");
			String one = myScanner.next();
			String two = myScanner.next();
			for (int i = 0; i < nextDepth.size(); i++) {
				if (nextDepth.get(i).getFromSquare() == Integer.parseInt(one)
						&& nextDepth.get(i).getToSquare() == Integer
								.parseInt(two)) {
					System.out.println("book move made");
					myScanner.close();
					return nextDepth.get(i);
				}
			}
			myScanner.close();
		} else {
			return calculateAlphaBeta(currentGameState, whiteToMove, nextDepth);
		}
		return calculateAlphaBeta(currentGameState, whiteToMove, nextDepth);
	}

	private static FullGameState calculateAlphaBeta(
			FullGameState currentGameState, boolean whiteToMove,
			ArrayList<FullGameState> nextDepth) {
		int depth = 3;

		if (whiteToMove) {
			int bestIndex = 0;
			long bestValue = Integer.MIN_VALUE;
			long currentScore;

			for (int i = 0; i < nextDepth.size(); i++) {
				currentScore = alphaBeta(nextDepth.get(i), depth,
						Integer.MIN_VALUE, Integer.MAX_VALUE, !whiteToMove,
						true);
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
						true);
				if (currentScore < bestValue) {
					bestIndex = i;
					bestValue = currentScore;
				}
			}

			return nextDepth.get(bestIndex);
		}
	}

	private static FullGameState calculateAlphaBeta(
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
	 * @param shouldOrder
	 *            - Whether or not this call should order the list of moves or
	 *            not.
	 * @return - The evaluation of the position to a given depth.
	 */
	private static long alphaBeta(FullGameState currentGameState, int depth,
			long alpha, long beta, boolean whiteToMove, boolean shouldOrder) {

		if (depth == 0) {
			return Evaluator.evaluatePosition(currentGameState);
		}

		if (whiteToMove) {
			if (shouldOrder) {
				ArrayList<FullGameState> nextDepth = MoveGenerator
						.generateWhiteLegalMoves(currentGameState);
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
									!whiteToMove, false));
					alpha = Math.max(alpha, value);
					if (alpha >= beta) {
						break;
					}
				}
				return value;
				// without move ordering
			} else {
				long value = Integer.MIN_VALUE;
				ArrayList<FullGameState> childNodes = MoveGenerator
						.generateWhiteLegalMoves(currentGameState);
				for (int i = 0; i < childNodes.size(); i++) {
					value = Math.max(
							value,
							alphaBeta(childNodes.get(i), depth - 1, alpha,
									beta, !whiteToMove, false));
					alpha = Math.max(alpha, value);
					if (alpha >= beta) {
						break;
					}
				}
				return value;
			}
		}

		else {
			if (shouldOrder) {
				ArrayList<FullGameState> nextDepth = MoveGenerator
						.generateBlackLegalMoves(currentGameState);
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
									!whiteToMove, false));
					orderedNextDepth.remove(orderedNextDepth.firstKey());
					beta = Math.min(beta, value);
					if (alpha >= beta) {
						break;
					}
				}
				return value;
			} else {

				long value = Integer.MAX_VALUE;
				ArrayList<FullGameState> childNodes = MoveGenerator
						.generateBlackLegalMoves(currentGameState);
				for (int i = 0; i < childNodes.size(); i++) {
					value = Math.min(
							value,
							alphaBeta(childNodes.get(i), depth - 1, alpha,
									beta, !whiteToMove, false));
					beta = Math.min(beta, value);
					if (alpha >= beta) {
						break;
					}

				}
				return value;
			}
		}
	}

	private static FullGameState makeEvaluatedMove(boolean playingWhite,
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

	// Plays a completely random next move without a care
	private static FullGameState makeRandomMove(boolean playingWhite,
			FullGameState gamestate) {

		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

		if (playingWhite) {
			listOfMoves = generateWhiteLegalMoves(gamestate);
		} else {
			listOfMoves = generateBlackLegalMoves(gamestate);
		}

		Random myRandom = new Random();
		return listOfMoves.get(myRandom.nextInt(listOfMoves.size()));

	}

	private static ArrayList<FullGameState> generateWhiteLegalMoves(
			FullGameState gamestate) {

		return MoveGenerator.generateWhiteLegalMoves(gamestate);
	}

	private static ArrayList<FullGameState> generateBlackLegalMoves(
			FullGameState gamestate) {
		return MoveGenerator.generateBlackLegalMoves(gamestate);
	}

}
