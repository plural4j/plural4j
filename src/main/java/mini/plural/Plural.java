package mini.plural;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plural  - Utility class to generate plural word forms. Designed without any dependencies
 * for easy integration into other projects.
 * <p/>
 * Usage:
 * Plural p = new Plural(Plural.RUSSIAN, russianWords);
 * p.pl("год", 1) ➟ "год"
 * p.pl("год", 2) ➟ "года"
 * p.pl("год", 5) ➟ "лет"
 * p.pl("год", 0) ➟ "лет"
 * <p/>
 * <p/>
 * If you need plural forms other languages, except embedded English, German and Russian,
 * check http://localization-guide.readthedocs.org/en/latest/l10n/pluralforms.html
 * and implement your own one-liner Form class.
 */
public final class Plural {
    /**
     * In word dictionaries line is considered as a comment and ignored if starts with '#'.
     */
    public static final String COMMENT_PREFIX = "#";

    /**
     * Plural form for English language.
     */
    public static final Form ENGLISH = new Plural.Form(2) {
        public int getPluralWordFormIdx(int n) {
            return n == 1 ? 0 : 1;
        }
    };

    /**
     * Plural form for German language.
     */
    public static final Form GERMAN = ENGLISH;


    /**
     * Plural form for Russian language.
     */
    public static final Form RUSSIAN = new Plural.Form(3) {
        public int getPluralWordFormIdx(int n) {
            return (n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2);
        }
    };

    /**
     * The form to use.
     */
    private final Form form;

    /**
     * Dictionary. All forms by 1-st word form.
     */
    private final Map<String, Word> words = new HashMap<String, Word>();

    //    TODO
    public Plural(Form form, String words) {
        this(form, parse(words));
    }

    //    TODO
    public Plural(Form form, Word[] words) {
        this.form = form;
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException("No words provided: " + Arrays.toString(words));
        }
        for (Word w : words) {
            if (w.wordForms.length != form.nPlurals) {
                throw new IllegalArgumentException("Illegal count of word forms: " + Arrays.toString(w.wordForms));
            }
            if (this.words.containsKey(w.wordForms[0])) {
                throw new IllegalArgumentException("Duplicate word: " + w.wordForms[0]);
            }
            this.words.put(w.wordForms[0], w);
        }
    }

    //    TODO
    public String pl(int n, String word) {
        if (n < 0) {
            return word;
        }
        int wordStartIdx = 0;
        while (wordStartIdx < word.length()) {
            char c = word.charAt(wordStartIdx);
            if (!isSpaceCharacter(c)) {
                break;
            }
            wordStartIdx++;
        }
        Word w = words.get(wordStartIdx > 0 ? word.substring(wordStartIdx) : word);
        if (w == null) {
            return word;
        }
        int formIdx = form.getPluralWordFormIdx(n);
        String resultWord = w.wordForms[formIdx];
        return wordStartIdx > 0 ? word.substring(0, wordStartIdx) + resultWord : resultWord;
    }

    /**
     * N + plural. Same as n + pl(n, word).
     */
    public String npl(int n, String word) {
        return n + pl(n, word);
    }

    //TODO
    private boolean isSpaceCharacter(char c) {
        return c == ' ' || c == '-';
    }


    // TODO
    public static Word[] parse(String dictionary) {
        String lines[] = dictionary.split("\\r?\\n");
        List<Word> words = new ArrayList<Word>();
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty() || trimmedLine.startsWith(COMMENT_PREFIX)) {
                continue;
            }
            words.add(new Word(trimmedLine.split(",")));
        }
        return words.toArray(new Word[words.size()]);
    }

    /**
     * TODO:
     * See http://localization-guide.readthedocs.org/en/latest/l10n/pluralforms.html for details.
     */
    public static abstract class Form {
        public final int nPlurals;

        public Form(int nPlurals) {
            this.nPlurals = nPlurals;
        }

        public abstract int getPluralWordFormIdx(int n);
    }

    /**
     * TODO:
     */
    public static class Word {
        final String[] wordForms;

        public Word(String[] wordForm) {
            if (wordForm == null || wordForm.length == 0) {
                throw new IllegalArgumentException("Illegal word form: " + Arrays.toString(wordForm));
            }
            this.wordForms = wordForm;
        }
    }

}
