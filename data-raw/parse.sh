#!/usr/bin/bash

DIR=$1
HANDFILE="$DIR"hdb
ROSTERFILE="$DIR"hroster
PLAYERPREFIX="$DIR"pdb/

IFS="\n"

cat $HANDFILE | while read LINE; do # For each hand played

	TIMESTAMP=`echo $LINE | awk '{print $1}'`

	IFS=" "
	PLAYERS=($(grep $TIMESTAMP $ROSTERFILE| awk '{print $1=$2=""; print $0}' | sed 's/^ *//')) #Get the player list
	IFS="\n"

done
