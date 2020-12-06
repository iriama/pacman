package pacman.utility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UI {

    public static Font getSizedFont(String fontId, int size) {
        return new Font(FontsEngine.getFontName(fontId), Font.PLAIN, size);
    }

    public static JLabel getSeparator(int size) {
        JLabel separator = new JLabel();
        separator.setBorder(new EmptyBorder(size, 0, 0, 0));

        return separator;
    }

    public static JLabel getTxtLabel(String text, Color color, Font font) {
        JLabel txt = new JLabel(text);
        txt.setFont(font);
        txt.setForeground(color);

        return txt;
    }

    public static JLabel getCenteredTxtLabel(String text, Color color, Font font) {
        JLabel label = UI.getTxtLabel(text, color, font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
