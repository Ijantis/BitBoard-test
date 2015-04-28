package other;

import gui.MainFrame;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import board.ChessBoard;

public class PerftTesting implements Runnable {

	// public static void main(String[] args) {
	// long time = System.currentTimeMillis();
	// new PerftTesting();
	//
	// // new ChessBoard();
	//
	// System.out.println("That took :" + (System.currentTimeMillis() - time)
	// + "ms");
	//
	// }

	public PerftTesting() {

		long count = 1;
		long numberOfMismatches = 0;
		long readDepth = 0;
		Scanner myScanner;
		ChessBoard myboard = new ChessBoard();
		try {
			myScanner = new Scanner(Paths.get("src/other/perftsuite.txt"));
			while (myScanner.hasNextLine()) {

				myScanner.useDelimiter(" ;");
				myboard.newGameFromFEN(myScanner.next());
				readDepth = Long.parseLong(myScanner.next().substring(3));
				readDepth = Long.parseLong(myScanner.next().substring(3));
				readDepth = Long.parseLong(myScanner.next().substring(3));
				readDepth = Long.parseLong(myScanner.next().substring(3));
				// readDepth = Long.parseLong(myScanner.next().substring(3));
				long calcDepth = myboard.generateDepthMoves(4);
				if (readDepth != calcDepth) {
					System.out.println();
					System.out.println("mismatch");
					System.out.println("calc depth " + calcDepth);
					System.out.println("read depth " + readDepth);
					System.out.println(count);
					System.out.println("==============");
					myboard.printBoard();
					System.out.println();
					System.out.println();
					numberOfMismatches++;
				} else {
					System.out.println("calc depth " + calcDepth);
					System.out.println("read depth " + readDepth);
				}
				System.out.println(count);
				myScanner.nextLine();
				count++;
			}
			System.out.println("Missed : " + numberOfMismatches);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public PerftTesting(boolean ololol) {

	}

	private static void drawBoard(char[][] currentBoard) {
		MainFrame.appendText("======================");

		char board = '8';

		for (int y = 7; y >= 0; y--) {
			String output = board + " | ";
			for (int x = 0; x < currentBoard.length; x++) {
				char temp = currentBoard[x][y];
				if (temp == ' ') {
					output += ", ";
				} else {
					output += currentBoard[x][y] + " ";
				}
			}
			MainFrame.appendText(output);
			board--;
		}
		MainFrame.appendText("  \\----------------");
		MainFrame.appendText("    A B C D E F G H");
		MainFrame.appendText("====================== \n");
	}

	@Override
	public void run() {

		long time = System.currentTimeMillis();
		long count = 1;
		long numberOfMismatches = 0;
		long readDepth = 0;
		long calcDepth = 0;
		long numberOfMoves = 0;

		Scanner myScanner;
		ChessBoard myBoard = new ChessBoard();

		try {
			myScanner = new Scanner(Paths.get("src/other/perftsuite.txt"));
			while (myScanner.hasNextLine()) {
				myScanner.useDelimiter(" ;");
				myBoard.newGameFromFEN(myScanner.next());
				drawBoard(myBoard.getBoard());

				for (int i = 1; i < 6; i++) {

					readDepth = Long.parseLong(myScanner.next().substring(3));
					MainFrame.appendText("Reading in " + readDepth
							+ " moves at depth " + i);
					calcDepth = myBoard.generateDepthMoves(i);
					MainFrame.appendText("Calculated " + calcDepth
							+ " moves at depth " + i);
					numberOfMoves += calcDepth;

					if (calcDepth == readDepth) {
						MainFrame.appendText("OK");
					} else {
						MainFrame.appendText("ERROR");
					}
				}
				count++;
				myScanner.nextLine();
			}

			long newTime = (System.currentTimeMillis() - time);

			MainFrame.appendText("That took :" + newTime + "ms\n");
			MainFrame.appendText("Generated " + numberOfMoves + " moves\n");
			MainFrame.appendText("Mismatches : " + numberOfMismatches + "\n");

			double numberOfMovesd = numberOfMoves;
			double timed = newTime;

			System.out.println(newTime);
			System.out.println(numberOfMoves);
			MainFrame.appendText("Speed "
					+ Math.round((numberOfMovesd / timed) * 1000)
					+ " moves per second");

		} catch (Exception e) {
			// TODO: handle exception
		}
		MainFrame.enableCmd();
	}

}
