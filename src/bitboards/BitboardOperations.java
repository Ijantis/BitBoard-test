package bitboards;

import board.FullGameState;

public class BitboardOperations {

	/*
	 * Returns a bitboard for a coordinate
	 */
	public static long getPositionBitboard(long position) {

		return 1L << position;

	}

	public static long getBottomRightSquares(long bitboard) {
		long currentFile = 0L;
		long bottomRightSquares = 0L;

		for (int fileToMask = 8; fileToMask > 0; fileToMask--) {
			currentFile = bitboard & maskFile(fileToMask);
			// optimises it
			if (currentFile == 0) {
				continue;
			}
			for (int shift = fileToMask; shift < 8; shift++) {
				currentFile = currentFile | currentFile >>> 7;
			}
			bottomRightSquares = (currentFile | bottomRightSquares);
		}
		return bottomRightSquares & ~bitboard;
	}

	public static long getTopRightSquares(long bitboard) {
		long currentFile = 0L;
		long topRightSquares = 0L;

		for (int fileToMask = 8; fileToMask > 0; fileToMask--) {
			currentFile = bitboard & maskFile(fileToMask);
			// optimises it
			if (currentFile == 0) {
				continue;
			}
			for (int shift = fileToMask; shift < 8; shift++) {
				currentFile = currentFile | currentFile << 9;
			}
			topRightSquares = (currentFile | topRightSquares);
		}
		return topRightSquares & ~bitboard;

	}

	public static long getBottomLeftSquares(long bitboard) {
		long currentFile = 0L;
		long bottomLeftSquares = 0L;

		for (int fileToMask = 1; fileToMask < 9; fileToMask++) {
			currentFile = bitboard & maskFile(fileToMask);
			// optimises it
			if (currentFile == 0) {
				continue;
			}
			for (int shift = 7; shift < (fileToMask) * 7; shift += 7) {
				currentFile = currentFile | currentFile >>> 9;
			}
			bottomLeftSquares = (currentFile | bottomLeftSquares);
		}
		return bottomLeftSquares & ~bitboard;
	}

	public static long getTopLeftSquares(long bitboard) {
		long currentFile = 0L;
		long topLeftSquares = 0L;

		for (int fileToMask = 1; fileToMask < 9; fileToMask++) {
			currentFile = bitboard & maskFile(fileToMask);
			// optimises it
			if (currentFile == 0) {
				continue;
			}
			for (int shift = 7; shift < (fileToMask) * 7; shift += 7) {
				currentFile = currentFile | currentFile << 7;
			}
			topLeftSquares = (currentFile | topLeftSquares);
		}
		return topLeftSquares & ~bitboard;
	}

	public static long clearRank(int rankToClear) {
		return ~maskRank(rankToClear);
	}

	public static long clearFile(int fileToClear) {
		return ~maskFile(fileToClear);
	}

	public static long maskRank(int rankToMask) {
		long temp = 255L;
		return temp << (8 * (rankToMask - 1));
	}

	/**
	 * 
	 * @param fileToMask
	 *            input between 1 and 8
	 * @return
	 */
	public static long maskFile(int fileToMask) {

		// String temp =
		// "0000000100000001000000010000000100000001000000010000000100000001";
		// equivalent
		long temp = 72340172838076673L;
		return temp << (fileToMask - 1);
	}

	/*
	 * Returns all squares to the right of a possible bitboard.
	 */
	public static long getRightSquares(long bitboard) {

		long rightSquares = 0L;
		long currentRank = 0L;
		long temp = 0L;

		for (int i = 1; i < 9; i++) {
			currentRank = bitboard & maskRank(i);
			// optimising
			if (currentRank == 0) {
				continue;
			}
			temp = temp | currentRank << 1 | currentRank << 2
					| currentRank << 3 | currentRank << 4 | currentRank << 5
					| currentRank << 6 | currentRank << 7;

			temp = temp & maskRank(i);
			rightSquares = rightSquares | temp;
			temp = 0L;
		}

		return rightSquares;
	}

	/*
	 * Returns all squares to the left of a possible bitboard
	 */
	public static long getLeftSquares(long bitboard) {

		long leftSquares = 0L;
		long currentRank = 0L;
		long temp = 0L;

		for (int i = 1; i < 9; i++) {
			currentRank = bitboard & maskRank(i);
			if (currentRank == 0) {
				continue;
			}
			temp = temp | currentRank >>> 1 | currentRank >>> 2
					| currentRank >>> 3 | currentRank >>> 4 | currentRank >>> 5
					| currentRank >>> 6 | currentRank >>> 7;
			temp = temp & maskRank(i);
			leftSquares = leftSquares | temp;
			temp = 0L;
		}
		return leftSquares;
	}

	/*
	 * Returns all squares above the possible bitboard
	 */
	public static long getUpSquares(long bitboard) {
		long upSquares = 0L;
		long currentFile = 0L;
		long temp = 0L;

		for (int i = 1; i < 9; i++) {
			currentFile = bitboard & maskFile(i);
			if (currentFile == 0) {
				continue;
			}
			for (int j = 8; j < 57; j += 8) {
				temp = temp | currentFile << j;
			}
			upSquares = upSquares | temp;
		}
		return upSquares;
	}

	/*
	 * Returns all squares below the possible bitboard
	 */
	public static long getDownSquares(long bitboard) {
		long downSquares = 0L;
		long currentFile = 0L;
		long temp = 0L;

		for (int i = 1; i < 9; i++) {
			currentFile = bitboard & maskFile(i);
			if (currentFile == 0) {
				continue;
			}
			for (int j = 8; j < 57; j += 8) {
				temp = temp | currentFile >>> j;
			}
			downSquares = downSquares | temp;
		}
		return downSquares;
	}

	public static void printBitboard(long bitBoard) {
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

	public static void printBoard(char[][] currentBoard) {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < currentBoard.length; x++) {
				char temp = currentBoard[x][y];
				if (temp == ' ') {
					System.out.print(", ");
				} else {
					System.out.print(currentBoard[x][y] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public static char[][] createArrayFromGamestate(FullGameState state,
			boolean print) {

		char[][] tempBoard = new char[8][8];
		for (int i = 0; i < 64; i++) {
			tempBoard[i % 8][i / 8] = ' ';
		}

		long temp = state.getWhiteKing();

		int nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
		tempBoard[nextLength % 8][nextLength / 8] = 'K';

		temp = state.getWhitePawns();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'P';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getWhiteRooks();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'R';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getWhiteKnights();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'N';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getWhiteBishops();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'B';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getWhiteQueens();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'Q';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackKing();
		nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;

		tempBoard[nextLength % 8][nextLength / 8] = 'k';

		temp = state.getBlackPawns();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'p';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackRooks();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'r';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackKnights();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'n';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackBishops();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'b';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackQueens();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'q';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		if (print) {
			printBoard(tempBoard);
		}
		return tempBoard;

	}
}
