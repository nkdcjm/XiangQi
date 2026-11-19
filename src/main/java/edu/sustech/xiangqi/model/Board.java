package edu.sustech.xiangqi.model;

// 保存棋盘数据，并统一检查和执行移动

public class Board {
    // 定义棋盘大小
    public static final int ROWS = 10;
    public static final int COLS = 9;

    //判断是否游戏结束
    public static boolean gameover = false;
    //判断哪一方胜利
    public static boolean redwin = false;

    // 存储棋子位置
    private final Piece[][] pieces = new Piece[ROWS][COLS];
    //红方先行
    private boolean redToMove = true;
    //判断是否是红方的回合
    public boolean isRedToMove() {
        return redToMove;
    }

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
    public int waycount (int fromRow , int toRow, int fromCol, int toCol){
        int count = 0;
        int n = 0;
        if(fromRow == toRow){
            n = Math.abs(fromCol - toCol);
            for (int i = 0; i < n-1; i++) {
                if(toCol > fromCol) {
                    if (getPieceAt(fromRow, fromCol + i + 1) != null) {
                        count++;
                    }
                }
                if(toCol < fromCol) {
                    if (getPieceAt(fromRow, fromCol - i - 1) != null) {
                        count++;
                    }
                }

            }
        }
        if(fromCol == toCol){
            n = Math.abs(fromRow - toRow);
            for (int i = 0; i < n-1; i++) {
                if(toRow > fromRow) {
                    if (getPieceAt(fromRow + i + 1, fromCol) != null) {
                        count++;
                    }
                }
                if(toRow < fromRow) {
                    if (getPieceAt(fromRow - i - 1, fromCol) != null) {
                        count++;
                    }
                }
            }
        }
        return count;

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

        if(gameover){

            return false;
        }

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
        if(piece.isRed() != redToMove){
            return false;
        }
        // TODO: 加入回合检查，只允许当前一方移动自己的棋子

        // TODO: 区分己方占用与敌方棋子，并实现吃子
        //只拦截吃自己的棋子，敌方棋子允许通过
        Piece targetPiece = getPieceAt(toRow, toCol);
        if (targetPiece != null && targetPiece.isRed() == piece.isRed()) {
            return false; // 目标是自己人，拦截
        }

        if (!piece.canMove(this, fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        // TODO: 检查走完后己方将帅是否安全
        pieces[fromRow][fromCol] = null;
        pieces[toRow][toCol] = piece;
        redToMove = !redToMove;
//如果目标是某一个将或者帅，那么游戏会结束，并打印结果
        if (targetPiece instanceof GeneralPiece){
            pieces[fromRow][fromCol] = null;
            pieces[toRow][toCol] = piece;
            if(!redToMove){
                redwin = true;
                System.out.println("红方胜利了");
            }
            else {
                redwin = false;
                System.out.println("黑方胜利了");
            }
            gameover = true;
        }

        // TODO: 仅在成功移动后切换回合，并判断将军、胜负等状态
        return true;
    }
}
