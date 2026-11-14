package edu.sustech.xiangqi;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        // 调用登录窗口
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}