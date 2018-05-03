package com.company;

public class Player {

    private String name;
    private int lastScore, highScore, wins;
    public enum Level {BRONZE, SILVER, GOLD};
    private Level level;
    private String password;

    Player(String name, int lastScore, int highScore, int wins, Level level, String password)
    {
        this.name = name;
        this.lastScore = lastScore;
        this.highScore = highScore;
        this.wins = wins;
        this.level = level;
        this.password = password;
    }

    public void checkScore(int score)
    {
        if(score > highScore)
        {
            highScore = score;
            lastScore = score;
        }
        else lastScore = score;
    }

    public boolean checkPassword(String password)
    {
        if(password.equals(this.password)) return true;
        else return false;
    }

    public String getName() { return name; }

    public int getLastScore(){ return lastScore; }

    public int getHighScore(){ return highScore; }

    public int getWins(){ return  wins; }

    public Level getLevel(){ return level; }

    public static Level parseLevel(String str)
    {
        Level temp;
        switch (str)
        {
            case "BRONZE": temp = Level.BRONZE;
            break;
            case "SILVER": temp = Level.SILVER;
            break;
            case "GOLD": temp = Level.GOLD;
            break;
            default: temp = Level.BRONZE;
            break;
        }
        return temp;
    }

    /**
     * Save scores in a dropbox or git repository
     * make a random password so that only the program can access the file
     * read and write to that file
     * write to a local file so every player can see everyone else's score
     */
}