package applicationLayer;

import boardLayer.Board;
import boardLayer.Position;

public class Program {

	public static void main(String[] args) {

		Position p = new Position(0, 0);
		Board board = new Board(0, 0);
		
		System.out.println(board);
	}
}
