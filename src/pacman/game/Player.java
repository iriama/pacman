package pacman.game;

import framework.core.Character;
import framework.geometry.Point;
import framework.geometry.Rect;
import framework.input.sources.KeyStateEnum;
import framework.input.sources.Keyboard;
import framework.physics.PhyObject;
import framework.rendering.graphics.Sprite;
import framework.rendering.graphics.SpriteSheet;
import pacman.parsing.Preset;

import java.util.HashMap;

/**
 * Generic game character
 */
public class Player {
    protected HashMap<PlayerDirection, SpriteSheet> directionsSheet;
    private final Character character;
    private PlayerDirection direction;
    private int currentVelocityCount;
    private int currentVelocity;
    private int speed;
    private final Keyboard keyboard;
    private PlayerDirection queue;
    private boolean stopped;
    private boolean disabled;
    private boolean autoPilot = false;


    public Player(Character character, int speed) {
        direction = PlayerDirection.UP;
        currentVelocity = 0;
        currentVelocityCount = 0;
        stopped = true;
        disabled = false;
        queue = null;
        keyboard = new Keyboard();
        directionsSheet = new HashMap<>();
        this.character = character;
        this.speed = speed;
    }

    /**
     * Set character speed
     *
     * @param speed speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Return the CoreEngine Character instance
     *
     * @return Character
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Returns the player direction
     *
     * @return PlayerDirection
     */
    public PlayerDirection getDirection() {
        return direction;
    }

    /**
     * Binds a direction to a spritesheet
     *
     * @param direction direction
     * @param sheet     sheet
     */
    public void bindDirection(PlayerDirection direction, SpriteSheet sheet) {
        directionsSheet.put(direction, sheet);
    }

    /**
     * Initialise the character keyboard
     *
     * @param preset key preset
     * @return Keyboard
     */
    public Keyboard bindControls(Preset preset) {
        keyboard.mapKey(preset.keyUp, KeyStateEnum.PRESSED, () -> attemptChangeDirection(PlayerDirection.UP));
        keyboard.mapKey(preset.keyDown, KeyStateEnum.PRESSED, () -> attemptChangeDirection(PlayerDirection.DOWN));
        keyboard.mapKey(preset.keyLeft, KeyStateEnum.PRESSED, () -> attemptChangeDirection(PlayerDirection.LEFT));
        keyboard.mapKey(preset.keyRight, KeyStateEnum.PRESSED, () -> attemptChangeDirection(PlayerDirection.RIGHT));

        return keyboard;
    }

    /**
     * Return the next X based on direction
     *
     * @param direction PlayerDirection
     * @param step      step
     * @return next X
     */
    public int getModX(PlayerDirection direction, int step) {
        return direction == PlayerDirection.LEFT ? -step : direction == PlayerDirection.RIGHT ? step : 0;
    }

    /**
     * Return the next Y based on direction
     *
     * @param direction PlayerDirection
     * @param step      step
     * @return next Y
     */
    public int getModY(PlayerDirection direction, int step) {
        return direction == PlayerDirection.UP ? -step : direction == PlayerDirection.DOWN ? step : 0;
    }

    /**
     * Return the next hitbox based on direction
     *
     * @param direction PlayerDirection
     * @return next hitbox
     */
    protected Rect nextHitbox(PlayerDirection direction) {
        int modX = getModX(direction, 1);
        int modY = getModY(direction, 1);

        return getWallHitbox().extend(modX, modY);
    }

    /**
     * Determines if will hit a wall on direction change
     *
     * @param direction PlayerDirection
     * @return boolean
     */
    public boolean willHitWall(PlayerDirection direction) {
        if (stopped) return false;

        Rect nextHitbox = nextHitbox(direction);

        // Walls
        for (Rect wall : Game.current.map.walls) {
            if (nextHitbox.intersect(wall)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if player is disabled
     *
     * @return boolean
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Sets disabled status
     *
     * @param disabled boolean
     */
    public void setDisabled(boolean disabled) {
        if (disabled) {
            queue = null;
            setVelocity(0, 0);
        }
        this.disabled = disabled;
    }

    /**
     * Sets autopilot (multi mode)
     *
     * @param autoPilot boolean
     */
    public void setAutoPilot(boolean autoPilot) {
        this.autoPilot = autoPilot;
    }

    /**
     * Attempt to change direction, queue otherwise
     *
     * @param direction PlayerDirection
     */
    public void attemptChangeDirection(PlayerDirection direction) {
        if (autoPilot || isDisabled()) return;

        if (willHitWall(direction)) {
            queue = direction;
            return;
        }

        changeDirection(direction);
    }

    /**
     * Sets the PhyObject velocity
     *
     * @param x x-velocity
     * @param y y-velocity
     */
    public void setVelocity(int x, int y) {
        getCharacter().getPhyObject().setVelocity(x, y);
    }

    /**
     * Changes the direction (no queue)
     *
     * @param direction PlayerDirection
     */
    public void changeDirection(PlayerDirection direction) {
        resume();
        this.direction = direction;
        queue = null;
        character.getGraphicObject().getSprite().setSpriteSheet(
                directionsSheet.get(direction)
        );
    }

    /**
     * Stops the sprite
     */
    protected void stop() {
        stopped = true;
        Sprite sprite = getSprite();
        sprite.pause();
        sprite.setFrame(Math.min(sprite.getFrameCount() - 1, 3));
    }

    /**
     * Resume the sprite
     */
    protected void resume() {
        stopped = false;
        getSprite().resume();
    }

    /**
     * Sets velocity based on speed
     */
    private void updateCurrentVelocity() {
        if (currentVelocityCount >= 100 / speed) {
            currentVelocityCount = 0;
            currentVelocity = 1;
            return;
        }
        currentVelocityCount++;
        currentVelocity = 0;
    }


    /**
     * Common logic for all characters (walls, teleport...)
     */
    public void update() {
        if (isDisabled()) return;

        // Teleport
        if (getX() <= -Game.PLAYER_SIZE) {
            setX(Game.current.map.width);
            return;
        }
        if (getX() >= Game.current.map.width) {
            setX(-Game.PLAYER_SIZE);
            return;
        }
        if (getY() <= -Game.PLAYER_SIZE) {
            setY(Game.current.map.height);
            return;
        }
        if (getY() >= Game.current.map.height) {
            setY(-Game.PLAYER_SIZE);
            return;
        }

        // Attempt to change velocity
        if (onTile()) {
            if (queue != null) {
                attemptChangeDirection(queue);
            }
            if (!stopped && willHitWall(direction)) {
                stop();
            }
        }

        // Actually change direction speed
        updateCurrentVelocity();
        PhyObject phyObject = character.getPhyObject();
        if (stopped) {
            phyObject.setVelocity(0, 0);
        } else switch (direction) {
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


    /**
     * Returns the sprite
     *
     * @return Sprite
     */
    protected Sprite getSprite() {
        return character.getGraphicObject().getSprite();
    }

    /**
     * Return the hitobject
     *
     * @return Rect
     */
    public Rect getHitbox() {
        return character.getPhyObject().getHitbox();
    }

    /**
     * Return the wall hitbox (slightly bigger)
     *
     * @return Rect
     */
    public Rect getWallHitbox() {
        Rect hitbox = getHitbox();
        return new Rect(
                hitbox.getX(),
                Game.PLAYER_SIZE,
                hitbox.getY(),
                Game.PLAYER_SIZE
        );
    }

    /**
     * Returns the effective hitbox (slightly smaller for more challenge)
     *
     * @return Rect
     */
    public Rect getEffectiveHitbox() {
        Rect base = getHitbox();

        return new Rect(
                base.getX() + Game.STEP_SIZE,
                base.getWidth() / 2,
                base.getY() + Game.STEP_SIZE,
                base.getHeight() / 2
        );
    }

    /**
     * Return the player position
     *
     * @return Point
     */
    public Point getPosition() {
        return character.getPhyObject().getPosition();
    }

    /**
     * Sets the player position
     *
     * @param position Point
     */
    public void setPosition(Point position) {
        setX(position.getX());
        setY(position.getY());
    }

    /**
     * Gets X position
     *
     * @return X position
     */
    public int getX() {
        return getPosition().getX();
    }

    /**
     * Sets the X position
     *
     * @param x X position
     */
    public void setX(int x) {
        getPosition().setX(x);
    }

    /**
     * Gets the Y position
     *
     * @return Y position
     */
    public int getY() {
        return getPosition().getY();
    }

    /**
     * Sets the Y position
     *
     * @param y Y position
     */
    public void setY(int y) {
        getPosition().setY(y);
    }

    /**
     * Gets the centred position of hibox
     *
     * @return Point
     */
    public Point getCentredPosition() {
        return getHitbox().getCenter();
    }

    /**
     * Determines if the player is on a valid tile
     *
     * @return boolean
     */
    public boolean onTile() {
        Point position = getPosition();
        return position.getX() % Game.STEP_SIZE == 0 && position.getY() % Game.STEP_SIZE == 0;
    }
}
