package applicationLayer;

import java.util.Scanner;

import chessLayer.ChessMatch;
import chessLayer.ChessPiece;
import chessLayer.ChessPosition;

public class Program {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();

		boolean rollingGame = true;
		while (rollingGame) {

			UI.printBoard(chessMatch.getPieces());

			System.out.println();
			System.out.print("Peca: ");
			ChessPosition source = UI.readChessPosition(scanner);

			System.out.print("Posicao: ");
			ChessPosition target = UI.readChessPosition(scanner);

			ChessPiece chessPiece = chessMatch.performChessMove(source, target);
		}
		
		scanner.close();
	}
}
