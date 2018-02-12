package donationAnalytics;

import java.util.Calendar;



/**
 * Class that parses single donation entry.
 * Determines if entry is valid
 */
public class DonationEntry {

    private static final int NUMBER_OF_FIELDS    = 21;
    
    // entry is not valid if this is not true
    private static final int ZIPCODE_LENGTH      = 5;
    // private static final int FILER_ID_LENGTH     = 9;
    
    
    
    // relevant fields positions in the entry
    private static final int FILER_ID_POSITION   = 0;
    private static final int DONOR_NAME_POSITION = 7;
    private static final int ZIPCODE_POSITION    = 10;
    private static final int DATE_POSITION       = 13;
    private static final int AMOUNT_POSITION     = 14;
    private static final int OTHER_ID_POSITION   = 15;
    
    
    private String recipient, donor_name, zipcode;
    private double amount;
    private int year;
    
    private boolean is_valid = true;
    
    
    
    /**
     * Parses donation entry into the following fields:
     * recipient ID, donor name, donor zip code, donation amount, donation date.
     * Checks whether the donor is an individual and if the entry is valid 
     * @param line donation entry
     * @throws Exception
     */
    public DonationEntry(String line)
    {
        String[] fields = line.split("\\|", -1);
        if (fields.length != NUMBER_OF_FIELDS) is_valid = false;
        else                                   is_valid = isValid(fields);
    }
    
    
    
    /*************************
     *      Helper methods
     *************************/
    
    
    /**
     * Returns recipient ID
     * @return recipient ID
     */
    public String getRecipientID()
    { return recipient; }
    
    
    
    /**
     * Returns the name of the donor
     * @return name of donor
     */
    public String getDonorName()
    { return donor_name; }
    
    
    
    /**
     * Returns zip code of donor
     * @return zip code
     */
    public String getZipcode()
    { return zipcode; }
    
    
    
    /**
     * Returns the donation amount
     * @return donation amount
     */
    public double getAmount()
    { return amount; }
    
    
    
    /**
     * Returns year in which the donation was made
     * @return donation year
     */
    public int getYear()
    { return year; }
    
    
    
    /**
     * Returns {@code true} if entry is valid
     * @return true or false
     */
    public boolean isValid()
    {
        return is_valid;
    }
    
    
    
    /**
     * Returns {@code true} if the donation entry passes all the validity checks
     * @param fields all fields of the entry
     * @return true or false
     */
    private boolean isValid(String[] fields)
    {
        return (   checkIfValidOtherID(  fields[OTHER_ID_POSITION])
                && checkIfValidRecipient(fields[FILER_ID_POSITION]) 
                && checkIfValidDonorName(fields[DONOR_NAME_POSITION])
                && checkIfValidZipcode(  fields[ZIPCODE_POSITION])
                && checkIfValidAmount(   fields[AMOUNT_POSITION])
                && checkIfValidDate(     fields[DATE_POSITION]));
    }
    
    
    
    /**
     * Returns {@code true} if Other ID is valid, i.e. empty
     * @param otheridstring string with other ID
     * @return true or false
     */
    private boolean checkIfValidOtherID(String otheridstring)
    {
        return (otheridstring.equals(""));
    }
    
    
    
    /**
     * Returns {@code true} if recipient is valid, i.e. the ID is non-empty sequence of characters
     * @param recipientstring string with recipient ID
     * @return true or false
     */
    private boolean checkIfValidRecipient(String recipientstring)
    {
        if (recipientstring.equals("")) return false;
        // if (s.length() > FILER_ID_LENGTH) return false;
        recipient = recipientstring;
        return true;
    }
    
    
    
    /**
     * Returns {@code true} if donor name is valid, i.e. if both first and last names consist
     * only of letters and possibly spaces.
     * The valid separation between last name and first name is assumed to be
     * a comma followed by one whitespace.
     * Valid first name and last name may consist of several valid words with exactly one whitespace
     * separating them, and with no leading or trailing whitespaces. 
     * @param namestring string with last name and first name separated by ", "
     * @return true or false
     */
    private boolean checkIfValidDonorName(String namestring)
    {
        try
        {
            donor_name = namestring;
            String[] names = namestring.split(", ", -1);
            if (names.length != 2) throw new Exception();
            
            for (String n : names)
                for (String w : n.split(" ", -1)) 
                    if (w.equals("") || !w.matches("[a-zA-Z]+")) throw new Exception();
        }
        catch (Exception e) { return false; }
        return true;
    }
    
    
    
    /**
     * Returns {@code true} if zip code is a valid 5-digit number
     * @param zipcodestring string with zip code
     * @return true or false
     */
    private boolean checkIfValidZipcode(String zipcodestring)
    {
        try
        {
            zipcode = zipcodestring.substring(0, ZIPCODE_LENGTH);
            if (Integer.parseInt(zipcode) < 0) throw new Exception();
        }
        catch (Exception e) { return false; }
        return true;
    }
    
    
    
    /**
     * Returns {@code true} if donation amount is a valid positive number 
     * @param amountstring string with amount
     * @return true or false
     */
    private boolean checkIfValidAmount(String amountstring)
    {
        if (amountstring.equals("")) return false;
        try
        {
            amount = Double.parseDouble(amountstring);
            if (amount < 0.0) throw new Exception();
        }
        catch (Exception e) { return false; }
        return true;
    }
    
    
    
    /**
     * Returns {@code true} if datestring represents a valid date
     * @param datestring string with date
     * @return true or false
     */
    private boolean checkIfValidDate(String datestring)
    {
        try
        {
            if (datestring.length() != 8) throw new Exception();
                        
            year      = Integer.parseInt(datestring.substring(4, 8));
            int month = Integer.parseInt(datestring.substring(0, 2));
            int day   = Integer.parseInt(datestring.substring(2, 4));
            
                        
            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false);
            calendar.set(year, month - 1, day);
            calendar.getTime();
        }
        catch (Exception e) { return false; }
        return true;
    }
       
}
