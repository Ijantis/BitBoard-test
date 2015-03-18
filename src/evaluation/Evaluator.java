package evaluation;

import movegen.MoveGenerator;
import bitboards.BitboardOperations;
import board.FullGameState;

public class Evaluator {

	private static final long centralSquares = Long.parseLong(
			"0000000000000000000000000001100000011000000000000000000000000000",
			2);
	private static int pawnMaterial = 150;
	private static int rookMaterial = 650;
	private static int knightMaterial = 400;
	private static int bishopMaterial = 400;
	private static int queenMaterial = 1200;

	private static int attackingCentralSquare = 25;
	private static int occupyingCentralSquare = 50;

	private static int mobility = 10;

	private static int hangingPawns = -40;
	private static int hangingRooks = -200;
	private static int hangingKnights = -100;
	private static int hangingBishops = -100;
	private static int hangingQueens = -500;

	// taken from
	// http://chessprogramming.wikispaces.com/Simplified+evaluation+function

	static int pawnBoard[][] = { { 0, 5, 5, 0, 5, 10, 50, 0 },
			{ 0, 10, -5, 0, 5, 10, 50, 0 }, { 0, 10, -10, 0, 10, 20, 50, 0 },
			{ 0, -20, 0, 20, 25, 30, 50, 0 }, { 0, -20, 0, 20, 25, 30, 50, 0 },
			{ 0, 10, -10, 0, 10, 20, 50, 0 }, { 0, 10, -5, 0, 5, 10, 50, 0 },
			{ 0, 5, 5, 0, 5, 10, 50, 0 } };;

	static int rookBoard[][] = { { 0, -5, -5, -5, -5, -5, 5, 0 },
			{ 0, 0, 0, 0, 0, 0, 10, 0 }, { 0, 0, 0, 0, 0, 0, 10, 0 },
			{ 5, 0, 0, 0, 0, 0, 10, 0 }, { 5, 0, 0, 0, 0, 0, 10, 0 },
			{ 0, 0, 0, 0, 0, 0, 10, 0 }, { 0, 0, 0, 0, 0, 0, 10, 0 },
			{ -5, -5, -5, -5, -5, -5, 5, 0 }, };;

	static int knightBoard[][] = { { -50, -40, -30, -30, -30, -30, -40, -50 },
			{ -40, -20, 5, 0, 5, 0, -20, -40 },
			{ -30, 0, 10, 15, 15, 10, 0, -30 },
			{ -30, 5, 15, 20, 20, 15, 0, -30 },
			{ -30, 5, 15, 20, 20, 15, 0, -30 },
			{ -30, 0, 10, 15, 15, 10, 0, -30 },
			{ -40, -20, 5, 0, 5, 0, -20, -40 },
			{ -50, -40, -30, -30, -30, -30, -40, -50 }, };;

	static int bishopBoard[][] = { { -20, -10, -10, -10, -10, -10, -10, -20 },
			{ -10, 5, 10, 0, 5, 0, 0, -10 }, { -10, 0, 10, 10, 5, 5, 0, -10 },
			{ -10, 10, 10, 10, 10, 10, 0, -10 },
			{ -10, 10, 10, 10, 10, 10, 0, -10 },
			{ -10, 0, 10, 10, 5, 5, 0, -10 }, { -10, 5, 10, 0, 5, 0, 0, -10 },
			{ -20, -10, -10, -10, -10, -10, -10, -20 }, };

	static final int queenBoard[][] = {
			{ -20, -10, -10, 0, -5, -10, -10, -20 },
			{ -10, 0, 5, 0, 0, 0, 0, -10 }, { -10, 5, 5, 5, 5, 5, 0, -10 },
			{ -5, 0, 5, 5, 5, 5, 0, -5 }, { -5, 0, 5, 5, 5, 5, 0, -5 },
			{ -10, 0, 5, 5, 5, 5, 0, -10 }, { -10, 0, 0, 0, 0, 0, 0, -10 },
			{ -20, -10, -10, -5, -5, -10, -10, -20 }, };;

	static final int kingMidGameBoard[][] = {
			{ 20, 20, -10, -20, -30, -30, -30, -30 },
			{ 30, 20, -20, -30, -40, -40, -40, -40 },
			{ 10, 0, -20, -30, -40, -40, -40, -40 },
			{ 0, 0, -20, -40, -50, -50, -50, -50 },
			{ 0, 0, -20, -40, -40, -50, -50, -50 },
			{ 10, 0, -20, -30, -40, -40, -40, -40 },
			{ 30, 20, -20, -30, -40, -40, -40, -40 },
			{ 20, 20, -10, -20, -30, -30, -30, -30 }, };;

	static final int kingLateGameBoard[][] = {
			{ -50, -30, -30, -30, -30, -30, -30, -50 },
			{ -30, -30, -10, -10, -10, -10, -20, -40 },
			{ -30, 0, 20, 30, 30, 20, -10, -30 },
			{ -30, 0, 30, 40, 40, 30, 0, -20 },
			{ -30, 0, 30, 40, 40, 30, 0, -20 },
			{ -30, 0, 20, 30, 30, 20, -10, -30 },
			{ -30, -30, -10, -10, -10, -10, -20, -40 },
			{ -50, -30, -30, -30, -30, -30, -30, -50 }, };;

	static final int template[][] = { { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, };;

	// USE GLOBAL VARIABLES FOR CHANGING STUFF IN THE EVALUATION FUNCTION

	// for central control evaluate number of pieces in central square and extra
	// points for pieces in central squares that are not under attack

	// piece specific positioning

	public static long evaluatePosition(FullGameState currentGameState) {

		long whiteScore = 0;
		long blackScore = 0;

		/*
		 * If its white to move and the king is under attack then you need to
		 * calculate moves
		 */
		if (currentGameState.getWhiteToMove()
				&& (currentGameState.getWhiteKing() & currentGameState
						.getBlackAttackingSquares()) != 0) {

		}

		// if white to move
		if (currentGameState.getWhiteToMove()) {

			long noMoves = MoveGenerator
					.generateWhiteLegalMoveCount(currentGameState);
			// if white king is under attack
			if ((currentGameState.getWhiteKing() & currentGameState
					.getBlackAttackingSquares()) != 0) {
				// checkmate
				if (noMoves == 0) {
					whiteScore -= 200000;
				}
			} else {
				// stalemate
				if (noMoves == 0) {
					System.out.println("Stalemate white to move");
					// whiteScore -= 150000;
					return 0;
				}
			}
		} else {
			long noMoves = MoveGenerator
					.generateBlackLegalMoveCount(currentGameState);
			if ((currentGameState.getBlackKing() & currentGameState
					.getWhiteAttackingSquares()) != 0) {
				if (noMoves == 0) {
					blackScore -= 200000;
				}
			} else if (noMoves == 0) {
				System.out.println("stalemate black to move");
				// blackScore -= 150000;
				return 0;
			}

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

		whiteScore += evaluateWhiteCentralControl(currentGameState);
		blackScore += evaluateBlackCentralControl(currentGameState);

		// this evaluates negatively for black
		// whiteScore += evaluatePositional(currentGameState.getCurrentBoard(),
		// Long.bitCount(currentGameState.getAllPieces()));

		// System.out.println("White score " + whiteScore);
		// System.out.println("Black score " + blackScore);

		return whiteScore - blackScore;

	}

	private static long evaluatePositional(char[][] currentBoard,
			int numberOfPieces) {

		int score = 0;

		for (int i = 0; i < currentBoard.length; i++) {
			switch (currentBoard[i / 8][i % 8]) {

			case 'P':
				score += pawnBoard[i / 8][i % 8];
				break;
			case 'R':
				score += rookBoard[i / 8][i % 8];
				break;
			case 'N':
				score += knightBoard[i / 8][i % 8];
				break;
			case 'B':
				score += bishopBoard[i / 8][i % 8];
				break;
			case 'Q':
				score += queenBoard[i / 8][i % 8];
				break;
			case 'K':
				if (numberOfPieces > 7) {
					score += kingMidGameBoard[i / 8][i % 8];
				} else {
					score += kingLateGameBoard[i / 8][i % 8];
				}
				break;
			case 'p':
				score -= pawnBoard[flipCoordinates(i / 8)][flipCoordinates(i % 8)];
				break;
			case 'r':
				score -= rookBoard[flipCoordinates(i / 8)][flipCoordinates(i % 8)];
				break;
			case 'n':
				score -= knightBoard[flipCoordinates(i / 8)][flipCoordinates(i % 8)];
				break;
			case 'b':
				score -= bishopBoard[flipCoordinates(i / 8)][flipCoordinates(i % 8)];
				break;
			case 'q':
				score -= queenBoard[flipCoordinates(i / 8)][flipCoordinates(i % 8)];
				break;
			case 'k':
				if (numberOfPieces > 7) {
					score -= kingMidGameBoard[flipCoordinates(i / 8)][flipCoordinates(i % 8)];
				} else {
					score -= kingLateGameBoard[flipCoordinates(i / 8)][flipCoordinates(i % 8)];
				}
				break;
			}
		}
		return score * 2;
	}

	private static int flipCoordinates(int coord) {
		switch (coord) {
		case 0:
			return 7;
		case 1:
			return 6;
		case 2:
			return 5;
		case 3:
			return 4;
		case 4:
			return 3;
		case 5:
			return 2;
		case 6:
			return 1;
		case 7:
			return 0;
		}

		return 0;
	}

	private static int evaluateWhiteCentralControl(
			FullGameState currentGameState) {
		int score = 0;

		score += Long.bitCount(currentGameState.getWhiteAttackingSquares()
				& centralSquares)
				* attackingCentralSquare;

		score += Long.bitCount(currentGameState.getWhitePieces()
				& centralSquares)
				* occupyingCentralSquare;

		// System.out.println("White central control " + score);

		return score;
	}

	private static int evaluateBlackCentralControl(
			FullGameState currentGameState) {
		int score = 0;

		score += Long.bitCount(currentGameState.getBlackAttackingSquares()
				& centralSquares)
				* attackingCentralSquare;

		score += Long.bitCount(currentGameState.getBlackPieces()
				& centralSquares)
				* occupyingCentralSquare;

		// System.out.println("Black central control " + score);

		return score;
	}

	private static int evaluateBlackProtectedHangingPieces(
			FullGameState currentGameState) {
		int protectedScore = 0;
		int hangingScore = 0;

		long protectedPieces = currentGameState.getBlackAttackingSquares()
				& currentGameState.getBlackPieces();

		protectedScore += Long.bitCount(currentGameState.getBlackPawns()
				& protectedPieces) * 30;

		protectedScore += Long
				.bitCount((currentGameState.getBlackBishops() | currentGameState
						.getBlackKnights()) & protectedPieces) * 30;

		protectedScore += Long.bitCount(currentGameState.getBlackRooks()
				& protectedPieces) * 50;

		protectedScore += Long.bitCount(currentGameState.getBlackQueens()
				& protectedPieces) * 65;

		long hangingPieces = (currentGameState.getBlackPieces() & ~protectedPieces)
				& currentGameState.getWhiteAttackingSquares();

		hangingScore += Long.bitCount(currentGameState.getBlackPawns()
				& hangingPieces)
				* hangingPawns;
		hangingScore += Long.bitCount(currentGameState.getBlackRooks()
				& hangingPieces)
				* hangingRooks;
		hangingScore += Long.bitCount(currentGameState.getBlackKnights()
				& hangingPieces)
				* hangingKnights;
		hangingScore += Long.bitCount(currentGameState.getBlackBishops()
				& hangingPieces)
				* hangingBishops;
		hangingScore += Long.bitCount(currentGameState.getBlackQueens()
				& hangingPieces)
				* hangingQueens;

		return hangingScore + protectedScore;
	}

	private static int evaluateWhiteProtectedHangingPieces(
			FullGameState currentGameState) {

		int protectedScore = 0;
		int hangingScore = 0;

		long protectedPieces = currentGameState.getWhiteAttackingSquares()
				& currentGameState.getWhitePieces();

		protectedScore += Long.bitCount(currentGameState.getWhitePawns()
				& protectedPieces) * 30;

		protectedScore += Long
				.bitCount((currentGameState.getWhiteBishops() | currentGameState
						.getWhiteKnights()) & protectedPieces) * 30;

		protectedScore += Long.bitCount(currentGameState.getWhiteRooks()
				& protectedPieces) * 50;

		protectedScore += Long.bitCount(currentGameState.getWhiteQueens()
				& protectedPieces) * 65;

		// System.out.println("White protected pieces " + score);

		long hangingPieces = (currentGameState.getWhitePieces() & ~protectedPieces)
				& currentGameState.getBlackAttackingSquares();

		hangingScore += Long.bitCount(currentGameState.getWhitePawns()
				& hangingPieces)
				* hangingPawns;
		hangingScore += Long.bitCount(currentGameState.getWhiteRooks()
				& hangingPieces)
				* hangingRooks;
		hangingScore += Long.bitCount(currentGameState.getWhiteKnights()
				& hangingPieces)
				* hangingKnights;
		hangingScore += Long.bitCount(currentGameState.getWhiteBishops()
				& hangingPieces)
				* hangingBishops;
		hangingScore += Long.bitCount(currentGameState.getWhiteQueens()
				& hangingPieces)
				* hangingQueens;

		// System.out.println("White hanging pieces " + score1);
		return hangingScore + protectedScore;
	}

	private static int evaluateWhitePawnStructure(FullGameState currentGameState) {
		int score = 0;
		// System.out.print("White ");
		score += evaluateDoublePawns(currentGameState.getWhitePawns());
		// System.out.print("White ");
		score += evaluateIsolatedPawns(currentGameState.getWhitePawns());
		return score;
	}

	private static int evaluateBlackPawnStructure(FullGameState currentGameState) {
		int score = 0;
		// System.out.print("Black ");
		score += evaluateDoublePawns(currentGameState.getBlackPawns());
		// System.out.print("Black ");
		score += evaluateIsolatedPawns(currentGameState.getBlackPawns());
		return score;
	}

	// maybe use an array to keep track of the masked files
	// first check if there are pawns in a file then check if adjacent files are
	// empty
	private static int evaluateIsolatedPawns(long pawns) {
		int score = 0;

		// evaluating the first file
		int pawnsInFile = Long.bitCount(pawns & BitboardOperations.maskFile(1));
		if (pawnsInFile != 0 && (pawns & BitboardOperations.maskFile(2)) == 0) {
			score -= 20 * pawnsInFile;
		}

		for (int file = 2; file < 8; file++) {
			if ((BitboardOperations.maskFile(file) & pawns) == 0) {
				continue;
			} else {
				if ((BitboardOperations.maskFile(file - 1) & pawns) == 0
						&& (BitboardOperations.maskFile(file + 1) & pawns) == 0) {
					score -= 20 * Long.bitCount(BitboardOperations
							.maskFile(file) & pawns);
				}
			}
		}

		// evaluating the eighth file
		pawnsInFile = Long.bitCount(pawns & BitboardOperations.maskFile(8));
		if (pawnsInFile != 0 && (pawns & BitboardOperations.maskFile(7)) == 0) {
			score -= 20 * pawnsInFile;
		}
		return score;
	}

	private static int evaluateDoublePawns(long pawns) {
		int score = 0;
		long maskedFile = BitboardOperations.maskFile(1);
		for (int file = 0; file < 8; file++) {
			int pawnsInFile = 0;
			pawnsInFile = Long.bitCount(pawns & (maskedFile << file));
			if (pawnsInFile > 1) {
				score -= (pawnsInFile * 20) * (pawnsInFile - 1);
			}
		}
		return score;
	}

	// return positive value
	private static int evaluateWhiteMaterial(FullGameState myGameState) {

		int score = 0;

		score += Long.bitCount(myGameState.getWhitePawns()) * pawnMaterial;
		score += Long.bitCount(myGameState.getWhiteRooks()) * rookMaterial;
		score += Long.bitCount(myGameState.getWhiteBishops()) * bishopMaterial;
		score += Long.bitCount(myGameState.getWhiteKnights()) * knightMaterial;
		score += Long.bitCount(myGameState.getWhiteQueens()) * queenMaterial;
		return score;
	}

	// returns negative value
	private static int evaluateBlackMaterial(FullGameState myGameState) {

		int score = 0;

		score += Long.bitCount(myGameState.getBlackPawns()) * pawnMaterial;
		score += Long.bitCount(myGameState.getBlackRooks()) * rookMaterial;
		score += Long.bitCount(myGameState.getBlackBishops()) * bishopMaterial;
		score += Long.bitCount(myGameState.getBlackKnights()) * knightMaterial;
		score += Long.bitCount(myGameState.getBlackQueens()) * queenMaterial;
		return score;
	}

}