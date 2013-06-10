#!/bin/sh

# Simple example shell script which demonstrates
# how to use the PDFDoclet with javadoc directly
# (which means: without ANT).

# Set the JAVA_HOME variable correctly !!
export JAVA_HOME=`/usr/libexec/java_home -v 1.7`
PATH=$JAVA_HOME/bin

VERSION=1.0.2

DOCLET=com.tarsec.javadoc.pdfdoclet.PDFDoclet
JARS=pdfdoclet-$VERSION-all.jar
PACKAGES="controllers models"

export JAVA_HOME PATH DOCLET JARS PACKAGES

# javadoc -doclet $DOCLET -docletpath $JARS -pdf nu-planner.pdf -config nu-planner.properties -private -sourcepath ../ $PACKAGES

javadoc -doclet $DOCLET -docletpath $JARS -pdf nu-planner.pdf -config nu-planner.properties -sourcepath ../app $PACKAGES
