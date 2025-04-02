package chessLayer;

import boardLayer.Board;
import boardLayer.Piece;
import boardLayer.Position;
import chessLayer.pieces.King;
import chessLayer.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	
	private Board board;

	public ChessMatch() {
		turn = 1;
		currentPlayer = Color.WHITE;
		board = new Board(8, 8);
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
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

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position p = sourcePosition.toPosition();
		validateSourcePosition(p);
		return board.piece(p).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		ChessPiece capturedPiece = makeMove(source, target);
		nextTurn();
		return capturedPiece;
	}

	private ChessPiece makeMove(Position source, Position target) {
		Piece movingPiece = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(movingPiece, target);
		return (ChessPiece) capturedPiece;
	}

	private void validateSourcePosition(Position source) {
		if (!board.positionExists(source)) {
			throw new ChessException("position does not exists on the board!");
		}

		if (!board.thereIsAPiece(source)) {
			throw new ChessException("Nao existe uma peca na posicao informada!");
		}
		
		if (getCurrentPlayer() != ((ChessPiece)board.piece(source)).getColor()) {
			throw new ChessException("Essa peca nao e sua!");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.positionExists(target)) {
			throw new ChessException("position does not exists on the board!");
		}
	
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("Esse movimento não é possível!");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private void initialSetup() {
		placeNewPiece(new King(board, Color.WHITE), 'd', 5);
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