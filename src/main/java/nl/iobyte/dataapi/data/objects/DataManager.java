package nl.iobyte.dataapi.data.objects;

import nl.iobyte.dataapi.data.DataService;
import nl.iobyte.dataapi.data.interfaces.IData;

public class DataManager<T, S, R extends IData<S>> extends DataService<S, R> implements IData<T> {

    private final T id;

    public DataManager(T id) {
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
