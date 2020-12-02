package pacman;

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

    public Map(String path) throws Exception {
        walls = new Vector<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));

        String imgName = reader.readLine();
        if (imgName == null) throw new Exception(path + " wrong format at line 1");

        String imgPath = path.replace(Paths.get(path).getFileName().toString(), imgName);
        image = ImageIO.read(new File(imgPath));

        String[] dimensions = reader.readLine().split(" ");
        if (dimensions.length != 2) throw new Exception(path + " wrong format at line 2");

        width = Integer.parseInt(dimensions[0]);
        height = Integer.parseInt(dimensions[1]);

        int n = Integer.parseInt(reader.readLine());

        for (int i = 0; i < n; i++) {
            String[] split = reader.readLine().split(" ");
            walls.add(new Rect(
                    Integer.parseInt(split[0]),
                    Integer.parseInt(split[2]),
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[3]))
            );
        }
    }
}
