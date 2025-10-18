#!/bin/sh
#
#
# Small helper script to produce a coverage report when executing the fuzz-model
# against the current corpus.
#

set -eu


# Build the fuzzer and fetch dependency-jars
./gradlew shadowJar getDeps


# extract jar-files of Apache Tika
mkdir -p build/tikafiles
cd build/tikafiles

# then unpack the class-files
for i in `find ../runtime -name tika-*.jar`; do
  echo $i
  unzip -o -q $i
done

cd -


# Fetch JaCoCo Agent
test -f jacoco-0.8.14.zip || wget --continue https://repo1.maven.org/maven2/org/jacoco/jacoco/0.8.14/jacoco-0.8.14.zip
unzip -o jacoco-0.8.14.zip lib/jacocoagent.jar lib/jacococli.jar
mv lib/jacocoagent.jar lib/jacococli.jar build/
rmdir lib

# Need to remove these as otherwise JaCoCo fails on duplicate classes
rm -r build/tikafiles/META-INF/versions/

mkdir -p build/jacoco


# Run Jazzer with JaCoCo-Agent to produce coverage information
./jazzer \
  --cp=build/libs/tika-fuzz-all.jar \
  --instrumentation_excludes=org.apache.logging.**:org.slf4j.**:com.microsoft.schemas.**:org.openxmlformats.schemas.**:org.apache.xmlbeans.**:com.google.protobuf.**:com.google.common.**:ucar.nc2.**:org.mozilla.universalchardet.**:org.jdom2.**:javax.activation.**:javax.xml.bind.**:com.sun.** \
  --target_class=org.dstadler.tika.fuzz.Fuzz \
  --nohooks \
  --jvm_args="-XX\\:-OmitStackTraceInFastThrow:-javaagent\\:build/jacocoagent.jar=destfile=build/jacoco/corpus.exec" \
  -rss_limit_mb=8192 \
  -runs=0 \
  corpus


# Finally create the JaCoCo report
java -jar build/jacococli.jar report build/jacoco/corpus.exec \
 --classfiles build/tikafiles \
 --classfiles build/classes/java/main \
 --sourcefiles /opt/apache/tika/trunk/tika-core/src/main/java \
 --sourcefiles src/main/java \
 --html build/reports/jacoco


echo All Done, report is at build/reports/jacoco/index.html
