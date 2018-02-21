package donationAnalytics;



/**
 * Wrapper class for the RedBlackBST class.
 * This class allows multiple insertions of identical keys.
 */
public class OrderedTree<Key extends Comparable<Key>> {
    // The auxiliary Integer key, equal to current tree.size(), is used to avoid collisions between equal primary keys.
    // The value will always be assigned 0, since only the ordering of primary keys is of interest.
    
    private final RedBlackBST<Tuple<Key, Integer>, Integer> rbtree;
    
    
    
    /**
     * Initializes new instance of OrderedTree.
     */
    public OrderedTree()
    { rbtree = new RedBlackBST<>(); }
    
    
    
    /**
     * Inserts the key-value pair into the OrderedTree.
     * The size of the tree is inserted as an auxiliary key.
     * The value inserted is 0.
     * @param key a Tuple data type
     */
    public void put(Key key)
    { rbtree.put(new Tuple<>(key, size()), 0); }
    
    
    
    /**
     * Returns the kth smallest key in the tree, where 0 <= k < size()
     * @param k the order statistic
     * @return the kth smallest key
     */
    public Key selectKMin(int k)
    { return rbtree.select(k).getKey1(); }

    
    
    /**
     * Returns the size of the tree
     * @return size of the tree
     */
    public int size()
    { return rbtree.size(); }
    
}
