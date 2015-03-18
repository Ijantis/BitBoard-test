package search;

import hash.ZobristKey;

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import evaluation.Evaluator;
import movegen.MoveGenerator;
import board.AlphaBetaSearch;
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
			return AlphaBetaSearch.calculateAlphaBeta(currentGameState,
					whiteToMove);
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
			return AlphaBetaSearch.calculateAlphaBeta(currentGameState,
					whiteToMove, nextDepth);
		}
		return AlphaBetaSearch.calculateAlphaBeta(currentGameState,
				whiteToMove, nextDepth);
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

		Random myRandom = new Random(System.currentTimeMillis());
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
