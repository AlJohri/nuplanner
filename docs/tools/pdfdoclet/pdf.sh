#!/bin/sh

# Simple example shell script which demonstrates
# how to use the PDFDoclet with javadoc directly
# (which means: without ANT).

# Set the JAVA_HOME variable correctly !!
export JAVA_HOME=`/usr/libexec/java_home -v 1.7`
JARS=pdfdoclet-1.0.2-all.jar
PATH=$JAVA_HOME/bin:$PATH
DOCLET=com.tarsec.javadoc.pdfdoclet.PDFDoclet
PACKAGES="models controllers"
export JAVA_HOME PATH DOCLET JARS PACKAGES

javadoc -doclet $DOCLET -docletpath $JARS -pdf ../../nu-planner-backend.pdf -config nu-planner.properties -sourcepath ../../../app $PACKAGES

cp ../../nu-planner-backend.pdf ../../../public/nu-planner-backend.pdf
