package edu.sustech.xiangqi; // 【注意】确保这里和你原来项目的包名一致

// 引入你原本的象棋类（如果报错，说明你的包名不对，按 Alt+Enter 修复）
import edu.sustech.xiangqi.model.Board;
import edu.sustech.xiangqi.view.ChessFrame;
import edu.sustech.xiangqi.controller.ChessController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

// 所有的界面逻辑都在这里，继承了 JFrame
public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn;
    private JButton regBtn;

    public LoginFrame() {
        // 1. 窗口基础设置
        setUndecorated(true); // 去掉 Windows 默认的白色标题栏
        setSize(800, 600);    // 窗口大小
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2. 设置带背景图的面板
//        JPanel bgPanel = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                // 【注意】确保你的项目根目录有一张叫 bg.jpg 的图片
//                ImageIcon icon = new ImageIcon("bg.jpg");
//                if (icon.getImage() != null) {
//                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
//                } else {
//                    // 如果没找到图片，用深灰色垫底
//                    g.setColor(new Color(40, 40, 40));
//                    g.fillRect(0, 0, getWidth(), getHeight());
//                }
//            }
//        };
        // 2. 优化后的背景面板（解决闪烁问题）
        // 提前把图片加载进内存，只加载一次
        Image bgImage = new ImageIcon("bg.jpg").getImage();

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 开启双缓冲（防闪烁）
                setDoubleBuffered(true);

                if (bgImage != null) {
                    // 直接从内存画图，速度极快，不会闪烁
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(40, 40, 40));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        bgPanel.setLayout(null); // 使用绝对定位
        setContentPane(bgPanel);

        // 3. 初始化所有的按钮和输入框
        initUI(bgPanel);
    }

    private void initUI(JPanel bgPanel) {
        int centerX = getWidth() / 2; // 屏幕中心 X 坐标
        int startY = 200;             // 起始 Y 坐标

        //用户名输入框 (纯手绘提示词 & 半透明圆角)
        userField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 1. 画半透明圆角背景
                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // 2. 画提示词（当内容为空且没有焦点时）
                if (getText().isEmpty() && !hasFocus()) {
                    g2.setColor(Color.GRAY);
                    g2.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("请输入用户名", 16, y); // 坐标为 x=16
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        userField.setOpaque(false);
        userField.setForeground(Color.WHITE); // 输入的字变白
        userField.setCaretColor(Color.WHITE); // 光标变白
        userField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        userField.setBounds(centerX - 125, startY, 250, 40);
        userField.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16)); // 左边距也设为 16
        // 增加监听，用来在输入或失去焦点时刷新界面
        userField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { repaint(); }
        });
        userField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { repaint(); }
            public void focusLost(FocusEvent e) { repaint(); }
        });
        bgPanel.add(userField);

        //密码输入框 (纯手绘提示词 & 半透明圆角)
        passField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 1. 画半透明圆角背景
                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // 2. 画提示词（当密码为空且没有焦点时）
                if (getPassword().length == 0 && !hasFocus()) {
                    g2.setColor(Color.GRAY);
                    g2.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("请输入密码", 16, y); // 坐标也是 x=16
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        passField.setOpaque(false);
        passField.setForeground(Color.WHITE); // 密码圆点变白
        passField.setCaretColor(Color.WHITE);
        passField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        passField.setBounds(centerX - 125, startY + 60, 250, 40);
        passField.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16)); // 左边距 16 完全一致
        // 增加监听
        passField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { repaint(); }
        });
        passField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { repaint(); }
            public void focusLost(FocusEvent e) { repaint(); }
        });
        bgPanel.add(passField);

        // --- 登录按钮 ---
        loginBtn = createGameButton("步入江湖");
        loginBtn.setBounds(centerX - 125, startY + 130, 250, 45);
        bgPanel.add(loginBtn);

        // --- 注册按钮 ---
        regBtn = createGameButton("榜上有名");
        regBtn.setBounds(centerX - 125, startY + 190, 250, 45);
        bgPanel.add(regBtn);

        // --- 右上角关闭按钮 ---
        JButton closeBtn = new JButton("X");
        closeBtn.setBounds(760, 10, 30, 30);
        closeBtn.setContentAreaFilled(false); // 透明背景
        closeBtn.setBorderPainted(false);     // 无边框
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        bgPanel.add(closeBtn);

        // --- 核心逻辑：点击登录进入游戏 ---
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            // 判断输入是否为空
            if (username.equals("请输入用户名") || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名或密码不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("登录成功，准备进入游戏...");
            this.dispose(); // 关闭当前的登录窗口

            // 【这里是你原本的代码】启动象棋界面
            Board board = new Board();
            ChessFrame frame = new ChessFrame(board);
            ChessController controller = new ChessController(board, frame.getBoardPanel());
            frame.getBoardPanel().addMouseListener(controller);
            frame.setVisible(true);
        });

        // --- 核心逻辑：点击注册 ---
        regBtn.addActionListener(e -> {
            String username = userField.getText();
            if (username.equals("请输入用户名") || username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先输入要注册的用户名！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 写入文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt", true))) {
                writer.write(username + "\n");
                JOptionPane.showMessageDialog(this, "注册成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "注册失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // 自定义方法：画一个好看的圆角变色按钮
    private JButton createGameButton(String text) {
        return new JButton(text) {
            // 1. 加载你做好的两张透明图
            Image imgNormal = new ImageIcon("btn_normal.png").getImage();
            Image imgHover = new ImageIcon("btn_hover.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // 开启抗锯齿，让图片边缘和文字极度平滑
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                // 2. 圆角裁剪（防止图片直角溢出，虽然你的图可能已经是圆角了）
                java.awt.geom.RoundRectangle2D roundedRect = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setClip(roundedRect);
                // 3. 核心：根据鼠标状态切换图片
                if (getModel().isRollover()) {
                    g2d.drawImage(imgHover, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2d.drawImage(imgNormal, 0, 0, getWidth(), getHeight(), this);
                }
                // 4. 绘制文字（居中 + 阴影防看不清）
                g2d.setFont(new Font("楷体", Font.BOLD, 22));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                // 文字黑阴影
                g2d.setColor(new Color(0, 0, 0, 200));
                g2d.drawString(getText(), x + 1, y + 1);
                // 文字本体（白色）
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
    }
}