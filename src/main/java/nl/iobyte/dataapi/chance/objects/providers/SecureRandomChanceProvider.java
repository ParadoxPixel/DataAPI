package nl.iobyte.dataapi.chance.objects.providers;

import nl.iobyte.dataapi.chance.interfaces.IChanceProvider;
import java.security.SecureRandom;
import java.util.Random;

public class SecureRandomChanceProvider implements IChanceProvider {

    private final Random random = new SecureRandom();

    /**
     * Get number bellow upper range
     * @param upper Integer
     * @return Integer
     */
    public int get(int upper) {
        return random.nextInt(upper);
    }

}
