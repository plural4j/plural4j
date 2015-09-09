package mini.plurals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO comment & cleanup
 */
public class Plural {
    /**
     * There are 3 plural word forms in Russian.
     */
    public static final int PLURAL_WORD_FORMS = 3;
    private static final String COMMENT_PREFIX = "#";

    private final Map<String, Word> dictionary = new HashMap<String, Word>();

    public Plural() {
        this(Dictionary.WORDS);
    }

    public Plural(Word[] words) {
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException("No words provided: " + Arrays.toString(words));
        }
        for (Word w : words) {
            if (dictionary.containsKey(w.wordForms[0])) {
                throw new IllegalArgumentException("Duplicate word: " + w.wordForms[0]);
            }
            dictionary.put(w.wordForms[0], w);
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
        Word w = dictionary.get(wordStartIdx > 0 ? word.substring(wordStartIdx) : word);
        if (w == null) {
            return word;
        }
        int formIdx = getPluralWordFormIdx(n);
        String resultWord = w.wordForms[formIdx];
        return wordStartIdx > 0 ? word.substring(0, wordStartIdx) + resultWord : resultWord;
    }

    private boolean isSpaceCharacter(char c) {
        return c == ' ' || c == '-';
    }

    private int getPluralWordFormIdx(int n) {
        return (n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2);
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
}
