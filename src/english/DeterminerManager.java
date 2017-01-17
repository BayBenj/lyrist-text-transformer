package english;

import java.util.HashSet;
import java.util.Set;

public abstract class DeterminerManager {

    //pre
    private Set<String> multipliers;
    private Set<String> fractions;
    private Set<String> exclamative;

    //central
    private Set<String> articles;
    private Set<String> demonstratives;
    private Set<String> possessiveDeterminers;
    private Set<String> interrogativeDeterminers;
    private Set<String> relativeDeterminers;
    private Set<String> nominalDeterminers;
    private Set<String> indefiniteDeterminers;

    //post
    private Set<String> cardinalNumbers;
    private Set<String> ordinalNumbers;
    private Set<String> generalOrdinals;
    private Set<String> primaryQuantifiers;


    public void initializeSets() {
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

}


















































































































