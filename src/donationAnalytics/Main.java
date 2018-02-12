package donationAnalytics;

import java.io.*;



/**
 * Solver class for the donation analytics problem
 */
public class Main {
    
    private final int percentile;
    private final String input_path, output_path;
    private final DonationsDatabase database;
    
    
    
    /**
     * Initializes the parameters for the donation analytics solver 
     * @param input_path path to the input file
     * @param perc_path path to the percentile file
     * @param output_path path to the output file
     * @throws Exception
     */
    public Main(String input_path, String perc_path, String output_path) throws Exception
    {
        this.input_path  = input_path;
        this.output_path = output_path;
        
        percentile = readPercentile(perc_path);
        
        database = new DonationsDatabase();
    }
    
    
    
    /**
     * Reads the percentile value (assuming 1 <= p <= 100) from the file
     * @param path path to the percentile file
     * @return percentile value
     * @throws Exception if percentile is not Integer in the appropriate range
     */
    private static int readPercentile(String path) throws Exception
    {
        BufferedReader reader = initializeReader(path);
        String line           = reader.readLine();
        reader.close();
        
        int percentile = Integer.parseInt(line);
        if (percentile < 1 || percentile > 100) throw new Exception();
        
        return percentile;
    }
    
    
    
    /**
     * Helper method that initializes the reader in order to read from the file line by line
     * @param file_path path to the file
     * @return reader
     * @throws Exception if file not found
     */
    private static BufferedReader initializeReader(String file_path) throws Exception
    {
        File file           = new File(file_path);
        FileInputStream fis = new FileInputStream(file);
        
        return new BufferedReader(new InputStreamReader(fis));
    }
    
    
    
    /**
     * Helper method that initializes the writer in order to write into the file
     * @param file_path path to the file
     * @return writer
     * @throws Exception if file not found
     */
    private static BufferedWriter initializeWriter(String file_path) throws Exception
    {
        return new BufferedWriter(new FileWriter(file_path));
    }
    
    
        
    /**
     * Reads the input file, processes donation entries and outputs donation statistics into the output file
     * @throws Exception if files not found
     */
    public void performDonationAnalysis() throws Exception
    {
        // initialize reader and writer
        BufferedReader reader = initializeReader(input_path);
        BufferedWriter writer = initializeWriter(output_path);
        
        String line = null;
        while ((line = reader.readLine()) != null)  // read file line by line
        {
            
            DonationEntry entry = new DonationEntry(line);      // process new entry from the line
            
            if (!entry.isValid())   continue;                   // if entry invalid, skip and read next line
            
            
            // produce output if donor is repeat donor
            if (database.ifRepeatDonor(new PairKey<>(entry.getDonorName(), entry.getZipcode())))
            {
                database.addDonation(entry);                    // add entry into database that allows to calculate statistics
                
                String recipient = entry.getRecipientID();      // 9-digit CMTE_ID
                String zipcode = entry.getZipcode();            // 5-digit zip code
                Integer year = entry.getYear();                 // 4-digit year
                
                // calculate required percentile for given (recipient, zip code, year)
                Integer percentile_value = database.findPercentile(recipient, zipcode, year, percentile);
                
                // calculate cumulative donation for given (recipient, zip code, year)
                Integer cumulative = database.findCumulative(recipient, zipcode, year);
                
                // calculate total transaction count for given (recipient, zip code, year)
                Integer transaction_count = database.findTransactionCount(recipient, zipcode, year);
                
                
                // output the statistics separated by '|' character
                String output_line = recipient + "|" 
                                   + zipcode + "|"
                                   + Integer.toString(year) + "|"
                                   + Integer.toString(percentile_value) + "|"
                                   + Integer.toString(cumulative) + "|"
                                   + Integer.toString(transaction_count) + System.lineSeparator();
                
                writer.write(output_line);
            }
        }
        
        // close input and output files
        reader.close();
        writer.close();
    }
    
    
    
    /**
     * {@code main} method that executes the donation analytics code
     * @param args {@code path1, path2, path3} - paths to the input file, percentile file, and output file, respectively
     * @throws Exception if files not found
     */
    public static void main(String[] args) throws Exception
    {
        Main solver = new Main(args[0], args[1], args[2]);
        solver.performDonationAnalysis();
    }

}
