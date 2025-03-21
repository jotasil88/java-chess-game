package chessLayer;

import boardLayer.Board;
import boardLayer.BoardException;
import boardLayer.Piece;
import boardLayer.Position;
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
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		return makeMove(source, target);
	}
	
	private ChessPiece makeMove(Position source, Position target) {
		Piece movingPiece = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(movingPiece, target);
		return (ChessPiece) capturedPiece;
	}
	
	private void validateSourcePosition(Position source) {
		if (!board.positionExists(source)) {
			throw new BoardException("position does not exists on the board!");
		}
		
		if (!board.thereIsAPiece(source)) {
			throw new BoardException("Nao existe uma peca na posicao informada!");
		}
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