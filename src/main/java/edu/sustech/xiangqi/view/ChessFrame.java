package edu.sustech.xiangqi.view; // 👈 注意确认这里的包名和你的目录一致

import edu.sustech.xiangqi.model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChessFrame extends JFrame {
    // 核心组件：棋盘面板
    private final BoardPanel boardPanel;

    // 拖拽用的起点
    private Point origin;

    // 全屏状态记录
    private Rectangle normalBounds = null;
    private boolean isFullScreen = false;

    public ChessFrame(Board board) {
        super("中国象棋");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. 窗口基础设置：无边框、大小、居中
        setUndecorated(true);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        // 2. 初始化主面板和棋盘 (提前初始化，防止报错)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));
        setContentPane(mainPanel);

        // 【关键】把棋盘面板提前创建出来，后面的按钮就能直接引用它了
        boardPanel = new BoardPanel(board);

        // 3. 顶部自定义“标题栏”
        // 提前加载标题栏背景图
        Image titleBgImage = new ImageIcon("title_bg.png").getImage();

        JPanel titleBar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (titleBgImage != null && titleBgImage.getWidth(null) > 0) {
                    // 将龙纹图拉伸铺满整个标题栏
                    g.drawImage(titleBgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        titleBar.setPreferredSize(new Dimension(1000, 40));
        // 不用 setBackground，已经被重绘方法接管了

        // 创建标签，并设置为居中对齐 (SwingConstants.CENTER)
        JLabel titleLabel = new JLabel("中国象棋 - 楚河汉界", SwingConstants.CENTER);
        // 换一个帅气的颜色：亮金色 (RGB: 255, 215, 0)
        titleLabel.setForeground(new Color(255, 215, 0));
        /* 字体建议换成“楷体”或“行书”，字号加大到 20，更有古风感 */
        titleLabel.setFont(new Font("楷体", Font.BOLD, 20));
        // 初始大小先随便给，重点是宽度必须铺满（比如 1000），X 坐标设为 0
        titleLabel.setBounds(0, 0, 1000, 40);
        titleBar.add(titleLabel);

        // 4. 全屏按钮 (纯代码绘制)
        JButton fullScreenBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 鼠标悬停变红
                if (getModel().isRollover()) {
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.LIGHT_GRAY);
                }

                g2d.setStroke(new BasicStroke(2));

                if (isFullScreen) {
                    // 全屏状态下，画两个重叠的方框（还原图标）
                    g2d.drawRect(9, 9, 12, 12);
                    g2d.drawRect(5, 5, 12, 12);
                } else {
                    // 窗口状态下，画单个方框（最大化图标）
                    g2d.drawRect(7, 7, 14, 14);
                }
                g2d.dispose();
            }
        };
        fullScreenBtn.setContentAreaFilled(false);
        fullScreenBtn.setBorderPainted(false);
        fullScreenBtn.setFocusPainted(false);
        fullScreenBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fullScreenBtn.setBounds(910, 5, 30, 30);

        // 全屏切换逻辑（保留任务栏版）
        fullScreenBtn.addActionListener(e -> {
            if (!isFullScreen) {
                normalBounds = getBounds(); // 记住当前大小

                // 获取屏幕可用区域（自动避开任务栏）
                Rectangle usableBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                setBounds(usableBounds); // 将窗口设置到可用区域

                isFullScreen = true;
            } else {
                // 退出全屏，恢复原大小
                setBounds(normalBounds);
                isFullScreen = false;
            }

            // 强制重绘，防止黑边
            SwingUtilities.invokeLater(() -> {
                revalidate();
                repaint();
                boardPanel.revalidate();
                boardPanel.repaint();
            });
            fullScreenBtn.repaint();
        });
        titleBar.add(fullScreenBtn);

        // 5. 刀剑关闭按钮
        Image closeNormal = new ImageIcon("close_normal.png").getImage();
        Image closeHover = new ImageIcon("close_hover.png").getImage();
        JButton closeBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.drawImage(closeHover, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2d.drawImage(closeNormal, 0, 0, getWidth(), getHeight(), this);
                }
                g2d.dispose();
            }
        };
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setBounds(950, 5, 30, 30);
        closeBtn.addActionListener(e -> System.exit(0));
        titleBar.add(closeBtn);

        // 动态调整按钮位置（始终靠右）
        titleBar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                closeBtn.setLocation(titleBar.getWidth() - 40, 5);
                fullScreenBtn.setLocation(titleBar.getWidth() - 80, 5);
                //使标题栏文字框的宽度随窗口变化，居中
                titleLabel.setBounds(0, 0, titleBar.getWidth(), titleBar.getHeight());
            }
        });

        // 6. 拼装界面
        mainPanel.add(titleBar, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // 7. 交互逻辑：拖拽与拉伸
        // 7.1 顶部标题栏拖拽 (支持全屏状态下拖拽退出全屏)
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                origin = e.getPoint();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    // 如果当前是全屏状态
                    if (isFullScreen) {
                        // 1. 获取鼠标在屏幕上的绝对位置
                        Point mouseOnScreen = e.getLocationOnScreen();

                        // 2. 退出全屏，恢复原来的窗口大小
                        setExtendedState(JFrame.NORMAL);
                        setBounds(normalBounds);
                        isFullScreen = false;

                        // 3. 重绘防止黑边
                        SwingUtilities.invokeLater(() -> {
                            revalidate();
                            repaint();
                            boardPanel.revalidate();
                            boardPanel.repaint();
                        });
                        fullScreenBtn.repaint();

                        // 4. 平滑过渡：让恢复后的窗口瞬间吸附到鼠标位置，不突兀
                        // 把鼠标放在新窗口标题栏的中间偏下一点的位置
                        int newX = mouseOnScreen.x - titleBar.getWidth() / 2;
                        int newY = mouseOnScreen.y - 20;
                        setLocation(newX, newY);

                        // 5. 更新拖拽起点，防止鼠标一直吸附
                        origin = new Point(titleBar.getWidth() / 2, 20);
                        return;
                    }

                    // 如果是普通窗口状态，直接执行拖拽
                    Point p = getLocation();
                    setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
                }
            }
        });

        // 7.2 右下角等比例拉伸
        Point start = new Point();
        boardPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                start.setLocation(e.getX(), e.getY());
            }
        });
        boardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (e.getX() > boardPanel.getWidth() - 15 && e.getY() > boardPanel.getHeight() - 15) {
                    boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                } else {
                    boardPanel.setCursor(Cursor.getDefaultCursor());
                }
            }

            public void mouseDragged(MouseEvent e) {
                if (boardPanel.getCursor().getType() == Cursor.SE_RESIZE_CURSOR && !isFullScreen) {
                    int newWidth = getWidth() + e.getX() - start.x;
                    int newHeight = getHeight() + e.getY() - start.y;
                    if (newWidth > 600 && newHeight > 600) {
                        setSize(newWidth, newHeight);
                    }
                }
                start.setLocation(e.getX(), e.getY());
            }
        });

        // 8. 只要窗口大小变了，强制重绘面板
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (boardPanel != null) {
                    boardPanel.revalidate();
                    boardPanel.repaint();
                }
            }
        });
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}