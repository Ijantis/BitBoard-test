package ai;

import java.util.Random;
import java.util.Vector;

import board.Gamestate;
import operations.MoveGenerator;

public class Engine {

	public static final int AI_RANDOM = 0;
	public static final int AI_VERY_EASY = 1;

	public static char[][] makeMove(int difficulty, boolean playingWhite,
			Gamestate gamestate) {

		switch (difficulty) {
		case AI_RANDOM:
			return makeRandomMove(playingWhite, gamestate);

		default:
			return null;
		}

	}

	// Generates a list of next possible moves
	private static char[][] makeRandomMove(boolean playingWhite,
			Gamestate gamestate) {

		Vector<char[][]> listOfMoves = new Vector<char[][]>();

		if (playingWhite) {
			listOfMoves = generateWhiteLegalMoves(gamestate);
		} else {
			listOfMoves = generateBlackLegalMoves(gamestate);
		}

		Random myRandom = new Random();
		return listOfMoves.get(myRandom.nextInt(listOfMoves.size()));

	}

	private static Vector<char[][]> generateWhiteLegalMoves(Gamestate gamestate) {

		return MoveGenerator
				.generateWhiteLegalMoves(gamestate.getCurrentBoard(),
						gamestate.getWhitePawns(), gamestate.getWhiteRooks(),
						gamestate.getWhiteKnights(),
						gamestate.getWhiteBishops(),
						gamestate.getWhiteQueens(), gamestate.getWhiteKing(),
						gamestate.getBlackPieces(), gamestate.getWhitePieces(),
						gamestate.getBlackAttackingSquares(),
						gamestate.getBlackPawns(), gamestate.getBlackRooks(),
						gamestate.getBlackKnights(),
						gamestate.getBlackBishops(),
						gamestate.getBlackQueens(), gamestate.getBlackKing(),
						gamestate.canWhiteCastleKing(),
						gamestate.canWhiteCastleQueen(),
						gamestate.getEnPassantSquare());
	}

	private static Vector<char[][]> generateBlackLegalMoves(Gamestate gamestate) {
		return MoveGenerator
				.generateBlackLegalMoves(gamestate.getCurrentBoard(),
						gamestate.getWhitePawns(), gamestate.getWhiteRooks(),
						gamestate.getWhiteKnights(),
						gamestate.getWhiteBishops(),
						gamestate.getWhiteQueens(), gamestate.getWhiteKing(),
						gamestate.getBlackPieces(), gamestate.getWhitePieces(),
						gamestate.getWhiteAttackingSquares(),
						gamestate.getBlackPawns(), gamestate.getBlackRooks(),
						gamestate.getBlackKnights(),
						gamestate.getBlackBishops(),
						gamestate.getBlackQueens(), gamestate.getBlackKing(),
						gamestate.canBlackCastleKing(),
						gamestate.canBlackCastleQueen(),
						gamestate.getEnPassantSquare());
	}

}
