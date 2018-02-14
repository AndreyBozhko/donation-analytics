#!/bin/bash

DIR=$(dirname ${BASH_SOURCE})

classpath=$DIR/bin

input1=$DIR/input/itcont.txt
input2=$DIR/input/percentile.txt
output=$DIR/output/repeat_donors.txt

javac -d $DIR/bin $DIR/src/donationAnalytics/*.java

java -cp $classpath donationAnalytics.Main $input1 $input2 $output
