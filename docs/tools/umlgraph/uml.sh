#!/bin/sh

export JAVA_HOME=`/usr/libexec/java_home -v 1.7`
JARS="UmlGraph-5.6.6.jar"
PATH=$PATH:$JAVA_HOME/bin:$JAVA_HOME/lib/tools.jar:$JARS:"/usr/local/graphviz-2.14/bin"
DOCLET=org.umlgraph.doclet.UmlGraph
export PATH DOCLET JARS

PACKAGES="models"
OUTPUT="models.png"
java -classpath $PATH $DOCLET -sourcepath ../../../app $PACKAGES -attributes -operations -types
perl -pi -e 's/<any>/any/g' graph.dot
dot -Tpng -o ../../$OUTPUT graph.dot; rm graph.dot
cp ../../$OUTPUT ../../../public/$OUTPUT

PACKAGES="controllers.scrape controllers.seed controllers.utilities"
OUTPUT="controller.png"
java -classpath $PATH $DOCLET -sourcepath ../../../app $PACKAGES -attributes -operations -types
perl -pi -e 's/<any>/any/g' graph.dot
dot -Tpng -o ../../$OUTPUT graph.dot; rm graph.dot
cp ../../$OUTPUT ../../../public/$OUTPUT

PACKAGES="controllers.facebook"
OUTPUT="facebook.png"
java -classpath $PATH $DOCLET -sourcepath ../../../app $PACKAGES -attributes -operations -types
perl -pi -e 's/<any>/any/g' graph.dot
dot -Tpng -o ../../$OUTPUT graph.dot; rm graph.dot
cp ../../$OUTPUT ../../../public/$OUTPUT