# donation-analytics
My solution to the Insight Data Engineering Coding Competition (https://github.com/InsightDataScience/donation-analytics).

## Executing the project
The solution is written in Java and the source files are compiled using the `javac` compiler from the package `openjdk-9-jdk-headless`.
The script `run.sh` compiles and executes the project; the two input data files are `input/itcont.txt` and `input/percentile.txt`, and the result is written into `output/repeat_donors.txt`. Running `insight_testsuite/run_tests.sh` evaluates the solution using the tests from `insight_suite/tests/`.

## Algorithm overview
The algorithm successively reads data entries from the input file `itcont.txt` line by line, parsing each line after it is read, adding the new data to the data structures that allow efficient computation of the required statistics, and generating the corresponding output line. The memory requirement of the algorithm is **O(N)**. In the worst case **N** is the total number of entries scanned so far, and all of them will need to be stored to guarantee precise calculation of the *n*-th percentile.

The data structure used to store the donation information is implemented as several nested maps, `java.util.HashMap` in this case, so that the value/object stored is indexed by the sequence of three keys (`recipientID`, `zipcode` and `year`), and which can also be accessed in amortized **O(1)** time.
In order to calculate the cumulative donation amount for a given set of keys (`recipientID`, `zipcode`, `year`), I store and update the running total sum as the map value.

The other map stores instances of `RedBlackBST`, which stores the donation amounts in a sorted fashion.
I used the open-source implementation of the red-black binary search tree that can be found at https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html.
This particular version of the red-black tree provides methods to find the *k*-th smallest key in the tree (which is basically the same as finding a percentile), with the **O(log(N))** time guarantee.
