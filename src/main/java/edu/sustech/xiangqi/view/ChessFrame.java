package edu.sustech.xiangqi.view;

import edu.sustech.xiangqi.model.Board;

import javax.swing.*;

public final class ChessFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final BoardPanel boardPanel;

    public ChessFrame(Board board) {
        super("中国象棋");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardPanel = new BoardPanel(board);
        boardPanel.setLayout(null);  // 关键：关闭布局管理器

        JButton button = new JButton("Click");
        button.setBounds(10, 10, 80, 30);  // x, y, width, height
        boardPanel.add(button);
        JLabel label=new JLabel("Sample");
        boardPanel.label=label;//点中棋子
        label.setBounds(100, 10, 80, 30);
        boardPanel.add(label);
        button.addActionListener(e->{
            label.setText("点击按钮啦");//点按钮的字幕
            System.out.println("hahaha");
        });
        setContentPane(boardPanel);
        setSize(1000,1000);//调面板大小
        //pack();//默认面板大小+居中 不能调整棋盘大小
        setResizable(true);//允许调整棋盘大小
        setLocationRelativeTo(null);//居中棋盘
        //setExtendedState(JFrame.MAXIMIZED_BOTH);//自动全屏
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
