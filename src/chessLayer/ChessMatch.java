package chessLayer;

import java.util.ArrayList;
import java.util.List;

import boardLayer.Board;
import boardLayer.Piece;
import boardLayer.Position;
import chessLayer.pieces.Bishop;
import chessLayer.pieces.King;
import chessLayer.pieces.Knight;
import chessLayer.pieces.Pawn;
import chessLayer.pieces.Queen;
import chessLayer.pieces.Rook;

public class ChessMatch {

	private int turn = 1;
	private Color currentPlayer = Color.WHITE;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
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

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean isCheck() {
		return check;
	}

	public boolean isCheckMate() {
		return checkMate;
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
		check = (testCheck(opponent(currentPlayer)));

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = testCheckMate(opponent(currentPlayer));
		} else {
			nextTurn();
		}

		ChessPiece movingPiece = (ChessPiece) board.piece(target);
		if (movingPiece instanceof Pawn && target.getRow() == source.getRow() - 2
				|| target.getRow() == source.getRow() + 2) {
			enPassantVulnerable = movingPiece;
		} else {
			enPassantVulnerable = null;
		}
	}

	private ChessPiece makeMove(Position source, Position target) {
		ChessPiece movingPiece = (ChessPiece) board.removePiece(source);
		ChessPiece capturedPiece = (ChessPiece) board.removePiece(target);
//		en passant
		if (movingPiece instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position p = enPassantVulnerable.getChessPosition().toPosition();
				capturedPiece = (ChessPiece) board.removePiece(p);
			}
		}

		board.placePiece(movingPiece, target);
		movingPiece.increaseMoveCount();

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		if (movingPiece instanceof King && target.getColumn() == source.getColumn() + 2) {
			ChessPiece rook = (ChessPiece) board.removePiece(new Position(source.getRow(), source.getColumn() + 3));
			board.placePiece(rook, new Position(source.getRow(), source.getColumn() + 1));
			rook.increaseMoveCount();
		}

		if (movingPiece instanceof King && target.getColumn() == source.getColumn() - 2) {
			ChessPiece rook = (ChessPiece) board.removePiece(new Position(source.getRow(), source.getColumn() - 4));
			board.placePiece(rook, new Position(source.getRow(), source.getColumn() - 1));
			rook.increaseMoveCount();
		}

		return capturedPiece;
	}

	private void undoMove(Position source, Position target, ChessPiece capturedPiece) {
		ChessPiece movingPiece = (ChessPiece) board.removePiece(target);
		board.placePiece(movingPiece, source);
		movingPiece.decreaseMoveCount();

		if (capturedPiece != null) {
//			enPassant
			if (movingPiece instanceof Pawn && capturedPiece == enPassantVulnerable) {
				Position enPassantTarget = new Position(source.getRow(), target.getColumn());
				board.placePiece(capturedPiece, enPassantTarget);
			} else {
				board.placePiece(capturedPiece, target);
			}
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		if (movingPiece instanceof King && target.getColumn() == source.getColumn() + 2) {
			ChessPiece rook = (ChessPiece) board.removePiece(new Position(source.getRow(), source.getColumn() + 1));
			board.placePiece(rook, new Position(source.getRow(), source.getColumn() + 3));
			rook.decreaseMoveCount();
		}

		if (movingPiece instanceof King && target.getColumn() == source.getColumn() - 2) {
			ChessPiece rook = (ChessPiece) board.removePiece(new Position(source.getRow(), source.getColumn() - 1));
			board.placePiece(rook, new Position(source.getRow(), source.getColumn() - 4));
			rook.decreaseMoveCount();
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

		if (!board.piece(source).isThereAnyPossibleMove()) {
			throw new ChessException("Nao existe um movimento possivel para a peca na posicao informada!");
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
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<ChessPiece> opponentPieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == opponent(color))
				.toList();
		for (ChessPiece chessPiece : opponentPieces) {
			if (chessPiece.possibleMove(kingPosition)) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<ChessPiece> partnerPieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == color).toList();
		for (ChessPiece partnerPiece : partnerPieces) {
			boolean[][] mat = partnerPiece.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = partnerPiece.getChessPosition().toPosition();
						Position target = new Position(i, j);
						ChessPiece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);

						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private void initialSetup() {
		placeNewPiece(new King(board, Color.WHITE, this), 'e', 1);
		placeNewPiece(new Queen(board, Color.WHITE), 'd', 1);
		placeNewPiece(new Rook(board, Color.WHITE), 'a', 1);
		placeNewPiece(new Rook(board, Color.WHITE), 'h', 1);
		placeNewPiece(new Bishop(board, Color.WHITE), 'b', 1);
		placeNewPiece(new Bishop(board, Color.WHITE), 'g', 1);
		placeNewPiece(new Knight(board, Color.WHITE), 'c', 1);
		placeNewPiece(new Knight(board, Color.WHITE), 'f', 1);
		placeNewPiece(new Pawn(board, Color.WHITE, this), 'a', 2);
		placeNewPiece(new Pawn(board, Color.WHITE, this), 'b', 2);
		placeNewPiece(new Pawn(board, Color.WHITE, this), 'c', 2);
		placeNewPiece(new Pawn(board, Color.WHITE, this), 'd', 2);
		placeNewPiece(new Pawn(board, Color.WHITE, this), 'e', 2);
		placeNewPiece(new Pawn(board, Color.WHITE, this), 'f', 2);
		placeNewPiece(new Pawn(board, Color.WHITE, this), 'g', 2);
		placeNewPiece(new Pawn(board, Color.WHITE, this), 'h', 2);

		placeNewPiece(new King(board, Color.BLACK, this), 'e', 8);
		placeNewPiece(new Queen(board, Color.BLACK), 'd', 8);
		placeNewPiece(new Rook(board, Color.BLACK), 'a', 8);
		placeNewPiece(new Rook(board, Color.BLACK), 'h', 8);
		placeNewPiece(new Bishop(board, Color.BLACK), 'b', 8);
		placeNewPiece(new Bishop(board, Color.BLACK), 'g', 8);
		placeNewPiece(new Knight(board, Color.BLACK), 'c', 8);
		placeNewPiece(new Knight(board, Color.BLACK), 'f', 8);
		placeNewPiece(new Pawn(board, Color.BLACK, this), 'a', 7);
		placeNewPiece(new Pawn(board, Color.BLACK, this), 'b', 7);
		placeNewPiece(new Pawn(board, Color.BLACK, this), 'c', 7);
		placeNewPiece(new Pawn(board, Color.BLACK, this), 'd', 7);
		placeNewPiece(new Pawn(board, Color.BLACK, this), 'e', 7);
		placeNewPiece(new Pawn(board, Color.BLACK, this), 'f', 7);
		placeNewPiece(new Pawn(board, Color.BLACK, this), 'g', 7);
		placeNewPiece(new Pawn(board, Color.BLACK, this), 'h', 7);
	}

	private void placeNewPiece(Piece piece, char column, int row) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add((ChessPiece) piece);
	}

}