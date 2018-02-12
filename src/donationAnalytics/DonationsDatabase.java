package donationAnalytics;

import java.util.*;



/**
 * Class that constructs the database from valid donation entries and provides methods for statistics calculations
 */
public class DonationsDatabase {

    private final Map<PairKey<String,String>, Date> donors;                   // collection of all unique donors

    // data structures that contain all the donations from repeat donors and cumulative donations, respectively
    // data is indexed by the combination of keys: recipient ID -> zip code -> year
    private final MapOfMaps<OrderedTree> fromRepeatDonors;
    private final MapOfMaps<Double>      cumulative;
    
    
    
    /**
     * Initializes empty data structures
     */
    public DonationsDatabase()
    {
        donors           = new TreeMap<>();
        fromRepeatDonors = new MapOfMaps<>();
        cumulative       = new MapOfMaps<>();
    }
    
    
    
    /**
     * Processes single donation entry and updates the ordered tree of all donations
     * and the cumulative donation for a given combination of keys: recipient ID -> zip code -> year
     * @param entry
     */
    public void addDonation(DonationEntry entry)
    {
        String recipient = entry.getRecipientID();          // key 1 - recipient ID
        String zipcode   = entry.getZipcode();              // key 2 - zip code 
        Integer year     = entry.getYear();                 // key 3 - year

        Double amount = entry.getAmount();                  // donation amount to add to collection of past donations and to update cumulative  
        
        
        // insert amount in the OrderedTree
        // growing tree.size() serves as auxiliary key preventing collisions of equal primary keys
        fromRepeatDonors.putIfAbsent(recipient, zipcode, year, new OrderedTree());
        OrderedTree tree = fromRepeatDonors.get(recipient, zipcode, year); 
        tree.put(new PairKey<>(amount, tree.size()));
        
        
        // update cumulative
        cumulative.putIfAbsent(recipient, zipcode, year, 0.0);
        Double currentsum = cumulative.get(recipient, zipcode, year);
        cumulative.put(recipient, zipcode, year, currentsum + amount);
    }
    
    
    
    /**
     * Returns the amount that corresponds to the n-th percentile.
     * Guaranteed O(log(N)) performance due to O(1) lookup time in the HashMaps
     * and O(log(N)) time for the operations with the RedBlackBST
     * @param recipient key1 recipient ID
     * @param zipcode key2 zip code
     * @param year key 3 year
     * @param percentile key percentile to search for
     * @return value corresponding to the percentile
     */
    public int findPercentile(String recipient, String zipcode, int year, int percentile)
    {
        OrderedTree amounts = fromRepeatDonors.get(recipient, zipcode, year);
        
        // percentile is computed using the nearest-rank method
        int percentile_rank = (int) Math.ceil(percentile * amounts.size() / 100.0);
        return (int) Math.round(amounts.select(percentile_rank - 1).getKey());
    }
    
    
    
    /**
     * Returns the cumulative donation.
     * Constant time performance due to O(1) lookup time in HashMaps
     * @param recipient key1 recipient ID
     * @param zipcode key2 zip code
     * @param year key 3 year
     * @return cumulative donation
     */
    public int findCumulative(String recipient, String zipcode, int year)
    {
        Double total = cumulative.get(recipient, zipcode, year);
        return (int) Math.round(total);
    }
    
    /**
     * Returns the total transaction count.
     * Constant time performance due to O(1) lookup time in HashMaps and O(1) lookup time of RedBlackBST.size()
     * @param recipient key1 recipient ID
     * @param zipcode key2 zip code
     * @param year key 3 year
     * @return transaction count
     */
    public int findTransactionCount(String recipient, String zipcode, int year)
    {
        return fromRepeatDonors.get(recipient, zipcode, year).size();
    }
   
    
    
    /**
     * Attempts to add new donor into the map. If the donor is already present in the map with earlier date,
     * then it is a repeat donor, if with later date - then the donor is discarded; otherwise the donor is added.
     * @param donor donor name and zip code to uniquely identify a donor
     * @return {@code true} if the donor is repeat donor, {@code false} if the donor is new
     */
    public boolean ifRepeatDonor(PairKey<String, String> donor, Date date)
    { 
        if (donors.containsKey(donor))
        {
            Date previousdate = donors.get(donor);
            return (previousdate.compareTo(date) <= 0);
        }
        donors.put(donor, date);
        return false;
    }
    
    
}
