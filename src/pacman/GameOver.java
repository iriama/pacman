package pacman;

import pacman.utility.UI;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GameOver extends JPanel {

    JPanel table;
    JLabel scoreLabel;

    Font arcade40 = UI.getSizedFont("arcadeclassic", 40);
    Font arcade20 = UI.getSizedFont("arcadeclassic", 20);
    private IEvent restart;

    private Score score;
    private Vector<Score> best;

    public GameOver(IEvent restart) {
        super();
        this.restart = restart;
        build();
    }

    public void set(Score score, Vector<Score> best) {
        this.score = score;
        this.best = best;
        this.best.sort((a, b) -> b.value - a.value);

        refresh();
    }

    private void refresh() {
        scoreLabel.setText(Integer.toString(score.value));

        table.removeAll();
        for (Score entry : best) {

            int nbPoints = Math.max(0, 12 - entry.name.length() + Integer.toString(entry.value).length());
            String points = new String(new char[nbPoints]).replace("\0", ".");

            JLabel row = UI.getCenteredTxtLabel(entry.name + " " + points + " " + entry.value, Color.WHITE, arcade20);
            if (score.equals(entry)) row.setForeground(Color.yellow);
            else row.setForeground(Color.white);

            table.add(row);
        }
    }


    private void build() {
        setBackground(Color.black);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Game over
        JLabel gameOverTxt = UI.getCenteredTxtLabel("GAME OVER", Color.WHITE, arcade40);

        // pacman.Score
        JLabel scoreTxt = UI.getCenteredTxtLabel("SCORE", Color.RED, arcade20);
        scoreLabel = UI.getCenteredTxtLabel("", Color.WHITE, arcade20);

        // Hi-pacman.Score
        JLabel hiScoreTxt = UI.getCenteredTxtLabel("BEST SCORES", Color.GREEN, arcade20);
        table = new JPanel();
        table.setBackground(Color.black);
        table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));


        // Play again
        RetroButton playAgain = new RetroButton("play again", Color.GREEN, arcade20, restart);

        add(UI.getSeparator(50));
        add(gameOverTxt);
        add(UI.getSeparator(50));
        add(scoreTxt);
        add(UI.getSeparator(10));
        add(scoreLabel);
        add(UI.getSeparator(20));
        add(hiScoreTxt);
        add(UI.getSeparator(10));
        add(table);
        add(UI.getSeparator(50));
        add(playAgain);
    }

}
