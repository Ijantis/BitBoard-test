package test;

public class ChessBoard {

	public static void main(String[] args) {

		new ChessBoard();
	}

	private long whitePawns, whiteRooks, whiteKnights, whiteBishops,
			whiteQueens, whiteKing;
	private long blackPawns, blackRooks, blackKnights, blackBishops,
			blackQueens, blackKing;
	// Upper case for WHITE
	// Lower case for BLACK
	// 0,0 is top left 0,7 is top right 7,7 bottom right
	private String[][] currentBoard = {
			{ "R", "P", " ", " ", " ", " ", "p", "r" },
			{ "N", "P", " ", " ", " ", " ", "p", "n" },
			{ "B", "P", " ", " ", " ", " ", "p", "b" },
			{ "Q", "P", " ", " ", " ", " ", "p", "q" },
			{ "K", "P", " ", " ", " ", " ", "p", "k" },
			{ "B", "P", " ", " ", " ", " ", "p", "b" },
			{ "N", "P", " ", " ", " ", " ", "p", "n" },
			{ "R", " ", "P", " ", " ", " ", "p", "r" } };;

	public ChessBoard() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = " ";
			}
		}
		for (int i = 0; i < 64; i++) {
			clearBoard();
			System.out.println(i / 8 + "," + i % 8);
			currentBoard[i / 8][i % 8] = "K";
			initialiseBoard();
			printBitBoard(getKingMoves());
			printBoard();
		}

	}

	private void clearBoard() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = " ";
			}
		}
	}

	private long getKingMoves() {

		// vertical movement 8 bit shift
		long up = whiteKing << 8;
		long down = whiteKing >>> 8;

		long left = ((whiteKing & clearFile(1)) >>> 1);
		long upLeft = (whiteKing & clearFile(1)) << 7;
		long downLeft = (whiteKing & clearFile(1)) >>> 9;
		
		long right = ((whiteKing & clearFile(8)) << 1);
		long upRight = ((whiteKing & clearFile(8)) << 9);
		long downRight = ((whiteKing & clearFile(8)) >>> 7);
		
		return (up | down | left | right | upRight | downRight | upLeft | downLeft);
	}

	private void initialiseBoard() {
		whitePawns = 0L;
		whiteRooks = 0L;
		whiteKnights = 0L;
		whiteBishops = 0L;
		whiteQueens = 0L;
		whiteKing = 0L;
		blackPawns = 0L;
		blackRooks = 0L;
		blackKnights = 0L;
		blackBishops = 0L;
		blackQueens = 0L;
		blackKing = 0L;
		String binaryLong;
		int currentIndex;
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				// 64 bits and currentIndex is the square being looked at
				binaryLong = "0000000000000000000000000000000000000000000000000000000000000000";
				currentIndex = (y * 8) + x;
				binaryLong = binaryLong.substring(currentIndex,
						binaryLong.length() - 1)
						+ "1" + binaryLong.substring(0, currentIndex);
				switch (currentBoard[x][y]) {
				case "P":
					whitePawns += convertStringToBinary(binaryLong);
					break;
				case "R":
					whiteRooks += convertStringToBinary(binaryLong);
					break;
				case "N":
					whiteKnights += convertStringToBinary(binaryLong);
					break;
				case "B":
					whiteBishops += convertStringToBinary(binaryLong);
					break;
				case "Q":
					whiteQueens += convertStringToBinary(binaryLong);
					break;
				case "K":
					whiteKing += convertStringToBinary(binaryLong);
					break;
				case "p":
					blackPawns += convertStringToBinary(binaryLong);
					break;
				case "r":
					blackRooks += convertStringToBinary(binaryLong);
					break;
				case "n":
					blackKnights += convertStringToBinary(binaryLong);
					break;
				case "b":
					blackBishops += convertStringToBinary(binaryLong);
					break;
				case "q":
					blackQueens += convertStringToBinary(binaryLong);
					break;
				case "k":
					blackKing += convertStringToBinary(binaryLong);
					break;
				default:
					break;
				}
			}
		}
	}

	private long convertStringToBinary(String binaryLong) {
		if (binaryLong.charAt(0) == '0') {
			return Long.parseLong(binaryLong, 2);
		} else {
			return Long.parseLong("1" + binaryLong.substring(2), 2) * 2;
		}
	}

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public void printBoard() {
		System.out.println();
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < currentBoard.length; x++) {
				String temp = currentBoard[x][y];
				if (temp.equals(" ")) {
					System.out.print(",");
				} else {
					System.out.print(currentBoard[x][y]);
				}
			}
			System.out.println();
		}
	}

	private long getWhiteSquares() {
		return (whiteBishops | whiteKing | whiteKnights | whitePawns
				| whiteQueens | whiteRooks);
	}

	private long getBlackSquares() {
		return (blackBishops | blackKing | blackKnights | blackPawns
				| blackQueens | blackRooks);
	}

	private long getOccupiedSquares() {
		return (getWhiteSquares() | getBlackSquares());
	}

	private long clearRank(int rankToClear) {
		return ~maskRank(rankToClear);
	}

	private long clearFile(int fileToClear) {
		return ~maskFile(fileToClear);
	}

	private long maskRank(int rankToMask) {
		String temp = "0000000000000000000000000000000000000000000000000000000011111111";
		long maskedRank = Long.parseLong(temp, 2);
		maskedRank = maskedRank << (8 * (rankToMask - 1));
		return maskedRank;
	}

	private long maskFile(int fileToMask) {

		String temp = "0000000100000001000000010000000100000001000000010000000100000001";
		long maskedFile = Long.parseLong(temp, 2);
		maskedFile = maskedFile << (fileToMask - 1);
		return maskedFile;
	}

	private void printBitBoard(long bitBoard) {
		String stringBitBoard = Long.toBinaryString(bitBoard);
		System.out.println(stringBitBoard);
		while (stringBitBoard.length() != 64) {
			stringBitBoard = "0" + stringBitBoard;
		}

		for (int i = 0; i < 8; i++) {
			StringBuilder stringReverser = new StringBuilder(
					stringBitBoard.substring(i * 8, ((i + 1) * 8)));
			stringReverser.reverse();
			System.out.println(stringReverser.toString());
		}

	}
}
