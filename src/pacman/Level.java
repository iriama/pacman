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
                Integer.parseInt(split[2])
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

        public Actor(String skinId, String typeId, int speed) {
            this.skinId = skinId;
            this.typeId = typeId;
            this.speed = speed;
        }
    }

}
