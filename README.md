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

## Adding new languages

To add new language implement *Plural.Rule* class:

```
public static final Rule RUSSIAN = new Rule(3) {
    public int getPluralWordFormIdx(long n) {
        return (n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2);
    }
};
```

Check [*gettext*](http://localization-guide.readthedocs.org/en/latest/l10n/pluralforms.html) plural forms page for the list of predefined rules for different languages.

### Requirements

Java 1.6+


### License

This project available under MIT license