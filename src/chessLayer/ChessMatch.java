package chessLayer;

import java.util.ArrayList;
import java.util.List;

import boardLayer.Board;
import boardLayer.Piece;
import boardLayer.Position;
import chessLayer.pieces.Bishop;
import chessLayer.pieces.King;
import chessLayer.pieces.Pawn;
import chessLayer.pieces.Rook;

public class ChessMatch {

	private int turn = 1;
	private Color currentPlayer = Color.WHITE;
	private boolean check;
	private boolean checkMate;
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
	}

	private ChessPiece makeMove(Position source, Position target) {
		ChessPiece movingPiece = (ChessPiece) board.removePiece(source);
		ChessPiece capturedPiece = (ChessPiece) board.removePiece(target);
		board.placePiece(movingPiece, target);
		movingPiece.increaseMoveCount();

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}

	private void undoMove(Position source, Position target, ChessPiece capturedPiece) {
		ChessPiece movingPiece = (ChessPiece) board.removePiece(target);
		board.placePiece(movingPiece, source);
		movingPiece.decreaseMoveCount();

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
		placeNewPiece(new King(board, Color.WHITE), 'e', 1);
		placeNewPiece(new Rook(board, Color.WHITE), 'a', 1);
		placeNewPiece(new Rook(board, Color.WHITE), 'h', 1);
		placeNewPiece(new Bishop(board, Color.WHITE), 'b', 1);
		placeNewPiece(new Bishop(board, Color.WHITE), 'g', 1);
		placeNewPiece(new Pawn(board, Color.WHITE), 'a', 2);
		placeNewPiece(new Pawn(board, Color.WHITE), 'b', 2);
		placeNewPiece(new Pawn(board, Color.WHITE), 'c', 2);
		placeNewPiece(new Pawn(board, Color.WHITE), 'd', 2);
		placeNewPiece(new Pawn(board, Color.WHITE), 'e', 2);
		placeNewPiece(new Pawn(board, Color.WHITE), 'f', 2);
		placeNewPiece(new Pawn(board, Color.WHITE), 'g', 2);
		placeNewPiece(new Pawn(board, Color.WHITE), 'h', 2);
		
		placeNewPiece(new King(board, Color.BLACK), 'e', 8);
		placeNewPiece(new Rook(board, Color.BLACK), 'a', 8);
		placeNewPiece(new Rook(board, Color.BLACK), 'h', 8);
		placeNewPiece(new Bishop(board, Color.BLACK), 'b', 8);
		placeNewPiece(new Bishop(board, Color.BLACK), 'g', 8);
		placeNewPiece(new Pawn(board, Color.BLACK), 'a', 7);
		placeNewPiece(new Pawn(board, Color.BLACK), 'b', 7);
		placeNewPiece(new Pawn(board, Color.BLACK), 'c', 7);
		placeNewPiece(new Pawn(board, Color.BLACK), 'd', 7);
		placeNewPiece(new Pawn(board, Color.BLACK), 'e', 7);
		placeNewPiece(new Pawn(board, Color.BLACK), 'f', 7);
		placeNewPiece(new Pawn(board, Color.BLACK), 'g', 7);
		placeNewPiece(new Pawn(board, Color.BLACK), 'h', 7);
	}

	private void placeNewPiece(Piece piece, char column, int row) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add((ChessPiece) piece);
	}

}