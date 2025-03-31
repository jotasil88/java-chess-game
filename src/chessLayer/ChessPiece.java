package chessLayer;

import boardLayer.Board;
import boardLayer.Piece;
import boardLayer.Position;

public abstract class ChessPiece extends Piece {

	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = ((ChessPiece) getBoard().piece(position));
		return p != null && p.color != color;
	}

	public Color getColor() {
		return color;
	}
}
