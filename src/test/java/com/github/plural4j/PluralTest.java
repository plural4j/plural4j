package com.github.plural4j;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static com.github.plural4j.Plural.WordForms;
import static com.github.plural4j.Plural.parse;

public class PluralTest extends Assert {

    private WordForms[] russianWords;
    private WordForms[] englishWords;

    @Before
    public void setUp() throws IOException {
        russianWords = parse(load("dictionary-ru.txt"));
        englishWords = parse(load("dictionary-en.txt"));
    }

    //FIXME: add tests for russianWords parsing & errors.

    @Test
    public void checkRussianPluralForm() {
        Plural p = new Plural(Plural.RUSSIAN, russianWords);
        assertEquals("год", p.pl(1, "год"));
        assertEquals("года", p.pl(3, "год"));
        assertEquals("лет", p.pl(7, "год"));
        assertEquals("лет", p.pl(10, "год"));
        assertEquals("лет", p.pl(100, "год"));
        assertEquals("лет", p.pl(0, "год"));
    }

    @Test
    public void checkEnglishPluralForm() {
        Plural p = new Plural(Plural.ENGLISH, englishWords);
        assertEquals("year", p.pl(1, "year"));
        assertEquals("years", p.pl(3, "year"));
        assertEquals("months", p.pl(7, "month"));
        assertEquals("hours", p.pl(10, "hour"));
        assertEquals("clients", p.pl(0, "client"));
    }

    @Test
    public void checkNegativeNumbers() {
        Plural p = new Plural(Plural.ENGLISH, englishWords);
        assertEquals("year", p.pl(-1, "year"));
        assertEquals("day", p.pl(-3, "day"));
        assertEquals("month", p.pl(Integer.MIN_VALUE, "month"));
    }

    @Test
    public void checkRussianPluralFormWithPrefix() {
        Plural p = new Plural(Plural.RUSSIAN, russianWords);
        assertEquals(" клиент", p.pl(1, " клиент"));
        assertEquals("  клиента", p.pl(2, "  клиент"));
        assertEquals("-клиента", p.pl(3, "-клиент"));
        assertEquals("--клиентов", p.pl(5, "--клиент"));
        assertEquals(" -клиентов", p.pl(8, " -клиент"));
        assertEquals("- клиентов", p.pl(120, "- клиент"));
        assertEquals(" - клиентов", p.pl(11, " - клиент"));
    }

    @Test
    public void checkRussianPluralFormWithPrefixAndNumber() {
        Plural p = new Plural(Plural.RUSSIAN, russianWords);
        assertEquals("1 клиент", p.npl(1, " клиент"));
        assertEquals("2  клиента", p.npl(2, "  клиент"));
        assertEquals("3-клиента", p.npl(3, "-клиент"));
        assertEquals("5--клиентов", p.npl(5, "--клиент"));
        assertEquals("8 -клиентов", p.npl(8, " -клиент"));
        assertEquals("120- клиентов", p.npl(120, "- клиент"));
    }

    @Test
    public void checkNoPluralForm() {
        Plural p = new Plural(Plural.RUSSIAN, russianWords);
        assertEquals("гд", p.pl(1, "гд"));
        assertEquals("гд", p.pl(3, "гд"));
        assertEquals("гд", p.pl(7, "гд"));
        assertEquals("", p.pl(0, ""));
        assertEquals("", p.pl(3, ""));
        assertEquals("", p.pl(7, ""));
        assertEquals(" ", p.pl(2, " "));
        assertEquals("-", p.pl(3, "-"));
    }

    private static String load(String fileName) throws IOException {
        InputStreamReader reader = new InputStreamReader(PluralTest.class.getResourceAsStream("/" + fileName), "UTF-8");
        try {
            StringBuilder res = new StringBuilder();
            int len;
            char[] chr = new char[4096];
            while ((len = reader.read(chr)) > 0) {
                res.append(chr, 0, len);
            }
            return res.toString();
        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {
            }
        }
    }
}
