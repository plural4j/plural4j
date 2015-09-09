package mini.plural;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class PluralTest {

    private Word[] dictionary;

    @Before
    public void setUp() throws IOException {
        String dictionaryContent = load("dictionary.txt");
        dictionary = Plural.parse(dictionaryContent);
    }

    //FIXME: add tests for dictionary parsing & errors.

    @Test
    public void checkPluralForm() {
        Plural p = new Plural(dictionary);
        Assert.assertEquals("год", p.pl("год", 1));
        Assert.assertEquals("года", p.pl("год", 3));
        Assert.assertEquals("лет", p.pl("год", 7));
        Assert.assertEquals("лет", p.pl("год", 10));
        Assert.assertEquals("лет", p.pl("год", 100));
    }

    @Test
    public void checkPluralFormWithPrefix() {
        Plural p = new Plural(dictionary);
        Assert.assertEquals(" стул", p.pl(" стул", 1));
        Assert.assertEquals("  стула", p.pl("  стул", 2));
        Assert.assertEquals("-стула", p.pl("-стул", 3));
        Assert.assertEquals("--стульев", p.pl("--стул", 5));
        Assert.assertEquals(" -стульев", p.pl(" -стул", 8));
        Assert.assertEquals("- стульев", p.pl("- стул", 120));
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
