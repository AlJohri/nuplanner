#!/bin/sh

export JAVA_HOME=`/usr/libexec/java_home -v 1.7`
JARS="UmlGraph-5.6.6.jar"
PATH=$PATH:$JAVA_HOME/bin:$JAVA_HOME/lib/tools.jar:$JARS:"/usr/local/graphviz-2.14/bin"
DOCLET=org.umlgraph.doclet.UmlGraph
PACKAGES="models controllers.scrape controllers.seed"
export PATH DOCLET JARS PACKAGES

java -classpath $PATH $DOCLET -sourcepath ../../../app $PACKAGES -attributes -operations -types
perl -pi -e 's/<any>/any/g' graph.dot
dot -Tpng -o ../../graph1.png graph.dot
rm graph.dot
cp ../../graph1.png ../../../public/graph1.png

PACKAGES="controllers.utilities"
java -classpath $PATH $DOCLET -sourcepath ../../../app $PACKAGES -attributes -operations -types
perl -pi -e 's/<any>/any/g' graph.dot
dot -Tpng -o ../../graph2.png graph.dot
rm graph.dot
cp ../../graph2.png ../../../public/graph2.png


