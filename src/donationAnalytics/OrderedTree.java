package donationAnalytics;



/**
 * Class that implements the order statistics tree based on the RedBlackBST,
 * where the key is a (Double, Integer) pair.
 * The auxiliary Integer key is used to avoid collisions between equal primary Double keys.
 * The value will always be assigned 0, since only the ordering of Double keys is of interest.
 */
public class OrderedTree {
    
    private final RedBlackBST<Tuple<Double, Integer>, Integer> tree;
    
    
    
    /**
     * Initializes empty Red Black Binary Search Tree
     */
    public OrderedTree()
    {
        tree = new RedBlackBST<>();
    }
    
    
    
    /**
     * Inserts the key-value pair into the OrderedTree.
     * The value inserted is 0.
     * @param key a PairKey data type
     */
    public void put(Tuple<Double, Integer> key)
    { tree.put(key, 0); }
    
    
    
    /**
     * Returns the value corresponding to key
     * @param key a PairKey data type
     * @return value
     */
    public int get(Tuple<Double, Integer> key)
    { return tree.get(key); }
    
    
    
    /**
     * Returns k-th smallest key from the tree.
     * Guaranteed O(log(N)) performance.
     * @param k index of the key
     * @return key
     */
    public Tuple<Double, Integer> select(int k)
    { return tree.select(k); }
    
    
    
    /**
     * Returns size of the tree.
     * @return tree size
     */
    public int size()
    { return tree.size(); }
    
    

}