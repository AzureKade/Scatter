package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends JPanel
{
    //frame variables
    public static JFrame game;
    public static int width = 816;
    public static int height = 838;
    public static Main app;
    public static Dimension canvas; //Java why the heck is the canvas not the same size as the frame

    //position space variables
    public static int cols, rows;
    public static final int scl = 25;
    public static boolean spaceFree;

    //fruit vars
    public static Fruit[] fruits = new Fruit[5];

    //wall vars
    public static ArrayList<BodySegment> walls = new ArrayList<>();

    public static boolean tie = false;

    //p1 vars
    public static BodySegment p1;
    public static Vector2D p1Speed;
    public static boolean p1HasLost = false;
    public static int p1Score = 0;
    public static String p1Name;

    //p2 vars
    public static BodySegment p2;
    public static Vector2D p2Speed;
    public static boolean p2HasLost = false;
    public static int p2Score = 0;
    public static String p2Name;

    //color scheme vars
    public static Color bg, p, f, b, t;
    public enum ColorScheme {CLASSIC, NEW, LIGHT}
    public static ColorScheme scheme;

    public static void main(String[] args) throws InterruptedException
    {
        PreGameInit.getNewPlayers();
        PreGameInit.chooseColorScheme();
        runGame();
    }

    private static void runGame() throws InterruptedException {
        //frame init
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        game = new JFrame("Scatter");
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setResizable(false);
        game.setSize(width, height);
        game.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
        app = new Main();
        game.add(app);
        game.setVisible(true);
        canvas = game.getContentPane().getSize();
        //p1 movement listener
        game.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_D) p1Speed.setVals( 1, 0);
                else if(e.getKeyCode() == KeyEvent.VK_A) p1Speed.setVals(-1, 0);
                else if(e.getKeyCode() == KeyEvent.VK_W) p1Speed.setVals(0, -1);
                else if(e.getKeyCode() == KeyEvent.VK_S) p1Speed.setVals(0, 1);
            }
        });
        //p2 movement listener
        game.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_RIGHT) p2Speed.setVals( 1, 0);
                else if(e.getKeyCode() == KeyEvent.VK_LEFT) p2Speed.setVals(-1, 0);
                else if(e.getKeyCode() == KeyEvent.VK_UP) p2Speed.setVals(0, -1);
                else if(e.getKeyCode() == KeyEvent.VK_DOWN) p2Speed.setVals(0, 1);
            }
        });

        //space init
        cols = canvas.width / scl;
        rows = canvas.height / scl;

        playerInit();

        //fruit init
        fruitInit();

        //draw game
        while (!p1HasLost && !p2HasLost && !tie)
        {
            movePlayers();
            checkCollision();
            checkFruits();
            game.repaint();
            checkLose();
            Thread.sleep(100);
        }
    }

    private static void checkLose() throws InterruptedException {
        if(p1HasLost || p2HasLost || tie)
        {
            JPanel panel = new JPanel(new GridLayout(3, 1));
            if(p1HasLost) panel.add(new JLabel(p2Name + " wins!"));
            if(p2HasLost) panel.add(new JLabel(p1Name + " wins!"));
            if(tie) panel.add(new JLabel("It's a Tie!"));
            panel.add(new JLabel(p1Name + "'s score : " + p1Score));
            panel.add(new JLabel(p2Name + "'s score : " + p2Score));

            Object[] ops = {"Rematch", "New Match", "Exit"};
            int choice = JOptionPane.showOptionDialog(panel, panel, "Game Over!", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ops, ops[1]);
            if(choice == 0)
            {
                resetScoreboard();
                game.setVisible(false);
                game = null;
                runGame();
            }
            else if(choice == 1)
            {
                resetScoreboard();
                game.setVisible(false);
                game = null;
                PreGameInit.getNewPlayers();
                PreGameInit.chooseColorScheme();
                runGame();
            }
            else{ System.exit(0); }
        }
    }

    private static void resetScoreboard() {
        //wall vars
        walls.clear();

        //p1 vars
        p1 = null;
        p1Speed = new Vector2D(0, 0);
        p1HasLost = false;
        p1Score = 0;

        //p2 vars
        p2 = null;
        p2Speed = new Vector2D(0, 0);
        p2HasLost = false;
        p2Score = 0;
    }

    private static void checkCollision()
    {
        if(p1.x > (cols - 2) * scl || p1.x < scl || p1.y > (rows - 2) * scl || p1.y < scl)
        {
            p2Score += 5;
            determineWinner();
        }
        for(BodySegment wall : walls)
        {
            if(p1.x == wall.x && p1.y == wall.y)
            {
                p2Score += 5;
                determineWinner();
            }
            if(p2.x == wall.x && p2.y == wall.y)
            {
                p1Score += 5;
                determineWinner();
            }
        }
        if(p2.x > (cols - 2) * scl || p2.x < scl || p2.y > (rows - 2) * scl || p2.y < scl)
        {
            p1Score += 5;
            determineWinner();
        }
    }

    private static void determineWinner() {
        if(p1Score > p2Score) p2HasLost = true;
        else if(p2Score > p1Score) p1HasLost = true;
        else if(p1Score == p2Score) tie = true;
    }

    private static void fruitInit() {
        for(int i = 0; i < fruits.length; i++)
        {
            fruits[i] = new Fruit();
        }
        placeFruit();
    }

    private static void playerInit()
    {
        p1Speed = new Vector2D(0, 0);
        p1 = new BodySegment();
        p1.setPos(scl, scl);
        p1.setSpeed(p1Speed);

        p2Speed = new Vector2D(0, 0);
        p2 = new BodySegment();
        p2.setPos((cols - 2) * scl, (rows - 2) * scl);
        p2.setSpeed(p2Speed);
    }

    private static void placeFruit()
    {
        for(int i = 0; i < fruits.length; i++)
        {
            do {
                spaceFree = true;
                int fx = ThreadLocalRandom.current().nextInt(0, cols - 2);
                int fy = ThreadLocalRandom.current().nextInt(0, rows - 2);
                fruits[i].setPos((fx * scl) + scl, (fy * scl) + scl);

                if(fruits[i].x == p1.x && fruits[i].y == p1.y || fruits[i].x == p2.x && fruits[i].y == p2.y)
                {
                    spaceFree = false;
                }
            }
            while(!spaceFree);
        }
    }

    private static void placeFruit(int index)
    {
        do {
            spaceFree = true;
            int fx = ThreadLocalRandom.current().nextInt(0, cols - 2);
            int fy = ThreadLocalRandom.current().nextInt(0, rows - 2);
            fruits[index].setPos((fx * scl) + scl, (fy * scl) + scl);

            if(fruits[index].x == p1.x && fruits[index].y == p1.y || fruits[index].x == p2.x && fruits[index].y == p2.y)
            {
                spaceFree = false;
            }
        }
        while(!spaceFree);
    }

    private static void checkFruits()
    {
        for(int i = 0; i < fruits.length; i ++)
        {
            if(p1.x == fruits[i].x && p1.y == fruits[i].y)
            {
                placeFruit(i);
                p1Score++;
                addSegment(p1);
            }

            if(p2.x == fruits[i].x && p2.y == fruits[i].y)
            {
                placeFruit(i);
                p2Score++;
                addSegment(p2);
            }
        }
    }

    private static void movePlayers()
    {
        p1.setSpeed(p1Speed);
        p2.setSpeed(p2Speed);

        p1.setPos(p1.x + (p1.xspeed * scl), p1.y + (p1.yspeed * scl));
        p2.setPos(p2.x + (p2.xspeed * scl), p2.y + (p2.yspeed * scl));
    }

    private static void addSegment(BodySegment player) {
        walls.add(new BodySegment());
        walls.get(walls.size() - 1).setPos(player.x, player.y);
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        super.paint(g);

        //background
        g2d.setColor(b);
        g2d.fillRect(0, 0, canvas.width ,canvas. height);
        g2d.setColor(bg);
        g2d.fillRect(scl, scl, canvas.width - 2 * scl, canvas.height - 2 * scl);

        //players
        g2d.setColor(p);
        g2d.fillRect(p1.x, p1.y, scl, scl);
        g2d.fillRect(p2.x, p2.y, scl, scl);
        if(walls.size() > 0)
        {
            for(BodySegment wall : walls)
            {
                g2d.fillRect(wall.x, wall.y, scl, scl);
            }
        }

        //fruit
        g2d.setColor(f);
        for(Fruit fruit : fruits)
        {
            g2d.fillRect(fruit.x, fruit.y, scl, scl);
        }

        //scores
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g2d.setColor(t);
        String score2 = p2Name + " : " + p2Score;
        int strWidth = g2d.getFontMetrics().stringWidth(score2);
        g2d.drawString(p1Name + " : " + p1Score, 4, 18);
        g2d.drawString(score2, canvas.width - 4 - strWidth, 18);
    }
}