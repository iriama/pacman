package pacman;

import pacman.parsing.MemoryDB;
import pacman.utility.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Menu extends JPanel {

    Font pac40 =  UI.getSizedFont("pacfont", 40);
    Font arcade10 =  UI.getSizedFont("arcadeclassic", 10);
    Font arcade20 =  UI.getSizedFont("arcadeclassic", 20);
    Font arcade40 =  UI.getSizedFont("arcadeclassic", 40);

    public static int WIDTH = 500;
    public static int HEIGHT = 500;
    private static String[] authors = { "AMAIRI Hatem", "BADDOUJ Youssef", "CHANAA Reda", "EL MOUHTADI Sohaib", "TAKHCHI Mohamed" };

    public Menu() {
        super();
        common();
        mainMenu();
    }

    private void common() {
        setBackground(Color.black);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void mainMenu() {
        removeAll();

        JLabel menuTxt = UI.getCenteredTxtLabel("pacman", Color.yellow, pac40);

        RetroButton arcade = new RetroButton("SOLO ARCADE", Color.GREEN, arcade20, () -> soloMenu());
        RetroButton multi = new RetroButton("MULTIPLAYER", Color.GREEN, arcade20, () -> multiMenu());

        JLabel copy = UI.getCenteredTxtLabel("- v1.0 by EQUIPE 27 -", Color.GRAY, arcade10);

        add(UI.getSeparator(50));
        add(menuTxt);
        add(UI.getSeparator(100));
        add(arcade);
        add(UI.getSeparator(20));
        add(multi);
        add(UI.getSeparator(50));
        add(copy);
        for (String author: authors) {
            add(UI.getSeparator(10));
            add(UI.getCenteredTxtLabel(author, Color.GRAY, arcade10));
        }

        revalidate();
        repaint();
    }

    private void soloMenu() {
        removeAll();

        JLabel menuTxt = UI.getCenteredTxtLabel("ARCADE", Color.yellow, arcade40);


        JLabel nameTxt = UI.getCenteredTxtLabel("NAME : PLAYER", Color.WHITE, arcade20);

        AtomicReference<String> typed = new AtomicReference<>("PLAYER");
        AtomicBoolean input = new AtomicBoolean(false);

        nameTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                input.set(!input.get());
                if (input.get()) {
                    nameTxt.setForeground(Color.CYAN);
                    typed.set("");
                    nameTxt.setText("(TYPE)");
                } else {
                    nameTxt.setForeground(Color.WHITE);
                    nameTxt.setText("NAME : " + typed.get());
                }

            }
        });

        KeyListener listener = (new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (!input.get()) return;

                Character c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                    System.out.println(c);
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        if (typed.get().length() >= 2)
                            typed.set(typed.get().substring(0, typed.get().length() - 1));
                        else
                            typed.set(new String(""));
                    }

                } else {
                    typed.set((typed.get() + c).toUpperCase());
                }

                nameTxt.setText(typed.get().length() > 0 ? typed.get() : "(TYPE)");
            }
        });

        Game.mainWindow.addKeyListener(listener);

        RetroButton play = new RetroButton("NEW GAME", Color.GREEN, arcade20, () -> {
            Game.mainWindow.removeKeyListener(listener);
            Arcade.start(this, typed.get(), "arrows");
        });
        RetroButton high = new RetroButton("SCOREBOARD", Color.GREEN, arcade20, () -> {
            Game.mainWindow.removeKeyListener(listener);
            scoreMenu("SCOREBOARD");
        });
        RetroButton ret = new RetroButton("RETURN", Color.CYAN, arcade20, () -> {
            Game.mainWindow.removeKeyListener(listener);
            mainMenu();
        });


        add(UI.getSeparator(50));
        add(menuTxt);
        add(UI.getSeparator(100));
        add(nameTxt);
        add(UI.getSeparator(20));
        add(play);
        add(UI.getSeparator(20));
        add(high);
        add(UI.getSeparator(50));
        add(ret);

        revalidate();
        repaint();
    }

    public void scoreMenu(String title) {
        removeAll();

        Vector<Score> scores = MemoryDB.getScores();

        JLabel menuTxt = UI.getCenteredTxtLabel(title, Color.yellow, arcade40);

        RetroButton ret = new RetroButton("RETURN", Color.CYAN, arcade20, () -> soloMenu());

        add(UI.getSeparator(50));
        add(menuTxt);
        add(UI.getSeparator(50));


        int i = 1;
        for (Score entry: scores) {
            int nbPoints = Math.max(0, 12 - entry.name.length() + Integer.toString(entry.value).length());
            String points = new String(new char[nbPoints]).replace("\0", ".");

            JLabel row = UI.getCenteredTxtLabel(i + ". " + entry.name + " " + points + " " + entry.value, Color.WHITE, arcade20);
            //if (score.equals(entry)) row.setForeground(Color.yellow);
            /*else*/ row.setForeground(Color.white);

            add(row);
            i++;
        }

        add(UI.getSeparator(50));
        add(ret);

        revalidate();
        repaint();
    }

    private void multiMenu() {
        removeAll();

        JLabel menuTxt = UI.getCenteredTxtLabel("MULTIPLAYER", Color.yellow, arcade40);

        RetroButton ret = new RetroButton("RETURN", Color.CYAN, arcade20, () -> mainMenu());

        add(UI.getSeparator(50));
        add(menuTxt);
        add(UI.getSeparator(100));


        // stuff

        add(UI.getSeparator(50));
        add(ret);

        revalidate();
        repaint();
    }


}
