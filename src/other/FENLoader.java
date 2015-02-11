package board;

import java.util.Scanner;


/*
 * 
 */
public class FENLoader {

	
	protected static char[][] createPieceArrayFromFEN(String FENPieces) {
		
		char[][] currentBoard = new char[8][8];
		Scanner pieceScanner = new Scanner(FENPieces);
		pieceScanner.useDelimiter("/");
		
		while (pieceScanner.hasNext()) {
			System.out.println(pieceScanner.next());
		}
		
		
		
		System.out.println(FENPieces);
		
		
		return currentBoard;
	}
	
	
}
