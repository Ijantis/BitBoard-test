package other;

import java.util.Scanner;

/*
 * 
 */
public class FENLoader {

	public static char[][] createPieceArrayFromFEN(String FENPieces) {

		char[][] currentBoard = new char[8][8];
		Scanner pieceScanner = new Scanner(FENPieces);
		pieceScanner.useDelimiter("/");

		int yCoord = 7;
		while (pieceScanner.hasNext()) {
			String nextLine = pieceScanner.next();
			// System.out.println();
			// System.out.println(nextLine);
			int xCoord = 0;
			while (nextLine.length() > 0) {
				// System.out.println("Found a " + nextLine.charAt(0));
				switch (nextLine.charAt(0)) {
				case 'r':
					currentBoard[xCoord][yCoord] = 'r';
					// System.out.println("r in " + xCoord + " " + yCoord);
					break;
				case 'n':
					currentBoard[xCoord][yCoord] = 'n';
					// System.out.println("n in " + xCoord + " " + yCoord);
					break;
				case 'b':
					currentBoard[xCoord][yCoord] = 'b';
					// System.out.println("b in " + xCoord + " " + yCoord);
					break;
				case 'q':
					currentBoard[xCoord][yCoord] = 'q';
					// System.out.println("q in " + xCoord + " " + yCoord);
					break;
				case 'k':
					currentBoard[xCoord][yCoord] = 'k';
					// System.out.println("k in " + xCoord + " " + yCoord);
					break;
				case 'p':
					currentBoard[xCoord][yCoord] = 'p';
					// System.out.println("p in " + xCoord + " " + yCoord);
					break;
				case 'R':
					currentBoard[xCoord][yCoord] = 'R';
					// System.out.println("R in " + xCoord + " " + yCoord);
					break;
				case 'N':
					currentBoard[xCoord][yCoord] = 'N';
					// System.out.println("N in " + xCoord + " " + yCoord);
					break;
				case 'B':
					currentBoard[xCoord][yCoord] = 'B';
					// System.out.println("B in " + xCoord + " " + yCoord);
					break;
				case 'Q':
					currentBoard[xCoord][yCoord] = 'Q';
					// System.out.println("Q in " + xCoord + " " + yCoord);
					break;
				case 'K':
					currentBoard[xCoord][yCoord] = 'K';
					// System.out.println("K in " + xCoord + " " + yCoord);
					break;
				case 'P':
					currentBoard[xCoord][yCoord] = 'P';
					// System.out.println("P in " + xCoord + " " + yCoord);
					break;
				// do a loop for the number
				default:
					int numberOfLoops = Integer.parseInt(nextLine.charAt(0)
							+ "");
					while (numberOfLoops > 0) {
						currentBoard[xCoord][yCoord] = ' ';
						// System.out.println("empty in " + xCoord + " " +
						// yCoord);
						numberOfLoops--;
						if (numberOfLoops == 0) {
							break;
						}
						xCoord++;
					}
					break;
				}
				xCoord++;
				nextLine = nextLine.substring(1);

			}
			yCoord--;
		}

		// System.out.println(FENPieces);

		return currentBoard;
	}

	public static boolean getActiveColour(String next) {
		return next.equals("w");
	}

	/*
	 * whiteK whiteQ blackK blackQ
	 */
	public static boolean[] getCastlingPermissions(String next) {

		boolean[] permissions = new boolean[] { false, false, false, false };
		if (next.equals("-")) {
			return permissions;
		}

		while (next.length() != 0) {
			switch (next.charAt(0)) {
			case 'K':
				permissions[0] = true;
				// System.out.println("WK");
				break;
			case 'Q':
				permissions[1] = true;
				// System.out.println("WQ");
				break;
			case 'k':
				permissions[2] = true;
				// System.out.println("bk");
				break;
			case 'q':
				permissions[3] = true;
				// System.out.println("bq");
				break;
			}
			next = next.substring(1);
		}

		return permissions;
	}

	public static int getHalfMoves(String string) {
		return Integer.parseInt(string);
	}

	public static int getFullMoves(String string) {
		return Integer.parseInt(string);
	}
}
