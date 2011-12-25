.PHONY: run build

JAVAC=javac
JAVA=java
CP=lib/pircbot/pircbot.jar:lib/informa/informa.jar
BUILD=build

HOST=irc.esper.net
CHANNEL=hpelizausers

MAIN=uk.co.harcourtprogramming.netcat.docitten.Main
FILES=$(wildcard src/uk/co/harcourtprogramming/netcat/*.java) $(wildcard src/uk/co/harcourtprogramming/netcat/docitten/*.java)

run:
	$(JAVA) -cp $(CP):$(BUILD) $(MAIN) $(HOST) $(CHANNEL)

build: $(FILES)
	$(JAVAC) -classpath $(CP) -d $(BUILD) $(FILES)

