package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

public class Main {

    public static void listener() {
        System.out.println("开始监听");
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
                handleKey(nativeEvent);
            }
        });
    }

    public static void removeListener() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            throw new RuntimeException(ex);
        }
    }

    static JLabel _label;
    static JFrame _frame;

    public static void handleKey(NativeKeyEvent keyEvent) {
        String keyName = NativeKeyEvent.getKeyText(keyEvent.getKeyCode());

        // 获取标签的首选尺寸
        Dimension labelSize = _label.getPreferredSize();

        if (_label.getText().length() > 20 && labelSize.width >= _frame.getWidth()-100) {
            _label.setText(" " + keyName);

        } else {
            _label.setText(_label.getText() + " " + keyName);
        }
    }

    public static void addLabel() {
        // 创建一个标签控件
        _label = new JLabel("Hello, World!");

        // 设置标签的位置和大小
        //_label.setBackground(Color.BLUE);
        _label.setForeground(Color.white);
        _label.setLocation(0, 0);
        _label.setSize(_frame.getWidth(), _frame.getHeight());
        _label.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
        _label.setHorizontalAlignment(SwingConstants.CENTER);
        _label.setVerticalAlignment(SwingConstants.CENTER);

        // 将标签添加到窗口中
        _frame.add(_label);
    }

    public static void addRightMenu() {
        JPopupMenu menu = new JPopupMenu();   //弹出式菜单

        JMenuItem item1 = new JMenuItem("关闭");
        item1.addActionListener(e -> {
            removeListener();
            System.exit(0);
        });
        menu.add(item1);   //弹出式菜单添加一个菜单项

        class myMouseListener implements MouseListener {
            JPopupMenu popupMenu;  //一个弹出式菜单对象

            myMouseListener(JPopupMenu jpopupmenu)   //构造函数
            {
                popupMenu = jpopupmenu;
            }

            //自己写的一个显示的方法
            public void display(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                this.display(e);
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
            }
        }

        _frame.addMouseListener(new myMouseListener(menu));
    }

    public static void addWindowDrag() {
        final int[] mouseClickX = {0};
        final int[] mouseClickY = {0};

        // 设置 拖动窗口
        _frame.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录鼠标点击时的窗口位置
                mouseClickX[0] = e.getX();
                mouseClickY[0] = e.getY();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        _frame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 计算窗口的新位置
                int newX = _frame.getX() + e.getX() - mouseClickX[0];
                int newY = _frame.getY() + e.getY() - mouseClickY[0];

                // 设置窗口的新位置
                _frame.setLocation(newX, newY);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    public static void main(String[] args) {
        // 创建 JFrame 实例
        _frame = new JFrame("ShowKeyboard");

        // 设置窗口大小
        _frame.setSize(800, 70);

        // 设置窗口在屏幕中央显示
        _frame.setLocationRelativeTo(null);

        // 设置关闭窗口时结束程序运行
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置窗口无样式
        _frame.setUndecorated(true);

        _frame.getContentPane().setBackground(Color.black);
        // 设置透明度
        _frame.setOpacity(0.6f);
        // 置顶
        _frame.setAlwaysOnTop(true);

        addWindowDrag();
        addLabel();
        addRightMenu();

        _frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                listener();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                removeListener();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        // 设置窗口可见
        _frame.setVisible(true);
    }
}