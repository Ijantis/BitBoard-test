package ai;

import java.util.Random;
import java.util.Vector;

import board.FullGameState;
import board.GameState;
import operations.MoveGenerator;

public class Engine {

	public static final int AI_RANDOM = 0;
	public static final int AI_VERY_EASY = 1;

	public static GameState makeMove(int difficulty, boolean playingWhite,
			FullGameState gamestate) {

		switch (difficulty) {
		case AI_RANDOM:
			return makeRandomMove(playingWhite, gamestate);

		default:
			return null;
		}

	}

	// Generates a list of next possible moves
	private static GameState makeRandomMove(boolean playingWhite,
			FullGameState gamestate) {

		Vector<GameState> listOfMoves = new Vector<GameState>();

		if (playingWhite) {
			listOfMoves = generateWhiteLegalMoves(gamestate);
		} else {
			listOfMoves = generateBlackLegalMoves(gamestate);
		}

		Random myRandom = new Random();
		return listOfMoves.get(myRandom.nextInt(listOfMoves.size()));

	}

	private static Vector<GameState> generateWhiteLegalMoves(
			FullGameState gamestate) {

		return MoveGenerator.generateWhiteLegalMoves(gamestate);
	}

	private static Vector<GameState> generateBlackLegalMoves(
			FullGameState gamestate) {
		return MoveGenerator.generateBlackLegalMoves(gamestate);
	}

}
