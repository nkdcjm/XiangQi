package edu.sustech.xiangqi.model;

public class MaPiece extends Piece {
    public MaPiece(boolean isRed) {super("馬", isRed);}
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        int colDiff = Math.abs(toCol - fromCol);
        int rowDiff = Math.abs(fromRow - toRow);

        // 必须是日字：行差2列差1，或者行差1列差2
        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false;
        }

        // 蹩马腿：马腿那一格不能有棋子
        int legRow = fromRow;
        int legCol = fromCol;
        if (rowDiff == 2) {
            // 纵跨两格，先竖着走一步，马腿在正上方或正下方
            legRow = fromRow + (toRow > fromRow ? 1 : -1);
        } else {
            // 横跨两格，先横着走一步，马腿在左边或右边
            legCol = fromCol + (toCol > fromCol ? 1 : -1);
        }
        return board.getPieceAt(legRow, legCol) == null;
    }
}
