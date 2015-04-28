package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

import other.PerftTesting;
import search.Engine;
import board.ChessBoard;

public class Init {

	private static ChessBoard myChessBoard;
	private static boolean engineOn = true;

	public static void main(String[] args) {

		myChessBoard = new ChessBoard();
		myChessBoard.newGame();

		new MainFrame();
		drawBoard();
	}

	public static void processInput(String input) {

		switch (input) {
		case "quit":
			System.exit(0);
			break;

		case "help":
			printHelp();
			break;

		case "clear":
			MainFrame.clearOutputArea();
			break;

		case "disp":
			drawBoard();
			break;

		case "newgame":
			myChessBoard.newGame();
			drawBoard();
			break;
		case "engine":
			if (engineOn) {
				MainFrame.appendText("Engine off\n");
			} else {
				MainFrame.appendText("Engine on\n");
			}
			engineOn = !engineOn;
			break;
		case "testmovegen":
			MainFrame.appendText("Running movegen suite \n");
			Thread t1 = new Thread(new PerftTesting(false));
			t1.start();
			MainFrame.disableCmd();
			break;
		case "aimove":
			myChessBoard.makeAIMove(Engine.AI_ITERATIVE);
			drawBoard();
			break;
		default:
			if (Pattern.matches("[a-h][1-8][a-h][1-8]", input)) {
				processMove(input);
				if (engineOn) {
					myChessBoard.makeAIMove(Engine.AI_ITERATIVE);
				}
				drawBoard();
				break;
			} else if (Pattern.matches("perft[1-5]", input)) {
				long time = System.currentTimeMillis();
				MainFrame.appendText(input
						+ " : "
						+ myChessBoard.generateDepthMoves(Integer
								.parseInt(input.charAt(5) + "")));
				MainFrame.appendText("That took "
						+ (System.currentTimeMillis() - time) + "ms");
				break;
			} else if (input.contains("setboard")) {
				System.out.println(input.substring(8));
				myChessBoard.newGameFromFEN(input.substring(8));
				drawBoard();
				break;
			} else if (Pattern.matches("time ([0-9]{1,2})", input)) {
				MainFrame.appendText("Search time set to: "
						+ input.substring(5) + " seconds\n");
			}
			break;
		}
	}

	private static void printHelp() {

		File myFile = new File("src/gui/help.txt");
		try {
			Scanner myScanner = new Scanner(myFile);
			while (myScanner.hasNextLine()) {
				MainFrame.appendText(myScanner.nextLine());
			}
			MainFrame.appendText("");
			myScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void drawBoard() {
		char[][] currentBoard = myChessBoard.getBoard();
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

	private static void processMove(String input) {

		int from = 0;
		int to = 0;

		from += getCharacterValue(input.charAt(0));
		to += getCharacterValue(input.charAt(2));

		from += (Integer.parseInt(input.charAt(1) + "") - 1) * 8;
		to += (Integer.parseInt(input.charAt(3) + "") - 1) * 8;

		myChessBoard.makeMove(from, to);
		drawBoard();
	}

	private static int getCharacterValue(char charAt) {

		switch (charAt) {
		case 'a':
			return 0;
		case 'b':
			return 1;
		case 'c':
			return 2;
		case 'd':
			return 3;
		case 'e':
			return 4;
		case 'f':
			return 5;
		case 'g':
			return 6;
		case 'h':
			return 7;
		}
		return 0;
	}

}
