This is a small project for fuzzing Apache Tika with the [jazzer](https://github.com/CodeIntelligenceTesting/jazzer) fuzzing tool.

See [Fuzzing](https://en.wikipedia.org/wiki/Fuzzing) for a general description of the theory behind fuzzy testing.

Because Java uses a runtime environment which does not crash on invalid actions of an 
application (unless native code is invoked), Fuzzing of Java-based applications  
focuses on the following:

* verify if only expected exceptions are thrown
* verify any JNI or native code calls 

Apache Tika does not use JNI or native code, therefore the fuzzing target mainly
tries to trigger unexpected exceptions.

# How to fuzz

Build the fuzzing target:

    ./gradlew shadowJar

Download the corpus of test-files from Apache POI sources

    svn co https://svn.apache.org/repos/asf/poi/trunk/test-data corpus

Download Jazzer from the [releases page](https://github.com/CodeIntelligenceTesting/jazzer/releases), 
choose the latest version and select the file `jazzer-<os>-<version>.tar.gz`

Unpack the archive:

    tar xzf jazzer-*.tar.gz

Invoke the fuzzing:

    ./jazzer --cp=build/libs/tikafuzz-all.jar --instrumentation_excludes=org.apache.logging.**:org.slf4j.** --target_class=org.dstadler.tika.fuzz.Fuzz -rss_limit_mb=8192 corpus

In this mode Jazzer will stop whenever it detects an unexpected exception
or crashes.

You can use `--keep_going=10` to report a given number of exceptions before stopping.

See `./jazzer` for options which can control details of how Jazzer operates.

# Fuzzing with locally compiled Apache Tika libraries

If you want to test with a more recent version of Apache Tika, you can add 
locally compiled jars of Apache Tika to the front `--cp` commandline argument (delimited with colon ':')
