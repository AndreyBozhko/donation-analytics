#!/bin/bash

DIR=$(dirname ${BASH_SOURCE})

input1=$DIR/input/itcont.txt
input2=$DIR/input/percentile.txt
output=$DIR/output/repeat_donors.txt

javac -d $DIR/bin $DIR/src/donationAnalytics/*.java

java -cp $DIR/bin donationAnalytics.Main $input1 $input2 $output
