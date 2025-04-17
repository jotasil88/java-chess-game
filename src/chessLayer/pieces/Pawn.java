package chessLayer.pieces;

import boardLayer.Board;
import boardLayer.Position;
import chessLayer.ChessMatch;
import chessLayer.ChessPiece;
import chessLayer.Color;

public class Pawn extends ChessPiece {

	private ChessMatch chessMatch;

	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		Position p = new Position();
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		if (getColor() == Color.WHITE) {
			p.setValues(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setValues(p.getRow() - 1, position.getColumn());
				if (getBoard().positionExists(p) && getMoveCount() == 0 && !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
			}
//		west
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
//		east
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
//		right en passant
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && canEnPassant(p)) {
				mat[p.getRow()][p.getColumn()] = true; 
			}
//		left en passant
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && canEnPassant(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

		} else {
			p.setValues(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setValues(p.getRow() + 1, position.getColumn());
				if (getBoard().positionExists(p) && getMoveCount() == 0 && !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
			}
//		west
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
//		east
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

//			right en passant
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && canEnPassant(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

//			left en passant
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && canEnPassant(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}

		return mat;
	}

	private boolean canEnPassant(Position p) {
		Position pTest = new Position(position.getRow(), p.getColumn());

		return isThereOpponentPiece(pTest) && chessMatch.getEnPassantVulnerable() == getBoard().piece(pTest);
	}

	@Override
	public String toString() {
		return "P";
	}

}
