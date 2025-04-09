package chessLayer;

import boardLayer.Board;
import boardLayer.Piece;
import boardLayer.Position;

public abstract class ChessPiece extends Piece {

	private Color color;
	private int moveCount;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public ChessPosition getChessPosition() {
		return ChessPosition.toChessPosition(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = ((ChessPiece) getBoard().piece(position));
		return p != null && p.color != color;
	}
	
	protected void increaseMoveCount() {
		moveCount++;
	}

	protected void decreaseMoveCount() {
		moveCount--;
	}
}
