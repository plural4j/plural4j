__plural4j__  - is a Java utility to generate plural word forms.

[![Build Status](https://travis-ci.org/plural4j/plural4j.svg?branch=master)]	(https://travis-ci.org/plural4j/plural4j)

## Maven
```xml
<dependency>
  <groupId>com.github.plural4j</groupId>
  <artifactId>plural4j</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Building

```
mvn -DskipTests=true clean package install
```


## Usage

```java
Plural p = new Plural(Plural.RUSSIAN, russianWords);
p.pl("год", 1) ➟ "год"
p.pl("год", 2) ➟ "года"
p.pl("год", 5) ➟ "лет"
p.pl("год", 0) ➟ "лет"
```

### Requirements

Java 1.6+


### License

This project available under MIT license