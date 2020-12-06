package pacman.parsing;

public class Actor {
    public String skinId;
    public String typeId;

    public int speed;

    public Actor(String skinId, String typeId, int speed) {
        this.skinId = skinId;
        this.typeId = typeId;
        this.speed = speed;
    }
}
