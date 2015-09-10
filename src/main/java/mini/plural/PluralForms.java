package mini.plural;

import mini.plural.Plural.Form;

/**
 * See http://localization-guide.readthedocs.org/en/latest/l10n/pluralforms.html for details.
 */
public class PluralForms {

    public static final Form ENGLISH = new Plural.Form(2) {
        public int getPluralWordFormIdx(int n) {
            return n == 1 ? 0 : 1;
        }
    };

    public static final Form GERMAN = ENGLISH;

    public static final Form RUSSIAN = new Plural.Form(3) {
        public int getPluralWordFormIdx(int n) {
            return (n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2);
        }
    };

}
