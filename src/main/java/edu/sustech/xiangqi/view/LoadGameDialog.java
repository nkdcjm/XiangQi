package edu.sustech.xiangqi.view;

import edu.sustech.xiangqi.model.Board;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoadGameDialog extends JDialog {
    private Board board;
    private BoardPanel boardPanel;

    public LoadGameDialog(JFrame parent, Board board, BoardPanel boardPanel) {
        super(parent, "选择存档", true);
        this.board = board;
        this.boardPanel = boardPanel;

        setUndecorated(true);
        setSize(900, 800);
        setLocationRelativeTo(parent);

        // 【防穿透核心1】给整个弹窗的根面板画上纯黑底色
        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 强制画一个绝对纯黑的矩形，封死底层
                g.setColor(new Color(20, 20, 20));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(rootPanel);

        // 顶部导航栏
        JPanel topPanel = new JPanel(null);
        topPanel.setPreferredSize(new Dimension(900, 50));
        topPanel.setBackground(new Color(15, 15, 15));

        JButton backBtn = new JButton("← 返回");
        backBtn.setBounds(15, 10, 80, 30);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setForeground(new Color(200, 180, 120));
        backBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        topPanel.add(backBtn);
        rootPanel.add(topPanel, BorderLayout.NORTH);

        // 中间的 3 个槽位
        JPanel slotsPanel = new JPanel(new GridLayout(2, 3, 25, 20));
        slotsPanel.setOpaque(false); // 让槽位面板透明，露出底下的纯黑底板
        slotsPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));

        for (int i = 1; i <= 6; i++) {
            slotsPanel.add(createSaveSlot(i));
        }
        rootPanel.add(slotsPanel, BorderLayout.CENTER);
    }

    private JPanel createSaveSlot(int slotIndex) {
        String filename = "save" + slotIndex + ".txt";
        File file = new File(filename);

        // 外层槽位框
        JPanel slotPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 【防穿透核心2】每个槽位也必须画上实心底板
                g2d.setColor(new Color(40, 40, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // 画金色边框
                g2d.setColor(new Color(200, 180, 120));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

                // 画槽位文字
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
                g2d.drawString("存档位 " + slotIndex, 15, 30);
                g2d.dispose();
            }
        };

        slotPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        slotPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (file.exists()) {
                    board.loadGame(filename);
                    boardPanel.repaint();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "这个存档位是空的！");
                }
            }
        });

        if (file.exists()) {
            // 如果有存档，创建一个专门的【迷你棋盘面板】放进去
            MiniBoardPanel miniBoard = new MiniBoardPanel(file);
            // 放在槽位下半部分，居中
            miniBoard.setBounds(10, 40, 280, 360);
            slotPanel.add(miniBoard);
        } else {
            JLabel emptyLabel = new JLabel("空存档位", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            emptyLabel.setBounds(0, 0, 250, 340);
            slotPanel.add(emptyLabel);
        }

        return slotPanel;
    }

    // 独立出来的迷你棋盘面板类
    class MiniBoardPanel extends JPanel {
        private File file;

        public MiniBoardPanel(File file) {
            this.file = file;
            setOpaque(false); // 背景透明，露出外层的深灰色
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 宽高固定在 230x300 左右
            int cellSize = 24;
            int boardW = cellSize * 8;
            int boardH = cellSize * 9;

            // 在这个面板内居中计算
            int startX = (getWidth() - boardW) / 2 - 15;
            int startY = (getHeight() - boardH) / 2 - 15;

            // 1. 画棋盘底板
            g2d.setColor(new Color(20, 20, 20));
            g2d.fillRect(startX - 5, startY - 5, boardW + 10, boardH + 10);

            // 2. 画网格线
            g2d.setColor(new Color(200, 180, 100));
            g2d.setStroke(new BasicStroke(1));

            for (int i = 0; i <= 9; i++) {
                g2d.drawLine(startX, startY + i * cellSize, startX + boardW, startY + i * cellSize);
            }
            for (int i = 0; i <= 8; i++) {
                if (i == 0 || i == 8) {
                    g2d.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + boardH);
                } else {
                    g2d.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + 4 * cellSize);
                    g2d.drawLine(startX + i * cellSize, startY + 5 * cellSize, startX + i * cellSize, startY + boardH);
                }
            }
            // 九宫格
            g2d.drawLine(startX + 3 * cellSize, startY, startX + 5 * cellSize, startY + 2 * cellSize);
            g2d.drawLine(startX + 5 * cellSize, startY, startX + 3 * cellSize, startY + 2 * cellSize);
            g2d.drawLine(startX + 3 * cellSize, startY + 7 * cellSize, startX + 5 * cellSize, startY + 9 * cellSize);
            g2d.drawLine(startX + 5 * cellSize, startY + 7 * cellSize, startX + 3 * cellSize, startY + 9 * cellSize);

            // 楚河汉界
            g2d.setFont(new Font("楷体", Font.BOLD, 14));
            g2d.drawString("楚河", startX + 2 * cellSize - 5, startY + 4 * cellSize + 14);
            g2d.drawString("汉界", startX + 6 * cellSize - 5, startY + 4 * cellSize + 14);

            // 3. 读取文件画棋子
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("TURN,")) continue;
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        int row = Integer.parseInt(parts[0].trim());
                        int col = Integer.parseInt(parts[1].trim());
                        boolean isRed = Boolean.parseBoolean(parts[2].trim());
                        String name = parts[3].trim();

                        int pieceX = startX + col * cellSize;
                        int pieceY = startY + row * cellSize;
                        int radius = 12;

                        g2d.setColor(new Color(250, 240, 220));
                        g2d.fillOval(pieceX - radius, pieceY - radius, radius * 2, radius * 2);

                        g2d.setColor(isRed ? new Color(180, 30, 30) : new Color(30, 30, 30));
                        g2d.setStroke(new BasicStroke(1));
                        g2d.drawOval(pieceX - radius, pieceY - radius, radius * 2, radius * 2);

                        g2d.setFont(new Font("楷体", Font.BOLD, 16));
                        FontMetrics fm = g2d.getFontMetrics();
                        int textX = pieceX - fm.stringWidth(name) / 2;
                        int textY = pieceY + fm.getAscent() / 2 - 1;

                        g2d.setColor(isRed ? new Color(180, 30, 30) : Color.BLACK);
                        g2d.drawString(name, textX, textY);
                    }
                }
            } catch (Exception ex) {
                // 忽略
            }
            g2d.dispose();
        }
    }
}