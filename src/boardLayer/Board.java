package boardLayer;

public class Board {

	private int rows;
	private int columns;

	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException(
					"Error creating the board: a board requires at least 1 row and 1 column to be created!");
		}

		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) {
			throw new BoardException("Error returning piece: position does not exists on the board!");
		}
		return pieces[row][column];
	}

	public Piece piece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Error returning piece: position does not exists on the board!");
		}
		return pieces[position.getRow()][position.getColumn()];
	}

	public void placePiece(Piece piece, Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Error placing piece: position does not exists on the board!");
		}
		
		if (thereIsAPiece(position)) {
			throw new BoardException("Error placing piece: there is a piece on the position already!");
		}
		
		piece.position = position;
		pieces[position.getRow()][position.getColumn()] = piece;
		;
	}

	public boolean positionExists(int row, int column) {
		return row < rows && row >= 0 && column < columns && column >= 0;
	}

	public boolean positionExists(Position position) {
		return position.getRow() < rows && position.getRow() >= 0 && position.getColumn() < columns
				&& position.getColumn() >= 0;
	}

	public boolean thereIsAPiece(Position position) {
		return piece(position.getRow(), position.getColumn()) != null;
	}

	@Override
	public String toString() {
		return "Board [rows=" + rows + ", columns=" + columns + "]";
	}
}
