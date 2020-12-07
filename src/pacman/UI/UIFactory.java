package pacman.UI;

import pacman.utility.FontsEngine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * UIFactory utility class
 */
public class UIFactory {

    public static Font getSizedFont(String fontId, int size) {
        return new Font(FontsEngine.getFontName(fontId), Font.PLAIN, size);
    }

    /**
     * Horizontal separator label
     *
     * @param size size
     * @return JLabel label
     */
    public static JLabel getSeparator(int size) {
        JLabel separator = new JLabel();
        separator.setBorder(new EmptyBorder(size, 0, 0, 0));

        return separator;
    }

    /**
     * Text label
     *
     * @param text  text
     * @param color color
     * @param font  font
     * @return JLabel
     */
    public static JLabel getTxtLabel(String text, Color color, Font font) {
        JLabel txt = new JLabel(text);
        txt.setFont(font);
        txt.setForeground(color);

        return txt;
    }

    /**
     * Centred text label
     *
     * @param text  text
     * @param color color
     * @param font  font
     * @return JLabel
     */
    public static JLabel getCenteredTxtLabel(String text, Color color, Font font) {
        JLabel label = UIFactory.getTxtLabel(text, color, font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
