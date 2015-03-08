package ai;

import java.util.Random;
import java.util.ArrayList;
import java.util.Vector;

import ai.evaluation.Evaluator;
import board.FullGameState;
import operations.MoveGenerator;

public class Engine {

	public static final int AI_RANDOM = 0;
	public static final int AI_VERY_EASY = 1;
	public static final int AI_EASY = 2;

	public static FullGameState makeMove(int difficulty, boolean whiteToMove,
			FullGameState currentGameState) {

		switch (difficulty) {
		case AI_RANDOM:
			return makeRandomMove(whiteToMove, currentGameState);
		case AI_VERY_EASY:
			return makeEvaluatedMove(whiteToMove, currentGameState);
		case AI_EASY:
			return calculateAlphaBeta(currentGameState, whiteToMove);
		default:
			return null;
		}

	}

	private static FullGameState calculateAlphaBeta(
			FullGameState currentGameState, boolean whiteToMove) {

		ArrayList<FullGameState> nextDepth;
		if (whiteToMove) {
			nextDepth = MoveGenerator.generateWhiteLegalMoves(currentGameState);
			int bestIndex = 0;
			long bestValue = Integer.MIN_VALUE;
			long currentScore;

			for (int i = 0; i < nextDepth.size(); i++) {
				currentScore = alphaBeta(nextDepth.get(i), 1,
						Integer.MIN_VALUE, Integer.MAX_VALUE, !whiteToMove);
				if (currentScore > bestValue) {
					bestIndex = i;
					bestValue = currentScore;
				}
			}

			return nextDepth.get(bestIndex);

		} else {
			nextDepth = MoveGenerator.generateBlackLegalMoves(currentGameState);
			int bestIndex = 0;
			long bestValue = Integer.MAX_VALUE;
			long currentScore;

			for (int i = 0; i < nextDepth.size(); i++) {
				currentScore = alphaBeta(nextDepth.get(i), 1,
						Integer.MIN_VALUE, Integer.MAX_VALUE, !whiteToMove);
				if (currentScore < bestValue) {
					bestIndex = i;
					bestValue = currentScore;
				}
			}

			return nextDepth.get(bestIndex);
		}

	}

	private static long alphaBeta(FullGameState currentGameState, int depth,
			long alpha, long beta, boolean whiteToMove) {

		if (depth == 0) {
			return Evaluator.evaluatePosition(currentGameState);
		}

		if (whiteToMove) {
			long value = Integer.MIN_VALUE;
			ArrayList<FullGameState> childNodes = MoveGenerator
					.generateWhiteLegalMoves(currentGameState);
			for (int i = 0; i < childNodes.size(); i++) {
				value = Math.max(
						value,
						alphaBeta(childNodes.get(i), depth - 1, alpha, beta,
								!whiteToMove));
				alpha = Math.max(alpha, value);
				if (alpha >= beta) {
					break;
				}
			}
			return value;
		}

		else {
			long value = Integer.MAX_VALUE;
			ArrayList<FullGameState> childNodes = MoveGenerator
					.generateBlackLegalMoves(currentGameState);
			for (int i = 0; i < childNodes.size(); i++) {
				value = Math.min(
						value,
						alphaBeta(childNodes.get(i), depth - 1, alpha, beta,
								!whiteToMove));
				beta = Math.min(beta, value);
				if (alpha >= beta) {
					break;
				}

			}
			return value;
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

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public static void printBoard(char[][] board) {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < board.length; x++) {
				char temp = board[x][y];
				if (temp == ' ') {
					System.out.print(", ");
				} else {
					System.out.print(board[x][y] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
