package pacman.UI;

import javax.swing.*;
import java.awt.*;

/**
 * Game status bar
 */
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
        arcadeFont = UIFactory.getSizedFont("arcadeclassic", 20);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(Color.black);

        // LEVEL
        JLabel levelTxt = UIFactory.getTxtLabel("LEVEL", Color.red, arcadeFont);
        level = UIFactory.getTxtLabel("?", Color.WHITE, arcadeFont);


        // SCORE
        JLabel scoreTxt = UIFactory.getTxtLabel("SCORE", Color.RED, arcadeFont);
        score = UIFactory.getTxtLabel("0", Color.WHITE, arcadeFont);

        // TIME
        JLabel timeTxt = UIFactory.getTxtLabel("TIME", Color.RED, arcadeFont);
        time = UIFactory.getTxtLabel("0", Color.WHITE, arcadeFont);

        // LIVES
        JLabel livesTxt = UIFactory.getTxtLabel("LIVES", Color.RED, arcadeFont);
        lives = UIFactory.getTxtLabel("0", Color.WHITE, arcadeFont);


        inner.add(UIFactory.getSeparator(MARGIN));
        inner.add(levelTxt);
        inner.add(UIFactory.getSeparator(MARGIN / 2));
        inner.add(level);
        inner.add(UIFactory.getSeparator(MARGIN));
        inner.add(scoreTxt);
        inner.add(UIFactory.getSeparator(MARGIN / 2));
        inner.add(score);
        inner.add(UIFactory.getSeparator(MARGIN));
        inner.add(timeTxt);
        inner.add(UIFactory.getSeparator(MARGIN / 2));
        inner.add(time);
        inner.add(UIFactory.getSeparator(MARGIN));
        inner.add(livesTxt);
        inner.add(UIFactory.getSeparator(MARGIN / 2));
        inner.add(lives);

        add(inner);
    }

    /**
     * Set level id
     *
     * @param level level
     */
    public void setLevel(String level) {
        this.level.setText(level);
    }

    /**
     * Set score
     *
     * @param score score
     */
    public void setScore(int score) {
        this.score.setText(Integer.toString(score));
    }

    /**
     * Set time
     *
     * @param time time
     */
    public void setTime(int time) {
        this.time.setText(Integer.toString(time));
    }

    /**
     * Set lives count
     *
     * @param lives lives count
     */
    public void setLives(int lives) {
        if (lives == 1) this.lives.setForeground(Color.yellow);
        if (lives == 0) this.lives.setForeground(Color.red);

        this.lives.setText("x" + lives);
    }

}
