package search.random;

import java.util.ArrayList;
import java.util.Random;

import movegen.MoveGenerator;
import board.FullGameState;

public class RandomSearch {

	// Plays a completely random next move without a care
	public static FullGameState makeRandomMove(boolean playingWhite,
			FullGameState gamestate) {

		ArrayList<FullGameState> listOfMoves = new ArrayList<FullGameState>();

		if (playingWhite) {
			listOfMoves = MoveGenerator.generateWhiteLegalMoves(gamestate);
		} else {
			listOfMoves = MoveGenerator.generateBlackLegalMoves(gamestate);
		}

		Random myRandom = new Random(System.currentTimeMillis());
		return listOfMoves.get(myRandom.nextInt(listOfMoves.size()));

	}
	
	
}
