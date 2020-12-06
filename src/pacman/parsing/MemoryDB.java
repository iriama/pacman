package pacman.parsing;

import framework.rendering.graphics.SpriteSheet;
import pacman.Score;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class MemoryDB {

    private static HashMap<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static HashMap<String, Preset> presets = new HashMap<>();
    private static HashMap<String, Map> maps = new HashMap<>();
    private static HashMap<String, Level> levels = new HashMap<>();
    private static Vector<Score> scores = new Vector<>();
    private static final int MAX_SCORE_COUNT = 10;

    public static SpriteSheet getSpriteSheet(String identifier, int spriteWidth) throws IOException {
        if (spriteSheets.containsKey(identifier)) return spriteSheets.get(identifier);
        SpriteSheet spriteSheet = new SpriteSheet("ressources/sprites/" + identifier + ".png", spriteWidth);
        spriteSheets.put(identifier, spriteSheet);
        return spriteSheet;
    }

    public static Preset loadPreset(String identifier) throws Exception {
        if (presets.containsKey(identifier)) return presets.get(identifier);
        Preset preset = new Preset("ressources/presets/" + identifier + ".txt");
        presets.put(identifier, preset);
        return preset;
    }

    public static Map getMap(String identifier) throws Exception {
        if (maps.containsKey(identifier)) return maps.get(identifier);
        Map map = new Map("ressources/maps/" + identifier + ".txt");
        maps.put(identifier, map);
        return map;
    }

    public static Level getLevel(String identifier) throws Exception {
        if (levels.containsKey(identifier)) return levels.get(identifier);
        Level level = new Level("ressources/levels/" + identifier + ".txt");
        levels.put(identifier, level);
        return level;
    }

    public static String[] getArcadeSequence() {
        Vector<String> levels = new Vector<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ressources/arcade.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                levels.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return levels.toArray(new String[levels.size()]);
    }

    public static void updateScores(Score score) {
        boolean modified = false;
        if (scores.size() < MAX_SCORE_COUNT) {
            scores.add(new Score(score.name, score.value));
            scores.sort((a, b) -> b.value - a.value);
            modified = true;
        }
        else {
            for (int i = 0; i < scores.size(); i++) {
                Score curr = scores.get(i);
                if (score.value > curr.value) {
                    scores.set(i, new Score(score.name, score.value));
                    modified = true;
                    break;
                }
            }
        }

        if (modified) {
            String str = "";
            for (Score entry: scores) {
                str += entry.name + " " + entry.value + "\n";
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("ressources/scores.txt"));
                writer.write(str);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static Vector<Score> getScores() {

        if (scores.size() < 1) {
            try {

                File f = new File("ressources/scores.txt");
                if (!f.exists()) {
                    f.createNewFile();
                }

                BufferedReader reader = new BufferedReader(new FileReader(f));

                String line;
                while((line = reader.readLine()) != null) {
                    String[] split = line.split(" ");
                    if (line == "" || split.length != 2) continue;
                    scores.add(new Score(split[0], Integer.parseInt(split[1])));
                }

                scores.sort((a, b) -> b.value - a.value);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return scores;
    }

}
