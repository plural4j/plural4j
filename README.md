__mini-plural__ Utility to generate plural forms for Russian words

__mini-plural__  - это Java утилита для склонения слов по формам множественного числа.

[![Build Status](https://travis-ci.org/mfursov/mini-plural.svg?branch=master)]	(https://travis-ci.org/mfursov/mini-plural)

## Building

```
mvn -DskipTests=true clean package install
```

## Usage

```java
Plural p = new Plural();
p.pl("год", 1) ➟ "год"
p.pl("год", 2) ➟ "года"
p.pl("год", 5) ➟ "лет"
```

### Конфигурация

TODO:


### License

This project available under MIT license