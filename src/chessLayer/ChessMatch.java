package chessLayer;

import boardLayer.Board;
import boardLayer.Piece;
import chessLayer.pieces.King;
import chessLayer.pieces.Rook;

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
		placeNewPiece(new King(board, Color.WHITE), 'd', 1);
		placeNewPiece(new King(board, Color.BLACK), 'd', 8);
		placeNewPiece(new Rook(board, Color.WHITE), 'c', 1);
		placeNewPiece(new Rook(board, Color.WHITE), 'c', 2);
		placeNewPiece(new Rook(board, Color.WHITE), 'd', 2);
		placeNewPiece(new Rook(board, Color.WHITE), 'e', 2);
		placeNewPiece(new Rook(board, Color.WHITE), 'e', 1);
		placeNewPiece(new Rook(board, Color.BLACK), 'c', 8);
		placeNewPiece(new Rook(board, Color.BLACK), 'c', 7);
		placeNewPiece(new Rook(board, Color.BLACK), 'd', 7);
		placeNewPiece(new Rook(board, Color.BLACK), 'e', 8);
		placeNewPiece(new Rook(board, Color.BLACK), 'e', 7);
	}

	private void placeNewPiece(Piece piece, char column, int row) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

}