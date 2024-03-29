package donationAnalytics;

import java.util.*;


/**
 * Class that constructs the database from valid donation entries and provides methods for statistics calculations.
 */
public class DonationsDatabase {

    // set of all unique donors
    private final Map<Tuple<String, String>, Calendar> donors;

    // data structures that contain all the donations from repeat donors and cumulative donations, respectively
    // data is indexed by the combination of keys: recipient ID -> zip code -> year
    private final MapOfMaps<OrderedTree<Double>> fromRepeatDonors;
    private final MapOfMaps<Double> cumulative;


    /**
     * Initializes empty data structures.
     */
    public DonationsDatabase() {
        donors = new TreeMap<>();
        fromRepeatDonors = new MapOfMaps<>();
        cumulative = new MapOfMaps<>();
    }


    /**
     * Processes single donation entry and updates the ordered tree of all donations
     * and the cumulative donation for a given combination of keys: recipient ID -> zip code -> year.
     *
     * @param entry donation entry
     */
    public void addDonation(DonationEntry entry) {
        String recipient = entry.getRecipientID();          // key 1 - recipient ID
        String zipcode = entry.getZipcode();              // key 2 - zip code
        String year = entry.getYear();                 // key 3 - year

        Double amount = entry.getAmount();                  // donation amount to add to collection of past donations and to update cumulative  

        // insert amount in the OrderedTree
        fromRepeatDonors.putIfAbsent(recipient, zipcode, year, new OrderedTree<>());
        OrderedTree<Double> tree = fromRepeatDonors.get(recipient, zipcode, year);
        tree.put(amount);

        // update cumulative
        cumulative.putIfAbsent(recipient, zipcode, year, 0.0);
        Double currentSum = cumulative.get(recipient, zipcode, year);
        cumulative.put(recipient, zipcode, year, currentSum + amount);
    }

    /**
     * Returns the amount that corresponds to the n-th percentile.
     * Guaranteed O(log(N)) performance due to O(1) lookup time in the HashMaps
     * and O(log(N)) time for the operations with the RedBlackBST.
     *
     * @param recipient  key1 recipient ID
     * @param zipcode    key2 zip code
     * @param year       key 3 year
     * @param percentile key percentile to search for
     * @return value corresponding to the percentile
     */
    public int findPercentile(String recipient, String zipcode, String year, int percentile) {
        OrderedTree<Double> amounts = fromRepeatDonors.get(recipient, zipcode, year);

        // percentile is computed using the nearest-rank method
        int percentileRank = (int) Math.ceil(percentile * amounts.size() / 100.0);
        return (int) Math.round(amounts.selectKMin(percentileRank - 1));
    }

    /**
     * Returns the cumulative donation.
     * Constant time performance due to O(1) lookup time in HashMaps.
     *
     * @param recipient key1 recipient ID
     * @param zipcode   key2 zip code
     * @param year      key 3 year
     * @return cumulative donation
     */
    public int findCumulative(String recipient, String zipcode, String year) {
        Double total = cumulative.get(recipient, zipcode, year);
        return (int) Math.round(total);
    }

    /**
     * Returns the total transaction count.
     * Constant time performance due to O(1) lookup time in HashMaps and O(1) lookup time of RedBlackBST.size().
     *
     * @param recipient key1 recipient ID
     * @param zipcode   key2 zip code
     * @param year      key 3 year
     * @return transaction count
     */
    public int findTransactionCount(String recipient, String zipcode, String year) {
        return fromRepeatDonors.get(recipient, zipcode, year).size();
    }

    /**
     * Attempts to add new donor into the map. If the donor is already present in the map with same or earlier year,
     * then it is a repeat donor, if with later date - then it's not a repeat donor, but the donation date is updated;
     * otherwise the donor is added.
     *
     * @param donorName donor name and zip code to uniquely identify a donor
     * @return {@code true} if the donor is repeat donor, {@code false} if the donor is new
     */
    public boolean ifRepeatDonor(String donorName, String donorZipCode, Calendar laterDate) {
        Tuple<String, String> donor = new Tuple<>(donorName, donorZipCode);

        if (donors.containsKey(donor)) {
            Calendar previousdate = donors.get(donor);

            // if new entry has same or later year, then it is a repeat donor 
            if (previousdate.get(Calendar.YEAR) <= laterDate.get(Calendar.YEAR)) {
                return true;
            }

            // otherwise it is not a repeat donor, and the entry date is updated
            donors.remove(donor);
        }

        donors.put(donor, laterDate);
        return false;
    }

}
