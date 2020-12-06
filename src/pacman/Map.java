package pacman;

import framework.geometry.Point;
import framework.geometry.Rect;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Vector;

public class Map {
    public BufferedImage image;
    public int width;
    public int height;
    public Vector<Rect> walls;
    public Vector<Rect> specialJetons;
    public Rect prisonWall;
    public Point pacmanSpawn;
    public Point ghostSpawn;
    public Point ghostPrison;

    public Map(String path) throws Exception {
        walls = new Vector<>();
        specialJetons = new Vector<>();
        pacmanSpawn = new Point();
        ghostSpawn = new Point();
        ghostPrison = new Point();
        BufferedReader reader = new BufferedReader(new FileReader(path));

        String imgName = reader.readLine();
        if (imgName == null) throw new Exception(path + " wrong format at line 1");

        String imgPath = path.replace(Paths.get(path).getFileName().toString(), imgName);
        image = ImageIO.read(new File(imgPath));

        String[] dimensions = reader.readLine().split(" ");
        if (dimensions.length != 2) throw new Exception(path + " wrong format at line 2");

        width = Integer.parseInt(dimensions[0]);
        height = Integer.parseInt(dimensions[1]);

        String line;
        while((line = reader.readLine()) != null) {
            String[] split = line.split(" ");
            String type = split[0];
            Rect rect = new Rect(
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[3]),
                    Integer.parseInt(split[2]),
                    Integer.parseInt(split[4])
            );

            if (type.equals("special_jeton"))
                specialJetons.add(rect);
            else if (type.equals("wall"))
                walls.add(rect);
            else if (type.equals("prison_wall"))
                prisonWall = rect;
            else if (type.equals("pacman_spawn"))
                pacmanSpawn.set(rect.getX(), rect.getY());
            else if (type.equals("ghost_spawn"))
                ghostSpawn.set(rect.getX(), rect.getY());
            else if (type.equals("ghost_prison"))
                ghostPrison.set(rect.getX(), rect.getY());
        }
    }
}
