package pacman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class Level {

    public String mapIdentifier;
    public Actor pacman;
    public Vector<Actor> ghosts;

    public Level(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        mapIdentifier = reader.readLine();
        pacman = parseActor(reader.readLine());

        int ghostCount = Integer.parseInt(reader.readLine());
        ghosts = new Vector<>(ghostCount);
        for (int i = 0; i < ghostCount; i++) {
            ghosts.add(parseActor(reader.readLine()));
        }


    }

    private Actor parseActor(String str) {
        String[] split = str.split(" ");
        return new Actor(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Integer.parseInt(split[4]),
                Integer.parseInt(split[5]),
                Integer.parseInt(split[6])
        );
    }

    @Override
    public String toString() {
        return "Level{" +
                "mapIdentifier='" + mapIdentifier + '\'' +
                ", pacman=" + pacman +
                ", ghosts=" + ghosts +
                '}';
    }

    class Actor {
        public String skinId;
        public String typeId;

        public int speed;
        public int x;
        public int y;
        public int width;
        public int height;


        public Actor(String skinId, String typeId, int speed, int x, int y, int width, int height) {
            this.skinId = skinId;
            this.typeId = typeId;
            this.speed = speed;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

}
