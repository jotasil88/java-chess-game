package chessLayer;

import java.util.ArrayList;
import java.util.List;

import boardLayer.Board;
import boardLayer.Piece;
import boardLayer.Position;
import chessLayer.pieces.King;
import chessLayer.pieces.Rook;

public class ChessMatch {

	private int turn = 1;
	private Color currentPlayer = Color.WHITE;
	private boolean check;
	private List<ChessPiece> capturedPieces = new ArrayList<ChessPiece>();
	private List<ChessPiece> piecesOnTheBoard = new ArrayList<ChessPiece>();

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean isCheck() {
		return check;
	}

	public List<ChessPiece> getCapturedPieces() {
		return capturedPieces;
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

	public void performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		ChessPiece capturedPiece = makeMove(source, target);
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voce nao pode se colocar em cheque!");
		}
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		nextTurn();
	}

	private ChessPiece makeMove(Position source, Position target) {
		Piece movingPiece = board.removePiece(source);
		ChessPiece capturedPiece = (ChessPiece) board.removePiece(target);
		board.placePiece(movingPiece, target);

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}

	private void undoMove(Position source, Position target, ChessPiece capturedPiece) {
		Piece movingPiece = board.removePiece(target);
		board.placePiece(movingPiece, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}

	private void validateSourcePosition(Position source) {
		if (!board.positionExists(source)) {
			throw new ChessException("position does not exists on the board!");
		}

		if (!board.thereIsAPiece(source)) {
			throw new ChessException("Nao existe uma peca na posicao informada!");
		}

		if (getCurrentPlayer() != ((ChessPiece) board.piece(source)).getColor()) {
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

	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<ChessPiece> king = piecesOnTheBoard.stream().filter(x -> x.getColor() == color)
				.filter(King.class::isInstance).toList();

		if (king.getFirst() != null) {
			return king.getFirst();
		}

		throw new IllegalStateException("Error locating King!");
	}
	
	private boolean testCheck(Color color) {
		ChessPiece king = king(color);
		List<ChessPiece> opponentPieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == opponent(color)).toList();
		for (ChessPiece chessPiece : opponentPieces) {
			if (chessPiece.possibleMove(king.getChessPosition().toPosition())) {
				return true;
			}
		}
		return false;
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
		piecesOnTheBoard.add((ChessPiece) piece);
	}

}