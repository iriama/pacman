package core.game;

import physics.game.IPhyObject;
import rendering.game.IGraphicObject;

public class Character implements ICharacter {

    private IGraphicObject graphicObject;
    private IPhyObject phyObject;
    private int id;

    public Character(IGraphicObject graphicObject, IPhyObject phyObject, int id) {
        this.graphicObject = graphicObject;
        this.phyObject = phyObject;
        this.id = id;
    }


    /**
     * Returns the graphic object instance of the character
     *
     * @return graphic object instance
     */
    public IGraphicObject getGraphicObject() {
        return graphicObject;
    }

    /**
     * Returns the physical object instance of the character
     *
     * @return physical object instance
     */
    public IPhyObject getPhyObject() {
        return phyObject;
    }

    /**
     * Returns the id of the character
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return id == character.id;
    }

    @Override
    public String toString() {
        return "Character{" +
                "graphicObject=" + graphicObject +
                ", phyObject=" + phyObject +
                ", id=" + id +
                '}';
    }
}
