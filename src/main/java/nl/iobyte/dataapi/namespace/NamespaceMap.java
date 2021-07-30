package nl.iobyte.dataapi.namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class NamespaceMap<T> {

    private final Map<String, Object> map = new ConcurrentHashMap<>();

    /**
     * Get size of map
     * @return Integer
     */
    @SuppressWarnings("unchecked")
    public int size() {
        int i = 0;
        for(Object obj : map.values()) {
            if(obj instanceof NamespaceMap) {
                i += ((NamespaceMap<T>) obj).size();
            } else {
                i++;
            }
        }

        return i;
    }

    /**
     * Add value
     * @param namespace String
     * @param obj T
     */
    public void set(String namespace, T obj) {
        set(namespace.split("\\."), 0, obj);
    }

    /**
     * Add value
     * @param name String[]
     * @param i Integer
     * @param obj value
     */
    @SuppressWarnings("unchecked")
    private void set(String[] name, int i, T obj) {
        if(name.length <= i) {
            map.put("", obj);
            return;
        }

        NamespaceMap<T> nm;
        if(map.containsKey(name[i])) {
            nm = (NamespaceMap<T>) map.get(name[i]);
        } else {
            nm = new NamespaceMap<>();
            map.put(name[i], nm);
        }

        nm.set(name, i + 1, obj);
    }

    /**
     * Get list of values at path
     * @param path String
     * @return List<T>
     */
    public List<T> get(String path) {
        return get(0, path.split("\\."));
    }

    /**
     * Get list of values at path
     * @param i Integer
     * @param args String[]
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    private List<T> get(int i, String... args) {
        if(args.length <= i)
            return get(0, "");

        List<T> list = new ArrayList<>();
        if(args[i].contains("*")) {
            String str = args[i].replaceAll("\\*", "(.*)");
            for(String key : map.keySet()) {
                if(!key.matches(str))
                    continue;

                args[i] = key;
                list.addAll(get(i, args));
            }

            args[i] = "*";
        } else {
            Object obj = map.get(args[i]);
            if (obj == null) {
                if (!args[i].equals("") && map.containsKey(""))
                    list.add((T) map.get(""));

                return list;
            }

            if (args.length == 1) {
                if (obj instanceof NamespaceMap)
                    list.addAll(Objects.requireNonNull(((NamespaceMap<T>) obj).get(0, "")));

                list.add((T) obj);
                return list;
            }

            if (obj instanceof NamespaceMap)
                list.addAll(Objects.requireNonNull(((NamespaceMap<T>) obj).get(i + 1, args)));
        }

        return list;
    }

    /**
     * Remove values at path
     * @param path String
     * @return List<T>
     */
    public List<T> remove(String path) {
        return remove(0, path.split("\\."));
    }

    /**
     * Remove values at path
     * @param i Integer
     * @param args String[]
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    private List<T> remove(int i, String... args) {
        if(args.length <= i)
            return remove(0, "");

        List<T> list = new ArrayList<>();
        if(args[i].contains("*")) {
            String str = args[i].replaceAll("\\*", "(.*)");
            for(String key : map.keySet()) {
                if(!key.matches(str))
                    continue;

                args[i] = key;
                list.addAll(remove(i, args));
            }
        } else {
            Object obj = map.get(args[i]);
            if (obj == null) {
                if (!args[i].equals("") && map.containsKey(""))
                    list.add((T) map.remove(""));

                return list;
            }

            if (args.length == 1) {
                if (obj instanceof NamespaceMap)
                    list.addAll(Objects.requireNonNull(((NamespaceMap<T>) obj).remove(0, "")));

                list.add((T) map.remove(args[i]));
                return list;
            }

            if (obj instanceof NamespaceMap)
                list.addAll(Objects.requireNonNull(((NamespaceMap<T>) obj).remove(i + 1, args)));
        }

        return list;
    }

}
