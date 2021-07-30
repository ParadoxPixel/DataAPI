package nl.iobyte.dataapi.namespace;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
     * Get single value from map
     * @param path String
     * @return T
     */
    public T first(String path) {
        Set<T> s = get(path);
        if(s.isEmpty())
            return null;

        return s.iterator().next();
    }

    /**
     * Get set of values at path
     * @param path String
     * @return Set<T>
     */
    public Set<T> get(String path) {
        return get(0, path.split("\\."));
    }

    /**
     * Get set of values at path
     * @param i Integer
     * @param args String[]
     * @return Set<T>
     */
    @SuppressWarnings("unchecked")
    private Set<T> get(int i, String... args) {
        if(args.length <= i)
            return get(0, "");

        Set<T> list = new HashSet<>();
        if(args[i].contains("*")) {
            if(args[i].equals("**")) {
                for(Object obj : map.values()) {
                    if (obj instanceof NamespaceMap) {
                        list.addAll(Objects.requireNonNull(((NamespaceMap<T>) obj).get(0, "**")));
                        continue;
                    }

                    list.add((T) obj);
                }
            } else {
                String str = args[i].replaceAll("\\*", "(.*)");
                for (String key : map.keySet()) {
                    if (!key.matches(str))
                        continue;

                    args[i] = key;
                    list.addAll(get(i, args));
                }
            }
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
     * @return Set<T>
     */
    public Set<T> remove(String path) {
        return remove(0, path.split("\\."));
    }

    /**
     * Remove values at path
     * @param i Integer
     * @param args String[]
     * @return Set<T>
     */
    @SuppressWarnings("unchecked")
    private Set<T> remove(int i, String... args) {
        if(args.length <= i)
            return remove(0, "");

        Set<T> list = new HashSet<>();
        if(args[i].contains("*")) {
            if(args[i].equals("**")) {
                for(Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue() instanceof NamespaceMap) {
                        list.addAll(Objects.requireNonNull(((NamespaceMap<T>) entry.getValue()).remove(0, "**")));
                    } else {
                        list.add((T) entry.getValue());
                    }

                    map.remove(entry.getKey());
                }
            } else {
                String str = args[i].replaceAll("\\*", "(.*)");
                for (String key : map.keySet()) {
                    if (!key.matches(str))
                        continue;

                    args[i] = key;
                    list.addAll(remove(i, args));
                }
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
