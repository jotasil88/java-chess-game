package chessLayer.pieces;

import boardLayer.Board;
import boardLayer.Position;
import chessLayer.ChessMatch;
import chessLayer.ChessPiece;
import chessLayer.Color;

public class King extends ChessPiece {

	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		Position p = new Position();
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
//		up
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
//		below
		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
//		left
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
//		right
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
//		north-west
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
//		north-east
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
//		south-east
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
//		south-west
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
//		castling
		p.setValues(position.getRow(), position.getColumn() + 2);
		if (getMoveCount() == 0 && !chessMatch.isCheck() && canKingSideCastling()) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		p.setValues(position.getRow(), position.getColumn() - 2);
		if (getMoveCount() == 0 && !chessMatch.isCheck() && canKingSideCastling()) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		return mat;
	}

	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}

	private boolean canKingSideCastling() {
		boolean rookCanCast = (getBoard().piece(position.getRow(), position.getColumn() + 3)) != null
				&& ((ChessPiece) getBoard().piece(position.getRow(), position.getColumn() + 3)).getMoveCount() == 0;
		;

		return rookCanCast && !getBoard().thereIsAPiece(new Position(position.getRow(), position.getColumn() + 1))
				&& !getBoard().thereIsAPiece(new Position(position.getRow(), position.getColumn() + 2));

	}

	private boolean canQueenSideCastling() {
		boolean rookCanCast = (getBoard().piece(position.getRow(), position.getColumn() + 4)) != null
				&& ((ChessPiece) getBoard().piece(position.getRow(), position.getColumn() + 4)).getMoveCount() == 0;

		return rookCanCast && !getBoard().thereIsAPiece(new Position(position.getRow(), position.getColumn() + 1))
				&& !getBoard().thereIsAPiece(new Position(position.getRow(), position.getColumn() + 2))
				&& !getBoard().thereIsAPiece(new Position(position.getRow(), position.getColumn() + 3));

	}

	@Override
	public String toString() {
		return "K";
	}
}
