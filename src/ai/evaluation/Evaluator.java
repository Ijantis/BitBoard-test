package ai.evaluation;

import java.util.Vector;

import board.FullGameState;
import operations.BitboardOperations;
import operations.MoveGenerator;

public class Evaluator {

	private static final long centralSquares = Long.parseLong(
			"0000000000000000000000000001100000011000000000000000000000000000",
			2);

	// USE GLOBAL VARIABLES FOR CHANGING STUFF IN THE EVALUATION FUNCTION

	// for central control evaluate number of pieces in central square and extra
	// points for pieces in central squares that are not under attack

	// piece specific positioning

	public static long evaluatePosition(FullGameState currentGameState) {

		long whiteScore = 0;
		long blackScore = 0;

		Vector<FullGameState> whiteMoves = MoveGenerator
				.generateWhiteLegalMoves(currentGameState);
		Vector<FullGameState> blackMoves = MoveGenerator
				.generateBlackLegalMoves(currentGameState);

		// checkmate gives a lot of points
		if (currentGameState.getWhiteToMove()
				&& (currentGameState.getWhiteKing() & currentGameState
						.getBlackAttackingSquares()) != 0
				&& whiteMoves.size() == 0) {

			blackScore += 50000;
			return whiteScore - blackScore;
			// black checkmated
		} else if (!currentGameState.getWhiteToMove()
				&& (currentGameState.getBlackKing() & currentGameState
						.getWhiteAttackingSquares()) != 0
				&& blackMoves.size() == 0) {
			whiteScore += 50000;
			return whiteScore - blackScore;
		}

		// material
		whiteScore += evaluateWhiteMaterial(currentGameState);
		blackScore += evaluateBlackMaterial(currentGameState);

		// pawn structure
		whiteScore += evaluateWhitePawnStructure(currentGameState);
		blackScore += evaluateBlackPawnStructure(currentGameState);

		// hanging pieces
		whiteScore += evaluateWhiteProtectedHangingPieces(currentGameState);
		blackScore += evaluateBlackProtectedHangingPieces(currentGameState);

		// number of possible moves
		// System.out.println("White mobility "
		// + (MoveGenerator.generateWhiteLegalMoves(currentGameState)
		// .size() * 15));
		 whiteScore += MoveGenerator.generateWhiteLegalMoves(currentGameState)
		 .size() * 15;
		// System.out.println("Black mobility "
		// + (MoveGenerator.generateBlackLegalMoves(currentGameState)
		// .size() * 15));
		blackScore += MoveGenerator.generateBlackLegalMoves(currentGameState)
				.size() * 15;

		whiteScore += evaluateWhiteCentralControl(currentGameState);
		blackScore += evaluateBlackCentralControl(currentGameState);

		// System.out.println("White score " + whiteScore);
		// System.out.println("Black score " + blackScore);

		return whiteScore - blackScore;

	}

	// whiteScore += evaluateWhiteCentralControl(whiteAttackingSquares);
	// blackScore += evaluateBlackCentralControl(blackAttackingSquares);

	private static long evaluateWhiteCentralControl(
			FullGameState currentGameState) {
		long score = 0;

		score += Long.bitCount(currentGameState.getWhiteAttackingSquares()
				& centralSquares) * 15;

		score += Long.bitCount(currentGameState.getWhitePieces()
				& centralSquares) * 40;

		// System.out.println("White central control " + score);

		return score;
	}

	private static long evaluateBlackCentralControl(
			FullGameState currentGameState) {
		long score = 0;

		score += Long.bitCount(currentGameState.getBlackAttackingSquares()
				& centralSquares) * 15;

		score += Long.bitCount(currentGameState.getBlackPieces()
				& centralSquares) * 40;

		// System.out.println("Black central control " + score);

		return score;
	}

	private static long evaluateBlackProtectedHangingPieces(
			FullGameState currentGameState) {
		long score = 0;
		long score1 = 0;

		long protectedPieces = currentGameState.getBlackAttackingSquares()
				& currentGameState.getBlackPieces();

		score += Long.bitCount(currentGameState.getBlackPawns()
				& protectedPieces) * 30;

		score += Long
				.bitCount((currentGameState.getBlackBishops() | currentGameState
						.getBlackKnights()) & protectedPieces) * 30;

		score += Long.bitCount(currentGameState.getBlackRooks()
				& protectedPieces) * 50;

		score += Long.bitCount(currentGameState.getBlackQueens()
				& protectedPieces) * 65;

		// System.out.println("Black protected pieces " + score);

		long hangingPieces = currentGameState.getBlackPieces()
				^ protectedPieces;

		score1 += Long.bitCount(currentGameState.getBlackPawns()
				& hangingPieces)
				* -10;

		score1 += Long
				.bitCount((currentGameState.getBlackBishops() | currentGameState
						.getBlackKnights()) & hangingPieces)
				* -25;

		score1 += Long.bitCount(currentGameState.getBlackRooks()
				& hangingPieces)
				* -40;

		score1 += Long.bitCount(currentGameState.getBlackQueens()
				& hangingPieces)
				* -60;

		// System.out.println("Black hanging pieces " + score1);
		return score + score1;
	}

	private static long evaluateWhiteProtectedHangingPieces(
			FullGameState currentGameState) {

		long score = 0;
		long score1 = 0;

		long protectedPieces = currentGameState.getWhiteAttackingSquares()
				& currentGameState.getWhitePieces();

		score += Long.bitCount(currentGameState.getWhitePawns()
				& protectedPieces) * 30;

		score += Long
				.bitCount((currentGameState.getWhiteBishops() | currentGameState
						.getWhiteKnights()) & protectedPieces) * 30;

		score += Long.bitCount(currentGameState.getWhiteRooks()
				& protectedPieces) * 50;

		score += Long.bitCount(currentGameState.getWhiteQueens()
				& protectedPieces) * 65;

		// System.out.println("White protected pieces " + score);

		long hangingPieces = currentGameState.getWhitePieces()
				^ protectedPieces;

		score1 += Long.bitCount(currentGameState.getWhitePawns()
				& hangingPieces)
				* -10;

		score1 += Long
				.bitCount((currentGameState.getWhiteBishops() | currentGameState
						.getWhiteKnights()) & hangingPieces)
				* -25;

		score1 += Long.bitCount(currentGameState.getWhiteRooks()
				& hangingPieces)
				* -40;

		score1 += Long.bitCount(currentGameState.getWhiteQueens()
				& hangingPieces)
				* -60;

		// System.out.println("White hanging pieces " + score1);
		return score + score1;
	}

	private static long evaluateWhitePawnStructure(
			FullGameState currentGameState) {
		long score = 0;
//		System.out.print("White ");
		score += evaluatelongdPawns(currentGameState.getWhitePawns());
//		System.out.print("White ");
		score += evaluateIsolatedPawns(currentGameState.getWhitePawns());
		return score;
	}

	private static long evaluateBlackPawnStructure(
			FullGameState currentGameState) {
		long score = 0;
//		System.out.print("Black ");
		score += evaluatelongdPawns(currentGameState.getBlackPawns());
//		System.out.print("Black ");
		score += evaluateIsolatedPawns(currentGameState.getBlackPawns());
		return score;
	}

	private static long evaluateBlackCentralControl(long blackAttackingSquares) {
		long score = 0;
		score += Long.bitCount((blackAttackingSquares & centralSquares)) * 0.2;
		// System.out.println("Black central control : " + score);
		return score;
	}

	private static long evaluateWhiteCentralControl(long whiteAttackingSquares) {
		long score = 0;
		score += Long.bitCount((whiteAttackingSquares & centralSquares)) * 0.2;
		// System.out.println("White central control :" + score);
		return score;
	}

	// maybe use an array to keep track of the masked files
	// first check if there are pawns in a file then check if adjacent files are
	// empty
	private static long evaluateIsolatedPawns(long pawns) {
		long score = 0;

		// evaluating the first file
		int pawnsInFile = Long.bitCount(pawns & BitboardOperations.maskFile(1));
		if (pawnsInFile != 0 && (pawns & BitboardOperations.maskFile(2)) == 0) {
			score -= 20 * pawnsInFile;
			// System.out.println("isolated in 1");
		}

		for (int file = 2; file < 8; file++) {
			if ((BitboardOperations.maskFile(file) & pawns) == 0) {
				continue;
			} else {
				if ((BitboardOperations.maskFile(file - 1) & pawns) == 0
						&& (BitboardOperations.maskFile(file + 1) & pawns) == 0) {
					score -= 20 * Long.bitCount(BitboardOperations
							.maskFile(file) & pawns);
					// System.out.println("Isolated pawn in file " + file);
				}
			}
		}

		// evaluating the eighth file
		pawnsInFile = Long.bitCount(pawns & BitboardOperations.maskFile(8));
		if (pawnsInFile != 0 && (pawns & BitboardOperations.maskFile(7)) == 0) {
			score -= 20 * pawnsInFile;
			// System.out.println("isolated in 8");
		}
		// System.out.println("Isolated pawns " + score);
		return score;
	}

	private static long evaluatelongdPawns(long pawns) {
		long score = 0;
		for (int file = 1; file < 9; file++) {
			int pawnsInFile = 0;
			pawnsInFile = Long.bitCount(pawns
					& BitboardOperations.maskFile(file));
			if (pawnsInFile > 1) {
				score -= (pawnsInFile * 20) * (pawnsInFile - 1);
			}
		}
		// System.out.println("longd pawns : " + score);
		return score;
	}

	// return positive value
	private static long evaluateWhiteMaterial(FullGameState myGameState) {

		long score = 0;

		score += Long.bitCount(myGameState.getWhitePawns()) * 100;
		score += Long.bitCount(myGameState.getWhiteRooks()) * 500;
		score += Long.bitCount(myGameState.getWhiteBishops()) * 300;
		score += Long.bitCount(myGameState.getWhiteKnights()) * 300;
		score += Long.bitCount(myGameState.getWhiteQueens()) * 900;
		// System.out.println("White material " + score);
		return score;
	}

	// returns negative value
	private static long evaluateBlackMaterial(FullGameState myGameState) {

		long score = 0;

		score += Long.bitCount(myGameState.getBlackPawns()) * 100;
		score += Long.bitCount(myGameState.getBlackRooks()) * 500;
		score += Long.bitCount(myGameState.getBlackBishops()) * 300;
		score += Long.bitCount(myGameState.getBlackKnights()) * 300;
		score += Long.bitCount(myGameState.getBlackQueens()) * 900;

		// System.out.println("Black material " + score);
		return score;
	}

	private static void printBitboard(long bitBoard) {
		String stringBitBoard = Long.toBinaryString(bitBoard);
		System.out.println("Value : " + stringBitBoard);
		while (stringBitBoard.length() != 64) {
			stringBitBoard = "0" + stringBitBoard;
		}

		for (int i = 0; i < 8; i++) {
			StringBuilder stringReverser = new StringBuilder(
					stringBitBoard.substring(i * 8, ((i + 1) * 8)));
			stringReverser.reverse();
			for (int j = 0; j < stringReverser.toString().length(); j++) {
				System.out.print(stringReverser.toString().charAt(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}