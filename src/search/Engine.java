package search;

import hash.ZobristKey;

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import search.alphabeta.AlphaBetaSearch;
import search.alphabeta.ThreadManager;
import search.fixed.StaticSearch;
import search.iterative.IterativeAlphaBeta;
import search.random.RandomSearch;
import evaluation.Evaluator;
import movegen.MoveGenerator;
import board.FullGameState;
import book.OpeningBook;

public class Engine {

	public static final int AI_RANDOM = 0;
	public static final int AI_VERY_EASY = 1;
	public static final int AI_EASY = 2;
	public static final int AI_NORMAL = 3;
	public static final int AI_THREAD = 4;
	public static final int AI_ITERATIVE = 5;

	public static FullGameState makeMove(int difficulty, boolean whiteToMove,
			FullGameState currentGameState) {

		switch (difficulty) {
		case AI_RANDOM:
			return RandomSearch.makeRandomMove(whiteToMove, currentGameState);
		case AI_VERY_EASY:
			return StaticSearch.makeStaticEvaluatedMove(whiteToMove,
					currentGameState);
		case AI_EASY:
			return AlphaBetaSearch.calculateAlphaBeta(currentGameState,
					whiteToMove);
		case AI_NORMAL:
			return makeNormalMove(currentGameState, whiteToMove);
		case AI_THREAD:
			return ThreadManager.alphaBetaThreaded(currentGameState,
					whiteToMove);
		case AI_ITERATIVE:
			return IterativeAlphaBeta.iterativeAlphaBeta(currentGameState,
					whiteToMove);
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

}
