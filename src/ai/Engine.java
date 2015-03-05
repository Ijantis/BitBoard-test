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

		Vector<FullGameState> listOfMoves = new Vector<FullGameState>();

		if (playingWhite) {

			listOfMoves = MoveGenerator
					.generateWhiteLegalMoves(currentGameState);

			long bestScore = Long.MIN_VALUE;
			long currentScore;
			int bestIndex = 0;

			for (int i = 0; i < listOfMoves.size(); i++) {
				Vector<FullGameState> nextDepth = new Vector<FullGameState>();
				nextDepth = MoveGenerator.generateBlackLegalMoves(listOfMoves
						.get(i));
				if (nextDepth.size() == 0) {
					currentScore = Evaluator.evaluatePosition(listOfMoves
							.get(i));
					if (currentScore > bestScore) {
						bestScore = currentScore;
						bestIndex = i;
					}
				}
				for (int j = 0; j < nextDepth.size(); j++) {
					currentScore = Evaluator.evaluatePosition(nextDepth.get(j));
					if (currentScore > bestScore) {
						System.out.println(currentScore);
						System.out.println(bestScore);
						printBoard(nextDepth.get(j).getCurrentBoard());
						bestScore = currentScore;
						bestIndex = i;
					}
				}

			}
			return listOfMoves.get(bestIndex);
		} else {

			listOfMoves = MoveGenerator
					.generateBlackLegalMoves(currentGameState);

			long bestScore = Long.MAX_VALUE;
			long currentScore;
			int bestIndex = 0;

			for (int i = 0; i < listOfMoves.size(); i++) {
				Vector<FullGameState> nextDepth = new Vector<FullGameState>();
				nextDepth = MoveGenerator.generateWhiteLegalMoves(listOfMoves
						.get(i));
				if (nextDepth.size() == 0) {
					currentScore = Evaluator.evaluatePosition(listOfMoves
							.get(i));
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestIndex = i;
					}
				}
				for (int j = 0; j < nextDepth.size(); j++) {
					currentScore = Evaluator.evaluatePosition(nextDepth.get(j));
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestIndex = i;
					}
				}

			}
			printBoard(listOfMoves.get(bestIndex).getCurrentBoard());
			return listOfMoves.get(bestIndex);
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
