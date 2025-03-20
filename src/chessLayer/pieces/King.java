package chessLayer.pieces;

import boardLayer.Board;
import boardLayer.Position;
import chessLayer.ChessPiece;
import chessLayer.Color;

public class King extends ChessPiece {

	public King(Board board, Color color) {
		super(board, color);
	}

	@Override
	public boolean[][] possibleMoves() {
		
		Position testPosition = new Position(position.getRow(), position.getColumn());
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		return mat;
	}
	
	@Override
	public String toString() {
		return "K";
	}
}
