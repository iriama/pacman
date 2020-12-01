package pacman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

class Actor {
    public String modelId;
    public int speed;
    public int x;
    public int y;
    public int width;
    public int height;


    public Actor(String modelId, int speed, int x, int y, int width, int height) {
        this.modelId = modelId;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}

public class Level {
    public String mapIdentifier;
    public Vector<Actor> pacmans;
    public Vector<Actor> ghosts;

    private Actor parseActor(String str) {
        String[] split = str.split(" ");
        return new Actor(
                split[0],
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Integer.parseInt(split[4]),
                Integer.parseInt(split[5])
        );
    }
    public Level(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        mapIdentifier = reader.readLine();
        int pacCount = Integer.parseInt(reader.readLine());

        pacmans = new Vector<>(pacCount);
        for (int i=0; i<pacCount; i++) {
            pacmans.add(parseActor(reader.readLine()));
        }

        int ghostCount = Integer.parseInt(reader.readLine());
        ghosts = new Vector<>(ghostCount);
        for (int i=0; i<ghostCount; i++) {
            ghosts.add(parseActor(reader.readLine()));
        }


    }

    @Override
    public String toString() {
        return "Level{" +
                "mapIdentifier='" + mapIdentifier + '\'' +
                ", pacmans=" + pacmans +
                ", ghosts=" + ghosts +
                '}';
    }
}
