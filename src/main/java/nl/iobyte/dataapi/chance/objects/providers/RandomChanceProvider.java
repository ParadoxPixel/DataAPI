package nl.iobyte.dataapi.chance.objects.providers;

import nl.iobyte.dataapi.chance.interfaces.IChanceProvider;
import java.util.Random;

public class RandomChanceProvider implements IChanceProvider {

    private final Random random = new Random();

    /**
     * Get number bellow upper range
     * @param upper Integer
     * @return Integer
     */
    public int get(int upper) {
        return random.nextInt(upper);
    }

}
