package ai.evaluation;

import java.awt.BufferCapabilities.FlipContents;
import java.util.Vector;

import board.FullGameState;
import operations.BitboardOperations;
import operations.MoveGenerator;

public class Evaluator {

	private static final long centralSquares = Long.parseLong(
			"0000000000000000000000000001100000011000000000000000000000000000",
			2);
	private static int pawnMaterial = 100;
	private static int rookMaterial = 500;
	private static int knightMaterial = 300;
	private static int bishopMaterial = 300;
	private static int queenMaterial = 900;
	private static int attackingCentralSquare = 20;
	private static int occupyingCentralSquare = 40;
	private static int mobility = 20;

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
			{ -20, -10, -10, -10, -10, -10, -10, -20 }, };;

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
		whiteScore += MoveGenerator.generateWhiteLegalMoves(currentGameState)
				.size() * mobility;
		blackScore += MoveGenerator.generateBlackLegalMoves(currentGameState)
				.size() * mobility;

		whiteScore += evaluateWhiteCentralControl(currentGameState);
		blackScore += evaluateBlackCentralControl(currentGameState);

		// this evaluates negtively for black
		whiteScore += evaluatePositional(currentGameState.getCurrentBoard(),
				Long.bitCount(currentGameState.getAllPieces()));

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

	// whiteScore += evaluateWhiteCentralControl(whiteAttackingSquares);
	// blackScore += evaluateBlackCentralControl(blackAttackingSquares);

	private static long evaluateWhiteCentralControl(
			FullGameState currentGameState) {
		long score = 0;

		score += Long.bitCount(currentGameState.getWhiteAttackingSquares()
				& centralSquares)
				* attackingCentralSquare;

		score += Long.bitCount(currentGameState.getWhitePieces()
				& centralSquares)
				* occupyingCentralSquare;

		// System.out.println("White central control " + score);

		return score;
	}

	private static long evaluateBlackCentralControl(
			FullGameState currentGameState) {
		long score = 0;

		score += Long.bitCount(currentGameState.getBlackAttackingSquares()
				& centralSquares)
				* attackingCentralSquare;

		score += Long.bitCount(currentGameState.getBlackPieces()
				& centralSquares)
				* occupyingCentralSquare;

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
		// System.out.print("White ");
		score += evaluatelongdPawns(currentGameState.getWhitePawns());
		// System.out.print("White ");
		score += evaluateIsolatedPawns(currentGameState.getWhitePawns());
		return score;
	}

	private static long evaluateBlackPawnStructure(
			FullGameState currentGameState) {
		long score = 0;
		// System.out.print("Black ");
		score += evaluatelongdPawns(currentGameState.getBlackPawns());
		// System.out.print("Black ");
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

		score += Long.bitCount(myGameState.getWhitePawns()) * pawnMaterial;
		score += Long.bitCount(myGameState.getWhiteRooks()) * rookMaterial;
		score += Long.bitCount(myGameState.getWhiteBishops()) * bishopMaterial;
		score += Long.bitCount(myGameState.getWhiteKnights()) * knightMaterial;
		score += Long.bitCount(myGameState.getWhiteQueens()) * queenMaterial;
		return score;
	}

	// returns negative value
	private static long evaluateBlackMaterial(FullGameState myGameState) {

		long score = 0;

		score += Long.bitCount(myGameState.getBlackPawns()) * pawnMaterial;
		score += Long.bitCount(myGameState.getBlackRooks()) * rookMaterial;
		score += Long.bitCount(myGameState.getBlackBishops()) * bishopMaterial;
		score += Long.bitCount(myGameState.getBlackKnights()) * knightMaterial;
		score += Long.bitCount(myGameState.getBlackQueens()) * queenMaterial;
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