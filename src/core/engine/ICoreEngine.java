package core.engine;

import core.game.Character;

public interface ICoreEngine {
    Character addCharacter(Character character);

    void removeCharacter(Character character);
}
