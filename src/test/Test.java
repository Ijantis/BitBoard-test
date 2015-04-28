package test;

import java.util.Random;

import bitboards.BitboardOperations;
import bitboards.WhitePieces;
import board.ChessBoard;

public class Test {

	public static void main(String[] args) {
		// new ChessBoard();

		new Test();
	}

	public Test() {

		long whitePawns = Long.parseLong("100100000000", 2);
		BitboardOperations.printBitboard(whitePawns);
		BitboardOperations.printBitboard(WhitePieces.getPawnMovesVertical(whitePawns, Long.parseLong("10000000000000000", 2)));

	}

}
