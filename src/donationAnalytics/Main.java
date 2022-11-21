package donationAnalytics;

import java.io.*;
import java.util.*;


/**
 * Solver class for the donation analytics problem.
 */
public class Main {

    private final int percentile;
    private final String inputPath, outputPath;
    private final DonationsDatabase database;


    /**
     * Initializes the parameters for the donation analytics solver.
     *
     * @param inputPath  path to the input file
     * @param percPath   path to the percentile file
     * @param outputPath path to the output file
     * @throws IOException if something went wrong
     */
    public Main(String inputPath, String percPath, String outputPath) throws IOException {
        this.inputPath = inputPath;
        this.outputPath = outputPath;

        percentile = readPercentile(percPath);
        database = new DonationsDatabase();
    }

    /**
     * Reads the percentile value (assuming 1 <= p <= 100) from the file.
     *
     * @param path path to the percentile file
     * @return percentile value
     * @throws IOException if value cannot be read from the file
     */
    private static int readPercentile(String path) throws IOException {
        String line;
        try (BufferedReader reader = initializeReader(path)) {
            line = reader.readLine();
        }

        int percentile = Integer.parseInt(line);
        if (percentile < 1 || percentile > 100) {
            throw new IllegalArgumentException();
        }

        return percentile;
    }

    /**
     * Helper method that initializes the reader in order to read from the file line by line.
     *
     * @param filePath path to the file
     * @return reader
     * @throws FileNotFoundException if file not found
     */
    private static BufferedReader initializeReader(String filePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }

    /**
     * Helper method that initializes the writer in order to write into the file.
     *
     * @param filePath path to the file
     * @return writer
     * @throws IOException if file not found
     */
    private static BufferedWriter initializeWriter(String filePath) throws IOException {
        return new BufferedWriter(new FileWriter(filePath));
    }

    /**
     * Reads the input file, processes donation entries and outputs donation statistics into the output file.
     *
     * @throws IOException if files not found
     */
    public void performDonationAnalysis() throws IOException {
        // initialize reader and writer
        try (BufferedReader reader = initializeReader(inputPath);
             BufferedWriter writer = initializeWriter(outputPath)) {

            String line;
            while ((line = reader.readLine()) != null) {

                DonationEntry entry = new DonationEntry(line);      // process new entry from the line

                if (!entry.isValid()) {
                    continue;                   // if entry invalid, skip and read next line
                }

                // produce output if donor is repeat donor
                if (database.ifRepeatDonor(entry.getDonorName(), entry.getZipcode(), entry.getDate())) {
                    database.addDonation(entry);                    // add entry into database that allows to calculate statistics

                    String recipient = entry.getRecipientID();      // 9-digit CMTE_ID
                    String zipcode = entry.getZipcode();          // 5-digit zip code
                    String year = entry.getYear();             // 4-digit year

                    // calculate required percentile for given (recipient, zip code, year)
                    int percentile_value = database.findPercentile(recipient, zipcode, year, percentile);

                    // calculate cumulative donation for given (recipient, zip code, year)
                    int cumulative = database.findCumulative(recipient, zipcode, year);

                    // calculate total transaction count for given (recipient, zip code, year)
                    int transaction_count = database.findTransactionCount(recipient, zipcode, year);


                    // output the statistics separated by '|' character
                    StringJoiner output_line = new StringJoiner("|");

                    output_line.add(recipient).add(zipcode).add(year)
                            .add(Integer.toString(percentile_value))
                            .add(Integer.toString(cumulative))
                            .add(Integer.toString(transaction_count));

                    writer.write(output_line.toString());
                    writer.newLine();
                }
            }
        }
    }

    /**
     * {@code main} method that executes the donation analytics code.
     *
     * @param args {@code path1, path2, path3} - paths to the input file, percentile file, and output file, respectively
     * @throws Exception if files not found
     */
    public static void main(String[] args) throws Exception {
        Main solver = new Main(args[0], args[1], args[2]);
        solver.performDonationAnalysis();
    }

}
