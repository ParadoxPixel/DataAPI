package nl.iobyte.dataapi.data.objects;

import nl.iobyte.dataapi.data.interfaces.IData;

public class Data<T> implements IData<T> {

    private final T id;

    public Data(T id) {
        this.id = id;
    }

    /**
     * Get identifier
     * @return T
     */
    public T getID() {
        return id;
    }

}
