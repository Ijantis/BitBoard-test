package ai;

import java.util.Random;
import java.util.Vector;

import ai.evaluation.Evaluator;
import board.FullGameState;
import operations.MoveGenerator;

public class Engine {

	public static final int AI_RANDOM = 0;
	public static final int AI_VERY_EASY = 1;

	public static FullGameState makeMove(int difficulty, boolean playingWhite,
			FullGameState currentGameState) {

		switch (difficulty) {
		case AI_RANDOM:
			return makeRandomMove(playingWhite, currentGameState);
		case AI_VERY_EASY:
			return makeEvaluatedMove(playingWhite, currentGameState);
		default:
			return null;
		}

	}

	private static FullGameState makeEvaluatedMove(boolean playingWhite,
			FullGameState currentGameState) {

		Vector<FullGameState> depthOne = new Vector<FullGameState>();

		if (playingWhite) {

			depthOne = MoveGenerator
					.generateWhiteLegalMoves(currentGameState);

			long bestScore = Long.MIN_VALUE;
			long currentScore;
			int bestIndex = 0;

			for (int i = 0; i < depthOne.size(); i++) {
				Vector<FullGameState> depthTwo = new Vector<FullGameState>();
				depthTwo = MoveGenerator.generateBlackLegalMoves(depthOne
						.get(i));
				if (depthTwo.size() == 0) {
					currentScore = Evaluator.evaluatePosition(depthOne
							.get(i));
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

			depthOne = MoveGenerator
					.generateBlackLegalMoves(currentGameState);

			long bestScore = Long.MAX_VALUE;
			long currentScore;
			int bestIndex = 0;

			for (int i = 0; i < depthOne.size(); i++) {
				Vector<FullGameState> depthTwo = new Vector<FullGameState>();
				depthTwo = MoveGenerator.generateWhiteLegalMoves(depthOne
						.get(i));
				if (depthTwo.size() == 0) {
					currentScore = Evaluator.evaluatePosition(depthOne
							.get(i));
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
			printBoard(depthOne.get(bestIndex).getCurrentBoard());
			return depthOne.get(bestIndex);
		}
	}

	// Plays a completely random next move without a care
	private static FullGameState makeRandomMove(boolean playingWhite,
			FullGameState gamestate) {

		Vector<FullGameState> listOfMoves = new Vector<FullGameState>();

		if (playingWhite) {
			listOfMoves = generateWhiteLegalMoves(gamestate);
		} else {
			listOfMoves = generateBlackLegalMoves(gamestate);
		}

		Random myRandom = new Random();
		return listOfMoves.get(myRandom.nextInt(listOfMoves.size()));

	}

	private static Vector<FullGameState> generateWhiteLegalMoves(
			FullGameState gamestate) {

		return MoveGenerator.generateWhiteLegalMoves(gamestate);
	}

	private static Vector<FullGameState> generateBlackLegalMoves(
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
