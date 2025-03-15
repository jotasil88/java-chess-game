package chessLayer;

import boardLayer.Board;
import boardLayer.Piece;
import chessLayer.pieces.King;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}

		return mat;
	}

	private void initialSetup() {
		placeNewPiece(new King(board, Color.WHITE), 'e', 5);
		placeNewPiece(new King(board, Color.WHITE), 'd', 5);
		placeNewPiece(new King(board, Color.WHITE), 'g', 5);
	}

	private void placeNewPiece(Piece piece, char column, int row) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

}