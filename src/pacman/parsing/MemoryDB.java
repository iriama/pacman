package pacman.parsing;

import framework.rendering.graphics.SpriteSheet;
import pacman.game.Score;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

/**
 * Memeory database, keeps read files in memory cache
 */
public class MemoryDB {

    private static final int MAX_SCORE_COUNT = 10;
    private static final HashMap<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static final HashMap<String, Preset> presets = new HashMap<>();
    private static final HashMap<String, Map> maps = new HashMap<>();
    private static final HashMap<String, Level> levels = new HashMap<>();
    private static final Vector<Score> scores = new Vector<>();
    private static final Vector<String> multiPresets = new Vector<>();
    private static final Vector<String> multiSkins = new Vector<>();
    private static String multiMapId = "";
    private static int multiLives = 3;
    private static int multiPacSpeed = 2;
    private static int multiGhostSpeed = 2;

    /**
     * Gets a spritesheet from identifier (under ressources/sprites)
     *
     * @param identifier  identifier
     * @param spriteWidth sprite width
     * @return SpriteSheet
     * @throws IOException exception
     */
    public static SpriteSheet getSpriteSheet(String identifier, int spriteWidth) throws IOException {
        if (spriteSheets.containsKey(identifier)) return spriteSheets.get(identifier);
        SpriteSheet spriteSheet = new SpriteSheet("ressources/sprites/" + identifier + ".png", spriteWidth);
        spriteSheets.put(identifier, spriteSheet);
        return spriteSheet;
    }

    /**
     * Gets a key preset from identifier (under ressources/presets)
     *
     * @param identifier identifier
     * @return Preset
     * @throws Exception Exception
     */
    public static Preset loadPreset(String identifier) throws Exception {
        if (presets.containsKey(identifier)) return presets.get(identifier);
        Preset preset = new Preset("ressources/presets/" + identifier + ".txt");
        presets.put(identifier, preset);
        return preset;
    }

    /**
     * Gets a map from identifier (under ressources/maps)
     *
     * @param identifier identifier
     * @return Map
     * @throws Exception Exception
     */
    public static Map getMap(String identifier) throws Exception {
        if (maps.containsKey(identifier)) return maps.get(identifier);
        Map map = new Map("ressources/maps/" + identifier + ".txt");
        maps.put(identifier, map);
        return map;
    }

    /**
     * Gets a level from identifier (under ressources/levels)
     *
     * @param identifier identifier
     * @return Level
     * @throws Exception Exception
     */
    public static Level getLevel(String identifier) throws Exception {
        if (levels.containsKey(identifier)) return levels.get(identifier);
        Level level = new Level("ressources/levels/" + identifier + ".txt");
        levels.put(identifier, level);
        return level;
    }

    /**
     * Gets arcade levels sequence (under ressources/arcade.txt)
     *
     * @return arcade levels sequence
     */
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

        return levels.toArray(new String[0]);
    }

    /**
     * Updates scores to file
     *
     * @param score new score
     */
    public static void updateScores(Score score) {
        boolean modified = false;
        if (scores.size() < MAX_SCORE_COUNT) {
            scores.add(new Score(score.name, score.value));
            scores.sort((a, b) -> b.value - a.value);
            modified = true;
        } else {
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
            StringBuilder str = new StringBuilder();
            for (Score entry : scores) {
                str.append(entry.name).append(" ").append(entry.value).append("\n");
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("ressources/scores.txt"));
                writer.write(str.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private static void setMulti() {
        try {

            BufferedReader reader = new BufferedReader(new FileReader("ressources/multiplayer.txt"));
            multiMapId = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(" ");
                if (line.equals("") || split.length != 2) continue;

                switch (split[0]) {
                    case "skin":
                        multiSkins.add(split[1]);
                        break;
                    case "preset":
                        multiPresets.add(split[1]);
                        break;
                    case "lives":
                        multiLives = Integer.parseInt(split[1]);
                        break;
                    case "pacman_speed":
                        multiPacSpeed = Integer.parseInt(split[1]);
                        break;
                    case "ghost_speed":
                        multiGhostSpeed = Integer.parseInt(split[1]);
                        break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Return multiplayer key presets (under ressources/multiplayer.txt)
     *
     * @return String vector
     */
    public static Vector<String> getMultiPresets() {
        if (multiPresets.size() > 0)
            return multiPresets;

        setMulti();
        return multiPresets;
    }

    /**
     * Return multiplayer map id (under ressources/multiplayer.txt)
     *
     * @return map id
     */
    public static String getMultiMapId() {
        return multiMapId;
    }

    /**
     * Return multiplayer pacman lives count (under ressources/multiplayer.txt)
     *
     * @return lives
     */
    public static int getMultiLives() {
        return multiLives;
    }

    /**
     * Return multiplayer ghosts speed (under ressources/multiplayer.txt)
     *
     * @return speed
     */
    public static int getMultiGhostSpeed() {
        return multiGhostSpeed;
    }

    /**
     * Return multiplayer pacman speed (under ressources/multiplayer.txt)
     *
     * @return speed
     */
    public static int getMultiPacSpeed() {
        return multiPacSpeed;
    }

    /**
     * Return multiplayer skins (under ressources/multiplayer.txt)
     *
     * @return string vector
     */
    public static Vector<String> getMultiSkins() {
        if (multiSkins.size() > 0)
            return multiSkins;

        setMulti();
        return multiSkins;
    }

    /**
     * Return scoreboard
     *
     * @return Score vector
     */
    public static Vector<Score> getScores() {

        if (scores.size() < 1) {
            try {

                File f = new File("ressources/scores.txt");
                if (!f.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    f.createNewFile();
                }

                BufferedReader reader = new BufferedReader(new FileReader(f));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(" ");
                    if (line.equals("") || split.length != 2) continue;
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
