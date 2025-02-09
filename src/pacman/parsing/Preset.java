package pacman.parsing;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Key preset parsed from preset file
 */
public class Preset {
    public int keyUp;
    public int keyDown;
    public int keyLeft;
    public int keyRight;

    public Preset(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        keyUp = Integer.parseInt(reader.readLine());
        keyDown = Integer.parseInt(reader.readLine());
        keyLeft = Integer.parseInt(reader.readLine());
        keyRight = Integer.parseInt(reader.readLine());
    }
}
