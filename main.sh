PROBLEM_FILE=$1 #problem .c file location
OUTPUT_FILE=$2 #file location of outputted fixed .c file
MODE=$3 #0 for scan-build, 1 for cppcheck

SB_OPTION=0 #constant representing running in scan-build mode
CPP_OPTION=1 #constant representing running in cppcheck mode
CUST_OPTION=2 #custom bug checks through central system

GENERATED_FILES_LOCATION="/Users/emma/Desktop/Projects/FinalYear/fixBugAntlr/misc/generatedfiles"
LIB_DIR="/Users/emma/Desktop/Projects/FinalYear/fixBugAntlr/scriptReferenceJars"

PROJ_DIR="/Users/emma/Desktop/Projects/FinalYear/fixBugAntlr"

SB_REPORT_PARSER=$LIB_DIR"/ScanBuildParser.jar"
CPP_REPORT_PARSER=$LIB_DIR"/CppCheckParser.jar"
CENTRAL_SYS=$LIB_DIR"/CentralSystem.jar"

BUGSER="$GENERATED_FILES_LOCATION/bug.ser"

if [ $MODE == $SB_OPTION ]
then
	echo "Running in scan-build mode..."
	
	/Applications/checker-277/scan-build -o $GENERATED_FILES_LOCATION gcc -c $PROBLEM_FILE
	#running scan-build

	REPORT_DIR_NAME=$(ls -t $GENERATED_FILES_LOCATION | head -n 1)
	#obtaining report directory
	
	REPORT_DIR="${GENERATED_FILES_LOCATION}/${REPORT_DIR_NAME}"
	#full report directory
	
	cd $REPORT_DIR
	BUGREPORT=`ls -d report*`
	#obtaining report HTML file name
	
	echo "scan-build's report is stored at: " $BUGREPORT
	
	echo ""
	
	java -jar $SB_REPORT_PARSER "$BUGREPORT" "$PROBLEM_FILE" "$BUGSER"
	#running the scan build bug report parser
	
	echo ""
	
	cd $LIB_DIR
	
	java -jar $CENTRAL_SYS "$BUGSER" "$OUTPUT_FILE"
	#running the central system
fi

if [ $MODE == $CPP_OPTION ]
then
	echo "Running in cppcheck mode..."
	
	XML_OUTPUT="$GENERATED_FILES_LOCATION/report.xml"
	#the XML output of the report
	
	cppcheck $PROBLEM_FILE --xml 2>$XML_OUTPUT
	#storing cppcheck's results to the approrpiate output file
	
	echo "cppcheck's report is stored at: " $XML_OUTPUT
	
	echo ""
	
	java -jar $CPP_REPORT_PARSER "$XML_OUTPUT" "$BUGSER"
	#running the cpp check report parser
	
	cd $LIB_DIR
	
	java -jar $CENTRAL_SYS "$BUGSER" "$OUTPUT_FILE"
	#running the central system
	
fi

if [ $MODE == $CUST_OPTION ]
then
	echo "Checking for custom bugs..."
	
	cd $LIB_DIR
	
	java -jar $CENTRAL_SYS "0" "$PROBLEM_FILE" "$OUTPUT_FILE"
	
fi