package boardLayer;

public abstract class Piece {

	protected Position position;

	private Board board;

	public Piece(Board board) {
		this.board = board;
	}

	public Position getPosition() {
		return position;
	}

	protected Board getBoard() {
		return board;
	}

	@Override
	public String toString() {
		return "Piece [position=" + position + "]";
	}
}
