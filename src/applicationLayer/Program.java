package applicationLayer;

import java.util.InputMismatchException;
import java.util.Scanner;

import chessLayer.ChessException;
import chessLayer.ChessMatch;
import chessLayer.ChessPosition;

public class Program {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();

		while (!chessMatch.isCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch);

				System.out.println();
				System.out.print("Peca: ");
				ChessPosition source = UI.readChessPosition(scanner);

				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), chessMatch.possibleMoves(source));
				System.out.println();
				System.out.print("Posicao: ");
				ChessPosition target = UI.readChessPosition(scanner);

				chessMatch.performChessMove(source, target);
			} catch (ChessException | InputMismatchException e) {
				UI.clearScreen();
				System.out.println();
				System.out.println();
				System.out.println(e.getMessage() + "Aperte qualquer tecla para voltar ao jogo!");
				scanner.nextLine();
			}
		}

		UI.clearScreen();
		UI.printMatch(chessMatch);

		scanner.close();
	}
}
