package donationAnalytics;



/**
 * Data type that consists of a pair of keys - primary and auxiliary
 * @param <Key1> primary key
 * @param <Key2> auxiliary key
 */
public class PairKey<Key1 extends Comparable<Key1>, 
		     Key2 extends Comparable<Key2>> 
			  implements Comparable<PairKey<Key1, Key2>>{

    private final Key1 key1;
    private final Key2 key2;
    
    
    /**
     * Initializes a key pair
     * @param key1 - first (primary) key
     * @param key2 - second (auxiliary) key
     */
    public PairKey(Key1 key1, Key2 key2)
    {
        this.key1 = key1;
        this.key2 = key2;
    }
    
    
    /**
     * Implementation of Comparable Interface for a pair of keys.
     * Primary key {@code key1} is compared first.
     * Auxiliary key {@code key2} is compared second.
     */
    public int compareTo(PairKey<Key1, Key2> that)
    {
        int cmp = this.key1.compareTo(that.key1);
        if (cmp != 0) { return cmp; }
        
        cmp = this.key2.compareTo(that.key2);
        if (cmp != 0) { return cmp; }
        
        return 0;
    }
    
    
    /**
     * Returns primary key
     * @return primary key {@code key1}
     */
    public Key1 getKey()
    { return key1; }
    
    
    /**
     * Returns auxiliary key
     * @return auxiliary key {@code key2}
     */
    public Key2 getAuxiliaryKey()
    { return key2; }
    
    
    
    /**
     * Overrides {@code equals} method
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof PairKey<?, ?>)) return false;
        PairKey<?, ?> that = (PairKey<?, ?>) o;
        
        if (!(that.key1 instanceof String)) return false;
        if (!(that.key2 instanceof String)) return false;
        
        return (this.key1.equals(that.key1) && this.key2.equals(that.key2));
    }
}
