#!/bin/sh
#
#
# Small helper script to produce a coverage report when executing the fuzz-model
# against the current corpus.
#

set -eu


# Build the fuzzer and fetch dependency-jars
./gradlew shadowJar getDeps


# extract class-files of Apache POi
mkdir -p build/tikafiles
cd build/tikafiles
for i in `find /opt/apache/poi/dist/release/maven/ -type f | grep .jar | grep -v -- -sources.jar | grep -v -- -javadoc.jar | grep -v -- .sha512 | grep -v -- .sha256 | grep -v -- .asc | grep -v poi-ooxml-full | grep -v poi-integration | grep -v poi-examples | grep -v poi-excelant`; do
  echo $i
  unzip -o -q $i
done


# Remove some packages that we do not want to include in the Report
rm -r com
rm -r org/openxmlformats
rm -r org/etsi
rm -r org/w3

cd -



# Fetch JaCoCo Agent
test -f jacoco-0.8.7.zip || wget --continue https://repo1.maven.org/maven2/org/jacoco/jacoco/0.8.7/jacoco-0.8.7.zip
unzip -o jacoco-0.8.7.zip lib/jacocoagent.jar
mv lib/jacocoagent.jar build/
rmdir lib

mkdir -p build/jacoco


# Run Jazzer with JaCoCo-Agent to produce coverage information
./jazzer \
  --cp=build/libs/tika-fuzz-all.jar \
  --instrumentation_excludes=org.apache.logging.**:org.slf4j.**:com.microsoft.schemas.**:org.openxmlformats.schemas.**:org.apache.xmlbeans.** \
  --target_class=org.dstadler.tika.fuzz.Fuzz \
  --nohooks \
  --jvm_args="-XX\\:-OmitStackTraceInFastThrow:-javaagent\\:build/jacocoagent.jar=destfile=build/jacoco/corpus.exec" \
  -rss_limit_mb=8192 \
  -runs=0 \
  corpus


# Finally create the JaCoCo report
java -jar /opt/poi/lib/util/jacococli.jar report build/jacoco/corpus.exec \
 --classfiles build/tikafiles \
 --classfiles build/classes/java/main \
 --sourcefiles /opt/apache/tika/trunk/tika-core/src/main/java \
 --sourcefiles src/main/java \
 --html build/reports/jacoco


echo All Done, report is at build/reports/jacoco/index.html