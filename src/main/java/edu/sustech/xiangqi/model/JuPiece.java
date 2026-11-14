package edu.sustech.xiangqi.model;

public class JuPiece extends Piece{
    public JuPiece(boolean isRed) {super(isRed ? "俥" : "車", isRed);}
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol){
        int count = 0 ;
        if(!(toRow - fromRow == 0 || toCol - fromCol == 0)){
            return false;

        }
        while(true){
            if(toCol == fromCol){
            if(board.getPieceAt(fromRow + count + 1,toCol) != null)
                break;
            else count ++;}
            else if (fromRow == toRow) {
                if(board.getPieceAt(fromRow , toCol + count + 1) != null){
                    break;
                }
                else count++;
            }
        }
        if(fromRow == toRow && !(toRow - fromRow == 0 || toCol - fromCol == 0 && Math.abs(fromCol - toCol )< count)){
            count = 0;
            return false;
        }if(fromCol == toCol && !(toRow - fromRow == 0 || toCol - fromCol == 0 && Math.abs(fromRow - toRow)< count)){
            count = 0;
            return false;
        }
        return true;
    }
}
