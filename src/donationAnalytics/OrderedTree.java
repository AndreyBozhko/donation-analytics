package donationAnalytics;



/**
 * Wrapper class for the RedBlackBST class with specified types for keys and values.
 * A key of generic type is inserted as (Key, Integer) tuple.
 */
public class OrderedTree<Key extends Comparable<Key>> 
                             extends RedBlackBST<Tuple<Key, Integer>, Integer> {
    // The auxiliary Integer key is used to avoid collisions between equal primary keys.
    // The value will always be assigned 0, since only the ordering of primary keys is of interest.
    
    
    
    /**
     * Inserts the key-value pair into the OrderedTree.
     * The size of the tree is inserted as an auxiliary key.
     * The value inserted is 0.
     * @param key a Tuple data type
     */
    public void put(Key key)
    { put(new Tuple<>(key, size()), 0); }
    
    
    /**
     * Returns the kth smallest key in the tree, where 0 <= k < size()
     * @param k the order statistic
     * @return the kth smallest key
     */
    public Key selectKMin(int k)
    { return (super.select(k)).getKey1(); }
    
}
