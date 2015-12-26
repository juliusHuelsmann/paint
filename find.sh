#!/bin/bash



trs() {
    local string=$1
    shift
    while [ $# -gt 0 ]
    do
        string="$(sed -e "s/$1/$2/g" <<< "$string")"
        shift 5
    done
    printf "$string"
}



for var in `find PaintNotes/src/ -name "*.java"` 
do

	#echo $var
	myResult=$(less $var | grep System.out.)
	
	replace=";\n\n";
	len="${#myResult}" 
	if [ $len -ge 1 ]; then 
		echo -e "\n"
		echo $var
		#echo -e $myResult
		#echo -e $myResult | sed -e "s/;/${replace}/g"
		echo -e $myResult | trs "System.out" "\nSystem.out"
	fi
done







