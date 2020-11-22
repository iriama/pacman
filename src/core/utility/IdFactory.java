package core.utility;

public class IdFactory {
    private static int id = Integer.MIN_VALUE;

    public static int nextId() {
        if (id > Integer.MAX_VALUE - 2)
            id = Integer.MIN_VALUE;

        id++;
        return id;
    }
}
