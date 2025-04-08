package chessLayer;

import boardLayer.Position;

public class ChessPosition {

	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Error creating ChessPosition: only positions from a1 to h8 are on the board!");
		}
		
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	protected static ChessPosition toChessPosition(Position position) {
		return new ChessPosition(((char) (position.getColumn() + 'a')), 8 - position.getRow());
	}

	@Override
	public String toString() {
		return "ChessPosition [column=" + column + ", row=" + row + "]";
	}
}
