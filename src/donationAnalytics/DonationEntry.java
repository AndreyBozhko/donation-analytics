package donationAnalytics;

import java.util.*;


/**
 * Class that parses single donation entry.
 * Determines if entry is valid
 */
public class DonationEntry {

    private static final int NUMBER_OF_FIELDS = 21;

    // entry is not valid if this is not true
    private static final int ZIPCODE_LENGTH = 5;
    // private static final int FILER_ID_LENGTH = 9;


    // relevant fields positions in the entry
    private static final int FILER_ID_POSITION = 0;
    private static final int DONOR_NAME_POSITION = 7;
    private static final int ZIPCODE_POSITION = 10;
    private static final int DATE_POSITION = 13;
    private static final int AMOUNT_POSITION = 14;
    private static final int OTHER_ID_POSITION = 15;


    private String recipient, donorName, zipcode, year;
    private double amount;
    private Calendar date;

    private final boolean is_valid;


    /**
     * Parses donation entry into the following fields:
     * recipient ID, donor name, donor zip code, donation amount, donation date.
     * Checks whether the donor is an individual and if the entry is valid
     *
     * @param line donation entry
     */
    public DonationEntry(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length != NUMBER_OF_FIELDS) {
            is_valid = false;
        } else {
            is_valid = isValid(fields);
        }
    }

    /**
     * Returns recipient ID.
     *
     * @return recipient ID
     */
    public String getRecipientID() {
        return recipient;
    }

    /**
     * Returns the name of the donor.
     *
     * @return name of donor
     */
    public String getDonorName() {
        return donorName;
    }

    /**
     * Returns zip code of donor.
     *
     * @return zip code
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * Returns the donation amount.
     *
     * @return donation amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns year in which the donation was made.
     *
     * @return donation year
     */
    public String getYear() {
        return year;
    }

    /**
     * Returns date of donation.
     *
     * @return date
     */
    public Calendar getDate() {
        return (Calendar) date.clone();
    }

    /**
     * Returns {@code true} if entry is valid.
     *
     * @return true or false
     */
    public boolean isValid() {
        return is_valid;
    }

    /**
     * Returns {@code true} if the donation entry passes all the validity checks.
     *
     * @param fields all fields of the entry
     * @return true or false
     */
    private boolean isValid(String[] fields) {
        return (checkIfValidOtherID(fields[OTHER_ID_POSITION])
                && checkIfValidRecipient(fields[FILER_ID_POSITION])
                && checkIfValidDonorName(fields[DONOR_NAME_POSITION])
                && checkIfValidZipcode(fields[ZIPCODE_POSITION])
                && checkIfValidAmount(fields[AMOUNT_POSITION])
                && checkIfValidDate(fields[DATE_POSITION]));
    }

    /**
     * Returns {@code true} if Other ID is valid, i.e. empty.
     *
     * @param otherId string with other ID
     * @return true or false
     */
    private boolean checkIfValidOtherID(String otherId) {
        return "".equals(otherId);
    }

    /**
     * Returns {@code true} if recipient is valid, i.e. the ID is non-empty sequence of characters.
     *
     * @param recipient string with recipient ID
     * @return true or false
     */
    private boolean checkIfValidRecipient(String recipient) {
        if ("".equals(recipient)) {
            return false;
        }
        // if (s.length() > FILER_ID_LENGTH) return false;
        this.recipient = recipient;
        return true;
    }

    /**
     * Returns {@code true} if donor name is valid, i.e. if both first and last names consist
     * only of letters and possibly spaces.
     * The valid separation between last name and first name is assumed to be
     * a comma followed by one whitespace.
     * Valid first name and last name may consist of several valid words with exactly one whitespace
     * separating them, and with no leading or trailing whitespaces.
     *
     * @param name string with last name and first name separated by ", "
     * @return true or false
     */
    private boolean checkIfValidDonorName(String name) {
        donorName = name;
        String[] names = name.split(", ", -1);
        if (names.length != 2) {
            return false;
        }

        for (String n : names) {
            for (String w : n.split(" ", -1)) {
                if ("".equals(w) || !w.matches("[a-zA-Z]+")) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns {@code true} if zip code is a valid 5-digit number.
     *
     * @param zipCode string with zip code
     * @return true or false
     */
    private boolean checkIfValidZipcode(String zipCode) {
        try {
            zipcode = zipCode.substring(0, ZIPCODE_LENGTH);
            if (Integer.parseInt(zipcode) >= 0) {
                return true;
            }
        } catch (Exception ignored) {
            // ignored
        }
        return false;
    }

    /**
     * Returns {@code true} if donation amount is a valid positive number.
     *
     * @param amountString string with amount
     * @return true or false
     */
    private boolean checkIfValidAmount(String amountString) {
        if ("".equals(amountString)) {
            return false;
        }
        try {
            amount = Double.parseDouble(amountString);
            if (amount >= 0.0) {
                return true;
            }
        } catch (Exception ignored) {
            // ignored
        }
        return false;
    }

    /**
     * Returns {@code true} if datestring represents a valid date.
     *
     * @param dateString string with date
     * @return true or false
     */
    private boolean checkIfValidDate(String dateString) {
        try {
            if (dateString.length() != 8) {
                throw new IllegalArgumentException();
            }

            year = dateString.substring(4, 8);
            int yyyy = Integer.parseInt(year);
            int mm = Integer.parseInt(dateString.substring(0, 2));
            int dd = Integer.parseInt(dateString.substring(2, 4));

            date = Calendar.getInstance();
            date.setLenient(false);
            date.set(yyyy, mm - 1, dd);
            date.getTime();                 // throws exception if day or month values are out of their appropriate ranges
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
