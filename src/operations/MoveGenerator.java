package operations;

import java.util.ArrayList;

import main.GameLoop;
import operations.pieces.BlackPieces;
import operations.pieces.WhitePieces;
import board.BoardManager;
import board.FullGameState;

public class MoveGenerator {

	/*
	 * NOTE: Do not return a ArrayList<FullGameState> return squares numbers
	 * instead.
	 */
	public static ArrayList<FullGameState> generateWhiteLegalMoves(
			FullGameState myGamestate) {

		ArrayList<FullGameState> possibleStates = new ArrayList<FullGameState>();
		long temp;

		temp = myGamestate.getWhitePawns();
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getPawnMoves(nextPawn,
					myGamestate.getOccupiedSquares(),
					myGamestate.getBlackPieces(),
					myGamestate.getEnPassantSquare());
			possibleStates.addAll(WhitePieceMoves.whitePawnMoves(nextPawn,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getWhiteKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getKnightMoves(nextKnight,
					myGamestate.getWhitePieces());
			possibleStates.addAll(WhitePieceMoves.whiteKnightMoves(nextKnight,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getWhiteBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getBishopMoves(nextBishop,
					myGamestate.getOccupiedSquares(),
					myGamestate.getWhitePieces());
			possibleStates.addAll(WhitePieceMoves.whiteBishopMoves(nextBishop,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getWhiteQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getQueenMoves(nextQueen,
					myGamestate.getOccupiedSquares(),
					myGamestate.getWhitePieces());
			possibleStates.addAll(WhitePieceMoves.whiteQueenMoves(nextQueen,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getWhiteRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = WhitePieces.getRookMoves(nextRook,
					myGamestate.getOccupiedSquares(),
					myGamestate.getWhitePieces());
			possibleStates.addAll(WhitePieceMoves.whiteRookMoves(nextRook,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = WhitePieces
				.getKingMoves(myGamestate.getWhiteKing(),
						myGamestate.getWhitePieces(),
						myGamestate.getBlackAttackingSquares(),
						myGamestate.getWhiteCastleKing(),
						myGamestate.getWhiteCastleQueen(),
						myGamestate.getBlackPieces());
		possibleStates.addAll(WhitePieceMoves.whiteKingMoves(
				myGamestate.getWhiteKing(), kingMovesBitboard, myGamestate));

		return possibleStates;
	}

	public static ArrayList<FullGameState> generateBlackLegalMoves(
			FullGameState myGamestate) {

		ArrayList<FullGameState> possibleStates = new ArrayList<FullGameState>();
		long temp;

		temp = myGamestate.getBlackPawns();
		while (temp != 0) {

			long nextPawn = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getPawnMoves(nextPawn,
					myGamestate.getOccupiedSquares(),
					myGamestate.getWhitePieces(),
					myGamestate.getEnPassantSquare());
			possibleStates.addAll(BlackPieceMoves.BlackPawnMoves(nextPawn,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getBlackKnights();
		while (temp != 0) {

			long nextKnight = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getKnightMoves(nextKnight,
					myGamestate.getBlackPieces());
			possibleStates.addAll(BlackPieceMoves.BlackKnightMoves(nextKnight,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getBlackBishops();
		while (temp != 0) {
			long nextBishop = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getBishopMoves(nextBishop,
					myGamestate.getOccupiedSquares(),
					myGamestate.getBlackPieces());
			possibleStates.addAll(BlackPieceMoves.BlackBishopMoves(nextBishop,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getBlackQueens();
		while (temp != 0) {
			long nextQueen = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getQueenMoves(nextQueen,
					myGamestate.getOccupiedSquares(),
					myGamestate.getBlackPieces());
			possibleStates.addAll(BlackPieceMoves.BlackQueenMoves(nextQueen,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = myGamestate.getBlackRooks();
		while (temp != 0) {
			long nextRook = Long.highestOneBit(temp);
			long bitboardOfMoves = BlackPieces.getRookMoves(nextRook,
					myGamestate.getOccupiedSquares(),
					myGamestate.getBlackPieces());
			possibleStates.addAll(BlackPieceMoves.BlackRookMoves(nextRook,
					bitboardOfMoves, myGamestate));

			temp = Long.highestOneBit(temp) ^ temp;
		}

		long kingMovesBitboard = BlackPieces
				.getKingMoves(myGamestate.getBlackKing(),
						myGamestate.getBlackPieces(),
						myGamestate.getWhiteAttackingSquares(),
						myGamestate.getBlackCastleKing(),
						myGamestate.getBlackCastleQueen(),
						myGamestate.getWhitePieces());
		possibleStates.addAll(BlackPieceMoves.BlackKingMoves(
				myGamestate.getBlackKing(), kingMovesBitboard, myGamestate));

		return possibleStates;
	}

	private static char[][] copyCurrentBoard(char[][] currentBoard) {
		char[][] temp = new char[currentBoard.length][currentBoard.length];

		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < temp.length; y++) {
				temp[x][y] = currentBoard[x][y];
			}
		}
		return temp;
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
