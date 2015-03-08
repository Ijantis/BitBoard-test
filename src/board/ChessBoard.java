package board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;

import evaluation.Evaluator;
import bitboards.BitboardOperations;
import bitboards.BlackPieces;
import bitboards.WhitePieces;
import main.GameLoop;
import movegen.MoveGenerator;
import other.FENLoader;
//import other.HashGenerator;
import search.Engine;

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
	private long enPassantSquare = 100;
	private int numberOfFullMoves = 1;
	private int numberOfHalfMoves = 0;

	private Stack<FullGameState> listOfMoves = new Stack<FullGameState>();

	// Upper case for WHITE
	// Lower case for BLACK
	// 0,0 is top left 0,7 is top right 7,7 bottom right
	private char[][] currentBoard = {
			{ 'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r' },
			{ 'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n' },
			{ 'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b' },
			{ 'Q', 'P', ' ', ' ', ' ', ' ', 'p', 'q' },
			{ 'K', 'P', ' ', ' ', ' ', ' ', 'p', 'k' },
			{ 'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b' },
			{ 'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n' },
			{ 'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r' } };;

	public ChessBoard() {
		long time = System.currentTimeMillis();
		long timeNano = System.nanoTime();

		newGame();
		generateDepthMoves(5);

		System.out.println("That took :" + (System.currentTimeMillis() - time)
				+ "ms");
		System.out.println("That took :"
				+ ((System.nanoTime() - timeNano) / 1000) + " micro seconds");

	}

	private void printStatus() {

		System.out.println("White to move: " + whiteToMove);
		System.out.println("White castling kingside: " + whiteCastleKing);
		System.out.println("White castling queenside: " + whiteCastleQueen);
		System.out.println("Black castling kingside: " + blackCastleKing);
		System.out.println("Black castling queenside: " + blackCastleQueen);

		if (enPassantSquare == 100) {
			System.out.println("No en passant square");
		} else {
			System.out.println("En passant square is " + enPassantSquare);
		}

		System.out.println("Full moves: " + numberOfFullMoves);
		System.out.println("Half moves: " + numberOfHalfMoves);

	}

	private void generateMoveDepthDivide(int depth) {
		ArrayList<FullGameState> keyGenerator = MoveGenerator
				.generateBlackLegalMoves(createGamestate());
		FullGameState currentState = createGamestate();

		Hashtable<String, Integer> storage = new Hashtable<>();
		String key = "";
		System.out.println(keyGenerator.size());

		if (depth == 0) {
			for (int i = 0; i < keyGenerator.size(); i++) {
				key = numberToAlgebra((int) keyGenerator.get(i).getFromSquare())
						+ "-"
						+ numberToAlgebra((int) keyGenerator.get(i)
								.getToSquare());
				if (storage.containsKey(key)) {
					storage.put(key, storage.get(key) + 1);
				} else {
					storage.put(key, 1);
				}
			}
		} else {
			for (int i = 0; i < keyGenerator.size(); i++) {
				key = numberToAlgebra((int) keyGenerator.get(i).getFromSquare())
						+ "-"
						+ numberToAlgebra((int) keyGenerator.get(i)
								.getToSquare());
				if (storage.containsKey(key)) {
					storage.put(
							key,
							storage.get(key)
									+ generateMoveDepth(currentState,
											depth - 1,
											currentState.getWhiteToMove()));
				} else {
					storage.put(
							key,
							generateMoveDepth(currentState, depth - 1,
									currentState.getWhiteToMove()));
				}

			}
		}

		ArrayList<String> keySet = new ArrayList<String>(storage.keySet());
		Collections.sort(keySet);
		int count = 0;
		for (int i = 0; i < keySet.size(); i++) {
			String currentKey = keySet.get(i);
			System.out.println(currentKey + " " + storage.get(currentKey));
			count += storage.get(currentKey);

		}
		System.out.println(count);
	}

	public long generateDepthMoves(int depth) {
		return generateMoveDepth(createGamestate(), depth - 1, whiteToMove);
	}

	private int generateMoveDepth(FullGameState currentState, int depth,
			boolean whiteToMove) {

		ArrayList<FullGameState> currentMoves = new ArrayList<FullGameState>();

		do {
			if (whiteToMove) {
				currentMoves = MoveGenerator
						.generateWhiteLegalMoves(currentState);
				int count = 0;
				if (depth == 0) {
					return currentMoves.size();
				}
				for (int i = 0; i < currentMoves.size(); i++) {
					count += generateMoveDepth(currentMoves.get(i), depth - 1,
							!whiteToMove);
				}
				return count;

			} else {
				currentMoves = MoveGenerator
						.generateBlackLegalMoves(currentState);
				int count = 0;
				if (depth == 0) {
					return currentMoves.size();
				}
				for (int i = 0; i < currentMoves.size(); i++) {
					count += generateMoveDepth(currentMoves.get(i), depth - 1,
							!whiteToMove);
				}
				return count;

			}
		} while (depth != 0);

	}

	private String numberToAlgebra(int number) {

		int letter = number % 8;
		String firstNumber = Long.toString((number / 8) + 1);
		switch (letter) {
		case 7:
			return "h" + firstNumber;
		case 6:
			return "g" + firstNumber;
		case 5:
			return "f" + firstNumber;
		case 4:
			return "e" + firstNumber;
		case 3:
			return "d" + firstNumber;
		case 2:
			return "c" + firstNumber;
		case 1:
			return "b" + firstNumber;
		case 0:
			return "a" + firstNumber;
		}
		return null;
	}

	private ArrayList<FullGameState> generateWhiteLegalMoves() {
		FullGameState temp = createGamestate();
		return MoveGenerator.generateWhiteLegalMoves(temp);
	}

	private ArrayList<FullGameState> generateBlackLegalMoves() {
		FullGameState temp = createGamestate();
		return MoveGenerator.generateBlackLegalMoves(temp);
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
		listOfMoves.add(createGamestate());

	}

	public FullGameState createGamestate() {

		return new FullGameState(whitePawns, whiteRooks, whiteKnights,
				whiteBishops, whiteQueens, whiteKing, blackPawns, blackRooks,
				blackKnights, blackBishops, blackQueens, blackKing,
				whiteToMove, whiteCastleKing, whiteCastleQueen,
				blackCastleKing, blackCastleQueen, enPassantSquare,
				numberOfFullMoves, numberOfHalfMoves, 0, 0);
	}

	public int makeAIMove(int difficulty, boolean playingWhite) {

		FullGameState temp = Engine.makeMove(difficulty, playingWhite,
				createGamestate());
		whiteCastleKing = temp.getWhiteCastleKing();
		whiteCastleQueen = temp.getWhiteCastleQueen();
		blackCastleKing = temp.getBlackCastleKing();
		blackCastleQueen = temp.getBlackCastleQueen();

		currentBoard[(int) (temp.getToSquare() % 8)][(int) (temp.getToSquare() / 8)] = currentBoard[(int) (temp
				.getFromSquare() % 8)][(int) (temp.getFromSquare() / 8)];
		currentBoard[(int) (temp.getFromSquare() % 8)][(int) (temp
				.getFromSquare() / 8)] = ' ';
		updateBitboards();

		enPassantSquare = temp.getEnPassantSquare();
		numberOfFullMoves = temp.getNumberOfFullMoves();
		numberOfHalfMoves = temp.getNumberOfHalfMoves();

		listOfMoves.push(createGamestate());

		if (isCheckmate()) {
			updateBitboards();
			if (whiteToMove) {
				return 2;
			} else {
				return 3;
			}
		} else if (isStalemate()) {
			return 4;
		} else {
			updateBitboards();
			whiteToMove = !whiteToMove;
			return 1;
		}
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
					isValid = BoardManager.isSelfCheck(tempBoard, true);
				} else {
					isValid = BoardManager.isSelfCheck(tempBoard, false);
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
							System.out.println("A QUEEN IS MADE");
							tempBoard[(int) toSquare % 8][(int) toSquare / 8] = 'q';
						}
						break;

					default:
						enPassantSquare = 0;
						break;
					}

					currentBoard = tempBoard;
					updateBitboards();
					listOfMoves.push(createGamestate());
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
					whiteCastleQueen, getBlackPieces());
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
					blackCastleQueen, getWhitePieces());
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

		enPassantSquare = FENLoader.getEnPassantSquare(fenScanner.next());

		numberOfHalfMoves = FENLoader.getHalfMoves(fenScanner.next());
		numberOfFullMoves = FENLoader.getFullMoves(fenScanner.next());

		updateBitboards();
		fenScanner.close();

	}

	public char[][] getBoard() {
		return currentBoard;
	}

	private void clearCastlingRights() {
		whiteCastleKing = false;
		whiteCastleQueen = false;
		blackCastleKing = false;
		blackCastleQueen = false;
	}

	private char[][] createArrayFromBitboards(FullGameState state) {

		char[][] tempBoard = new char[8][8];
		for (int i = 0; i < 64; i++) {
			tempBoard[i % 8][i / 8] = ' ';
		}

		long temp = state.getWhiteKing();

		int nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
		tempBoard[nextLength % 8][nextLength / 8] = 'K';

		temp = state.getWhitePawns();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'P';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getWhiteRooks();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'R';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getWhiteKnights();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'N';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getWhiteBishops();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'B';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getWhiteQueens();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'Q';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackKing();
		nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;

		tempBoard[nextLength % 8][nextLength / 8] = 'k';

		temp = state.getBlackPawns();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'p';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackRooks();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'r';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackKnights();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'n';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackBishops();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'b';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		temp = state.getBlackQueens();
		while (temp != 0) {

			nextLength = Long.toBinaryString(Long.highestOneBit(temp)).length() - 1;
			tempBoard[nextLength % 8][nextLength / 8] = 'q';
			temp = Long.highestOneBit(temp) ^ temp;
		}

		return tempBoard;

	}

	public void undoMove() {

		if (!listOfMoves.isEmpty()) {
			listOfMoves.pop();
			if (listOfMoves.isEmpty()) {
				newGame();
			} else {
				FullGameState previousState = listOfMoves.peek();

				whiteCastleKing = previousState.getWhiteCastleKing();
				whiteCastleQueen = previousState.getWhiteCastleQueen();
				blackCastleKing = previousState.getBlackCastleKing();
				blackCastleQueen = previousState.getBlackCastleQueen();

				enPassantSquare = previousState.getEnPassantSquare();
				numberOfFullMoves = previousState.getNumberOfFullMoves();
				numberOfHalfMoves = previousState.getNumberOfHalfMoves();

				whiteToMove = !whiteToMove;

				currentBoard = createArrayFromBitboards(previousState);
			}

		}

	}

}
