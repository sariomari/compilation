###############
# DIRECTORIES #
###############
BASEDIR           = $(shell pwd)
JFlex_DIR         = ${BASEDIR}/jflex
CUP_DIR           = ${BASEDIR}/cup
SRC_DIR           = ${BASEDIR}/src
BIN_DIR           = ${BASEDIR}/bin
INPUT_DIR         = ${BASEDIR}/input
OUTPUT_DIR        = ${BASEDIR}/output
EXTERNAL_JARS_DIR = ${BASEDIR}/external_jars
MANIFEST_DIR      = ${BASEDIR}/manifest

#########
# FILES #
#########
JFlex_GENERATED_FILE      = ${SRC_DIR}/Lexer.java
CUP_GENERATED_FILES       = ${SRC_DIR}/Parser.java ${SRC_DIR}/TokenNames.java
JFlex_CUP_GENERATED_FILES = ${JFlex_GENERATED_FILE} ${CUP_GENERATED_FILES}
SRC_FILES                 = ${SRC_DIR}/*.java              \
                            ${SRC_DIR}/AST/*.java          \
                            ${SRC_DIR}/TYPES/*.java        \
                            ${SRC_DIR}/SYMBOL_TABLE/*.java
EXTERNAL_JAR_FILES        = ${EXTERNAL_JARS_DIR}/java-cup-11b-runtime.jar
MANIFEST_FILE             = ${MANIFEST_DIR}/MANIFEST.MF

########################
# DEFINITIONS :: JFlex #
########################
JFlex_PROGRAM  = jflex
JFlex_FLAGS    = -q
JFlex_DEST_DIR = ${SRC_DIR}
JFlex_FILE     = ${JFlex_DIR}/LEX_FILE.lex

######################
# DEFINITIONS :: CUP #
######################
CUP_PROGRAM                    = java -jar ${EXTERNAL_JARS_DIR}/java-cup-11b.jar 
CUP_FILE                       = ${CUP_DIR}/CUP_FILE.cup
CUP_GENERATED_PARSER_NAME      = Parser
CUP_GENERATED_SYMBOLS_FILENAME = TokenNames

######################
# DEFINITIONS :: CUP #
######################
CUP_FLAGS =                                \
-nowarn                                    \
-parser  ${CUP_GENERATED_PARSER_NAME}      \
-symbols ${CUP_GENERATED_SYMBOLS_FILENAME} 

#########################
# DEFINITIONS :: PARSER #
#########################
INPUT    = ${INPUT_DIR}/Input.txt
OUTPUT   = ${OUTPUT_DIR}/SemanticStatus.txt

##########
# TARGET #
##########
compile:
	clear
	@echo "* [0] Remove COMPILER program *"
	rm -rf COMPILER
	@echo "\n"
	@echo "* [1] Remove *.class files and JFlex-CUP generated files:  *"
	@echo "*     Lexer.java                                           *"
	@echo "*     Parser.java                                          *"
	@echo "*     TokenNames.java                                      *"
	rm -rf ${JFlex_CUP_GENERATED_FILES} ${BIN_DIR}/*.class ${BIN_DIR}/AST/*.class
	@echo "\n"
	@echo "* [2] Use JFlex to synthesize Lexer.java from LEX_FILE.lex *"
	$(JFlex_PROGRAM) ${JFlex_FLAGS} -d ${JFlex_DEST_DIR} ${JFlex_FILE}
	@echo "\n"
	@echo "* [3] Use CUP to synthesize Parser.java and TokenNames.java from CUP_FILE.cup *"
	$(CUP_PROGRAM) ${CUP_FLAGS} -destdir ${SRC_DIR} ${CUP_FILE}
	@echo "\n"
	@echo "* [4] Create *.class files from *.java files + CUP JAR *"
	javac -cp ${EXTERNAL_JAR_FILES} -d ${BIN_DIR} ${SRC_FILES}
	@echo "\n"
	@echo "* [5] Create a JAR file from from *.class files + CUP JAR *"
	jar cfm COMPILER ${MANIFEST_FILE} -C ${BIN_DIR} .
	
debug:
	clear
	@echo "* [0] Remove PARSER program *"
	rm -rf COMPILER
	@echo "\n"
	@echo "* [1] Remove *.class files and JFlex-CUP generated files:  *"
	@echo "*     Lexer.java                                           *"
	@echo "*     Parser.java                                          *"
	@echo "*     TokenNames.java                                      *"
	rm -rf ${JFlex_CUP_GENERATED_FILES} ${BIN_DIR}/*.class ${BIN_DIR}/AST/*.class
	@echo "\n"
	@echo "* [2] Use JFlex to synthesize Lexer.java from LEX_FILE.lex *"
	$(JFlex_PROGRAM) ${JFlex_FLAGS} -d ${JFlex_DEST_DIR} ${JFlex_FILE}
	@echo "\n"
	@echo "* [3] Use CUP to synthesize Parser.java and TokenNames.java from CUP_FILE.cup *"
	$(CUP_PROGRAM) ${CUP_FLAGS} -destdir ${SRC_DIR} ${CUP_FILE}
	@echo "\n"
	@echo "* [4] Create *.class files from *.java files + CUP JAR *"
	javac -cp ${EXTERNAL_JAR_FILES} -d ${BIN_DIR} ${SRC_FILES}
	@echo "\n"
	@echo "* [5] Create a JAR file from from *.class files + CUP JAR *"
	rm -f ${OUTPUT_DIR}/*.txt
	rm -f ${OUTPUT_DIR}/*.jpeg
	jar cfm COMPILER ${MANIFEST_FILE} -C ${BIN_DIR} .
	@echo "\n"
	@echo "* [6] Run resulting program *"
	java -jar COMPILER ${INPUT} ${OUTPUT}
	@echo "\n"
	@echo "* [7] Create a jpeg AST visualization *"
	dot -Tjpeg -o${OUTPUT_DIR}/AST.jpeg ${OUTPUT_DIR}/AST_IN_GRAPHVIZ_DOT_FORMAT.txt
	@echo "\n"
	@echo "* [8] Open the jpeg AST visualization with eog *"
	#eog ${OUTPUT_DIR}/AST.jpeg &
	@echo "* [9] Create a jpeg SYMBOL TABLE visualization in steps *"
	for i in 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 ; do \
		if test -f "${OUTPUT_DIR}/SYMBOL_TABLE_$${i}_IN_GRAPHVIZ_DOT_FORMAT.txt" ; then \
			dot -Tjpeg -o${OUTPUT_DIR}/SYMBOL_TABLE_$${i}.jpeg  ${OUTPUT_DIR}/SYMBOL_TABLE_$${i}_IN_GRAPHVIZ_DOT_FORMAT.txt ; \
		fi \
	done
	@echo "* [10] Open the (first) jpeg SYMBOL TABLE visualization with eog *"
	eog ${OUTPUT_DIR}/SYMBOL_TABLE_0.jpeg &
	
