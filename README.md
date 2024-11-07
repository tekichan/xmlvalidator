# XmlValidator
[![License](https://img.shields.io/badge/license-MIT-green.svg)](/LICENSE)

XmlValidator is a Java-based command-line utility for validating XML files against a [DTD (Document Type Definition)](https://en.wikipedia.org/wiki/Document_type_definition)
and evaluating [XPath](https://en.wikipedia.org/wiki/XPath) expressions on the XML document.
It is designed to help ensure XML documents are well-formed and conform to the specified DTD, while allowing you to query specific nodes using XPath.

## Features
- XML Validation: Validate XML files against an external DTD.
- XPath Evaluation: Run XPath expressions to extract specific nodes from XML files.

## Prerequisites
- Java: Version 21 or higher
- Gradle: Version 8.2 or higher

## Build Instructions
To build the XmlValidator JAR file, follow these steps:

1. Clone this repository:
```shell
git clone https://github.com/tekichan/xmlvalidator.git
cd xmlvalidator
```

2. Build the project using Gradle:
```shell
./gradlew clean build
```

3. The JAR file will be generated as `build/libs/XmlValidator-1.0.0-all.jar`

## Usage
To use XmlValidator, execute the JAR with the following parameters:
```shell
java -jar build/libs/XmlValidator-1.0.0-all.jar <XML_FILE_PATH> <DTD_FILE_PATH> <XPATH_EXPRESSION>
```

### Parameters
- <XML_FILE_PATH>: The path to the XML file you want to validate.
- <DTD_FILE_PATH>: The path to the external DTD file. Set to `null` to use the DTD specified within the XML file.
- <XPATH_EXPRESSION>: The XPath expression you wish to evaluate on the XML document.

### Example
To validate an XML file example.xml using a DTD example.dtd and evaluate the XPath `//book/title`, run:
```shell
java -jar build/libs/XmlValidator-1.0.0-all.jar example.xml example.dtd "//book/title"
```

## Change History
- 7 Nov 2024 : 1.0.0 Release

## Authors
- Teki Chan *tekichan@gmail.com*
