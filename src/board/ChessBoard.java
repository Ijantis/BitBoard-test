package board;

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
			{ "R", "P", " ", " ", " ", " ", "p", "r" } };;

	public ChessBoard() {

		long time = System.currentTimeMillis();
		long timeNano = System.nanoTime();

		initialiseBoard();
		printBitBoard(getWhiteAttackingSquares());
		printBitBoard(getWhitePawnMovesDiagonal());

		System.out.println("That took :" + (System.currentTimeMillis() - time)
				+ "ms");
		System.out.println("That took :"
				+ ((System.nanoTime() - timeNano) / 1000) + " micro seconds");
	}

	private boolean isWhiteKingInCheck() {

		long blackMoves = getBlackAttackingSquares();

		if ((blackMoves & whiteKing) == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isBlackKingInCheck() {

		long whiteMoves = getWhiteAttackingSquares();

		if ((whiteMoves & blackKing) == 0) {
			return false;
		} else {
			return true;
		}
	}

	private long getWhiteAttackingSquares() {
		long attackedSquares = 0L;

		attackedSquares = attackedSquares | getWhitePawnMovesDiagonal()
				| getAllWhiteRookMoves(whiteRooks) | getWhiteKnightMoves()
				| getWhiteBishopMoves(whiteBishops)
				| getWhiteQueenMoves(whiteQueens) | getWhiteKingMoves();
		return attackedSquares;
	}

	private long getBlackAttackingSquares() {

		long attackedSquares = 0L;

		attackedSquares = attackedSquares | getBlackPawnMovesDiagonal()
				| getAllBlackRookMoves(blackRooks) | getBlackKnightMoves()
				| getBlackBishopMoves(blackBishops)
				| getBlackQueenMoves(blackQueens) | getBlackKingMoves();

		return attackedSquares;
	}

	private long getAllWhiteRookMoves(long currentRookBitboard) {
		long nextRook = 0L;
		long rookMoves = 0L;

		while (currentRookBitboard != 0) {
			nextRook = Long.parseLong("1", 2);
			for (int i = 0; i < Long.toBinaryString(currentRookBitboard)
					.length() - 1; i++) {
				if (Long.toBinaryString(nextRook).length() == 63) {
					nextRook = nextRook * 2;
				} else {
					nextRook = Long.parseLong(Long.toBinaryString(nextRook)
							+ "0", 2);
				}
			}
			if (currentRookBitboard == 1) {
				rookMoves = rookMoves | getWhiteRookMoves(nextRook);
				break;
			} else {
				currentRookBitboard = Long.parseLong(
						Long.toBinaryString(currentRookBitboard).substring(1),
						2);
				rookMoves = rookMoves | getWhiteRookMoves(nextRook);
			}
		}
		return rookMoves;
	}

	private long getAllBlackRookMoves(long currentRookBitboard) {
		long nextRook = 0L;
		long rookMoves = 0L;

		while (currentRookBitboard != 0) {
			nextRook = Long.parseLong("1", 2);
			for (int i = 0; i < Long.toBinaryString(currentRookBitboard)
					.length() - 1; i++) {
				if (Long.toBinaryString(nextRook).length() == 63) {
					nextRook = nextRook * 2;
				} else {
					nextRook = Long.parseLong(Long.toBinaryString(nextRook)
							+ "0", 2);
				}
			}
			if (currentRookBitboard == 1) {
				rookMoves = rookMoves | getBlackRookMoves(nextRook);
				break;
			} else {
				currentRookBitboard = Long.parseLong(
						Long.toBinaryString(currentRookBitboard).substring(1),
						2);
				rookMoves = rookMoves | getBlackRookMoves(nextRook);
			}
		}
		return rookMoves;
	}

	private void clearChessBoard() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				currentBoard[x][y] = " ";
			}
		}
	}

	private long getWhiteQueenMoves(long bitboard) {
		return getWhiteBishopMoves(bitboard) | getAllWhiteRookMoves(bitboard);
	}

	private long getBlackQueenMoves(long bitboard) {
		return getBlackBishopMoves(bitboard) | getAllBlackRookMoves(bitboard);
	}

	private long getWhiteBishopMoves(long bitboard) {

		long topRight = BitboardOperations.getTopRightSquares(bitboard) & getOccupiedSquares();
		topRight = (topRight << 9) | (topRight << 18) | (topRight << 27)
				| (topRight << 36) | (topRight << 45) | (topRight << 54);
		topRight = topRight & BitboardOperations.getTopRightSquares(bitboard);
		topRight = topRight ^ BitboardOperations.getTopRightSquares(bitboard);
		topRight = topRight & ~getWhitePieces();

		long bottomLeft = BitboardOperations.getBottomLeftSquares(bitboard)
				& getOccupiedSquares();
		bottomLeft = (bottomLeft >>> 9) | (bottomLeft >>> 18)
				| (bottomLeft >>> 27) | (bottomLeft >>> 36)
				| (bottomLeft >>> 45) | (bottomLeft >>> 54);
		bottomLeft = bottomLeft
				& BitboardOperations.getBottomLeftSquares(bitboard);
		bottomLeft = bottomLeft
				^ BitboardOperations.getBottomLeftSquares(bitboard);
		bottomLeft = bottomLeft & ~getWhitePieces();

		long bottomRight = BitboardOperations.getBottomRightSquares(bitboard)
				& getOccupiedSquares();
		bottomRight = (bottomRight >>> 7) | (bottomRight >>> 14)
				| (bottomRight >>> 21) | (bottomRight >>> 28)
				| (bottomRight >>> 35) | (bottomRight >>> 42);
		bottomRight = bottomRight
				& BitboardOperations.getBottomRightSquares(bitboard);
		bottomRight = bottomRight
				^ BitboardOperations.getBottomRightSquares(bitboard);
		bottomRight = bottomRight & ~getWhitePieces();

		long topLeft = BitboardOperations.getTopLeftSquares(bitboard)
				& getOccupiedSquares();
		topLeft = (topLeft << 7) | (topLeft << 14) | (topLeft << 21)
				| (topLeft << 28) | (topLeft << 35) | (topLeft << 42);
		topLeft = topLeft & BitboardOperations.getTopLeftSquares(bitboard);
		topLeft = topLeft ^ BitboardOperations.getTopLeftSquares(bitboard);
		topLeft = topLeft & ~getWhitePieces();

		return topRight | topLeft | bottomRight | bottomLeft;
	}

	private long getBlackBishopMoves(long bitboard) {

		long topRight = BitboardOperations.getTopRightSquares(bitboard) & getOccupiedSquares();
		topRight = (topRight << 9) | (topRight << 18) | (topRight << 27)
				| (topRight << 36) | (topRight << 45) | (topRight << 54);
		topRight = topRight & BitboardOperations.getTopRightSquares(bitboard);
		topRight = topRight ^ BitboardOperations.getTopRightSquares(bitboard);
		topRight = topRight & ~getBlackPieces();

		long bottomLeft = BitboardOperations.getBottomLeftSquares(bitboard)
				& getOccupiedSquares();
		bottomLeft = (bottomLeft >>> 9) | (bottomLeft >>> 18)
				| (bottomLeft >>> 27) | (bottomLeft >>> 36)
				| (bottomLeft >>> 45) | (bottomLeft >>> 54);
		bottomLeft = bottomLeft
				& BitboardOperations.getBottomLeftSquares(bitboard);
		bottomLeft = bottomLeft
				^ BitboardOperations.getBottomLeftSquares(bitboard);
		bottomLeft = bottomLeft & ~getBlackPieces();

		long bottomRight = BitboardOperations.getBottomRightSquares(bitboard)
				& getOccupiedSquares();
		bottomRight = (bottomRight >>> 7) | (bottomRight >>> 14)
				| (bottomRight >>> 21) | (bottomRight >>> 28)
				| (bottomRight >>> 35) | (bottomRight >>> 42);
		bottomRight = bottomRight
				& BitboardOperations.getBottomRightSquares(bitboard);
		bottomRight = bottomRight
				^ BitboardOperations.getBottomRightSquares(bitboard);
		bottomRight = bottomRight & ~getBlackPieces();

		long topLeft = BitboardOperations.getTopLeftSquares(bitboard)
				& getOccupiedSquares();
		topLeft = (topLeft << 7) | (topLeft << 14) | (topLeft << 21)
				| (topLeft << 28) | (topLeft << 35) | (topLeft << 42);
		topLeft = topLeft & BitboardOperations.getTopLeftSquares(bitboard);
		topLeft = topLeft ^ BitboardOperations.getTopLeftSquares(bitboard);
		topLeft = topLeft & ~getBlackPieces();

		return topRight | topLeft | bottomRight | bottomLeft;
	}

	/*
	 * Returns the bitboard of possible moves for a single rook.
	 */
	/**
	 * Takes in a bitboard with a single piece on it and outputs the moves
	 * associated with that rook.
	 * 
	 * @param rookToMove
	 *            - The bitboard of a rook to compute the moves for.
	 * @return - The bitboard of possible moves for the inputed rook.
	 */
	private long getWhiteRookMoves(long rookToMove) {

		long right_moves = BitboardOperations.getRightSquares(rookToMove)
				& getOccupiedSquares();
		right_moves = (right_moves << 1) | (right_moves << 2)
				| (right_moves << 3) | (right_moves << 4) | (right_moves << 5)
				| (right_moves << 6);
		right_moves = right_moves
				& BitboardOperations.getRightSquares(rookToMove);
		right_moves = right_moves
				^ BitboardOperations.getRightSquares(rookToMove);
		right_moves = right_moves & ~getWhitePieces();

		long left_moves = BitboardOperations.getLeftSquares(rookToMove)
				& getOccupiedSquares();
		left_moves = (left_moves >>> 1) | (left_moves >>> 2)
				| (left_moves >>> 3) | (left_moves >>> 4) | (left_moves >>> 5)
				| (left_moves >>> 6);
		left_moves = left_moves & BitboardOperations.getLeftSquares(rookToMove);
		left_moves = left_moves ^ BitboardOperations.getLeftSquares(rookToMove);
		left_moves = left_moves & ~getWhitePieces();

		long up_moves = BitboardOperations.getUpSquares(rookToMove)
				& getOccupiedSquares();
		up_moves = (up_moves << 8) | (up_moves << 16) | (up_moves << 24)
				| (up_moves << 32) | (up_moves << 40) | (up_moves << 48);
		up_moves = up_moves & BitboardOperations.getUpSquares(rookToMove);
		up_moves = up_moves ^ BitboardOperations.getUpSquares(rookToMove);
		up_moves = up_moves & ~getWhitePieces();

		long down_moves = BitboardOperations.getDownSquares(rookToMove)
				& getOccupiedSquares();
		down_moves = (down_moves >>> 8) | (down_moves >>> 16)
				| (down_moves >>> 24) | (down_moves >>> 32)
				| (down_moves >>> 40) | (down_moves >>> 48);
		down_moves = down_moves & BitboardOperations.getDownSquares(rookToMove);
		down_moves = down_moves ^ BitboardOperations.getDownSquares(rookToMove);
		down_moves = down_moves & ~getWhitePieces();

		return left_moves | right_moves | up_moves | down_moves;
	}

	/*
	 * Returns the bitboard of possible moves for a single rook.
	 */
	/**
	 * Takes in a bitboard with a single piece on it and outputs the moves
	 * associated with that rook.
	 * 
	 * @param rookToMove
	 *            - The bitboard of a rook to compute the moves for.
	 * @return - The bitboard of possible moves for the inputed rook.
	 */
	private long getBlackRookMoves(long rookToMove) {

		long right_moves = BitboardOperations.getRightSquares(rookToMove)
				& getOccupiedSquares();
		right_moves = (right_moves << 1) | (right_moves << 2)
				| (right_moves << 3) | (right_moves << 4) | (right_moves << 5)
				| (right_moves << 6);
		right_moves = right_moves
				& BitboardOperations.getRightSquares(rookToMove);
		right_moves = right_moves
				^ BitboardOperations.getRightSquares(rookToMove);
		right_moves = right_moves & ~getBlackPieces();

		long left_moves = BitboardOperations.getLeftSquares(rookToMove)
				& getOccupiedSquares();
		left_moves = (left_moves >>> 1) | (left_moves >>> 2)
				| (left_moves >>> 3) | (left_moves >>> 4) | (left_moves >>> 5)
				| (left_moves >>> 6);
		left_moves = left_moves & BitboardOperations.getLeftSquares(rookToMove);
		left_moves = left_moves ^ BitboardOperations.getLeftSquares(rookToMove);
		left_moves = left_moves & ~getBlackPieces();

		long up_moves = BitboardOperations.getUpSquares(rookToMove)
				& getOccupiedSquares();
		up_moves = (up_moves << 8) | (up_moves << 16) | (up_moves << 24)
				| (up_moves << 32) | (up_moves << 40) | (up_moves << 48);
		up_moves = up_moves & BitboardOperations.getUpSquares(rookToMove);
		up_moves = up_moves ^ BitboardOperations.getUpSquares(rookToMove);
		up_moves = up_moves & ~getBlackPieces();

		long down_moves = BitboardOperations.getDownSquares(rookToMove)
				& getOccupiedSquares();
		down_moves = (down_moves >>> 8) | (down_moves >>> 16)
				| (down_moves >>> 24) | (down_moves >>> 32)
				| (down_moves >>> 40) | (down_moves >>> 48);
		down_moves = down_moves & BitboardOperations.getDownSquares(rookToMove);
		down_moves = down_moves ^ BitboardOperations.getDownSquares(rookToMove);
		down_moves = down_moves & ~getBlackPieces();

		return left_moves | right_moves | up_moves | down_moves;
	}

	private long getWhiteKnightMoves() {

		// variable names are first direction then second direction

		long upRight = (whiteKnights & BitboardOperations.clearFile(8)) << 17;
		long upLeft = (whiteKnights & BitboardOperations.clearFile(1)) << 15;

		long rightUp = (BitboardOperations.clearFile(8)
				& BitboardOperations.clearFile(7) & whiteKnights) << 10;
		long rightDown = (BitboardOperations.clearFile(8)
				& BitboardOperations.clearFile(7) & whiteKnights) >>> 6;

		long downRight = (whiteKnights & BitboardOperations.clearFile(8)) >>> 15;
		long downLeft = (whiteKnights & BitboardOperations.clearFile(1)) >>> 17;

		long leftUp = (BitboardOperations.clearFile(1)
				& BitboardOperations.clearFile(2) & whiteKnights) << 6;
		long leftDown = (BitboardOperations.clearFile(1)
				& BitboardOperations.clearFile(2) & whiteKnights) >>> 10;

		long possibleMoves = upRight | upLeft | rightUp | rightDown | downRight
				| downLeft | leftUp | leftDown;

		return possibleMoves & ~getWhitePieces();// & ~getWhitePieces();
	}

	private long getBlackKnightMoves() {

		// variable names are first direction then second direction

		long upRight = (blackKnights & BitboardOperations.clearFile(8)) << 17;
		long upLeft = (blackKnights & BitboardOperations.clearFile(1)) << 15;

		long rightUp = (BitboardOperations.clearFile(8)
				& BitboardOperations.clearFile(7) & blackKnights) << 10;
		long rightDown = (BitboardOperations.clearFile(8)
				& BitboardOperations.clearFile(7) & blackKnights) >>> 6;

		long downRight = (blackKnights & BitboardOperations.clearFile(8)) >>> 15;
		long downLeft = (blackKnights & BitboardOperations.clearFile(1)) >>> 17;

		long leftUp = (BitboardOperations.clearFile(1)
				& BitboardOperations.clearFile(2) & blackKnights) << 6;
		long leftDown = (BitboardOperations.clearFile(1)
				& BitboardOperations.clearFile(2) & blackKnights) >>> 10;

		long possibleMoves = upRight | upLeft | rightUp | rightDown | downRight
				| downLeft | leftUp | leftDown;

		return possibleMoves & ~getBlackPieces();// & ~getWhitePieces();
	}

	/*
	 * Generate white pawn moves for attacking a piece
	 */
	private long getWhitePawnMovesDiagonal() {

		long upLeft = ((whitePawns & BitboardOperations.clearFile(1)) << 7);
		long upRight = ((whitePawns & BitboardOperations.clearFile(8)) << 9);
		long attackingAPiece = (upLeft | upRight) & getBlackPieces();

		return attackingAPiece;
	}

	/*
	 * Generate black pawn moves when attacking a piece
	 */
	private long getBlackPawnMovesDiagonal() {

		long downRight = ((blackPawns & BitboardOperations.clearFile(8)) >>> 7);
		long downLeft = ((blackPawns & BitboardOperations.clearFile(1)) >>> 9);
		long attackingAPiece = (downRight | downLeft) & getWhitePieces();

		return 0;
	}

	/*
	 * Generate white king moves.
	 */
	private long getWhiteKingMoves() {

		// vertical movement 8 bit shift
		long up = whiteKing << 8;
		long down = whiteKing >>> 8;

		// clear file stops move generation to the left if on the 1st file
		long left = ((whiteKing & BitboardOperations.clearFile(1)) >>> 1);
		long upLeft = (whiteKing & BitboardOperations.clearFile(1)) << 7;
		long downLeft = (whiteKing & BitboardOperations.clearFile(1)) >>> 9;

		// clear file stops move generation to the right if on the 8th file
		long right = ((whiteKing & BitboardOperations.clearFile(8)) << 1);
		long upRight = ((whiteKing & BitboardOperations.clearFile(8)) << 9);
		long downRight = ((whiteKing & BitboardOperations.clearFile(8)) >>> 7);

		long possibleMoves = (up | down | left | right | upRight | downRight
				| upLeft | downLeft);

		// TODO: Add another check to not let the king move onto a square under
		// attack by the opposite colour.
		return possibleMoves & ~getWhitePieces();
	}

	/*
	 * Generate black king moves.
	 */
	private long getBlackKingMoves() {

		// vertical movement 8 bit shift
		long up = blackKing << 8;
		long down = blackKing >>> 8;

		// clear file stops move generation to the left if on the 1st file
		long left = ((blackKing & BitboardOperations.clearFile(1)) >>> 1);
		long upLeft = (blackKing & BitboardOperations.clearFile(1)) << 7;
		long downLeft = (blackKing & BitboardOperations.clearFile(1)) >>> 9;

		// clear file stops move generation to the right if on the 8th file
		long right = ((blackKing & BitboardOperations.clearFile(8)) << 1);
		long upRight = ((blackKing & BitboardOperations.clearFile(8)) << 9);
		long downRight = ((blackKing & BitboardOperations.clearFile(8)) >>> 7);

		long possibleMoves = (up | down | left | right | upRight | downRight
				| upLeft | downLeft);

		// TODO: Add another check to not let the king move onto a square under
		// attack by the opposite colour.
		return possibleMoves & ~getBlackPieces();
	}

	private long getWhitePawnMovesVertical() {

		// Vertical single rank
		long upOne = (whitePawns << 8) & ~getOccupiedSquares();

		// Moving up 2 spaces from 2nd rank
		long secondRankPawns = whitePawns & BitboardOperations.maskRank(2);
		long pawnsNotBlocked = (((secondRankPawns << 8) & ~getOccupiedSquares()) >>> 8);
		long upTwo = pawnsNotBlocked << 16 & ~getOccupiedSquares();

		return upOne | upTwo;
	}

	private long getBlackPawnMovesVertical() {

		// Vertical single rank
		long downOne = (blackPawns >>> 8) & ~getOccupiedSquares();

		// Moving down 2 ranks from 7th rank
		long seventhRankPawns = blackPawns & BitboardOperations.maskRank(7);
		long pawnsNotBlocked = (((seventhRankPawns >>> 8) & ~getOccupiedSquares()) << 8);
		long downTwo = pawnsNotBlocked >>> 16 & ~getOccupiedSquares();

		return downOne | downTwo;
	}

	private void initialiseBoard() {
		initialiseBitBoards();
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

	private void initialiseBitBoards() {
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
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < currentBoard.length; x++) {
				String temp = currentBoard[x][y];
				if (temp.equals(" ")) {
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

	private void printBitBoard(long bitBoard) {
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
}
