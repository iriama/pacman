package pacman;

import javax.swing.*;

public class GPanel extends JPanel {
    public String skin;
    public String keyset;

    public GPanel(String skin, String keyset) {
        this.skin = skin;
        this.keyset = keyset;
    }
}