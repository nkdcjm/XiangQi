package edu.sustech.xiangqi.model;

public class XiangPiece extends Piece {
    public XiangPiece(boolean isRed) {super(isRed ? "相" : "象", isRed);}
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol){
        if(isRed()){
            if(!(Math.abs(toCol - fromCol) == 2 && Math.abs(fromRow - toRow)== 2)){
                return false;
            }
            return toRow >= 5;
        }
        if(!isRed()){
            if(!(Math.abs(toCol - fromCol) == 2 && Math.abs(fromRow - toRow)== 2)){
                return false;
            }
            return toRow < 5;
        }
        return true;
    }
}
