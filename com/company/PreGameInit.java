package com.company;

import javax.swing.*;
import java.awt.*;

public class PreGameInit
{
    protected static void getNewPlayers() {
        JPanel panel = new JPanel(new GridLayout(7, 1));
        JTextField p1NameTxt = new JTextField(), p2NameTxt = new JTextField();
        panel.add(new JLabel("Player 1 Name"));
        panel.add(p1NameTxt);
        panel.add(new JLabel("[W][A][S][D]"));
        panel.add(new JLabel(""));
        panel.add(new JLabel("Player 2 Name"));
        panel.add(p2NameTxt);
        panel.add(new JLabel("[↑][←][↓][→]"));

        int op = JOptionPane.showConfirmDialog(panel, panel, "Who is Playing?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(op == 0)
        {
            if(p1NameTxt.getText().equals("") || p1NameTxt.getText().equals(" ")) Main.p1Name = "Player 1";
            else Main.p1Name = p1NameTxt.getText();
            if(p2NameTxt.getText().equals("") || p2NameTxt.getText().equals(" ")) Main.p2Name = "Player 2";
            else Main.p2Name = p2NameTxt.getText();
        }
        else System.exit(0);
    }

    protected static void chooseColorScheme()
    {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(new JLabel("Choose a color scheme", SwingConstants.CENTER));
        JButton CLASSIC = new JButton("Classic");
        CLASSIC.addActionListener(e -> Main.scheme = Main.ColorScheme.CLASSIC);
        JButton NEW = new JButton("New");
        NEW.addActionListener(e -> Main.scheme = Main.ColorScheme.NEW);
        JButton LIGHT = new JButton("Light");
        LIGHT.addActionListener(e -> Main.scheme = Main.ColorScheme.LIGHT);
        panel.add(CLASSIC);
        panel.add(NEW);
        panel.add(LIGHT);

        JOptionPane.showMessageDialog(panel, panel, "Color Scheme", JOptionPane.PLAIN_MESSAGE);

        if(Main.scheme == Main.ColorScheme.NEW)
        {
            Main.bg = new Color(30, 30, 30);
            Main.b = new Color(150, 150, 150);
            Main.f = new Color(0, 200, 200);
            Main.p = new Color(245, 245, 245);
            Main.t = Main.bg;
        }
        else if(Main.scheme == Main.ColorScheme.CLASSIC)
        {
            Main.bg = new Color(0, 0, 0);
            Main.b = new Color(25, 54, 76);
            Main.f = new Color(255, 0, 0);
            Main.p = new Color(0, 255, 0);
            Main.t = new Color(255, 255, 255);
        }
        else if(Main.scheme == Main.ColorScheme.LIGHT)
        {
            Main.bg = new Color(245, 245, 245);
            Main.b = new Color(150, 150, 150);
            Main.f = new Color(0, 58, 102);
            Main.p = new Color(40, 40, 40);
            Main.t = new Color(0, 0, 0);
        }
        else {
            Main.bg = new Color(0, 0, 0);
            Main.b = new Color(25, 54, 76);
            Main.f = new Color(255, 0, 0);
            Main.p = new Color(0, 255, 0);
            Main.t = new Color(255, 255, 255);
        }
    }
}