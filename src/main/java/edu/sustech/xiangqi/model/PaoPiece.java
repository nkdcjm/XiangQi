package edu.sustech.xiangqi.model;

public class PaoPiece extends Piece{
    public PaoPiece(boolean isRed) {super(isRed ? "炮" : "砲", isRed);}
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol){
        return false;
    }
}
