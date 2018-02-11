package donationAnalytics;

import java.util.Map;
import java.util.HashMap;



/**
 * Three-level map data structure.
 * The first-level map contains String keys and second-level map values.
 * The second-level map contains String keys and third-level map values.
 * The third-level map contains Integer keys and Generic values.
 * @param <T> data type of the third-level map values 
 */
public class MapOfMaps<T> {

    private final Map<String, 
                    Map<String, 
                      Map<Integer, T>>> map;
    
    
    
    /**
     * Initializes empty HashMap
     */
    public MapOfMaps()
    {
        map = new HashMap<>();
    }
    
    
    
    /**
     * Inserts the value {@code t} into the three-level map
     * @param key1 key to the 1st-level map
     * @param key2 key to the 2nd-level map
     * @param key3 key to the 3rd-level map
     * @param t value
     */
    public void put(String key1, String key2, Integer key3, T t)
    {
        Map<String, Map<String, Map<Integer, T>>> map0 = map;
        if (!map0.containsKey(key1))
            map0.put(key1, new HashMap<>());            // initialize empty HashMap on the 1st level if necessary 
        
        
        Map<String, Map<Integer, T>> map1 = map0.get(key1);
        if (!map1.containsKey(key2))
            map1.put(key2, new HashMap<>());            // initialize empty HashMap on the 2nd level if necessary
        
        
        Map<Integer, T> map2 = map1.get(key2);
        map2.put(key3, t);                              // insert the value
    }
    
    
    
    /**
     * Inserts the value {@code t} into the three-level map if no other value corresponds
     * to the combination of keys {@code key1}, {@code key2}, {@code key3} 
     * @param key1 key to the 1st-level map
     * @param key2 key to the 2nd-level map
     * @param key3 key to the 3rd-level map
     * @param t value
     */
    public void putIfAbsent(String key1, String key2, Integer key3, T t)
    {
        if (!containsKey(key1, key2, key3))
            put(key1, key2, key3, t);
    }
    
    
    
    /**
     * Returns {@code true} if the map contains the value that corresponds
     * to the combination of keys {@code key1}, {@code key2}, {@code key3}
     * @param key1 key to the 1st-level map
     * @param key2 key to the 2nd-level map
     * @param key3 key to the 3rd-level map
     * @return true or false
     */
    public boolean containsKey(String key1, String key2, Integer key3)
    {
        boolean flag = true;
        
        flag = map.containsKey(key1);
        if (flag) flag = map.get(key1).containsKey(key2));
        if (flag) flag = map.get(key1).get(key2).containsKey(key3));
        
        return flag;
    }
    
    
    
    /**
     * Returns value that corresponds to the combination of keys {@code key1}, {@code key2}, {@code key3}
     * @param key1 key to the 1st-level map
     * @param key2 key to the 2nd-level map
     * @param key3 key to the 3rd-level map
     * @return value
     */
    public T get(String key1, String key2, Integer key3)
    {
        if (!containsKey(key1, key2, key3)) return null;
        return map.get(key1).get(key2).get(key3);
    }
    
    
    
}
