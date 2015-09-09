__mini-plurals__ Utility to generate plural forms for Russian words

__mini-plurals  - это Java утилита для склонения слов по формам множественного числа.

[![Build Status](https://travis-ci.org/mfursov/mini-plurals.svg?branch=master)]	(https://travis-ci.org/mfursov/mini-plurals)

## Building

```
mvn -DskipTests=true clean package install
```

## Usage

```java
Plurals p = new Plurals();
p.pl("год", 1) ➟ "год"
p.pl("год", 2) ➟ "года"
p.pl("год", 5) ➟ "лет"
```

### Конфигурация

TODO:


### License

This project available under MIT license