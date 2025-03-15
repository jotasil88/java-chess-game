package chessLayer;

import boardLayer.Board;
import boardLayer.Position;
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
		board.placePiece(new King(board, Color.WHITE), new Position(0, 0));
		board.placePiece(new King(board, Color.WHITE), new Position(1, 0));
	}
}