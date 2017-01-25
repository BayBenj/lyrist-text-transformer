//package song;
//
//import rhyme.Phoneme;
//
//import static java.lang.Character.isLowerCase;
//
//public class SongSegmentAttributer extends SongSegmentTool {
//    //TODO: Use recursion?
//    //TODO: Decide whether to move this functionality to the actual SongElement class
//
//    public double getProportionVowelLetters(SongElement segment) {
//        while (segment) {
//
//        }
//        this.identifySegment(segment);
//    }
//
//    public void recurse(SongElement segment) {
//        if (segment instanceof Word)
//            return;
//        segment.getSubSegments();
//    }
//
//    public double getProportionVowelLettersAtWord(Word word) {
//        String spelling = word.getSpelling();
//        double count = word.getSpelling().length();
//        double vowelCount = 0;
//        double consonantCount = 0;
//        for (int i = 0; i < spelling.length(); i++)
//            if (this.isVowel(spelling.charAt(i)))
//                vowelCount++;
//            else
//                consonantCount++;
//        return (vowelCount / count);
//    }
//
//    public double getProportionConsonantLettersAtWord(Word word) {
//        //TODO: eliminate code duplication from getProportionVowelLetters
//        String spelling = word.getSpelling();
//        double count = word.getSpelling().length();
//        double vowelCount = 0;
//        double consonantCount = 0;
//        for (int i = 0; i < spelling.length(); i++)
//            if (this.isVowel(spelling.charAt(i)))
//                vowelCount++;
//            else
//                consonantCount++;
//        return (consonantCount / count);
//    }
//
//    private boolean isVowel(char c) {
//        c = Character.toLowerCase(c);
//        if (    c == 'a' ||
//                c == 'e' ||
//                c == 'i' ||
//                c == 'o' ||
//                c == 'u' ||
//                c == 'y' )
//            return true;
//        return false;
//    }
//
//}
//
//
///*
//Quantitative elements attribute brainstorm:
//
//Full elements
//    Rhyme density
//    Obedience to rules
//    Obedience to intention's emotional progression
//    Sentiment
//
//Word attributes
//    Ratio of vowel letters to consonant letters
//    Ratio of vowel phonemes to consonant phonemes
//    Noun density
//    Verb density
//    Adjective density
//    Pronoun density
//    Other Pos density
//    Mean number of syllables in words
//    Standard deviation on mean number of syllables in words
//    Top 5 phonemes
//    Top 5 vowel phonemes
//    Top 5 consonant phonemes
//    Stress density
//    Some metric that combines stress with syllable density
//    Ratio of slang words to in-dictionary words
//
//
//Differences between original and template on all these attributes. This requires scanning attributes of the first elements too.
//    Word similarity of template elements and generated elements
//    etc.



//From outside research: appropriateness, flamboyance, lyricism, and relevancy
//
//
//
//
//
//Eventually, on a higher level, add a human popularity attribute (likes on Twitter). Also add a creativityImitation attribute (proportion of people it tricked corrected with a control)
//
//
//
//
//
//
//
//
//
//
//
// */
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//



