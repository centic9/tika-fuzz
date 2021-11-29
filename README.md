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

    ./jazzer --cp=build/libs/tika-fuzz-all.jar --instrumentation_excludes=org.apache.logging.**:org.slf4j.**:com.microsoft.schemas.**:org.openxmlformats.schemas.**:org.apache.xmlbeans.** --target_class=org.dstadler.tika.fuzz.Fuzz -rss_limit_mb=8192 corpus

In this mode Jazzer will stop whenever it detects an unexpected exception
or crashes.

You can use `--keep_going=10` to report a given number of exceptions before stopping.

See `./jazzer` for options which can control details of how Jazzer operates.

# Fuzzing with locally compiled Apache Tika libraries

If you want to test with a more recent version of Apache Tika, you can add 
locally compiled jars of Apache Tika to the front `--cp` commandline argument (delimited with colon ':')

#### Licensing

Copyright 2021 Dominik Stadler

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
