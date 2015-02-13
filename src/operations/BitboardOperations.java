package operations;

public class BitboardOperations {

	/*
	 * Returns a bitboard for a coordinate
	 */
	public static long getPositionBitboard(long position) {

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
		String temp = "0000000000000000000000000000000000000000000000000000000011111111";
		long maskedRank = Long.parseLong(temp, 2);
		maskedRank = maskedRank << (8 * (rankToMask - 1));
		return maskedRank;
	}

	public static long maskFile(int fileToMask) {

		String temp = "0000000100000001000000010000000100000001000000010000000100000001";
		long maskedFile = Long.parseLong(temp, 2);
		maskedFile = maskedFile << (fileToMask - 1);
		return maskedFile;
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
}