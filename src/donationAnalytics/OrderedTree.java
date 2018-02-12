package donationAnalytics;



/**
 * Wrapper class for the RedBlackBST class with specified types for keys and values.
 * Key is a (Double, Integer) tuple.
 * The auxiliary Integer key is used to avoid collisions between equal primary Double keys.
 * The value will always be assigned 0, since only the ordering of Double keys is of interest.
 */
public class OrderedTree extends RedBlackBST<Tuple<Double, Integer>, Integer> {
    
    
    
    /**
     * Inserts the key-value pair into the OrderedTree.
     * The value inserted is 0.
     * @param key a PairKey data type
     */
    public void put(Tuple<Double, Integer> key)
    { super.put(key, 0); }
    
}
