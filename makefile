##
 #
 # @gaelfoppolo FOPPOLO Gaël
 # @Ebatsin     PHILIP Bastien
 #
 # @brief Makefile
 #
 # @see README for further instructions
 #
##

###################################################
# Variables:
###################################################

CC = javac
RUN = java
CCD = javadoc
LIB = lib
SRC = src
CLASS = class
DOC = doc

FLAGS_CC = -Xdiags:verbose -Xlint:deprecation
FLAGC_DOC = -encoding UTF-8 -charset UTF-8 -docencoding UTF-8
RM = rm -rf

# paths
CORE = locomotor/core
USER = 
ADMIN =

# same name for all
MAIN = Main.java

# packages
PACKAGE = locomotor
PACKAGE_CORE = $(PACKAGE).core.Main
PACKAGE_INTERFACE_USER =
PACKAGE_INTERFACE_ADMIN =

#TODO: handle windows

###################################################
# Targets:
###################################################

all: build-core

build-core: 
	-test -d $(CLASS) || mkdir $(CLASS)
	$(CC) -d $(CLASS) -sourcepath $(SRC)/ $(SRC)/$(CORE)/$(MAIN) -classpath "$(LIB)/*:" $(FLAGS_CC)

run-core:
	$(RUN) -classpath "$(LIB)/*:$(CLASS)" $(PACKAGE_CORE)

###################################################
# Doc:
###################################################

.PHONY: doc
doc: 
	test -d $(DOC) || mkdir $(DOC)
	$(CCD) -sourcepath $(SRC) -d $(DOC) -subpackages $(PACKAGE) $(FLAGS_DOC)

###################################################
# Housekeeping:
###################################################
clean:
	-$(MAKE) clean-core

clean-core:
	rm -f $(CLASS)/$(CORE)/*