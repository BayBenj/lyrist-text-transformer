package english;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PronounManager {

    private static Set<String> possessivePronouns;
    private static Set<String> subjects;
    private static Set<String> objects;
    private static Set<String> reflexives;
    private static Set<String> firstPerson;
    private static Set<String> secondPerson;
    private static Set<String> thirdPerson;
    private static Set<String> singulars;
    private static Set<String> plurals;
    private static Set<String> archaic;
    private static Set<String> male;
    private static Set<String> female;
    private static Set<String> neutral;
    private static Set<String> all;

    public static Set<String> get(List<Object> enumAttributes) {
        if (all == null)
            initializeSets();
        for (Object attribute : enumAttributes) {
            if (attribute instanceof Person)
                all.retainAll(getByPerson((Person)attribute));

            else if (attribute instanceof Number)
                all.retainAll(getByNumber((Number)attribute));

            else if (attribute instanceof Gender)
                all.retainAll(getByGender((Gender)attribute));

            else if (attribute instanceof Case)
                all.retainAll(getByCase((Case)attribute));
        }
        return all;
    }

    public static Set<String> getByPerson(Person person) {
        if (all == null)
            initializeSets();
        switch (person) {
            case FIRST: return firstPerson;
            case SECOND: return secondPerson;
            case THIRD: return thirdPerson;
            default: return null;
        }
    }

    public static Person getPerson(String s) {
        if (all == null)
            initializeSets();
        if (firstPerson.contains(s.toLowerCase()))
            return Person.FIRST;
        else if (secondPerson.contains(s.toLowerCase()))
            return Person.SECOND;
        else if (thirdPerson.contains(s.toLowerCase()))
            return Person.THIRD;
        return null;
    }

    public static Set<String> getByNumber(Number number) {
        if (all == null)
            initializeSets();
        switch (number) {
            case SINGULAR: return singulars;
            case PLURAL: return plurals;
            default: return null;
        }
    }

    public static Number getNumber(String s) {
        if (all == null)
            initializeSets();
        if (singulars.contains(s.toLowerCase()))
            return Number.SINGULAR;
        else if (plurals.contains(s.toLowerCase()))
            return Number.PLURAL;
        return null;
    }

    public static Set<String> getByGender(Gender gender) {
        if (all == null)
            initializeSets();
        switch (gender) {
            case MALE: return male;
            case FEMALE: return female;
            case NEUTRAL: return neutral;
            default: return null;
        }
    }

    public static Gender getGender(String s) {
        if (all == null)
            initializeSets();
        if (male.contains(s.toLowerCase()))
            return Gender.MALE;
        else if (female.contains(s.toLowerCase()))
            return Gender.FEMALE;
        else if (neutral.contains(s.toLowerCase()))
            return Gender.NEUTRAL;
        return null;
    }

    public static Set<String> getByCase(Case c) {
        if (all == null)
            initializeSets();
        switch (c) {
            case NOMINATIVE: return subjects;
            case ACCUSATIVE: return objects;
            case GENITIVE: return possessivePronouns;
            default: return null;
        }
    }

    public static Case getCase(String s) {
        if (all == null)
            initializeSets();
        if (subjects.contains(s.toLowerCase()))
            return Case.NOMINATIVE;
        else if (objects.contains(s.toLowerCase()))
            return Case.ACCUSATIVE;
        else if (possessivePronouns.contains(s.toLowerCase()))
            return Case.GENITIVE;
        return null;
    }

    public static void initializeAllSet() {
        //all
        all = new HashSet<>();
        all.addAll(male);
        all.addAll(female);
        all.addAll(singulars);
        all.addAll(plurals);
        all.addAll(archaic);
        all.addAll(firstPerson);
        all.addAll(secondPerson);
        all.addAll(thirdPerson);
        all.addAll(subjects);
        all.addAll(objects);
        all.addAll(reflexives);
        all.addAll(possessivePronouns);
    }

    public static void initializeSets() {
        //singulars
        singulars = new HashSet<>();
        singulars.add("mine");
        singulars.add("yours");
        singulars.add("his");
        singulars.add("her");
        singulars.add("hers");
        singulars.add("i");
        singulars.add("you");
        singulars.add("he");
        singulars.add("she");
        singulars.add("it");
        singulars.add("me");
        singulars.add("you");
        singulars.add("him");
        singulars.add("myself");
        singulars.add("yourself");
        singulars.add("thyself");
        singulars.add("himself");
        singulars.add("herself");
        singulars.add("itself");
        singulars.add("this");
        singulars.add("that");

        //plurals
        plurals = new HashSet<>();
        plurals.add("these");
        plurals.add("those");
        plurals.add("themselves");
        plurals.add("yourselves");
        plurals.add("thyselves");
        plurals.add("ourselves");
        plurals.add("them");
        plurals.add("us");
        plurals.add("we");
        plurals.add("they");
        plurals.add("you");
        plurals.add("y'all");
        plurals.add("theirs");
        plurals.add("ours");

        //first-person
        firstPerson = new HashSet<>();
        firstPerson.add("i");
        firstPerson.add("me");
        firstPerson.add("mine");
        firstPerson.add("myself");
        firstPerson.add("we");
        firstPerson.add("ours");
        firstPerson.add("ourselves");

        //second-person
        secondPerson = new HashSet<>();
        secondPerson.add("you");
        secondPerson.add("y'all");
        secondPerson.add("yours");
        secondPerson.add("yourself");
        secondPerson.add("yourselves");

        //third-person
        thirdPerson = new HashSet<>();
        thirdPerson.add("he");
        thirdPerson.add("she");
        thirdPerson.add("it");
        thirdPerson.add("him");
        thirdPerson.add("her");
        thirdPerson.add("his");
        thirdPerson.add("hers");
        thirdPerson.add("they");
        thirdPerson.add("them");
        thirdPerson.add("theirs");
        thirdPerson.add("himself");
        thirdPerson.add("herself");
        thirdPerson.add("itself");
        thirdPerson.add("themselves");

        //possessive pronouns
        possessivePronouns = new HashSet<>();
        possessivePronouns.add("mine");
        possessivePronouns.add("yours");
        possessivePronouns.add("thine");
        possessivePronouns.add("ours");
        possessivePronouns.add("theirs");
        possessivePronouns.add("his");
        possessivePronouns.add("hers");

        //subjects
        subjects = new HashSet<>();
        subjects.add("i");
        subjects.add("thou");
        subjects.add("ye");
        subjects.add("you");
        subjects.add("we");
        subjects.add("they");
        subjects.add("he");
        subjects.add("she");
        subjects.add("it");

        //objects
        objects = new HashSet<>();
        objects.add("me");
        objects.add("thee");
        objects.add("thee");
        objects.add("you");
        objects.add("us");
        objects.add("them");
        objects.add("him");
        objects.add("her");
        objects.add("it");

        //reflexives
        reflexives = new HashSet<>();
        reflexives.add("myself");
        reflexives.add("ourselves");
        reflexives.add("yourself");
        reflexives.add("yourselves");
        reflexives.add("himself");
        reflexives.add("herself");
        reflexives.add("itself");
        reflexives.add("themselves");
        reflexives.add("thyself");
        reflexives.add("thyselves");

        //archaic
        archaic = new HashSet<>();
        archaic.add("thou");
        archaic.add("thee");
        archaic.add("ye");
        archaic.add("thine");
        archaic.add("thyself");
        archaic.add("thyselves");

        //male
        male = new HashSet<>();
        male.add("he");
        male.add("his");
        male.add("him");
        male.add("himself");

        //female
        female = new HashSet<>();
        female.add("she");
        female.add("hers");
        female.add("her");
        female.add("herself");

        //all
        initializeAllSet();

        //neutral
        neutral = new HashSet<>(all);
        neutral.removeAll(male);
        neutral.removeAll(female);
    }

    public static String changeCase(String originalString, Case newCase) {
        if (getCase(originalString) == newCase)
            return originalString;
        return getPronoun(newCase, getGender(originalString), getNumber(originalString), getPerson(originalString));
    }

    public static String changeGender(String originalString, Gender newGender) {
        if (getGender(originalString) == newGender)
            return originalString;
        return getPronoun(getCase(originalString), newGender, getNumber(originalString), getPerson(originalString));
    }

    public static String changeNumber(String originalString, Number newNumber) {
        if (getNumber(originalString) == newNumber)
            return originalString;
        return getPronoun(getCase(originalString), getGender(originalString), newNumber, getPerson(originalString));
    }

    public static String changePerson(String originalString, Person newPerson) {
        if (getPerson(originalString) == newPerson)
            return originalString;
        return getPronoun(getCase(originalString), getGender(originalString), getNumber(originalString), newPerson);
    }

    public static String getPronoun(Case case_, Gender gender, Number number, Person person) {
        if (all == null)
            initializeSets();

        List<String> possibilities = new ArrayList<>(all);
        possibilities.retainAll(getByCase(case_));
        possibilities.retainAll(getByGender(gender));
        possibilities.retainAll(getByNumber(number));
        possibilities.retainAll(getByPerson(person));
        possibilities.removeAll(archaic);//TODO update this
        if (possibilities.size() == 1)
            return possibilities.get(0);
        else {
            System.out.println("ERROR");
            return null;
        }
    }

    public static Set<String> getAll() {
        if (all == null)
            initializeSets();
        return all;
    }

}













































