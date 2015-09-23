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
p.pl(1, "год") ➟ "год"
p.pl(2, "год") ➟ "года"
p.pl(5, "год") ➟ "лет"
p.pl(0, "год") ➟ "лет"
...
p = new Plural(Plural.ENGLISH, englishWords);
p.pl(5, "apple") ➟ "apples"
p.npl(5, " apple") ➟ "5 apples"
p.npl(10, " man") ➟ "10 people"
```

### Requirements

Java 1.6+


### License

This project available under MIT license