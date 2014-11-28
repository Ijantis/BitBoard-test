package board;

public class BitboardOperations {

	/*
	 * Returns a bitboard for a coordinate
	 */
	protected static long getPositionBitboard(long position) {

		String temp = "1";

		for (int i = 0; i < position; i++) {
			temp += "0";
		}

		if (temp.length() == 64) {
			temp = temp.substring(0, temp.length() - 1);
			return Long.parseLong(temp, 2) * 2;
		} else {
			return Long.parseLong(temp, 2);
		}

	}

	protected static long getBottomRightSquares(long bitboard) {
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

	protected static long getTopRightSquares(long bitboard) {
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

	protected static long getBottomLeftSquares(long bitboard) {
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

	protected static long getTopLeftSquares(long bitboard) {
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

	protected static long clearRank(int rankToClear) {
		return ~maskRank(rankToClear);
	}

	protected static long clearFile(int fileToClear) {
		return ~maskFile(fileToClear);
	}

	protected static long maskRank(int rankToMask) {
		String temp = "0000000000000000000000000000000000000000000000000000000011111111";
		long maskedRank = Long.parseLong(temp, 2);
		maskedRank = maskedRank << (8 * (rankToMask - 1));
		return maskedRank;
	}

	protected static long maskFile(int fileToMask) {

		String temp = "0000000100000001000000010000000100000001000000010000000100000001";
		long maskedFile = Long.parseLong(temp, 2);
		maskedFile = maskedFile << (fileToMask - 1);
		return maskedFile;
	}

	/*
	 * Returns all squares to the right of a possible bitboard.
	 */
	protected static long getRightSquares(long bitboard) {

		long rightSquares = 0L;
		long currentRank = 0L;
		long temp = 0L;

		for (int i = 1; i < 9; i++) {
			currentRank = bitboard & maskRank(i);
			for (int j = 1; j < 8; j++) {
				temp = temp | currentRank << j;
			}
			temp = temp & maskRank(i);
			rightSquares = rightSquares | temp;
			temp = 0L;
		}

		return rightSquares;
	}

	/*
	 * Returns all squares to the left of a possible bitboard
	 */
	protected static long getLeftSquares(long bitboard) {

		long leftSquares = 0L;
		long currentRank = 0L;
		long temp = 0L;

		for (int i = 1; i < 9; i++) {
			currentRank = bitboard & maskRank(i);
			for (int j = 1; j < 8; j++) {
				temp = temp | currentRank >>> j;
			}
			temp = temp & maskRank(i);
			leftSquares = leftSquares | temp;
			temp = 0L;
		}
		return leftSquares;
	}

	/*
	 * Returns all squares above the possible bitboard
	 */
	protected static long getUpSquares(long bitboard) {
		long upSquares = 0L;
		long currentFile = 0L;
		long temp = 0L;

		for (int i = 1; i < 9; i++) {
			currentFile = bitboard & maskFile(i);
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
	protected static long getDownSquares(long bitboard) {
		long downSquares = 0L;
		long currentFile = 0L;
		long temp = 0L;

		for (int i = 1; i < 9; i++) {
			currentFile = bitboard & maskFile(i);
			for (int j = 8; j < 57; j += 8) {
				temp = temp | currentFile >>> j;
			}
			downSquares = downSquares | temp;
		}
		return downSquares;
	}

}
