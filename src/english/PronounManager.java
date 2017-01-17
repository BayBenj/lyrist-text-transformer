package english;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PronounManager {

    private Set<String> possessivePronouns;
    private Set<String> subjects;
    private Set<String> objects;
    private Set<String> reflexives;
    private Set<String> firstPerson;
    private Set<String> secondPerson;
    private Set<String> thirdPerson;
    private Set<String> singulars;
    private Set<String> plurals;
    private Set<String> archaic;
    private Set<String> male;
    private Set<String> female;
    private Set<String> all;

    public Set<String> get(List<Object> enumAttributes) {
        initializeAllSet();
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

    public Set<String> getByPerson(Person person) {
        switch (person) {
            case FIRST: return firstPerson;
            case SECOND: return secondPerson;
            case THIRD: return thirdPerson;
            default: return null;
        }
    }

    public Set<String> getByNumber(Number number) {
        switch (number) {
            case SINGULAR: return singulars;
            case PLURAL: return plurals;
            default: return null;
        }
    }

    public Set<String> getByGender(Gender gender) {
        switch (gender) {
            case MALE: return male;
            case FEMALE: return female;
            default: return null;
        }
    }

    public Set<String> getByCase(Case c) {
        switch (c) {
            case NOMINATIVE: return subjects;
            case ACCUSATIVE: return objects;
            case GENITIVE: return possessivePronouns;
            default: return null;
        }
    }

    public void initializeAllSet() {
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

    public void initializeSets() {
        //singulars
        singulars = new HashSet<>();
        singulars.add("my");
        singulars.add("mine");
        singulars.add("your");
        singulars.add("yours");
        singulars.add("his");
        singulars.add("her");
        singulars.add("hers");
        singulars.add("its");
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
        plurals.add("their");
        plurals.add("theirs");
        plurals.add("our");
        plurals.add("ours");

        //first-person
        firstPerson = new HashSet<>();
        firstPerson.add("i");
        firstPerson.add("me");
        firstPerson.add("my");
        firstPerson.add("mine");
        firstPerson.add("myself");
        firstPerson.add("we");
        firstPerson.add("our");
        firstPerson.add("ours");
        firstPerson.add("ourselves");

        //second-person
        secondPerson = new HashSet<>();
        secondPerson.add("you");
        secondPerson.add("y'all");
        secondPerson.add("your");
        secondPerson.add("yours");
        secondPerson.add("yourself");
        secondPerson.add("yourselves");

        //third-person
        thirdPerson = new HashSet<>();
        thirdPerson.add("he");
        thirdPerson.add("she");
        thirdPerson.add("it");
        thirdPerson.add("its");
        thirdPerson.add("him");
        thirdPerson.add("her");
        thirdPerson.add("his");
        thirdPerson.add("hers");
        thirdPerson.add("they");
        thirdPerson.add("them");
        thirdPerson.add("their");
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
        archaic.add("thy");
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
    }

}







































































































