package other;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import board.ChessBoard;

public class PerftTesting {

	public static void main(String[] args) {

		new PerftTesting();

		// new ChessBoard();

	}

	public PerftTesting() {

		int count = 1;
		int numberOfMismatches = 0;
		int readDepth = 0;
		Scanner myScanner;
		ChessBoard myboard = new ChessBoard();
		try {
			myScanner = new Scanner(Paths.get("src/other/perftsuite.txt"));
			while (myScanner.hasNextLine()) {

				myScanner.useDelimiter(" ;");
				myboard.newGameFromFEN(myScanner.next());
				myScanner.next();
				readDepth = Integer.parseInt(myScanner.next().substring(3));
				int calcDepth = myboard.generateDepthTwoMoves();
				if (readDepth != calcDepth) {
					System.out.println("mismatch");
					System.out.println("calc depth " + calcDepth);
					System.out.println("read depth " + readDepth);
					System.out.println(count);
					System.out.println("==============");
					myboard.printBoard();
					System.out.println();
					System.out.println();
					numberOfMismatches++;
				}

				myScanner.nextLine();
				count++;
			}
			System.out.println("Missed : " + numberOfMismatches);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
