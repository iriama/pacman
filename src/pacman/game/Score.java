package pacman.game;

import java.util.Objects;

/**
 * Player score
 */
public class Score {

    public String name;
    public int value;


    public Score(String name, int value) {
        // truncate
        if (name.length() > 10)
            name = name.substring(0, 9);

        // sanitize
        name = name.replaceAll(" ", "_");

        this.name = name.toUpperCase();
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return value == score.value &&
                Objects.equals(name, score.name);
    }
}
