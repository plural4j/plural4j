package com.github.plural4j;

import org.jetbrains.annotations.NotNull;

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
     * No plural form.
     */
    public static final Form NO_PLURAL_FORM = new Plural.Form(1) {
        public int getPluralWordFormIdx(int n) {
            return 0;
        }
    };

    /**
     * Plural form for Arabic language.
     */
    public static final Form ARABIC = new Form(6) {
        @Override
        public int getPluralWordFormIdx(int n) {
            return (n == 0 ? 0 : n == 1 ? 1 : n == 2 ? 2 : n % 100 >= 3 && n % 100 <= 10 ? 3 : n % 100 >= 11 ? 4 : 5);
        }
    };

    /**
     * Plural form for Chinese language.
     */
    public static final Form CHINESE = NO_PLURAL_FORM;

    /**
     * Plural form for English language.
     */
    public static final Form ENGLISH = new Plural.Form(2) {
        public int getPluralWordFormIdx(int n) {
            return n != 1 ? 1 : 0;
        }
    };

    /**
     * Plural form for French language.
     */
    public static final Form FRENCH = new Plural.Form(2) {
        public int getPluralWordFormIdx(int n) {
            return n > 1 ? 1 : 0;
        }
    };

    /**
     * Plural form for German language.
     */
    public static final Form GERMAN = ENGLISH;


    /**
     * Plural form for Italian language.
     */
    public static final Form ITALIAN = ENGLISH;

    /**
     * Plural form for Japanese language.
     */
    public static final Form JAPANESE = NO_PLURAL_FORM;

    /**
     * Plural form for Portuguese language.
     */
    public static final Form PORTUGUESE = ENGLISH;

    /**
     * Plural form for Spanish language.
     */
    public static final Form SPANISH = ENGLISH;

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
     * Dictionary. Keeps all word forms by 1-st word form.
     */
    private final Map<String, Word> words = new HashMap<String, Word>();

    /**
     * Creates new instance of Plural with a given form and words dictionary.
     *
     * @param form  plural form to use.
     * @param words words dictionary. Format: 1 word per line, multiple word forms are separated by comma.
     *              Example: apple,apples
     */
    public Plural(@NotNull Form form, @NotNull String words) {
        this(form, parse(words));
    }

    /**
     * Creates new instance of Plural with a given form and words dictionary.
     *
     * @param form  plural form to use.
     * @param words list of work forms.
     */
    public Plural(@NotNull Form form, @NotNull Word[] words) {
        this.form = form;
        if (words.length == 0) {
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

    /**
     * Returns plural word for for the given number.
     *
     * @param n    the number.
     * @param word the word in a single form. Leading spaces are allowed and are preserved in result.
     * @return plural form of the word.
     * <p/>
     * Examples:
     * pl(2, 'apple') will return 'apples'.
     * pl(3, ' berry') will return ' berries'.
     */
    public String pl(int n, @NotNull String word) {
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
     * Same as {@link #pl(int, String)} but prepends the number to the result.
     *
     * @param n    the number.
     * @param word the word in a single form. Leading spaces are allowed and are preserved in result.
     * @return plural form of the word with the number prepended.
     * <p/>
     * Examples:
     * npl(2, 'apple') will return '2apples'.
     * pl(3, ' berry') will return '3 berries'.
     */
    public String npl(int n, String word) {
        return n + pl(n, word);
    }


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

    private boolean isSpaceCharacter(char c) {
        return c == ' ' || c == '-';
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
