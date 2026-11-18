package edu.sustech.xiangqi.model;

import javax.swing.*;

public class PaoPiece extends Piece {
    public PaoPiece(boolean isRed) {
        super(isRed ? "炮" : "砲", isRed);
    }

    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        if (!(toRow - fromRow == 0 || toCol - fromCol == 0)) {
            return false;
        }
        if(board.waycount(fromRow, toRow, fromCol, toCol) == 0 && board.getPieceAt(toRow ,toCol) == null){
            return true;
        }
        return (board.waycount(fromRow, toRow, fromCol, toCol) == 1 && board.getPieceAt(toRow ,toCol) != null);
    }
}
