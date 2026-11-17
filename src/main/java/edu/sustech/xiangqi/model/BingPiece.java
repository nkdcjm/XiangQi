package edu.sustech.xiangqi.model;

public class BingPiece extends Piece{
    public BingPiece(boolean isRed) {super(isRed ? "兵" : "卒", isRed);}
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        if (isRed()) {
            if (fromRow >= 5 && !(Math.abs(toCol - fromCol) == 0 && fromRow - toRow == 1)) {
                return false;
            }
            if (fromRow < 5 && !(Math.abs(toCol - fromCol) == 1 && toRow == fromRow ||
                    (fromRow - toRow == 1 && toCol == fromCol))) {
                return false;
            }
        }
        if (!isRed()) {
            if (fromRow < 5 && !(Math.abs(toCol - fromCol) == 0 && fromRow - toRow == -1)) {
                return false;
            }
            if (fromRow >= 5 && !(Math.abs(toCol - fromCol) == 1 && toRow == fromRow ||
                    (fromRow - toRow == 1 && toCol == fromCol))) {
                return false;
            }

        }return true;
    }
}
