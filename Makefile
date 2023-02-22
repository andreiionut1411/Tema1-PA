#! /bin/bash

build:
	javac Walsh.java Statistics.java Prinel.java Crypto.java Regele.java

run-p1:
	java Walsh

run-p2:
	java Statistics

run-p3:
	java Prinel

run-p4:
	java Crypto

run-p5:
	java Regele

clean:
	rm -f *.class
