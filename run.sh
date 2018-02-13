#!/bin/bash

DIR=$(dirname ${BASH_SOURCE})

classpath=$DIR:$DIR/src:$DIR/src/donationAnalytics

input1=$DIR/input/itcont.txt
input2=$DIR/input/percentile.txt
output=$DIR/output/repeat_donors.txt

javac $DIR/src/donationAnalytics/*.java

java -cp $classpath donationAnalytics.Main $input1 $input2 $output
