#!/bin/sh

JAVA_HOME="/usr/j2se"
BASE_HOME="/export/home/ripoche/java/marray"
CLASSES_HOME="$BASE_HOME/classes"
CLASSPATH=".:$CLASSES_HOME"

INTERPRETER="$JAVA_HOME/bin/java"
OPTIONS="-Xmx2048m -classpath $CLASSPATH" # -Xmx2048 -Xprof -verbose

APPLICATION="application.Application"

echo $INTERPRETER $OPTIONS $APPLICATION $@

exec $INTERPRETER $OPTIONS $APPLICATION $@
