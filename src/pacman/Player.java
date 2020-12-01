package pacman;

import framework.core.Character;
import framework.geometry.Point;
import framework.geometry.Rect;
import framework.input.sources.KeyStateEnum;
import framework.input.sources.Keyboard;
import framework.physics.PhyObject;
import framework.rendering.GraphicObject;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import java.util.HashMap;

enum PlayerDirection {
    NONE,
    UP,
    DOWN,
    RIGHT,
    LEFT
}

public class Player {
    public Character character;
    public PlayerDirection direction;
    private int currentVelocityCount;
    private int currentVelocity;
    public int speed;
    HashMap<PlayerDirection, SpriteSheet> directionsSheet;
    private Keyboard keyboard;
    private PlayerDirection queue;


    public Player(Character character, int speed) {
        direction = PlayerDirection.NONE;
        currentVelocity = 0;
        currentVelocityCount = 0;
        queue = null;
        keyboard = new Keyboard();
        directionsSheet = new HashMap<>();
        this.character = character;
        this.speed = speed;
    }

    public void bindDirection(PlayerDirection direction, SpriteSheet sheet) {
        directionsSheet.put(direction, sheet);
    }

    public Keyboard bindControls(Preset preset) {
        keyboard.mapKey(preset.keyUp, KeyStateEnum.PRESSED, () -> changeDirection(PlayerDirection.UP));
        keyboard.mapKey(preset.keyDown, KeyStateEnum.PRESSED, () -> changeDirection(PlayerDirection.DOWN));
        keyboard.mapKey(preset.keyLeft, KeyStateEnum.PRESSED, () -> changeDirection(PlayerDirection.LEFT));
        keyboard.mapKey(preset.keyRight, KeyStateEnum.PRESSED, () -> changeDirection(PlayerDirection.RIGHT));

        return keyboard;
    }

    private Rect nextWallHitbox(PlayerDirection direction) {
        int modX = direction == PlayerDirection.LEFT ? -1 : direction == PlayerDirection.RIGHT ? 1 : 0;
        int modY = direction == PlayerDirection.UP ? -1 : direction == PlayerDirection.DOWN ? 1 : 0;

        return getWallHitbox().extend(modX, modY);
    }

    private boolean willHitWall(PlayerDirection direction) {
        if (direction == PlayerDirection.NONE) return false;

        Rect nextWallHitbox = nextWallHitbox(direction);
        for (Rect wall: Pacman.game.currentMap.walls) {
            if (nextWallHitbox.intersect(wall)) {
                return true;
            }
        }

        return false;
    }

    public void changeDirection(PlayerDirection direction) {
        PhyObject phyObject = character.getPhyObject();

        if (willHitWall(direction)) {
            queue = direction;
            return;
        }

        this.direction = direction;
        getSprite().resume();
        queue = null;
        character.getGraphicObject().getSprite().setSpriteSheet(
                directionsSheet.get(direction)
        );
    }

    private void updateCurrentVelocity() {
        if (currentVelocityCount >= 100/speed) {
            currentVelocityCount = 0;
            currentVelocity = 1;
            return;
        }
        currentVelocityCount++;
        currentVelocity = 0;
    }


    public void update() {
        updateCurrentVelocity();

        //System.out.println(speed);

        if (queue != null) {
            changeDirection(queue);
        }

        // stop if will hit wall
        PhyObject phyObject = character.getPhyObject();
        if (direction != PlayerDirection.NONE && willHitWall(direction)) {
            direction = PlayerDirection.NONE;
            Sprite sprite = getSprite();
            sprite.pause();
            sprite.setFrame(Math.min(sprite.getFrameCount() - 1, 3));
        }

        switch (direction) {
            case NONE:
                phyObject.setVelocity(0, 0);
                break;
            case UP:
                phyObject.setVelocity(0, -currentVelocity);
                break;
            case DOWN:
                phyObject.setVelocity(0, currentVelocity);
                break;
            case RIGHT:
                phyObject.setVelocity(currentVelocity, 0);
                break;
            case LEFT:
                phyObject.setVelocity(-currentVelocity, 0);
                break;
        }
    }


    private Sprite getSprite() {
        return character.getGraphicObject().getSprite();
    }

    public Rect getHitbox() {
        return character.getPhyObject().getHitbox();
    }

    public Rect getWallHitbox() {
        Rect hitbox = getHitbox();
        return new Rect(
                hitbox.getX(),
                32,
                hitbox.getY(),
                32
        );
    }

    public Point getPosition() {
        return character.getPhyObject().getPosition();
    }
}
