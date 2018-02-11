#!/bin/bash

classpath=.:./src:./src/donationAnalytics
input1=./input/itcont.txt
input2=./input/percentile.txt
output=./output/repeat_donors.txt

javac ./src/donationAnalytics/*.java

java -cp $classpath donationAnalytics.Main $input1 $input2 $output
