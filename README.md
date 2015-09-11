__mini-plural__  - is a Java utility to generate plural word forms.

__mini-plural__  - это Java утилита для склонения слов по формам множественного числа.

[![Build Status](https://travis-ci.org/mfursov/mini-plural.svg?branch=master)]	(https://travis-ci.org/mfursov/mini-plural)

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