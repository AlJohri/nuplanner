#!/bin/sh

export JAVA_HOME=`/usr/libexec/java_home -v 1.7`
JARS="UmlGraph-5.6.6.jar"

PATH=$PATH:$JAVA_HOME/bin:$JAVA_HOME/lib/tools.jar:$JARS:"/usr/local/graphviz-2.14/bin"
DOCLET=org.umlgraph.doclet.UmlGraph
PACKAGES="controllers"

export JAVA_HOME PATH DOCLET JARS PACKAGES

echo $PATH

java -classpath $PATH $DOCLET -sourcepath ../../app $PACKAGES -attributes -operations -types
dot -Tpng -ograph.png graph.dot
rm graph.dot
open graph.png
