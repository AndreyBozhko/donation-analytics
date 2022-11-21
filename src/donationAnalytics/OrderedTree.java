package donationAnalytics;


/**
 * Wrapper class for the RedBlackBST class.
 * This class allows multiple insertions of identical keys.
 */
public class OrderedTree<Key extends Comparable<Key>> {
    // The auxiliary Integer key, equal to current tree.size(), is used to avoid collisions between equal primary keys.
    // The value will always be assigned 0, since only the ordering of primary keys is of interest.

    private final RedBlackBST<Tuple<Key, Integer>, Integer> redBlackBST;


    /**
     * Initializes new instance of OrderedTree.
     */
    public OrderedTree() {
        redBlackBST = new RedBlackBST<>();
    }

    /**
     * Inserts the key-value pair into the OrderedTree.
     * The size of the tree is inserted as an auxiliary key.
     * The value inserted is 0.
     *
     * @param key a Tuple data type
     */
    public void put(Key key) {
        redBlackBST.put(new Tuple<>(key, size()), 0);
    }

    /**
     * Returns the k-th smallest key in the tree, where 0 <= k < size().
     *
     * @param k the order statistic
     * @return the k-th smallest key
     */
    public Key selectKMin(int k) {
        return redBlackBST.select(k).getKey1();
    }

    /**
     * Returns the size of the tree.
     *
     * @return size of the tree
     */
    public int size() {
        return redBlackBST.size();
    }

}
