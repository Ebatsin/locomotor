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
CCD = doxygen
LIB = lib
SRC = src
CLASS = class
DOC = doc
RESOURCES = resources
DATA = data

FLAGS_CC = -Xdiags:verbose
RM = rm -rf

# packages
PACKAGE = locomotor
PACKAGE_CORE = $(PACKAGE).core.Main
PACKAGE_FRONT_USER = $(PACKAGE).front.user.Main
PACKAGE_FRONT_ADMIN =
MAIN = Main.java

# paths
COMPONENTS = $(PACKAGE)/components
COMPONENTS_TYPES = $(COMPONENTS)/types
COMPONENTS_MODELS = $(COMPONENTS)/models
CORE = $(PACKAGE)/core
FRONT = $(PACKAGE)/front
INT_COMPONENTS = $(FRONT)/components
INT_ADMIN = $(FRONT)/administration
INT_USER = $(FRONT)/user

# lib
LIBALL = $(LIB)/*
CHK_STY = $(LIB)/checkstyle-7.3-all.jar
CHK_STY_CONF = $(LIB)/google_checks.xml
DOC_CONF = doxygen.cfg

#TODO: handle windows

###################################################
# Targets:
###################################################

all: build-core

build-components: linter-components build-components-types build-components-models

build-components-types:
		$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(LIBALL):$(CLASS):" $(FLAGS_CC) $(SRC)/$(COMPONENTS_TYPES)/*.java

build-components-models:
	$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(LIBALL):$(CLASS):" $(FLAGS_CC) $(SRC)/$(COMPONENTS_MODELS)/*.java

build-core: linter-core
	-test -d $(CLASS) || mkdir $(CLASS)
	$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(LIBALL):$(CLASS):" $(FLAGS_CC) $(SRC)/$(CORE)/$(MAIN)

build-front-user:
	-test -d $(CLASS) || mkdir $(CLASS)
	$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(LIBALL):$(CLASS):$(RESOURCES):" $(FLAGS_CC) $(SRC)/$(INT_USER)/$(MAIN)

run-core:
	$(RUN) -classpath "$(LIB)/*:$(CLASS)" $(PACKAGE_CORE) $(ARGS)

run-front-user:
	$(RUN) -classpath "$(LIB)/*:$(CLASS):$(RESOURCES):" $(PACKAGE_FRONT_USER) $(ARGS)

###################################################
# Doc:
###################################################

.PHONY: doc
doc: 
	test -d $(DOC) || mkdir $(DOC)
	$(CCD) $(DOC_CONF)

###################################################
# Housekeeping:
###################################################
.PHONY: clean
clean: clean-components clean-core clean-front clean-doc

.PHONY: clean-components
clean-components:
	$(RM) $(CLASS)/$(COMPONENTS)/*

.PHONY: clean-core
clean-core:
	$(RM) $(CLASS)/$(CORE)/*

.PHONY: clean-front
clean-front:
	$(RM) $(CLASS)/$(FRONT)/*

.PHONY: clean-doc
clean-doc:
	$(RM) $(DOC)/*

###################################################
# Lintering:
###################################################

.PHONY: linter
linter: linter-components linter-core linter-front

.PHONY: linter-core
linter-core: linter-components
	$(RUN) -jar $(CHK_STY) -c $(CHK_STY_CONF) $(SRC)/$(CORE)

.PHONY: linter-components
linter-components:
	$(RUN) -jar $(CHK_STY) -c $(CHK_STY_CONF) $(SRC)/$(COMPONENTS)

.PHONY: linter-front
linter-front: linter-components linter-front-components linter-front-user

.PHONY: linter-front-components
linter-front-components:
	$(RUN) -jar $(CHK_STY) -c $(CHK_STY_CONF) $(SRC)/$(INT_COMPONENTS)

.PHONY: linter-front-user
linter-front-user:
	$(RUN) -jar $(CHK_STY) -c $(CHK_STY_CONF) $(SRC)/$(INT_USER)

###################################################
# Database:
###################################################

DATAJSON = $(shell echo $(DATA)/*.json)
update-database:
	$(foreach file, $(DATAJSON), mongoimport -d $(PACKAGE) -c $(shell basename $(file) .json) --drop --file $(file);)
