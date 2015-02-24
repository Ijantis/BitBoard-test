package other;

public class HashGenerator {

	public static String generatePositionHash(char[][] currentBoard) {

		String positionHash = "";

		for (int x = 0; x < currentBoard.length; x++) {
			for (int y = 0; y < currentBoard.length; y++) {
				positionHash += currentBoard[x][y];
			}
		}

		return positionHash;

	}

}
