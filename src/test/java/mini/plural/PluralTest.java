package mini.plural;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static mini.plural.Plural.Word;
import static mini.plural.Plural.parse;

public class PluralTest {

    private Word[] russianWords;
    private Word[] englishWords;

    @Before
    public void setUp() throws IOException {
        russianWords = parse(load("dictionary-ru.txt"));
        englishWords = parse(load("dictionary-en.txt"));
    }

    //FIXME: add tests for russianWords parsing & errors.

    @Test
    public void checkRussianPluralForm() {
        Plural p = new Plural(PluralForms.RUSSIAN, russianWords);
        Assert.assertEquals("год", p.pl("год", 1));
        Assert.assertEquals("года", p.pl("год", 3));
        Assert.assertEquals("лет", p.pl("год", 7));
        Assert.assertEquals("лет", p.pl("год", 10));
        Assert.assertEquals("лет", p.pl("год", 100));
        Assert.assertEquals("лет", p.pl("год", 0));
    }

    @Test
    public void checkEnglishPluralForm() {
        Plural p = new Plural(PluralForms.ENGLISH, englishWords);
        Assert.assertEquals("year", p.pl("year", 1));
        Assert.assertEquals("years", p.pl("year", 3));
        Assert.assertEquals("months", p.pl("month", 7));
        Assert.assertEquals("hours", p.pl("hour", 10));
        Assert.assertEquals("clients", p.pl("client", 0));
    }

    @Test
    public void checkRussianPluralFormWithPrefix() {
        Plural p = new Plural(PluralForms.RUSSIAN, russianWords);
        Assert.assertEquals(" клиент", p.pl(" клиент", 1));
        Assert.assertEquals("  клиента", p.pl("  клиент", 2));
        Assert.assertEquals("-клиента", p.pl("-клиент", 3));
        Assert.assertEquals("--клиентов", p.pl("--клиент", 5));
        Assert.assertEquals(" -клиентов", p.pl(" -клиент", 8));
        Assert.assertEquals("- клиентов", p.pl("- клиент", 120));
    }

    @Test
    public void checkNoPluralForm() {
        Plural p = new Plural(PluralForms.RUSSIAN, russianWords);
        Assert.assertEquals("гд", p.pl("гд", 1));
        Assert.assertEquals("гд", p.pl("гд", 3));
        Assert.assertEquals("гд", p.pl("гд", 7));
        Assert.assertEquals("", p.pl("", 0));
        Assert.assertEquals("", p.pl("", 3));
        Assert.assertEquals("", p.pl("", 7));
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
