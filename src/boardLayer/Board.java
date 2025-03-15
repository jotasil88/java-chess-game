package boardLayer;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private int rows;
	private int columns;
	
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRow() {
		return rows;
	}

	public int getColumn() {
		return columns;
	}

	@Override
	public String toString() {
		return "Board [rows=" + rows + ", columns=" + columns + "]";
	}
}
