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

FLAGS_CC = -Xdiags:verbose
FLAGC_DOC = -encoding UTF-8 -charset UTF-8 -docencoding UTF-8
RM = rm -rf

# paths
COMPONENTS = locomotor/components
CORE = locomotor/core
INT_COMPONENTS = 
INT_ADMIN =
INT_ADMIN =

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
	$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(LIB)/*:$(CLASS):" $(FLAGS_CC) $(SRC)/$(CORE)/$(MAIN)

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
clean: clean-components clean-core

clean-components:
	$(RM) $(CLASS)/$(COMPONENTS)/*
clean-core:
	$(RM) $(CLASS)/$(CORE)/*