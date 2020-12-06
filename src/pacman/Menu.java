package pacman;

import framework.rendering.graphics.SpriteSheet;
import pacman.parsing.MemoryDB;
import pacman.utility.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Menu extends JPanel {

    Font pac40 =  UI.getSizedFont("pacfont", 40);
    Font arcade10 =  UI.getSizedFont("arcadeclassic", 10);
    Font arcade20 =  UI.getSizedFont("arcadeclassic", 20);
    Font arcade40 =  UI.getSizedFont("arcadeclassic", 40);

    public static int WIDTH = 500;
    public static int HEIGHT = 520;
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
            row.setForeground(Color.white);

            add(row);
            i++;
        }

        add(UI.getSeparator(50));
        add(ret);

        revalidate();
        repaint();
    }


    HashMap<String, Boolean> skins = new HashMap<>();
    HashMap<String, Boolean> keys = new HashMap<>();

    private GPanel ghostPanel() {
        SpriteSheet spriteSheet = null;


        String skin = "";
        for (String k: skins.keySet()) {
            if (!skins.get(k)) {
                skin = k;
                skins.put(k, true);
                break;
            }
        }

        String keyStr = "";
        if (keys.containsKey("zsqd") && !keys.get("zsqd")) {
            keyStr = "zsqd";
            keys.put(keyStr, true);
        }
        else {
            for (String k : keys.keySet()) {
                if (!keys.get(k)) {
                    keyStr = k;
                    keys.put(k, true);
                    break;
                }
            }
        }

        try {
             spriteSheet = MemoryDB.getSpriteSheet(skin + "/right", 28);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        GPanel panel = new GPanel(skin, keyStr);
        panel.setLayout(new FlowLayout());
        JLabel txt = UI.getCenteredTxtLabel("  " + skin.toUpperCase() + "- KEYS : ", Color.WHITE, arcade10);
        AtomicReference<RetroButton> apreset = new AtomicReference<RetroButton>();
        Vector<String> ignoredKeys = new Vector<>();
        ignoredKeys.add(keyStr);
        apreset.set(new RetroButton(keyStr.toUpperCase(), Color.GRAY, arcade10, () -> {
            boolean found = false;
            while(!found) {
                for (String k : keys.keySet()) {
                    if (ignoredKeys.contains(k)) continue;

                    if (!keys.get(k) || k == panel.keyset) {
                        keys.put(panel.keyset, false);
                        panel.keyset = k;
                        keys.put(k, true);
                        ignoredKeys.add(k);
                        apreset.get().setText(k.toUpperCase());
                        found = true;
                        break;
                    }
                }

                if (!found) ignoredKeys.clear();
            }
        }));

        panel.setBackground(Color.black);
        panel.setMaximumSize(new Dimension(WIDTH, 38));
        JLabel img = new JLabel();
        img.setIcon(new ImageIcon(spriteSheet.getSpriteImage(0)));

        Vector<String> ignoredSkins = new Vector<>();
        ignoredSkins.add(skin);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                boolean found = false;
                while(!found) {
                    for (String k : skins.keySet()) {
                        if (ignoredSkins.contains(k)) continue;

                        if (!skins.get(k) || k == panel.skin) {
                            skins.put(panel.skin, false);
                            panel.skin = k;
                            skins.put(k, true);
                            ignoredSkins.add(k);
                            txt.setText("  " + panel.skin.toUpperCase() + "- KEYS : ");

                            try {
                                SpriteSheet spriteSheet = MemoryDB.getSpriteSheet(panel.skin + "/right", 28);
                                img.setIcon(new ImageIcon(spriteSheet.getSpriteImage(0)));

                            } catch (IOException ex) {
                                ex.printStackTrace();
                                System.exit(1);
                            }

                            found = true;
                            break;
                        }
                    }

                    if (!found) ignoredSkins.clear();
                }
            }
        });

        panel.add(img);
        panel.add(txt);
        panel.add(apreset.get());

        return panel;
    }

    private void multiMenu() {
        removeAll();

        skins = new HashMap<>();
        Vector<String> _skins = MemoryDB.getMultiSkins();
        for (String skin: _skins) {
            skins.put(skin, false);
        }
        Vector<String> _presets = MemoryDB.getMultiPresets();
        keys = new HashMap<>();
        for (String preset: _presets) {
            keys.put(preset, false);
        }

        int maxPlayers = Math.min(_presets.size(), _skins.size());

        JLabel menuTxt = UI.getCenteredTxtLabel("MULTIPLAYER", Color.yellow, arcade40);
        JLabel status = UI.getCenteredTxtLabel("PACMAN vs 0 GHOST(S)", Color.GREEN, arcade20);


        RetroButton ret = new RetroButton("RETURN", Color.CYAN, arcade20, () -> mainMenu());

        AtomicInteger nbGhosts = new AtomicInteger(0);

        JPanel gParent = new JPanel();
        gParent.setLayout(new BoxLayout(gParent, BoxLayout.Y_AXIS));
        gParent.setBackground(Color.red);

        Vector<GPanel> gpanels = new Vector<>();

        RetroButton add = new RetroButton("+", Color.GREEN, arcade20, () -> {
            if (nbGhosts.get() >= maxPlayers)
                return;

            nbGhosts.set(nbGhosts.get()+1);
            status.setText("PACMAN vs " + nbGhosts.get() + " GHOST(S)");
            gpanels.add(ghostPanel());

            gParent.removeAll();

            for (GPanel p: gpanels) {
                gParent.add(p);
            }

            gParent.revalidate();
            gParent.repaint();
        });
        RetroButton remove = new RetroButton("-", Color.RED, arcade20, () -> {
            if (nbGhosts.get() <= 0)
                return;

            nbGhosts.set(nbGhosts.get()-1);
            status.setText("PACMAN vs " + nbGhosts.get() + " GHOST(S)");
            GPanel last = gpanels.lastElement();
            skins.put(last.skin, false);
            keys.put(last.keyset, false);

            gpanels.removeElement(last);
            gParent.removeAll();

            for (GPanel p: gpanels) {
                gParent.add(p);
            }

            gParent.revalidate();
            gParent.repaint();
        });

        RetroButton start = new RetroButton("START", Color.yellow, arcade20, () -> {
            if (gpanels.size() < 1) return;

            Multi.start(this, gpanels);

        });

        JPanel controls = new JPanel();
        controls.setBackground(Color.black);
        controls.add(add);
        controls.add(remove);
        controls.setMaximumSize(new Dimension(WIDTH, 40));

        add(UI.getSeparator(50));
        add(menuTxt);
        add(UI.getSeparator(50));
        add(status);
        add(UI.getSeparator(20));


        // stuff
        add(controls);
        add(gParent);

        add(UI.getSeparator(20));
        add(start);
        add(UI.getSeparator(20));
        add(ret);

        revalidate();
        repaint();
    }


    public void multiOverMenu(String text) {
        removeAll();

        JLabel menuTxt = UI.getCenteredTxtLabel(text, Color.yellow, arcade40);

        RetroButton ret = new RetroButton("RETURN", Color.GREEN, arcade20, () -> multiMenu());

        add(UI.getSeparator(50));
        add(menuTxt);
        add(UI.getSeparator(100));
        add(ret);

        revalidate();
        repaint();
    }

}
