package applicationLayer;

import java.util.InputMismatchException;
import java.util.Scanner;

import chessLayer.ChessException;
import chessLayer.ChessMatch;
import chessLayer.ChessPiece;
import chessLayer.ChessPosition;

public class Program {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		boolean rollingGame = true;

		while (rollingGame) {
			try {
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces());

				System.out.println();
				System.out.print("Peca: ");
				ChessPosition source = UI.readChessPosition(scanner);

				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				
				System.out.print("Posicao: ");
				ChessPosition target = UI.readChessPosition(scanner);

				ChessPiece chessPiece = chessMatch.performChessMove(source, target);
			} catch (ChessException | InputMismatchException e) {
				UI.clearScreen();
				System.out.println(e.getMessage() + "\n\nAperte qualquer tecla para voltar ao jogo!");
				scanner.nextLine();
			}
		}

		scanner.close();
	}
}
