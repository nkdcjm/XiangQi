package edu.sustech.xiangqi.controller;

import edu.sustech.xiangqi.model.Board;
import edu.sustech.xiangqi.model.Piece;
import edu.sustech.xiangqi.view.BoardPanel;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 处理点击和选中状态
public class ChessController extends MouseAdapter {
    private final Board board;
    private final BoardPanel boardPanel;

    // 默认 -1 没有选中任何棋子
    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessController(Board board, BoardPanel boardPanel) {
        this.board = board;
        this.boardPanel = boardPanel;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (!SwingUtilities.isLeftMouseButton(event)) {
            return;
        }

        Point position = boardPanel.getIntersectionAt(event.getX(), event.getY());
        if (position != null) {
            handleClick(position.y, position.x);
        }
    }

    private void handleClick(int row, int col) {
        // 如果还没选中任何棋子
        if (selectedRow == -1) {
            if (board.getPieceAt(row, col) != null && board.getPieceAt(row, col).isRed() == board.isRedToMove()) {
                select(row, col);
            }
            return;
        }

        Piece selectedPiece = board.getPieceAt(selectedRow, selectedCol);
        Piece clickedPiece = board.getPieceAt(row, col);

        // 如果点的是另一个己方棋子，直接换选，不要走 tryMove！
        if (clickedPiece != null && selectedPiece != null &&
                clickedPiece.isRed() == selectedPiece.isRed()) {
            select(row, col);
            return;
        }

        // 如果点的是空地，或者敌方棋子，才尝试移动
        if (board.tryMove(selectedRow, selectedCol, row, col)) {
            clearSelection();
            boardPanel.repaint();
        }
    }

    private void select(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        boardPanel.showSelection(row, col);
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        boardPanel.showSelection(-1, -1);
    }
}
