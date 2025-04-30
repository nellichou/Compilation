### Variables ###

JC = javac
JCFLAGS = -implicit:none
JVM = java -jar
JVT = java
### Arbrorescence ###
SRC = ./projet/src/projet
CLASSCASE = ./projet/build
TEST = ./projet/src/projet/test
TESTCYCLE = ./projet/src/projet/testCycle
TESTEXISTANT = ./projet/src/projet/testExistant
TESTMODIF = ./projet/src/projet/testModif
TESTARGUMENT = ./projet/src/projet/testArgument

### Compilations dossiers ###### Variables ###

$(CLASSCASE)/Noeud.class : $(SRC)/Noeud.java
	$(JC) $(JCFLAGS) -d $(CLASSCASE) -cp $(CLASSCASE) -sourcepath $(SRC)  $(SRC)/Noeud.java

$(CLASSCASE)/Arbre.class : $(CLASSCASE)/Noeud.class $(SRC)/Arbre.java
	$(JC) $(JCFLAGS) -d $(CLASSCASE) -cp $(CLASSCASE) -sourcepath $(SRC)  $(SRC)/Arbre.java

$(CLASSCASE)/LectureBakefile.class : $(CLASSCASE)/Arbre.class $(SRC)/LectureBakefile.java
	$(JC) $(JCFLAGS) -d $(CLASSCASE) -cp $(CLASSCASE) -sourcepath $(SRC)  $(SRC)/LectureBakefile.java

$(CLASSCASE)/Bake.class : $(CLASSCASE)/Arbre.class $(CLASSCASE)/LectureBakefile.class $(SRC)/Bake.java
	$(JC) $(JCFLAGS) -d $(CLASSCASE) -cp $(CLASSCASE) -sourcepath $(SRC)  $(SRC)/Bake.java



### Commandes ###

## Commandes simples ##

compile : $(CLASSCASE)/Noeud.class $(CLASSCASE)/Arbre.class $(CLASSCASE)/LectureBakefile.class $(CLASSCASE)/Bake.class

test1 : compile
	cp $(TEST)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake
	$(JVT) -cp $(CLASSCASE)/projet Main

test1Debug : compile
	cp $(TEST)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake -d
	$(JVT) -cp $(CLASSCASE)/projet Main


testCycle : compile
	cp $(TESTCYCLE)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake
	$(JVT) -cp $(CLASSCASE)/projet Main

testCycleDebug : compile
	cp $(TESTCYCLE)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake -d
	$(JVT) -cp $(CLASSCASE)/projet Main


testExistant : compile 
	cp -p $(TESTEXISTANT)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake
	$(JVT) -cp $(CLASSCASE)/projet Main

testExistantDebug : compile
	cp -p $(TESTEXISTANT)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake -d
	$(JVT) -cp $(CLASSCASE)/projet Main


testModif : compile 
	cp -p $(TESTMODIF)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake
	$(JVT) -cp $(CLASSCASE)/projet Main

testModifDebug : compile
	cp -p $(TESTMODIF)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake -d
	$(JVT) -cp $(CLASSCASE)/projet Main
	

testArgument : compile 
	cp -p $(TESTARGUMENT)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake Utils.class

testArgumentDebug : compile
	cp -p $(TESTARGUMENT)/* $(CLASSCASE)/projet
	$(JVT) -cp $(CLASSCASE) projet.Bake -d Utils.class


## Commandes pour jar ##

ark : $(CLASSCASE)/Noeud.class $(CLASSCASE)/Arbre.class $(CLASSCASE)/LectureBakefile.class $(CLASSCASE)/Bake.class 
	jar cvef projet.Bake projet.jar -C $(CLASSCASE) .

run : ark
	java -jar projet.jar

clean :
	-rm -rf $(CLASSCASE)

.PHONY : transition
