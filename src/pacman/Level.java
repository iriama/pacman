package pacman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class Level {

    public String mapIdentifier;
    public Actor pacman;
    public Vector<Actor> ghosts;
    public int chaseDuration;
    public int scatterDuration;

    public Level(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        mapIdentifier = reader.readLine();
        String[] delays = reader.readLine().split(" ");
        chaseDuration = Integer.parseInt(delays[0]);
        scatterDuration = Integer.parseInt(delays[1]);
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
