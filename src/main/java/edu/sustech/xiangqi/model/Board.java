package edu.sustech.xiangqi.model;

// 保存棋盘数据，并统一检查和执行移动

public class Board {
    // 定义棋盘大小
    public static final int ROWS = 10;
    public static final int COLS = 9;

    // 存储棋子位置
    private final Piece[][] pieces = new Piece[ROWS][COLS];

    public Board() {
        initializePieces();
    }

    private void initializePieces() {
        // ===== 黑方（上方，row 0~2）=====
        pieces[0][0] = new JuPiece(false);         // 車
        pieces[0][1] = new MaPiece(false);         // 马
        pieces[0][2] = new XiangPiece(false);      // 象
        pieces[0][3] = new AdvisorPiece(false);    // 士
        pieces[0][4] = new GeneralPiece(false);    // 将
        pieces[0][5] = new AdvisorPiece(false);    // 士
        pieces[0][6] = new XiangPiece(false);      // 象
        pieces[0][7] = new MaPiece(false);         // 马
        pieces[0][8] = new JuPiece(false);         // 車
        pieces[2][1] = new PaoPiece(false);        // 砲
        pieces[2][7] = new PaoPiece(false);        // 砲
        pieces[3][0] = new BingPiece(false);       // 卒
        pieces[3][2] = new BingPiece(false);
        pieces[3][4] = new BingPiece(false);
        pieces[3][6] = new BingPiece(false);
        pieces[3][8] = new BingPiece(false);

        // ===== 红方（下方，row 9~6）=====
        pieces[9][0] = new JuPiece(true);          // 俥
        pieces[9][1] = new MaPiece(true);          // 马
        pieces[9][2] = new XiangPiece(true);       // 相
        pieces[9][3] = new AdvisorPiece(true);     // 仕
        pieces[9][4] = new GeneralPiece(true);     // 帅
        pieces[9][5] = new AdvisorPiece(true);     // 仕
        pieces[9][6] = new XiangPiece(true);       // 相
        pieces[9][7] = new MaPiece(true);          // 马
        pieces[9][8] = new JuPiece(true);          // 俥
        pieces[7][1] = new PaoPiece(true);         // 炮
        pieces[7][7] = new PaoPiece(true);         // 炮
        pieces[6][0] = new BingPiece(true);        // 兵
        pieces[6][2] = new BingPiece(true);
        pieces[6][4] = new BingPiece(true);
        pieces[6][6] = new BingPiece(true);
        pieces[6][8] = new BingPiece(true);
    }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public Piece getPieceAt(int row, int col) {
        if (!isInside(row, col)) {
            return null;
        }
        return pieces[row][col];
    }

    public boolean tryMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isInside(fromRow, fromCol) || !isInside(toRow, toCol)) {
            return false;
        }
        if (fromRow == toRow && fromCol == toCol) {
            return false;
        }

        Piece piece = getPieceAt(fromRow, fromCol);
        if (piece == null) {
            return false;
        }

        // TODO: 加入回合检查，只允许当前一方移动自己的棋子

        // TODO: 区分己方占用与敌方棋子，并实现吃子
        if (getPieceAt(toRow, toCol) != null) {
            return false;
        }

        if (!piece.canMove(this, fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        // TODO: 检查走完后己方将帅是否安全
        pieces[fromRow][fromCol] = null;
        pieces[toRow][toCol] = piece;

        // TODO: 仅在成功移动后切换回合，并判断将军、胜负等状态
        return true;
    }
}
