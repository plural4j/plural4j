package mini.plurals;

import java.util.Arrays;

/**
 * TODO
 */
public class Word {
    final String[] wordForms;

    public Word(String[] wordForm) {
        if (wordForm == null || wordForm.length != Plural.PLURAL_WORD_FORMS) {
            throw new IllegalArgumentException("Illegal word form: " + Arrays.toString(wordForm));
        }
        this.wordForms = wordForm;
    }
}
