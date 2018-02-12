# donation-analytics
My solution to the Insight Data Engineering Coding Competition (https://github.com/InsightDataScience/donation-analytics).

## Executing the project
The solution is written in Java and the source files are compiled using the `javac` compiler from the package `openjdk-9-jdk-headless`.
The script `run.sh` compiles and executes the project; the two input data files are `input/itcont.txt` and `input/percentile.txt`, and the result is written into `output/repeat_donors.txt`. Running `insight_testsuite/run_tests.sh` evaluates the solution using the tests from `insight_testsuite/tests/`.

***Note: to execute `run_tests.sh`, change you current_working_directory to `insight_testsuite/` first, and then call `./run_tests.sh`.***
***Somewhy the script wouldn't work when I tried to launch it from any other directory.***

## Algorithm overview
The algorithm successively reads data entries from the input file `itcont.txt` line by line, parsing each line after it is read, adding the new data to the data structures that allow efficient computation of the required statistics, and generating the corresponding output line. The memory requirement of the algorithm is **O(N)**. In the worst case **N** is the total number of entries scanned so far, and all of them will need to be stored to guarantee precise calculation of the *n*-th percentile.

The data structure used to store the donation information is implemented as several nested maps, `java.util.HashMap` in this case, so that the value/object stored is indexed by the sequence of three keys (`CMTE_ID`, `ZIP_CODE` and `YEAR`), and which can also be accessed in amortized **O(1)** time.
In order to calculate the cumulative donation amount for a given set of keys (`CMTE_ID`, `ZIP_CODE`, `YEAR`), I store and update the running total sum as the map value.


The other map stores instances of `RedBlackBST`, which stores the donation amounts in a sorted fashion and guarantees **O(log(N))** time for the *insert* operation.
I used the open-source implementation of the red-black binary search tree that can be found at https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html.

This particular version of the red-black tree provides methods to find the *k*-th smallest key in the tree (which is exactly what you need to compute a percentile), with the **O(log(N))** time guarantee as well.

Overall, getting the percentile and the transaction count (*simply the size of the red-black tree*) can be done at each iteration in logarithmic time.

The repeat donors are identified by maintaining a set of all unique donors (given by their `NAME` and `ZIP_CODE`).


## Assumptions
As for the main algorithm body, I assumed that input will not be extremely large so that all the processed data may be stored in the RAM.
The rest of them are concerning the validity of different entries:

1. `CMTE_ID` is valid if not empty, and the 9-digit restriction is ignored (*as seems to be suggested in the competition description*);
2. `ZIP_CODE` is valid if 0 <= zip_code <= 99999 and if the original string contains at least 5 characters;
3. `TRANSACTION_AMT` is valid if it parses into a positive real number;
4. `NAME` is valid if the last name and the first name are separated by ", " without any leading/trailing zeros, and each of the names may consist of several words (*letters only*) separated by a whitespace;
5. The total contribution amount must be rounded to the whole dollar, similar to how the percentile value is rounded.


## Project dependencies
My implementation only imports classes from standard Java packages such as `java.util` and `java.io`. The methods from the `RedBlackBST` class that require additional imports are commented out as they are not essential for the solution.
