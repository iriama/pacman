package pacman;

import pacman.utility.FontsEngine;
import pacman.utility.UI;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {


    public static final int MARGIN = 10;
    public static int WIDTH = 90;

    private JLabel level;
    private JLabel score;
    private JLabel time;
    private JLabel lives;
    private Font arcadeFont;


    public StatusBar() {
        super();
        build();
    }



    private void build() {
        setBackground(Color.black);
        arcadeFont = UI.getSizedFont("arcadeclassic", 20);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(Color.black);

        // LEVEL
        JLabel levelTxt = UI.getTxtLabel("LEVEL", Color.red, arcadeFont);
        level = UI.getTxtLabel("?", Color.WHITE, arcadeFont);


        // SCORE
        JLabel scoreTxt = UI.getTxtLabel("SCORE", Color.RED, arcadeFont);
        score = UI.getTxtLabel("0", Color.WHITE, arcadeFont);

        // TIME
        JLabel timeTxt =  UI.getTxtLabel("TIME", Color.RED, arcadeFont);
        time = UI.getTxtLabel("0", Color.WHITE, arcadeFont);

        // LIVES
        JLabel livesTxt = UI.getTxtLabel("LIVES", Color.RED, arcadeFont);
        lives = UI.getTxtLabel("0", Color.WHITE, arcadeFont);


        inner.add(UI.getSeparator(MARGIN));
        inner.add(levelTxt);
        inner.add(UI.getSeparator(MARGIN/2));
        inner.add(level);
        inner.add(UI.getSeparator(MARGIN));
        inner.add(scoreTxt);
        inner.add(UI.getSeparator(MARGIN/2));
        inner.add(score);
        inner.add(UI.getSeparator(MARGIN));
        inner.add(timeTxt);
        inner.add(UI.getSeparator(MARGIN/2));
        inner.add(time);
        inner.add(UI.getSeparator(MARGIN));
        inner.add(livesTxt);
        inner.add(UI.getSeparator(MARGIN/2));
        inner.add(lives);

        add(inner);
    }

    public void setLevel(String level) {
        this.level.setText(level);
    }

    public void setScore(int score) {
        this.score.setText(Integer.toString(score));
    }

    public void setTime(int time) {
        this.time.setText(Integer.toString(time));
    }

    public void setLives(int lives) {
        if (lives == 1) this.lives.setForeground(Color.yellow);
        if (lives == 0) this.lives.setForeground(Color.red);

        this.lives.setText("x" + Integer.toString(lives));
    }

}
