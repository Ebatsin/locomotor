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
INT_COMPONENTS = 
INT_ADMIN =
INT_USER = $(FRONT)/user

# lib
MONGO = $(LIB)/*
CHK_STY = $(LIB)/checkstyle-7.3-all.jar
CHK_STY_CONF = $(LIB)/google_checks.xml

#TODO: handle windows

###################################################
# Targets:
###################################################

all: build-core

build-components: linter-components build-components-types build-components-models

build-components-types:
		$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(MONGO):$(CLASS):" $(FLAGS_CC) $(SRC)/$(COMPONENTS_TYPES)/*.java

build-components-models:
	$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(MONGO):$(CLASS):" $(FLAGS_CC) $(SRC)/$(COMPONENTS_MODELS)/*.java

build-core: linter-core
	-test -d $(CLASS) || mkdir $(CLASS)
	$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(MONGO):$(CLASS):" $(FLAGS_CC) $(SRC)/$(CORE)/$(MAIN)

build-front-user:
	-test -d $(CLASS) || mkdir $(CLASS)
	$(CC) -d $(CLASS) -sourcepath $(SRC) -classpath "$(LIB)/*:$(CLASS):resources:" $(FLAGS_CC) $(SRC)/$(INT_USER)/$(MAIN)

run-core:
	$(RUN) -classpath "$(LIB)/*:$(CLASS)" $(PACKAGE_CORE)

run-front-user:
	$(RUN) -classpath "$(LIB)/*:$(CLASS):resources:" $(PACKAGE_FRONT_USER)

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
.PHONY: clean
clean: clean-components clean-core

.PHONY: clean-components
clean-components:
	$(RM) $(CLASS)/$(COMPONENTS)/*

.PHONY: clean-core
clean-core:
	$(RM) $(CLASS)/$(CORE)/*

###################################################
# Housekeeping:
###################################################

.PHONY: linter
linter: linter-components linter-core

.PHONY: linter-components
linter-core: linter-components
	$(RUN) -jar $(CHK_STY) -c $(CHK_STY_CONF) $(SRC)/$(CORE)

.PHONY: linter-components
linter-components:
	$(RUN) -jar $(CHK_STY) -c $(CHK_STY_CONF) $(SRC)/$(COMPONENTS)
