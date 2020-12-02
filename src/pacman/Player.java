package pacman;

import framework.core.Character;
import framework.geometry.Point;
import framework.geometry.Rect;
import framework.input.sources.KeyStateEnum;
import framework.input.sources.Keyboard;
import framework.physics.PhyObject;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import java.util.HashMap;

public class Player {
    private Character character;
    private PlayerDirection direction;
    private int currentVelocityCount;
    private int currentVelocity;
    private int speed;
    private HashMap<PlayerDirection, SpriteSheet> directionsSheet;
    private Keyboard keyboard;
    private PlayerDirection queue;
    private boolean stopped;


    public Player(Character character, int speed) {
        direction = PlayerDirection.RIGHT;
        currentVelocity = 0;
        currentVelocityCount = 0;
        stopped = false;
        queue = null;
        keyboard = new Keyboard();
        directionsSheet = new HashMap<>();
        this.character = character;
        this.speed = speed;
    }

    public PlayerDirection getDirection() {
        return direction;
    }

    public void bindDirection(PlayerDirection direction, SpriteSheet sheet) {
        directionsSheet.put(direction, sheet);
    }

    public Keyboard bindControls(Preset preset) {
        keyboard.mapKey(preset.keyUp, KeyStateEnum.PRESSED, () -> attemptChangeDirection(PlayerDirection.UP));
        keyboard.mapKey(preset.keyDown, KeyStateEnum.PRESSED, () -> attemptChangeDirection(PlayerDirection.DOWN));
        keyboard.mapKey(preset.keyLeft, KeyStateEnum.PRESSED, () -> attemptChangeDirection(PlayerDirection.LEFT));
        keyboard.mapKey(preset.keyRight, KeyStateEnum.PRESSED, () -> attemptChangeDirection(PlayerDirection.RIGHT));

        return keyboard;
    }


    public int getModX(PlayerDirection direction, int step) {
        return direction == PlayerDirection.LEFT ? -step : direction == PlayerDirection.RIGHT ? step : 0;
    }

    public int getModY(PlayerDirection direction, int step) {
        return direction == PlayerDirection.UP ? -step : direction == PlayerDirection.DOWN ? step : 0;
    }

    private Rect nextWallHitbox(PlayerDirection direction) {
        int modX = getModX(direction, 1);
        int modY = getModY(direction, 1);

        return getWallHitbox().extend(modX, modY);
    }

    public boolean willHitWall(PlayerDirection direction) {
        if (stopped) return false;

        Rect nextWallHitbox = nextWallHitbox(direction);
        for (Rect wall: Pacman.game.currentMap.walls) {
            if (nextWallHitbox.intersect(wall)) {
                return true;
            }
        }

        return false;
    }

    private void attemptChangeDirection(PlayerDirection direction) {
        if (willHitWall(direction)) {
            queue = direction;
            return;
        }

        changeDirection(direction);
    }

    public void changeDirection(PlayerDirection direction) {
        stopped = false;
        this.direction = direction;
        getSprite().resume();
        queue = null;
        character.getGraphicObject().getSprite().setSpriteSheet(
                directionsSheet.get(direction)
        );
    }

    private void stop() {
        //direction = PlayerDirection.NONE;
        stopped = true;
        Sprite sprite = getSprite();
        sprite.pause();
        sprite.setFrame(Math.min(sprite.getFrameCount() - 1, 3));
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

        if (onTile()) {
            if (queue != null) {
                attemptChangeDirection(queue);
            }
            if (!stopped && willHitWall(direction)) {
                stop();
            }
        }


        updateCurrentVelocity();
        PhyObject phyObject = character.getPhyObject();
        if (stopped) {
            phyObject.setVelocity(0, 0);
        }
        else switch (direction) {
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
                Pacman.PLAYER_SIZE,
                hitbox.getY(),
                Pacman.PLAYER_SIZE
        );
    }


    public Point getPosition() {
        return character.getPhyObject().getPosition();
    }

    public Point getCentredPosition() {
        return getHitbox().getCenter();
    }

    public boolean onTile() {
        Point position = getPosition();
        return position.getX() % Pacman.STEP_SIZE == 0 && position.getY() % Pacman.STEP_SIZE == 0;
    }
}
