package mini.plural;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 */
public class Plural {
    public static final String COMMENT_PREFIX = "#";

    private final Form form;
    private final Map<String, Word> words = new HashMap<String, Word>();

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

    public String pl(String word, int n) {
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

    private boolean isSpaceCharacter(char c) {
        return c == ' ' || c == '-';
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
