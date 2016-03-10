## Description 

The Email Ranking System can rank list of emails with respect to their event type and date. 

## Installation

### Download Stanford CoreNLP (for parsing word)

please download from [their official website](http://stanfordnlp.github.io/CoreNLP/api.html) (since it is too big)

### Download Cyc Core API Suite (for reasoning)

1. JDK 1.7 or greater to build; code can be run on Java 6.
2. Apache Maven, version 3.2 or higher. 
3. The following Cyc server releases are supported:
 1. ResearchCyc 4.0q or higher. Requires server code patching.
 2. EnterpriseCyc 1.7-preview or higher. Requires server code patching.

Please download from [their official website ](http://opencyc.org/)

### Set environment variable

Add all jar lib from both Stanford CoreNLP and Cyc Core API Suite to the 'lib' folder of project (If do not have 'lib', new one first.)
