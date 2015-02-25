package board;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import operations.BitboardOperations;
import operations.MoveGenerator;
import operations.WhiteMoveGeneratorThread;
import operations.pieces.BlackPieces;
import operations.pieces.WhitePieces;
import other.FENLoader;
//import other.HashGenerator;
import ai.Engine;
import ai.evaluation.Evaluator;

public class ChessBoard {

	private long whitePawns, whiteRooks, whiteKnights, whiteBishops,
			whiteQueens, whiteKing;
	private long blackPawns, blackRooks, blackKnights, blackBishops,
			blackQueens, blackKing;
	private boolean whiteToMove = true;
	private boolean whiteCastleKing = true;
	private boolean whiteCastleQueen = true;
	private boolean blackCastleKing = true;
	private boolean blackCastleQueen = true;
	private long enPassantSquare = 0;
	private int numberOfFullMoves = 1;
	private int numberOfHalfMoves = 0;

	// Upper case for WHITE
	// Lower case for BLACK
	// 0,0 is top left 0,7 is top right 7,7 bottom right
	private char[][] currentBoard = {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'k' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', 'q', ' ' },
			{ 'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ' } };;

	Vector<char[][]> threadedList;
	Vector<char[][]> serialList;

	public ChessBoard() {
		long time = System.currentTimeMillis();
		long timeNano = System.nanoTime();

		newGame();
		makeMove(12, 20);
		makeAIMove(Engine.AI_RANDOM, false);
		printBoard();

		System.out.println("That took :" + (System.currentTimeMillis() - time)
				+ "ms");
		System.out.println("That took :"
				+ ((System.nanoTime() - timeNano) / 1000) + " micro seconds");

	}

	private void testThreaded() {
		Thread temp = createWhiteMoveGenerator();
		try {
			temp.join();
			Vector<char[][]> moves = (Vector<char[][]>) WhiteMoveGeneratorThread
					.getStates().clone();
			System.out.println(moves.size());
			Vector<Thread> threads = new Vector<Thread>();

			while (!moves.isEmpty()) {
				currentBoard = moves.get(0);
				updateBitboards();
				threads.add(createWhiteMoveGenerator());
				moves.remove(0);
			}

			for (Thread thread : threads) {
				thread.join();
			}

			moves = (Vector<char[][]>) WhiteMoveGeneratorThread.getStates()
					.clone();
			threads.removeAllElements();

			System.out.println(moves.size());
			threadedList = (Vector<char[][]>) moves.clone();
			while (!moves.isEmpty()) {
				currentBoard = moves.get(0);
				updateBitboards();
				threads.add(createWhiteMoveGenerator());
				moves.remove(0);
			}

			for (Thread thread : threads) {
				thread.join();
			}
			moves = (Vector<char[][]>) WhiteMoveGeneratorThread.getStates()
					.clone();
			System.out.println(moves.size());
			// while (!moves.isEmpty()) {
			// currentBoard = moves.get(0);
			// updateBitboards();
			// threads.add(createWhiteMoveGenerator());
			// moves.remove(0);
			// }
			//
			// for (Thread thread : threads) {
			// thread.join();
			// }
			// moves = (Vector<char[][]>) WhiteMoveGeneratorThread.getStates()
			// .clone();

			System.out.println(moves.size());

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double evaluatePosition() {
		return Evaluator.evaluatePosition(whitePawns, whiteRooks, whiteKnights,
				whiteBishops, whiteQueens, whiteKing, blackPawns, blackRooks,
				blackKnights, blackBishops, blackQueens, blackKing,
				currentBoard, getWhiteAttackingSquares(),
				getBlackAttackingSquares(), whiteCastleKing, whiteCastleQueen,
				blackCastleKing, blackCastleQueen);
	}

	private Thread createWhiteMoveGenerator() {
		Thread WhiteMoveGeneratorThread = new Thread(
				new WhiteMoveGeneratorThread(currentBoard, whitePawns,
						whiteRooks, whiteKnights, whiteBishops, whiteQueens,
						whiteKing, getBlackPieces(), getWhitePieces(),
						getBlackAttackingSquares(), blackPawns, blackRooks,
						blackKnights, blackBishops, blackQueens, blackKing));
		WhiteMoveGeneratorThread.start();

		return WhiteMoveGeneratorThread;

	}

	private Vector<char[][]> generateWhiteLegalMoves() {

		return MoveGenerator.generateWhiteLegalMoves(currentBoard, whitePawns,
				whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
				getBlackPieces(), getWhitePieces(), getBlackAttackingSquares(),
				blackPawns, blackRooks, blackKnights, blackBishops,
				blackQueens, blackKing, whiteCastleKing, whiteCastleQueen,
				enPassantSquare);
	}

	private Vector<char[][]> generateBlackLegalMoves() {
		return MoveGenerator.generateBlackLegalMoves(currentBoard, whitePawns,
				whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
				getBlackPieces(), getWhitePieces(), getWhiteAttackingSquares(),
				blackPawns, blackRooks, blackKnights, blackBishops,
				blackQueens, blackKing, blackCastleKing, blackCastleQueen,
				enPassantSquare);
	}

	public void newGame() {

		currentBoard[0][0] = 'R';
		currentBoard[1][0] = 'N';
		currentBoard[2][0] = 'B';
		currentBoard[3][0] = 'Q';
		currentBoard[4][0] = 'K';
		currentBoard[5][0] = 'B';
		currentBoard[6][0] = 'N';
		currentBoard[7][0] = 'R';

		currentBoard[0][7] = 'r';
		currentBoard[1][7] = 'n';
		currentBoard[2][7] = 'b';
		currentBoard[3][7] = 'q';
		currentBoard[4][7] = 'k';
		currentBoard[5][7] = 'b';
		currentBoard[6][7] = 'n';
		currentBoard[7][7] = 'r';

		for (int x = 0; x < 8; x++) {
			currentBoard[x][1] = 'P';
		}

		for (int x = 0; x < 8; x++) {
			currentBoard[x][6] = 'p';
		}

		for (int y = 2; y < 6; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = ' ';
			}
		}

		whiteToMove = true;
		whiteCastleKing = true;
		whiteCastleQueen = true;
		blackCastleKing = true;
		blackCastleQueen = true;
		numberOfFullMoves = 1;
		numberOfHalfMoves = 0;

		updateBitboards();

	}

	public Gamestate createGamestate() {

		return new Gamestate(copyCurrentBoard(), whitePawns, whiteRooks,
				whiteKnights, whiteBishops, whiteQueens, whiteKing, blackPawns,
				blackRooks, blackKnights, blackBishops, blackQueens, blackKing,
				whiteToMove, whiteToMove, whiteCastleKing, whiteCastleQueen,
				blackCastleKing, blackCastleQueen, enPassantSquare,
				numberOfFullMoves, numberOfHalfMoves,
				getWhiteAttackingSquares(), getBlackAttackingSquares());
	}

	public void makeAIMove(int difficulty, boolean playingWhite) {

		currentBoard = Engine.makeMove(difficulty, playingWhite,
				createGamestate());
		updateBitboards();

	}

	/*
	 * fromSquare and toSquare should be between 0 and 63 inclusive
	 */
	/**
	 * 
	 * @param fromSquare
	 * @param toSquare
	 * @return 0 if move is not possible.
	 *         <p>
	 *         1 If the move is possible and play can continue.
	 *         <p>
	 *         2 if the move is possible and there is a checkmate.
	 *         <p>
	 *         3 if the move is possible and there is a draw.
	 * 
	 */
	public int makeMove(long fromSquare, long toSquare) {

		int x = (int) (fromSquare % 8);
		int y = (int) (fromSquare / 8);

		long fromBitboard = BitboardOperations.getPositionBitboard(fromSquare);
		long toBitboard = BitboardOperations.getPositionBitboard(toSquare);
		char[][] tempBoard = copyCurrentBoard();

		// first check to see if its the correct players turn for the piece
		// about to move
		if (Character.isUpperCase((currentBoard[x][y])) && whiteToMove
				|| (Character.isLowerCase(currentBoard[x][y]) && !whiteToMove)) {
			// check to see if a piece exists at the from coordinate
			// check to see if the piece has a move possible
			if (moveIsPossible(fromBitboard, fromSquare, toBitboard)) {
				// move the piece on the temporary board
				tempBoard[(int) toSquare % 8][(int) toSquare / 8] = tempBoard[(int) x][(int) y];
				tempBoard[x][y] = ' ';

				// next check if tempBoard puts the same colour king in check
				// and
				// if its all good then currentBoard = tempBoard;
				boolean isValid;
				if (Character
						.isUpperCase(tempBoard[(int) (toSquare % 8)][(int) (toSquare / 8)])) {
					isValid = BoardManager.IsSelfCheck(tempBoard, true);
				} else {
					isValid = BoardManager.IsSelfCheck(tempBoard, false);
				}

				// If the move is going to be made this if statement is entered.
				if (isValid) {

					// switch statement to update special conditions
					switch (tempBoard[(int) toSquare % 8][(int) toSquare / 8]) {

					// castling check
					case 'K':
						if (fromSquare == 4) {
							if (toSquare == 6) {
								tempBoard[5][0] = tempBoard[7][0];
								tempBoard[7][0] = ' ';
								whiteCastleKing = false;
								whiteCastleQueen = false;
							} else if (toSquare == 2) {
								tempBoard[3][0] = tempBoard[0][0];
								tempBoard[0][0] = ' ';
								whiteCastleQueen = false;
								whiteCastleKing = false;
							}
						}
						enPassantSquare = 0;
						break;

					// castling check
					case 'k':
						if (fromSquare == 60) {
							if (toSquare == 62) {
								tempBoard[5][7] = tempBoard[7][7];
								tempBoard[7][7] = ' ';
								blackCastleKing = false;
								blackCastleQueen = false;
							} else if (toSquare == 58) {
								tempBoard[3][7] = tempBoard[0][7];
								tempBoard[0][7] = ' ';
								blackCastleKing = false;
								blackCastleQueen = false;
							}

						}
						enPassantSquare = 0;
						break;

					// castling check
					case 'R':
						if (fromSquare == 0) {
							whiteCastleQueen = false;
						} else if (fromSquare == 7) {
							whiteCastleKing = false;
						}
						enPassantSquare = 0;
						break;

					// castling check
					case 'r':
						if (fromSquare == 56) {
							blackCastleQueen = false;
						} else if (fromSquare == 63) {
							blackCastleKing = false;
						}
						enPassantSquare = 0;
						break;

					case 'P':
						if (fromSquare >= 8 && fromSquare <= 15) {
							if (toSquare - 16 == fromSquare) {
								enPassantSquare = fromSquare + 8;
							}
						}
						if (toSquare == enPassantSquare) {
							tempBoard[(int) (enPassantSquare - 8) % 8][(int) (enPassantSquare - 8) / 8] = ' ';
						}

						// pawn promotion
						if (fromSquare >= 48) {
							tempBoard[(int) toSquare % 8][(int) toSquare / 8] = 'Q';
						}

						break;

					case 'p':
						if (fromSquare >= 45 && fromSquare <= 55) {
							if (toSquare + 16 == fromSquare) {
								enPassantSquare = fromSquare - 8;
							}
						}
						if (toSquare == enPassantSquare) {
							tempBoard[(int) (enPassantSquare + 8) % 8][(int) (enPassantSquare + 8) / 8] = ' ';
						}
						if (fromSquare <= 15) {
							tempBoard[(int) toSquare % 8][(int) toSquare / 8] = 'q';
						}
						break;

					default:
						enPassantSquare = 0;
						break;
					}

					currentBoard = tempBoard;
					updateBitboards();
					whiteToMove = !whiteToMove;

					// TODO: Draw by repetition check should happen here at some
					// point.
					if (isCheckmate()) {
						System.out.println("Checkmate!");
						updateBitboards();
						if (whiteToMove) {
							System.out.println("Black wins");
							return 2;
						} else {
							System.out.println("White wins");
							return 3;
						}
					} else if (isStalemate()) {
						System.out.println("Stalemate");
						return 4;
					} else {
						System.out.println("Normal move");
						updateBitboards();
						return 1;
					}
				} else {
					System.out.println("No move");
					return 0;
				}
			}
		}
		System.out.println("No moveF");
		return 0;
	}

	private void removeCastlingRights(long fromSquare) {
		if (whiteCastleKing || whiteCastleQueen) {
			switch ((int) fromSquare) {
			case 4:
				whiteCastleKing = false;
				whiteCastleQueen = false;
				break;
			case 0:
				whiteCastleQueen = false;
				break;
			case 7:
				whiteCastleKing = false;
				break;
			}
		}

		if (blackCastleKing || blackCastleQueen) {
			switch ((int) fromSquare) {
			case 60:
				blackCastleKing = false;
				blackCastleQueen = false;
				break;
			case 56:
				blackCastleQueen = false;
				break;
			case 63:
				blackCastleKing = false;
				break;
			}
		}
	}

	private boolean isCheckmate() {

		if (whiteToMove) {
			return (generateWhiteLegalMoves().size() == 0)
					&& ((whiteKing & getBlackAttackingSquares()) != 0);
		} else {
			return (generateBlackLegalMoves().size() == 0)
					&& ((blackKing & getWhiteAttackingSquares()) != 0);
		}

	}

	private boolean isStalemate() {

		if (whiteToMove) {
			return (generateWhiteLegalMoves().size() == 0)
					&& ((whiteKing & getBlackAttackingSquares()) == 0);
		} else {
			return (generateBlackLegalMoves().size() == 0)
					&& ((blackKing & getWhiteAttackingSquares()) == 0);
		}
	}

	/**
	 * 
	 * Checks to see if a move is possible by seeing if the piece exists and if
	 * one of its possible moves exists at the coordinate stated.
	 * 
	 * @param fromBitboard
	 *            - The bitboard of the piece
	 * @param fromSquare
	 *            - The coordinate of the piece being moved
	 * @param toBitboard
	 *            - The bitboard of the destination square
	 * @return true if the move is possible or false if the move is not possible
	 */
	private boolean moveIsPossible(long fromBitboard, long fromSquare,
			long toBitboard) {

		// checks if a piece exists at that coordinate
		long pieceExists = fromBitboard & getOccupiedSquares();
		// checks to see if a move exists
		long moveExists = generatePieceMoves(fromSquare, fromBitboard)
				& toBitboard;

		return pieceExists != 0 && moveExists != 0;
	}

	/*
	 * Returns a bitboard of the generated moves for a single coordinate
	 */
	/**
	 * 
	 * @param fromSquare
	 *            - The coordinate of the piece
	 * @param fromBitboard
	 *            - The bitboard of the piece
	 * @return - The bitboard of all possible moves
	 */
	private long generatePieceMoves(long fromSquare, long fromBitboard) {
		switch (currentBoard[(int) fromSquare % 8][(int) fromSquare / 8]) {
		case 'P':
			return WhitePieces.getPawnMoves(fromBitboard, getOccupiedSquares(),
					getBlackPieces(), enPassantSquare);
		case 'R':
			return WhitePieces.getRookMoves(fromBitboard, getOccupiedSquares(),
					getWhitePieces());
		case 'N':
			return WhitePieces.getKnightMoves(fromBitboard, getWhitePieces());
		case 'B':
			return WhitePieces.getBishopMoves(fromBitboard,
					getOccupiedSquares(), getWhitePieces());
		case 'Q':
			return WhitePieces.getQueenMoves(fromBitboard,
					getOccupiedSquares(), getWhitePieces());
		case 'K':
			return WhitePieces.getKingMoves(fromBitboard, getWhitePieces(),
					getBlackAttackingSquares(), whiteCastleKing,
					whiteCastleQueen);
		case 'p':
			return BlackPieces.getPawnMoves(fromBitboard, getOccupiedSquares(),
					getWhitePieces(), enPassantSquare);
		case 'r':
			return BlackPieces.getRookMoves(fromBitboard, getOccupiedSquares(),
					getBlackPieces());
		case 'n':
			return BlackPieces.getKnightMoves(fromBitboard, getBlackPieces());
		case 'b':
			return BlackPieces.getBishopMoves(fromBitboard,
					getOccupiedSquares(), getBlackPieces());
		case 'q':
			return BlackPieces.getQueenMoves(fromBitboard,
					getOccupiedSquares(), getBlackPieces());
		case 'k':
			return BlackPieces.getKingMoves(fromBitboard, getBlackPieces(),
					getWhiteAttackingSquares(), blackCastleKing,
					blackCastleQueen);
		}
		return 0L;
	}

	private long getWhiteAttackingSquares() {
		long attackedSquares = 0L;

		attackedSquares = attackedSquares
				| WhitePieces.getPawnAttackingSquares(whitePawns)
				| WhitePieces.getRookAttackingSquares(whiteRooks,
						getOccupiedSquares())
				| WhitePieces.getKnightAttackingSquares(whiteKnights)
				| WhitePieces.getBishopAttackingSquares(whiteBishops,
						getOccupiedSquares())
				| WhitePieces.getQueenAttackingSquares(whiteQueens,
						getOccupiedSquares())
				| WhitePieces.getKingAttackingSquares(whiteKing,
						getWhitePieces());

		return attackedSquares;
	}

	private long getBlackAttackingSquares() {

		long attackedSquares = 0L;

		attackedSquares = attackedSquares
				| BlackPieces.getPawnAttackingSquares(blackPawns)
				| BlackPieces.getRookAttackingSquares(blackRooks,
						getOccupiedSquares())
				| BlackPieces.getKnightAttackingSquares(blackKnights)
				| BlackPieces.getBishopAttackingSquares(blackBishops,
						getOccupiedSquares())
				| BlackPieces.getQueenAttackingSquares(blackQueens,
						getOccupiedSquares())
				| BlackPieces.getKingAttackingSquares(blackKing,
						getBlackPieces());

		return attackedSquares;
	}

	private void clearChessBoard() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = ' ';
			}
		}
	}

	private void updateBitboards() {
		resetBitboards();
		long currentPiece = 1;
		for (int i = 0; i < 64; i++) {
			switch (currentBoard[i % 8][i / 8]) {
			case 'P':
				whitePawns = whitePawns | currentPiece;
				break;
			case 'R':
				whiteRooks = whiteRooks | currentPiece;
				break;
			case 'N':
				whiteKnights = whiteKnights | currentPiece;
				break;
			case 'B':
				whiteBishops = whiteBishops | currentPiece;
				break;
			case 'Q':
				whiteQueens = whiteQueens | currentPiece;
				break;
			case 'K':
				whiteKing = whiteKing | currentPiece;
				break;
			case 'p':
				blackPawns = blackPawns | currentPiece;
				break;
			case 'r':
				blackRooks = blackRooks | currentPiece;
				break;
			case 'n':
				blackKnights = blackKnights | currentPiece;
				break;
			case 'b':
				blackBishops = blackBishops | currentPiece;
				break;
			case 'q':
				blackQueens = blackQueens | currentPiece;
				break;
			case 'k':
				blackKing = blackKing | currentPiece;
				break;
			default:
				break;
			}
			currentPiece = currentPiece << 1;
		}
	}

	private void resetBitboards() {
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
	}

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public void printBoard(char[][] board) {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < board.length; x++) {
				char temp = board[x][y];
				if (temp == ' ') {
					System.out.print(", ");
				} else {
					System.out.print(board[x][y] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	/*
	 * Temporary class for printing out the current state of the board wihout
	 * relying on a gui.
	 */
	public void printBoard() {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < currentBoard.length; x++) {
				char temp = currentBoard[x][y];
				if (temp == ' ') {
					System.out.print(", ");
				} else {
					System.out.print(currentBoard[x][y] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private long getWhitePieces() {
		return (whiteBishops | whiteKing | whiteKnights | whitePawns
				| whiteQueens | whiteRooks);
	}

	private long getBlackPieces() {
		return (blackBishops | blackKing | blackKnights | blackPawns
				| blackQueens | blackRooks);
	}

	private long getOccupiedSquares() {
		return (getWhitePieces() | getBlackPieces());
	}

	private void printBitboard(long bitBoard) {
		String stringBitBoard = Long.toBinaryString(bitBoard);
		System.out.println("Value : " + stringBitBoard);
		while (stringBitBoard.length() != 64) {
			stringBitBoard = "0" + stringBitBoard;
		}

		for (int i = 0; i < 8; i++) {
			StringBuilder stringReverser = new StringBuilder(
					stringBitBoard.substring(i * 8, ((i + 1) * 8)));
			stringReverser.reverse();
			for (int j = 0; j < stringReverser.toString().length(); j++) {
				System.out.print(stringReverser.toString().charAt(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private char[][] copyCurrentBoard() {
		char[][] temp = new char[currentBoard.length][currentBoard.length];

		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < temp.length; y++) {
				temp[x][y] = currentBoard[x][y];
			}
		}
		return temp;
	}

	private void printAllBitboards() {
		System.out.println("White Pawns");
		printBitboard(whitePawns);
		System.out.println("White Knights");
		printBitboard(whiteKnights);
		System.out.println("White Rooks");
		printBitboard(whiteRooks);
		System.out.println("White Bishops");
		printBitboard(whiteBishops);
		System.out.println("White Queens");
		printBitboard(whiteQueens);
		System.out.println("White King");
		printBitboard(whiteKing);

		System.out.println("Black Pawns");
		printBitboard(blackPawns);
		System.out.println("Black Knights");
		printBitboard(blackKnights);
		System.out.println("Black Rooks");
		printBitboard(blackRooks);
		System.out.println("Black Bishops");
		printBitboard(blackBishops);
		System.out.println("Black Queens");
		printBitboard(blackQueens);
		System.out.println("Black King");
		printBitboard(blackKing);

	}

	private void testMethod() {
		Vector<char[][]> firstMove = generateWhiteLegalMoves();
		Vector<char[][]> secondMove = new Vector<char[][]>();
		Vector<char[][]> thirdMove = new Vector<char[][]>();
		Vector<char[][]> fourthMove = new Vector<char[][]>();
		Vector<String> firstHash = new Vector<String>();
		Vector<String> thirdHash = new Vector<String>();

		int firstSize, secondSize, thirdSize, fourthSize;

		firstSize = firstMove.size();
		System.out.println(firstSize);
		while (!firstMove.isEmpty()) {
			currentBoard = firstMove.get(0);
			firstHash.add(HashGenerator.generatePositionHash(currentBoard));
			updateBitboards();
			secondMove.addAll(generateBlackLegalMoves());
			firstMove.remove(0);
		}

		secondSize = secondMove.size();
		System.out.println(secondSize);
		serialList = (Vector<char[][]>) secondMove.clone();
		while (!secondMove.isEmpty()) {
			currentBoard = secondMove.get(0);
			updateBitboards();
			thirdMove.addAll(generateWhiteLegalMoves());
			secondMove.remove(0);
		}

		thirdSize = thirdMove.size();
		System.out.println(thirdSize);
		while (!thirdMove.isEmpty()) {
			currentBoard = thirdMove.get(0);
			thirdHash.add(HashGenerator.generatePositionHash(currentBoard));
			updateBitboards();
			// fourthMove.addAll(generateBlackLegalMoves());
			thirdMove.remove(0);
		}

		String temp;
		for (int i = 0; i < thirdHash.size(); i++) {
			temp = thirdHash.get(i);
			for (int j = i + 1; j < thirdHash.size(); j++) {
				if (thirdHash.get(j).equals(temp)) {
					thirdHash.remove(j);
					j--;
				}
			}
		}
		System.out.println(thirdHash.size());

		for (int i = 0; i < firstHash.size(); i++) {
			temp = firstHash.get(i);
			for (int j = i + 1; j < firstHash.size(); j++) {
				if (firstHash.get(j).equals(temp)) {
					firstHash.remove(j);
					j--;
				}
			}
		}

		System.out.println(firstHash.size());

		while (!firstHash.isEmpty()) {
			System.out.println(firstHash.get(0));
			firstHash.remove(0);
		}

		fourthSize = fourthMove.size();
	}

	/*
	 * 1. Piece placement 2. Active colour 3. Castling 4. En passant 5. Halfmove
	 * clock 6. Full move number
	 */
	public void newGameFromFEN(String FENString) {

		Scanner fenScanner = new Scanner(FENString);
		fenScanner.useDelimiter(" ");

		currentBoard = FENLoader.createPieceArrayFromFEN(fenScanner.next());
		whiteToMove = FENLoader.getActiveColour(fenScanner.next());

		boolean[] castlingPermissions = FENLoader
				.getCastlingPermissions(fenScanner.next());
		whiteCastleKing = castlingPermissions[0];
		whiteCastleQueen = castlingPermissions[1];
		blackCastleKing = castlingPermissions[2];
		blackCastleQueen = castlingPermissions[3];

		// TODO: implement en passant
		enPassantSquare = FENLoader.getEnPassantSquare(fenScanner.next());

		numberOfHalfMoves = FENLoader.getHalfMoves(fenScanner.next());
		numberOfFullMoves = FENLoader.getFullMoves(fenScanner.next());

		updateBitboards();
		printBoard();

	}

	public char[][] getBoard() {
		return currentBoard;
	}

	private void removeCastlingRights() {
		whiteCastleKing = false;
		whiteCastleQueen = false;
		blackCastleKing = false;
		blackCastleQueen = false;
	}

	private static char[][] copyCurrentBoard(char[][] currentBoard) {
		char[][] temp = new char[currentBoard.length][currentBoard.length];

		for (int x = 0; x < temp.length; x++) {
			for (int y = 0; y < temp.length; y++) {
				temp[x][y] = currentBoard[x][y];
			}
		}
		return temp;
	}
}
