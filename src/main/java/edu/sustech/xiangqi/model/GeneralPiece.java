package edu.sustech.xiangqi.model;

// 将帅：九宫内直走一格（红方帅在行7~9，黑方将在行0~2）

public class GeneralPiece extends Piece {
    public GeneralPiece(boolean isRed) {
        super(isRed ? "帥" : "將", isRed);
    }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        int dr = Math.abs(toRow - fromRow);
        int dc = Math.abs(toCol - fromCol);

        // 直走一格：行差和列差中，恰好一个是1，另一个是0
        if (!((dr == 1 && dc == 0) || (dr == 0 && dc == 1))) {
            return false;
        }

        // 不出九宫：红方九宫在行7~9，黑方九宫在行0~2
        if (isRed()) {
            return toRow >= 7 && toRow <= 9 && toCol >= 3 && toCol <= 5;
        }
        return toRow >= 0 && toRow <= 2 && toCol >= 3 && toCol <= 5;
    }
}
