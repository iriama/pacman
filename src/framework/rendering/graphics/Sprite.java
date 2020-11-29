package framework.rendering.graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Generic sprite of the game
 * Can be still or animated
 */
public class Sprite {

    private SpriteSheet spriteSheet;
    private int currentFrame;

    private ESpriteState state;
    private int[] frames;
    private int playIndex;
    private int delay;
    private long nextFrameTime;

    private ESpriteOrientation orientationX;
    private ESpriteOrientation orientationY;
    private float scale;
    private float orientationAngle;

    private ISpriteEvent onFinish;


    public Sprite(SpriteSheet spriteSheet) {
        onFinish = null;
        currentFrame = 0;
        this.spriteSheet = spriteSheet;
        state = ESpriteState.PAUSED;
        scale = 1;
        orientationX = ESpriteOrientation.INITIAL;
        orientationY = ESpriteOrientation.INITIAL;
        orientationAngle = 0;
    }

    private int[] getRange(int start, int end) {
        int[] t = new int[end - start + 1];
        for (int i = 0; i < t.length; i++)
            t[i] = i + start;

        return t;
    }

    /**
     * Replaces the current spritesheet
     *
     * @param spriteSheet new sprite sheet
     */
    public void setSpriteSheet(SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    /**
     * Sets the current frame of the sprite
     *
     * @param frame frame
     */
    public void setFrame(int frame) {
        currentFrame = frame;
        state = ESpriteState.PAUSED; // paused
    }

    /**
     * Returns the current frame of the sprite
     *
     * @return current frame
     */
    public int getCurrentFrame() {
        return currentFrame;
    }

    /**
     * Play a sequence of frames
     *
     * @param frames sequence of frames
     * @param delay  delay between each frame
     */
    public void play(int[] frames, int delay) {
        this.frames = frames;
        playIndex = 0;
        currentFrame = frames[playIndex];
        this.delay = delay;
        nextFrameTime = System.currentTimeMillis() + delay;
        state = ESpriteState.PLAYING;
    }

    /**
     * Play a range of frames
     *
     * @param startFrame the first frame
     * @param endFrame   the last frame
     * @param delay      delay between each frame
     */
    public void play(int startFrame, int endFrame, int delay) {
        play(getRange(startFrame, endFrame), delay);
    }

    /**
     * Play from the current frame to the last frame
     *
     * @param delay delay between each frame
     */
    public void play(int delay) {
        play(getRange(currentFrame, spriteSheet.getSpriteCount() - 1), delay);
    }

    /**
     * Play and loop a sequence of frames
     *
     * @param frames sequence of frames
     * @param delay  delay between each frame
     */
    public void loop(int[] frames, int delay) {
        play(frames, delay);
        this.state = ESpriteState.LOOP;
    }

    /**
     * Play and loop a range of frames
     *
     * @param startFrame the first frame
     * @param endFrame   the last frame
     * @param delay      delay between each frame
     */
    public void loop(int startFrame, int endFrame, int delay) {
        loop(getRange(startFrame, endFrame), delay);
    }

    /**
     * Play and loop from the current frame to the last frame
     *
     * @param delay delay between each frame
     */
    public void loop(int delay) {
        loop(getRange(currentFrame, spriteSheet.getSpriteCount() - 1), delay);
    }

    /**
     * Execute an event when finish playing
     *
     * @param event event to execute
     */
    public void onPlayFinish(ISpriteEvent event) {
        onFinish = event;
    }

    private void updateSprite() {
        // PAUSED
        if (state == ESpriteState.PAUSED || System.currentTimeMillis() < nextFrameTime)
            return;

        switch (state) {
            case LOOP:
                if (playIndex + 1 < frames.length)
                    playIndex++;
                else
                    playIndex = 0;
                break;
            case PLAYING:
                if (playIndex + 1 < frames.length)
                    playIndex++;
                else {
                    state = ESpriteState.PAUSED;
                }
                break;
        }


        currentFrame = frames[playIndex];
        nextFrameTime = System.currentTimeMillis() + delay;

        // Finished playing
        if (onFinish != null && playIndex == frames.length - 1) {
            onFinish.action();
            onFinish = null;
        }
    }

    /**
     * Return the current Image (snapshot) of the current frame
     *
     * @return Image of the current sprite frame
     */
    public BufferedImage getImage() {
        updateSprite();
        BufferedImage img = spriteSheet.getSpriteImage(currentFrame);

        if (scale != 1 || orientationX == ESpriteOrientation.FLIP || orientationY == ESpriteOrientation.FLIP) {
            float orX = orientationX == ESpriteOrientation.FLIP ? -scale : scale;
            float orY = orientationY == ESpriteOrientation.FLIP ? -scale : scale;

            AffineTransform at = new AffineTransform();
            at.scale(orX, orY);
            at.translate(orX > 0 ? 0 : -img.getWidth(), orY > 0 ? 0 : -img.getHeight());
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            img = op.filter(img, null);
        }


        if (orientationAngle > 0) {
            AffineTransform at = new AffineTransform();
            double offset = (img.getWidth() - img.getHeight()) / 2;
            at.rotate(Math.toRadians(orientationAngle), img.getWidth() / 2, img.getHeight() / 2);
            at.translate(offset, offset);
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            img = op.filter(img, null);
        }


        return img;
    }


    /**
     * Returns the X axis orientation
     *
     * @return X axis orientation
     */
    public ESpriteOrientation getOrientationX() {
        return orientationX;
    }

    /**
     * Returns the Y axis orientation
     *
     * @return Y axis orientation
     */
    public ESpriteOrientation getOrientationY() {
        return orientationY;
    }

    /**
     * Flips the sprite image on the X axis
     */
    public void flipX() {
        if (orientationX == ESpriteOrientation.FLIP)
            orientationX = ESpriteOrientation.INITIAL;
        else
            orientationX = ESpriteOrientation.FLIP;
    }

    /**
     * Flips the sprite image on the Y axis
     */
    public void flipY() {
        if (orientationY == ESpriteOrientation.FLIP)
            orientationY = ESpriteOrientation.INITIAL;
        else
            orientationY = ESpriteOrientation.FLIP;
    }

    /**
     * Return the sprite scale
     *
     * @return sprite scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Set the sprite scale
     *
     * @param scale sprite scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Rotate the sprite image
     *
     * @param degree rotation degree
     */
    public void rotate(float degree) {
        orientationAngle = degree;
    }

    /**
     * Returns the rotation angle
     *
     * @return rotation angle
     */
    public float getOrientationAngle() {
        return orientationAngle;
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "currentFrame=" + currentFrame +
                ", state=" + state +
                ", playIndex=" + playIndex +
                ", delay=" + delay +
                ", orientationX=" + orientationX +
                ", orientationY=" + orientationY +
                ", scale=" + scale +
                '}';
    }
}
