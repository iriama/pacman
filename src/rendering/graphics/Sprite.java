package rendering.graphics;

import java.awt.image.BufferedImage;

/**
 * Generic sprite of the game
 * Can be still or animated
 */
public class Sprite {

    private final SpriteSheet spriteSheet;
    private int currentFrame;

    private ESpriteState state;
    private int[] frames;
    private int playIndex;
    private int delay;
    private long nextFrameTime;

    public Sprite(SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
        currentFrame = 0;
        state = ESpriteState.PAUSED;
    }

    private int[] getRange(int start, int end) {
        int[] t = new int[end - start + 1];
        for (int i = 0; i < t.length; i++)
            t[i] = i + start;

        return t;
    }

    /**
     * Sets the current frame of the sprite
     * @param frame frame
     */
    public void setFrame(int frame) {
        currentFrame = frame;
        state = ESpriteState.PAUSED; // paused
    }

    /**
     * Play a sequence of frames
     * @param frames sequence of frames
     * @param delay delay between each frame
     */
    public void play(int[] frames, int delay) {
        this.frames = frames;
        playIndex = 0;
        this.delay = delay;
        nextFrameTime = System.currentTimeMillis() + delay;
        state = ESpriteState.PLAYING;
    }

    /**
     * Play a range of frames
     * @param startFrame the first frame
     * @param endFrame the last frame
     * @param delay delay between each frame
     */
    public void play(int startFrame, int endFrame, int delay) {
        play(getRange(startFrame, endFrame), delay);
    }

    /**
     * Play from the current frame to the last frame
     * @param delay delay between each frame
     */
    public void play(int delay) {
        play(getRange(currentFrame, spriteSheet.getSpriteCount() - 1), delay);
    }

    /**
     * Play and loop a sequence of frames
     * @param frames sequence of frames
     * @param delay delay between each frame
     */
    public void loop(int[] frames, int delay) {
        play(frames, delay);
        this.state = ESpriteState.LOOP;
    }

    /**
     * Play and loop a range of frames
     * @param startFrame the first frame
     * @param endFrame the last frame
     * @param delay delay between each frame
     */
    public void loop(int startFrame, int endFrame, int delay) {
        loop(getRange(startFrame, endFrame), delay);
    }

    /**
     * Play and loop from the current frame to the last frame
     * @param delay delay between each frame
     */
    public void loop(int delay) {
        loop(getRange(currentFrame, spriteSheet.getSpriteCount() - 1), delay);
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
                    playIndex = 0;
                    state = ESpriteState.PAUSED;
                }
                break;
        }


        currentFrame = frames[playIndex];
        nextFrameTime = System.currentTimeMillis() + delay;
    }

    /**
     * Return the current Image (snapshot) of the current frame
     * @return Image of the current sprite frame
     */
    public BufferedImage getImage() {
        updateSprite();
        return spriteSheet.getSpriteImage(currentFrame);
    }

}
