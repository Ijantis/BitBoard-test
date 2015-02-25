package ai;

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
			listOfMoves = 
		}
		
		
		
		return null;

	}

	private Vector<char[][]> generateWhiteLegalMoves() {

		return MoveGenerator.generateWhiteLegalMoves(currentBoard, whitePawns,
				whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
				getBlackPieces(), getWhitePieces(), getBlackAttackingSquares(),
				blackPawns, blackRooks, blackKnights, blackBishops,
				blackQueens, blackKing, whiteCastleKing, whiteCastleQueen,
				enPassantSquare);
	}

	private Vector<char[][]> generateBlackLegalMoves() {
		return MoveGenerator.generateBlackLegalMoves(currentBoard, whitePawns,
				whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
				getBlackPieces(), getWhitePieces(), getWhiteAttackingSquares(),
				blackPawns, blackRooks, blackKnights, blackBishops,
				blackQueens, blackKing, blackCastleKing, blackCastleQueen,
				enPassantSquare);
	}

}
