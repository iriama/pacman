package pacman.UI;

import pacman.game.IEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Retro button
 */
public class RetroButton extends JLabel {

    private final Color color;
    private final Font font;
    private final String text;
    private final IEvent onClick;

    public RetroButton(String text, Color color, Font font, IEvent onClick) {
        super();

        this.onClick = onClick;
        this.text = text.toUpperCase();
        this.color = color;
        this.font = font;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setForeground(Color.WHITE);
                setBorder(border(Color.WHITE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setForeground(color);
                setBorder(border(color));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                setForeground(Color.CYAN);
                setBorder(border(Color.CYAN));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setForeground(color);
                setBorder(border(color));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onClick.action();
            }
        });

        build();
    }


    private Border border(Color color) {
        Border margin = new EmptyBorder(4, 4, 4, 4);
        return new CompoundBorder(BorderFactory.createLineBorder(color, 2, true), margin);
    }

    private void build() {
        setText(text);
        setFont(font);
        setForeground(color);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(border(color));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

}
