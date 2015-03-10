package board;

import bitboards.BlackPieces;
import bitboards.WhitePieces;

public class BoardManager {

	public static boolean IsSelfCheck(long whitePawns, long whiteRooks,
			long whiteKnights, long whiteBishops, long whiteQueens,
			long whiteKing, long blackPawns, long blackRooks,
			long blackKnights, long blackBishops, long blackQueens,
			long blackKing, boolean checkForWhite) {

		long blackPieces = (blackBishops | blackKing | blackKnights
				| blackPawns | blackQueens | blackRooks);

		long whitePieces = (whiteBishops | whiteKing | whiteKnights
				| whitePawns | whiteQueens | whiteRooks);

		long occupiedSquares = whitePieces | blackPieces;

		if (checkForWhite) {
			long blackAttackingSquares = 0L;

			blackAttackingSquares = blackAttackingSquares
					| BlackPieces.getPawnAttackingSquares(blackPawns)
					| BlackPieces.getRookAttackingSquares(blackRooks,
							occupiedSquares)
					| BlackPieces.getKnightAttackingSquares(blackKnights)
					| BlackPieces.getBishopAttackingSquares(blackBishops,
							occupiedSquares)
					| BlackPieces.getQueenAttackingSquares(blackQueens,
							occupiedSquares)
					| BlackPieces.getKingAttackingSquares(blackKing,
							blackPieces);

			return (whiteKing & blackAttackingSquares) == 0;
		} else {
			long whiteAttackingSquares = 0L;

			whiteAttackingSquares = whiteAttackingSquares
					| WhitePieces.getPawnAttackingSquares(whitePawns)
					| WhitePieces.getRookAttackingSquares(whiteRooks,
							occupiedSquares)
					| WhitePieces.getKnightAttackingSquares(whiteKnights)
					| WhitePieces.getBishopAttackingSquares(whiteBishops,
							occupiedSquares)
					| WhitePieces.getQueenAttackingSquares(whiteQueens,
							occupiedSquares)
					| WhitePieces.getKingAttackingSquares(whiteKing,
							whitePieces);

			return (blackKing & whiteAttackingSquares) == 0;
		}

	}

	public static boolean isSelfCheck(char[][] boardToCheck,
			boolean checkForWhite) {

		long whitePawns = 0L;
		long whiteRooks = 0L;
		long whiteBishops = 0L;
		long whiteKnights = 0L;
		long whiteQueens = 0L;
		long whiteKing = 0L;

		long blackPawns = 0L;
		long blackRooks = 0L;
		long blackBishops = 0L;
		long blackKnights = 0L;
		long blackQueens = 0L;
		long blackKing = 0L;

		long currentPiece = 1;
		for (int i = 0; i < 64; i++) {
			switch (boardToCheck[i % 8][i / 8]) {
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

		return IsSelfCheck(whitePawns, whiteRooks, whiteKnights, whiteBishops,
				whiteQueens, whiteKing, blackPawns, blackRooks, blackKnights,
				blackBishops, blackQueens, blackKing, checkForWhite);

	}

}
