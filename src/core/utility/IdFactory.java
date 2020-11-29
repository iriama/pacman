package core.utility;

/**
 * Generates unique Ids
 */
public class IdFactory {
    private static int id = Integer.MIN_VALUE;

    /**
     * Return unique id
     *
     * @return next unique id
     */
    public static int nextId() {
        if (id > Integer.MAX_VALUE - 2)
            id = Integer.MIN_VALUE;

        id++;
        return id;
    }
}
