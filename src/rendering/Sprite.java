package rendering;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.image.BufferedImage;

enum SpriteState {
    PAUSED,
    PLAYING,
    LOOP;
}

public class Sprite {

    private SpriteSheet spriteSheet;
    private int currentFrame;

    private SpriteState state;
    private int[] frames;
    private int playIndex;
    private int speed;
    private long nextFrameTime;


    public Sprite(SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
        currentFrame = 0;
        state = SpriteState.PAUSED;
    }

    private int[] getRange(int start, int end) {
        int[] t = new int[end - start + 1];
        for (int i = 0; i<t.length; i++)
            t[i] = i + start;

        return t;
    }

    public void setFrame(int frame) {
        currentFrame = frame;
        state = SpriteState.PAUSED; // paused
    }

    public void play(int[] frames, int speed) {
        this.frames = frames;
        state = SpriteState.PLAYING;
        playIndex = 0;
        this.speed = speed;
        nextFrameTime = System.currentTimeMillis() + speed;
    }

    public void play(int startFrame, int endFrame, int speed) {
        play(getRange(startFrame, endFrame), speed);
    }

    public void play() {
        play(getRange(currentFrame, spriteSheet.getSpriteCount() - 1), speed);
    }

    public void loop(int[] frames, int speed) {
        this.frames = frames;
        this.state = SpriteState.LOOP;
        playIndex = 0;
        this.speed = speed;
        nextFrameTime = System.currentTimeMillis() + speed;
    }

    public void loop(int startFrame, int endFrame, int speed) {
        loop(getRange(startFrame, endFrame), speed);
    }

    public BufferedImage getImage() {

        if (state != SpriteState.PAUSED && System.currentTimeMillis() >= nextFrameTime) {
            if (playIndex + 1 >= frames.length) {
                if (state == SpriteState.LOOP)
                    playIndex = 0;
                else
                    state = SpriteState.PAUSED;
            } else {
                playIndex++;
            }

            currentFrame = frames[playIndex];
            nextFrameTime = System.currentTimeMillis() + speed;
        }

        BufferedImage image = spriteSheet.getImage();
        int width = spriteSheet.getSpriteWidth();
        System.out.println("x=" + currentFrame * width + " ; w=" +  width + " ; h=" + image.getHeight());
        return spriteSheet.getImage().getSubimage(currentFrame*width, 0, width, image.getHeight());
    }

}
