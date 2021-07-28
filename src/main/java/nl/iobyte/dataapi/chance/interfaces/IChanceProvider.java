package nl.iobyte.dataapi.chance.interfaces;

public interface IChanceProvider {

    /**
     * Get number bellow upper range
     * @param upper Integer
     * @return Integer
     */
    int get(int upper);

}
