package edu.sustech.xiangqi.view;

import edu.sustech.xiangqi.model.Board;
import edu.sustech.xiangqi.model.Piece;

import javax.swing.*;
import java.awt.*;

// 绘制棋盘、棋子和选中效果
public final class BoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
//    private static final int CELL_SIZE = 52;
//    private static final int MARGIN = 36;
//    private static final int PIECE_RADIUS = 21;
//    private static final int HIT_RADIUS = 23;
    //成员变量 动态修改
    private int CELL_SIZE = 52;
    private int MARGIN = 36;
    private int PIECE_RADIUS = 21;
    private int HIT_RADIUS = 23;

    private final Board board;
    private int highlightedRow = -1;
    private int highlightedCol = -1;

    public JLabel label;//点中棋子的面板展现
    public BoardPanel(Board board) {
        this.board = board;
        setBackground(new Color(239, 218, 179));
//        setPreferredSize(new Dimension(   //固定面板大小
//                MARGIN * 2 + CELL_SIZE * (Board.COLS - 1),
//                MARGIN * 2 + CELL_SIZE * (Board.ROWS - 1)));
    }

    // 返回交叉点坐标
    public Point getIntersectionAt(int x, int y) {
        //判断行列，进行点击
        int col = (int) Math.round((x - screenX(0)) / (double) CELL_SIZE);
        int row = (int) Math.round((y - screenY(0)) / (double) CELL_SIZE);
        if (!board.isInside(row, col)) {
            return null;
        }

        int dx = x - screenX(col);
        int dy = y - screenY(row);
        if (dx * dx + dy * dy > HIT_RADIUS * HIT_RADIUS) {
            return null;
        }
        return new Point(col, row);
    }

    public void showSelection(int row, int col) {
        highlightedRow = row;
        highlightedCol = col;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. 加载并绘制木纹背景图
        Image bg = new ImageIcon("board_bg.jpg").getImage();
        if (bg != null && bg.getWidth(null) > 0) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            // 加入半透明黑色遮罩
            // 把整体亮度压暗，凸显棋盘线
            g.setColor(new Color(0, 0, 0, 100)); // 100是透明度，觉得暗可以改小
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // 2. 画棋盘线（你原本的代码）
        drawBoard(g);

        // 3. 画棋子（你原本的代码）
        drawPieces(g);

        g.dispose();
    }

    private void drawBoard(Graphics2D g) {
        // 1. 把线改成亮金色（RGB: 255, 215, 0），带一点点透明度让它融入背景
        g.setColor(new Color(255, 215, 0, 220));

        // 2. 把线条加粗到 2.0f（原本是 1.5f），这样在复杂的背景上更显眼
        g.setStroke(new BasicStroke(2.0f));

        for (int row = 0; row < Board.ROWS; row++) {
            drawGridLine(g, row, 0, row, Board.COLS - 1);
        }
        for (int col = 0; col < Board.COLS; col++) {
            if (col == 0 || col == Board.COLS - 1) {
                drawGridLine(g, 0, col, Board.ROWS - 1, col);
            } else {
                drawGridLine(g, 0, col, 4, col);
                drawGridLine(g, 5, col, Board.ROWS - 1, col);
            }
        }

        // 上下九宫各有两条对角线。
        drawGridLine(g, 0, 3, 2, 5);
        drawGridLine(g, 0, 5, 2, 3);
        drawGridLine(g, 7, 3, 9, 5);
        drawGridLine(g, 7, 5, 9, 3);

        g.setFont(new Font(Font.SERIF, Font.BOLD, 23));
        int riverY = (screenY(4) + screenY(5)) / 2;
        drawCenteredText(g, "楚 河", screenX(2), riverY);
        drawCenteredText(g, "汉 界", screenX(6), riverY);
    }

    private void drawPieces(Graphics2D g) {
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece == null) {
                    continue;
                }

                int x = screenX(col);
                int y = screenY(row);

                // 你原本的高亮选中代码
                if (row == highlightedRow && col == highlightedCol) {
                    g.setColor(new Color(31, 120, 119));
                    g.setStroke(new BasicStroke(3));
                    int radius = PIECE_RADIUS + 5;
                    g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
                }

                // 1. 画棋子阴影
                // 向右下角偏移 3 像素，半透明黑色，瞬间拥有立体感
                g.setColor(new Color(0, 0, 0, 100));
                g.fillOval(x - PIECE_RADIUS + 3, y - PIECE_RADIUS + 3, PIECE_RADIUS * 2, PIECE_RADIUS * 2);

                // 2. 画棋子白底（改为米黄色木头）
                g.setColor(new Color(250, 240, 220)); // 柔和的白
                g.fillOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);

                // 3. 画棋子边框（改粗变深）
                g.setColor(new Color(120, 79, 45)); // 深木色
                g.setStroke(new BasicStroke(2));
                g.drawOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);

                // 4. 画字（加大加粗，换楷体）
                g.setColor(piece.isRed() ? new Color(180, 30, 30) : new Color(30, 30, 30)); // 红方暗红，黑方纯黑
                g.setFont(new Font("楷体", Font.BOLD, 28)); // 换成了楷体，更有古风
                drawCenteredText(g, piece.getName(), x, y);
            }
        }
    }

    private void drawGridLine(Graphics2D g, int fromRow, int fromCol, int toRow, int toCol) {
        g.drawLine(screenX(fromCol), screenY(fromRow), screenX(toCol), screenY(toRow));
    }

    private void drawCenteredText(Graphics2D g, String text, int x, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int baseline = y + (metrics.getAscent() - metrics.getDescent()) / 2;
        g.drawString(text, x - metrics.stringWidth(text) / 2, baseline);
    }

//    private int screenX(int col) {
//        return MARGIN + col * CELL_SIZE;
//    }
//
//    private int screenY(int row) {
//        return MARGIN + row * CELL_SIZE;
//    }
    // 新增这个方法，用来实时计算当前的缩放比例和居中位置
    private void updateLayout() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        // 棋盘实际跨度：8个格子宽，9个格子高
        int cols = Board.COLS - 1;
        int rows = Board.ROWS - 1;

        // 计算格子大小：留出相当于2个格子的总边距（保证四周有留白）
        // Math.min 保证取宽和高里较小的比例，这样格子永远是正方形（长宽比不变）
        CELL_SIZE = Math.min(w / (cols + 2), h / (rows + 2));

        if (CELL_SIZE <= 0) CELL_SIZE = 1;

        // 让棋子的半径也跟着动态缩放（原来21大概是52的0.4倍）
        PIECE_RADIUS = (int)(CELL_SIZE * 0.4);
        HIT_RADIUS = (int)(CELL_SIZE * 0.45);
    }

        private int screenX(int col) {
            updateLayout(); // 每次获取 X 坐标前，先更新尺寸
            // 计算 X 方向的居中起始位置
            int startX = (getWidth() - (Board.COLS - 1) * CELL_SIZE) / 2;
            return startX + col * CELL_SIZE;
        }

        private int screenY(int row) {
            // 计算 Y 方向的居中起始位置
            int startY = (getHeight() - (Board.ROWS - 1) * CELL_SIZE) / 2;
            return startY + row * CELL_SIZE;
        }
}
