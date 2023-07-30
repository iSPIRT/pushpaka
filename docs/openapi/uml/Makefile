PLANTUML_JAR = /Users/sayandeep/Applications/plantuml.jar
OUT_DIR = out

# DIAGRAMS_SRC := $(wildcard */*.puml)
# DIAGRAMS_PNG := $(addprefix $(OUT_DIR)/, $(addsuffix .png, $(basename $(DIAGRAMS_SRC))))

all: DIAGRAMS_PNG

# $(OUT_DIR)/%.png: %.puml
# 	java -jar $(PLANTUML_JAR) -tpng -o ../$(OUT_DIR)/ $^

DIAGRAMS_PNG:
	java -jar $(PLANTUML_JAR) -tpng -o ../$(OUT_DIR)/registry registry
	java -jar $(PLANTUML_JAR) -tpng -o ../$(OUT_DIR)/restriction-mgmt restriction-mgmt
	java -jar $(PLANTUML_JAR) -tpng -o ../$(OUT_DIR)/airspace-authorisation airspace-authorisation

clean:
	rm -rf $(OUT_DIR)