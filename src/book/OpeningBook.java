package book;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class OpeningBook {

	public static String makeBookMove(String zobristKey) {

		ArrayList<String> listOfMoves = getListOfMoves(zobristKey);

		if (listOfMoves.size() == 0) {
			return "N";
		} else {

			Random myRandom = new Random();
			int test = myRandom.nextInt(listOfMoves.size());

			return decipherMove(listOfMoves.get(test).substring(0, 8));
		}
	}

	private static ArrayList<String> getListOfMoves(String zobristKey) {

		ArrayList<String> listOfMoves = new ArrayList<String>();

		Scanner myScanner;
		try {
			myScanner = new Scanner(new File("src/book/myBook.txt"));
			String next;
			while (myScanner.hasNext()) {
				next = myScanner.next();

				if (next.toString().contains(zobristKey)) {
					Scanner myLineScanner = new Scanner(next);
					myLineScanner.useDelimiter("-");
					myLineScanner.next();
					listOfMoves.add(myLineScanner.next());
					myLineScanner.close();
				}
			}
			myScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return listOfMoves;
	}

	private static String decipherMove(String move) {

		String moveBinaryString = Integer.toBinaryString(Integer.parseInt(
				move.substring(0, 4), 16));
		String weightBinaryString = Integer.toBinaryString(Integer.parseInt(
				move.substring(4, 8), 16));

		while (moveBinaryString.length() != 16) {
			moveBinaryString = "0" + moveBinaryString;
		}

		while (weightBinaryString.length() != 16) {
			weightBinaryString = "0" + weightBinaryString;
		}

		int toX, toY, fromX, fromY, weight;
		toX = Integer.parseInt(moveBinaryString.substring(
				moveBinaryString.length() - 3, moveBinaryString.length()), 2);
		toY = Integer.parseInt(moveBinaryString.substring(
				moveBinaryString.length() - 6, moveBinaryString.length() - 3),
				2);
		fromX = Integer.parseInt(moveBinaryString.substring(
				moveBinaryString.length() - 9, moveBinaryString.length() - 6),
				2);
		fromY = Integer.parseInt(moveBinaryString.substring(
				moveBinaryString.length() - 12, moveBinaryString.length() - 9),
				2);
		weight = Integer.parseInt(weightBinaryString, 2);

		String returnString = ((fromY * 8) + fromX) + "-" + ((toY * 8) + toX);
		return returnString;
	}
}
