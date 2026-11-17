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
        return board.waycount(fromRow, toRow, fromCol, toCol) == 1;
    }
}
