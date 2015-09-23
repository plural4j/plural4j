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
 * p.pl(1, "год") ➟ "год"
 * p.pl(2, "год") ➟ "года"
 * p.pl(5, "год") ➟ "лет"
 * p.pl(0, "год") ➟ "лет"
 * <p/>
 * p = new Plural(Plural.ENGLISH, englishWords);
 * p.npl(1, " apple") ➟ "1 apple"
 * p.npl(3, " apple") ➟ "3 apples"
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
    public static final Rule NO_PLURAL_FORM = new Rule(1) {
        public int getPluralWordFormIdx(long n) {
            return 0;
        }
    };

    /**
     * Plural form for Arabic language.
     */
    public static final Rule ARABIC = new Rule(6) {
        @Override
        public int getPluralWordFormIdx(long n) {
            return (n == 0 ? 0 : n == 1 ? 1 : n == 2 ? 2 : n % 100 >= 3 && n % 100 <= 10 ? 3 : n % 100 >= 11 ? 4 : 5);
        }
    };

    /**
     * Plural form for Chinese language.
     */
    public static final Rule CHINESE = NO_PLURAL_FORM;

    /**
     * Plural form for English language.
     */
    public static final Rule ENGLISH = new Rule(2) {
        public int getPluralWordFormIdx(long n) {
            return n != 1 ? 1 : 0;
        }
    };

    /**
     * Plural form for French language.
     */
    public static final Rule FRENCH = new Rule(2) {
        public int getPluralWordFormIdx(long n) {
            return n > 1 ? 1 : 0;
        }
    };

    /**
     * Plural form for German language.
     */
    public static final Rule GERMAN = ENGLISH;


    /**
     * Plural form for Italian language.
     */
    public static final Rule ITALIAN = ENGLISH;

    /**
     * Plural form for Japanese language.
     */
    public static final Rule JAPANESE = NO_PLURAL_FORM;

    /**
     * Plural form for Portuguese language.
     */
    public static final Rule PORTUGUESE = ENGLISH;

    /**
     * Plural form for Spanish language.
     */
    public static final Rule SPANISH = ENGLISH;

    /**
     * Plural form for Russian language.
     */
    public static final Rule RUSSIAN = new Rule(3) {
        public int getPluralWordFormIdx(long n) {
            return (n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2);
        }
    };

    /**
     * The form to use.
     */
    @NotNull
    private final Rule form;

    /**
     * Dictionary. Keeps all word forms by 1-st word form.
     */
    private final Map<String, WordForms> words = new HashMap<String, WordForms>();

    /**
     * Creates new instance of Plural with a given form and words dictionary.
     *
     * @param form  plural form to use.
     * @param words words dictionary. Format: 1 word per line, multiple word forms are separated by comma.
     *              Example: apple,apples
     */
    public Plural(@NotNull Rule form, @NotNull String words) {
        this(form, parse(words));
    }

    /**
     * Creates new instance of Plural with a given form and words dictionary.
     *
     * @param form  plural form to use.
     * @param words list of work forms.
     */
    public Plural(@NotNull Rule form, @NotNull WordForms[] words) {
        this.form = form;
        if (words.length == 0) {
            throw new IllegalArgumentException("No words provided: " + Arrays.toString(words));
        }
        for (WordForms w : words) {
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
     * @param n    the number. Positive, zero & negative value.
     * @param word the word in a single form. Leading spaces are allowed and are preserved in result.
     * @return plural form of the word.
     * <p/>
     * Examples:
     * pl(2, 'apple') will return 'apples'.
     * pl(3, ' berry') will return ' berries'.
     */
    @NotNull
    public String pl(long n, @NotNull String word) {
        if (n < 0) {
            return word;
        }
        int wordStartIdx = 0;
        while (wordStartIdx < word.length()) {
            char c = word.charAt(wordStartIdx);
            if (!isPrefixCharacter(c)) {
                break;
            }
            wordStartIdx++;
        }
        WordForms w = words.get(wordStartIdx > 0 ? word.substring(wordStartIdx) : word);
        if (w == null) {
            return word;
        }
        int formIdx = form.getPluralWordFormIdx(n);
        String resultWord = w.wordForms[formIdx];
        return wordStartIdx > 0 ? word.substring(0, wordStartIdx) + resultWord : resultWord;
    }

    /**
     * Same as {@link #pl(long, String)} but prepends the number to the result.
     *
     * @param n    the number. Positive, zero & negative value.
     * @param word the word in a single form. Leading spaces are allowed and are preserved in result.
     * @return plural form of the word with the number prepended.
     * <p/>
     * Examples:
     * npl(2, 'apple') will return '2apples'.
     * pl(3, ' berry') will return '3 berries'.
     */
    @NotNull
    public String npl(long n, @NotNull String word) {
        return n + pl(n, word);
    }


    @NotNull
    public static WordForms[] parse(@NotNull String dictionary) {
        String lines[] = dictionary.split("\\r?\\n");
        List<WordForms> words = new ArrayList<WordForms>();
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty() || trimmedLine.startsWith(COMMENT_PREFIX)) {
                continue;
            }
            words.add(new WordForms(trimmedLine.split(",")));
        }
        return words.toArray(new WordForms[words.size()]);
    }

    private boolean isPrefixCharacter(char c) {
        return c == ' ' || c == '-';
    }


    /**
     * Plural word form descriptor. Stores the number of plural form in a language and transformation function.
     * <p/>
     * See http://localization-guide.readthedocs.org/en/latest/l10n/pluralforms.html for details.
     */
    public static abstract class Rule {
        /**
         * Number of possible plural word forms.
         */
        public final int nPlurals;

        /**
         * Creates new plural form instance.
         *
         * @param nPlurals number of possible plural forms.
         */
        public Rule(int nPlurals) {
            this.nPlurals = nPlurals;
        }

        /**
         * Returns plural word form index for the given integer.
         *
         * @param n - some integer number. Both positive & negative values are allowed.
         * @return index of the word form for the given integer.
         */
        public abstract int getPluralWordFormIdx(long n);
    }

    /**
     * Helper model for {@link Rule class}.
     * List of plural forms for a single word by idx.
     */
    public static class WordForms {
        /**
         * List of word forms by idx. See {@link Rule#getPluralWordFormIdx(long)} method for details.
         * Number of word forms is equal to {@link Rule#nPlurals} field.
         */
        private final String[] wordForms;

        /**
         * Creates new words list.
         *
         * @param wordForm List of word forms by idx. See {@link Rule#getPluralWordFormIdx(long)} method for details.
         */
        public WordForms(String[] wordForm) {
            if (wordForm == null || wordForm.length == 0) {
                throw new IllegalArgumentException("Illegal word form: " + Arrays.toString(wordForm));
            }
            this.wordForms = wordForm;
        }
    }
}
