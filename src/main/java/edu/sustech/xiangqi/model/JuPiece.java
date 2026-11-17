package edu.sustech.xiangqi.model;

public class JuPiece extends Piece{
    public JuPiece(boolean isRed) {super(isRed ? "俥" : "車", isRed);}
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol){
        //只能横着走或者竖着走，不能斜着
        if(!(toRow - fromRow == 0 || toCol - fromCol == 0)){
            return false;

        }
        return board.waycount(fromRow, toRow, fromCol, toCol) == 0;
    }
}
