package pacman;

import framework.rendering.graphics.SpriteSheet;

import java.io.IOException;
import java.util.HashMap;

public class MemoryDB {

    private static HashMap<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static HashMap<String, Preset> presets = new HashMap<>();
    private static HashMap<String, Map> maps = new HashMap<>();
    private static HashMap<String, Level> levels = new HashMap<>();

    public static SpriteSheet getSpriteSheet(String identifier, int spriteWidth) throws IOException {
        if (spriteSheets.containsKey(identifier)) return spriteSheets.get(identifier);
        SpriteSheet spriteSheet = new SpriteSheet("ressources/sprites/" + identifier + ".png", spriteWidth);
        spriteSheets.put(identifier, spriteSheet);
        return spriteSheet;
    }

    public static Preset loadPreset(String identifier) throws Exception {
        if (presets.containsKey(identifier)) return presets.get(identifier);
        Preset preset = new Preset("ressources/presets/" + identifier + ".packeys");
        presets.put(identifier, preset);
        return preset;
    }

    public static Map getMap(String identifier) throws Exception {
        if (maps.containsKey(identifier)) return maps.get(identifier);
        Map map = new Map("ressources/maps/" + identifier + ".pacmap");
        maps.put(identifier, map);
        return map;
    }

    public static Level getLevel(String identifier) throws Exception {
        if (levels.containsKey(identifier)) return levels.get(identifier);
        Level level = new Level("ressources/levels/" + identifier + ".paclevel");
        levels.put(identifier, level);
        return level;
    }

}
