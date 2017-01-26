package english;

import rhyme.Phoneticizer;
import rhyme.VowelPronunciation;
import elements.Word;

import java.util.HashSet;
import java.util.Set;

public abstract class DeterminerManager {

    //pre
    private Set<String> multipliers;
    private Set<String> fractions;
    private Set<String> exclamative;

    //central
    private static Set<String> articles;
    private static Set<String> demonstratives;
    public static Set<String> possessiveDeterminers;
    private Set<String> interrogativeDeterminers;
    private Set<String> relativeDeterminers;
    private Set<String> nominalDeterminers;
    private Set<String> indefiniteDeterminers;

    //post
    private Set<String> cardinalNumbers;
    private Set<String> ordinalNumbers;
    private Set<String> generalOrdinals;
    private Set<String> primaryQuantifiers;


    public static void initializeSets() {
    //pre determiners


    //central determiners
        //articles
        articles = new HashSet<>();
        articles.add("a");
        articles.add("an");
        articles.add("the");

        //demonstratives
        demonstratives = new HashSet<>();
        demonstratives.add("this");
        demonstratives.add("these");
        demonstratives.add("that");
        demonstratives.add("those");

        //possessive determiners
        possessiveDeterminers = new HashSet<>();
        possessiveDeterminers.add("my");
        possessiveDeterminers.add("your");
        possessiveDeterminers.add("thy");
        possessiveDeterminers.add("our");
        possessiveDeterminers.add("their");
        possessiveDeterminers.add("his");
        possessiveDeterminers.add("her");
        possessiveDeterminers.add("its");

    //post determiners




    }

    public static String getCorrectIndefiniteArticle(String following) {
        if (following != null && following.length() > 0) {
            if (Phoneticizer.cmuDictContains(following)) {
            VowelPronunciation followingVowelPronunciation = Phoneticizer.getTopPronunciation(following);
            char firstChar = following.charAt(0);
            if (firstChar == 'a' ||
                    firstChar == 'e' ||
                    firstChar == 'i' ||
                    firstChar == 'o' ||
                    firstChar == 'u')
                return "an";
            else
                return "a";
            }
            return getCorrectIndefiniteArticleForOutOfDictionary(following);
        }
        return null;
    }

    public static String getCorrectIndefiniteArticle(Word following) {
        if (following != null) {
            if (following.getPhonemes() != null && following.getPhonemes().get(0) != null) {
                if (following.getPhonemes().get(0).isVowel())
                    return "an";
                else
                    return "a";
            }
        }
        return getCorrectIndefiniteArticle(following.getLowerSpelling());
    }

    public static String getCorrectIndefiniteArticleForOutOfDictionary(String following) {
        if (following != null && following.length() > 0) {
            char firstChar = following.charAt(0);
            if (    firstChar == 'a' ||
                    firstChar == 'e' ||
                    firstChar == 'i' ||
                    firstChar == 'o' ||
                    firstChar == 'u' )
                return "an";
            else
                return "a";
        }
        return null;
    }

    public static Person getPerson(String string) {
        if (string.toLowerCase().equals("my") || string.toLowerCase().equals("our"))
            return Person.FIRST;

        else if (string.toLowerCase().equals("your") || string.toLowerCase().equals("thy"))
            return Person.SECOND;

        else if (string.toLowerCase().equals("their") || string.toLowerCase().equals("his") || string.toLowerCase().equals("her") || string.toLowerCase().equals("its"))
            return Person.FIRST;

        return null;
    }

    public static String getPronoun(Case case_, Gender gender, Number number, Person person) {
        if (possessiveDeterminers == null)
            initializeSets();

////        List<String> possibilities = new ArrayList<>(all);
////        possibilities.retainAll(getByCase(case_));
////        possibilities.retainAll(getByGender(gender));
////        possibilities.retainAll(getByNumber(number));
////        possibilities.retainAll(getByPerson(person));
////        possibilities.removeAll(archaic);//TODO update this
//        if (possibilities.size() == 1)
//            return possibilities.get(0);
        else {
            System.out.println("ERROR");
            return null;
        }
        return null;
    }


}














































































































