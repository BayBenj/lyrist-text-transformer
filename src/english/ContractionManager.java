package english;

import java.util.HashMap;
import java.util.Map;

public abstract class ContractionManager {

    private static Map<String,String[]> expandMap = null;
    private static Map<String[],String> contractMap = null;

    public static String[] getExpansion(String contraction) {
        if (expandMap == null || contractMap == null)
            initializeMaps();

        contraction = contraction.toLowerCase();

        if (expandMap.containsKey(contraction))
            return expandMap.get(contraction);

        return guessExpansion(contraction);
    }

    public static String getContraction(String... expansion) {
        if (expandMap == null || contractMap == null)
            initializeMaps();

        for (int i = 0; i < expansion.length; i ++)
            expansion[i] = expansion[i].toLowerCase();

        if (contractMap.containsKey(expansion))
            return contractMap.get(expansion);

        return guessContraction(expansion);
    }

    public static void initializeMaps() {
        //populate expandMap
        expandMap = new HashMap<>();

        expandMap.put("why'd", new String[] {"why","did"});


        expandMap.put("i'm", new String[] {"I","am"});
        expandMap.put("you're", new String[] {"you","are"});
        expandMap.put("we're", new String[] {"we","are"});
        expandMap.put("they're", new String[] {"they","are"});
        expandMap.put("he's", new String[] {"he","is"});
        expandMap.put("she's", new String[] {"she","is"});
        expandMap.put("it's", new String[] {"it","is"});

                
        expandMap.put("i'll", new String[] {"I","will"});
        expandMap.put("you'll", new String[] {"you","will"});
        expandMap.put("we'll", new String[] {"we","will"});
        expandMap.put("they'll", new String[] {"they","will"});
        expandMap.put("he'll", new String[] {"he","will"});
        expandMap.put("she'll", new String[] {"she","will"});
        expandMap.put("it'll", new String[] {"it","will"});
                
        expandMap.put("aren't", new String[] {"are","not"});
        expandMap.put("isn't", new String[] {"is","not"});
        expandMap.put("wasn't", new String[] {"was","not"});
        expandMap.put("weren't", new String[] {"were","not"});

        expandMap.put("hasn't", new String[] {"has","not"});//TODO this could be bad when expanded
        expandMap.put("haven't", new String[] {"have","not"});//TODO this could be bad when expanded
        expandMap.put("hadn't", new String[] {"had","not"});//TODO this could be bad when expanded

        expandMap.put("don't", new String[] {"do","not"});
        expandMap.put("doesn't", new String[] {"does","not"});
        expandMap.put("didn't", new String[] {"did","not"});

        expandMap.put("wouldn't", new String[] {"would","not"});
        expandMap.put("couldn't", new String[] {"could","not"});
        expandMap.put("shouldn't", new String[] {"should","not"});
        expandMap.put("won't", new String[] {"will","not"});
        expandMap.put("can't", new String[] {"cannot"});
        expandMap.put("mightn't", new String[] {"might","not"});
        expandMap.put("mustn't", new String[] {"must","not"});
        expandMap.put("oughtn't", new String[] {"ought","not"});
        expandMap.put("shan't", new String[] {"shall","not"});
        expandMap.put("needn't", new String[] {"need","not"});

        expandMap.put("would've", new String[] {"would","have"});
        expandMap.put("could've", new String[] {"could","have"});
        expandMap.put("should've", new String[] {"should","have"});

        expandMap.put("what'll", new String[] {"what","will"});
        expandMap.put("what're", new String[] {"what","are"});
        expandMap.put("what's", new String[] {"what","is"});
        expandMap.put("what've", new String[] {"what","have"});
        expandMap.put("who's", new String[] {"who","is"});
        expandMap.put("who'll", new String[] {"who","will"});
        expandMap.put("who're", new String[] {"who","are"});
        expandMap.put("who've", new String[] {"who","have"});

        //other specifics
        expandMap.put("ol'", new String[] {"old"});
        expandMap.put("o'", new String[] {"of"});
        expandMap.put("y’all", new String[] {"you", "all"});
        expandMap.put("what'd", new String[] {"what", "did"});//TODO what would?
        expandMap.put("let's", new String[] {"let", "us"});
        expandMap.put("ma'am", new String[] {"madam"});
        expandMap.put("'til", new String[] {"until"});
        expandMap.put("'till", new String[] {"until"});
        expandMap.put("'n'", new String[] {"and"});
        expandMap.put("’em", new String[] {"them"});
        expandMap.put("’cept ", new String[] {"except"});
        expandMap.put("’fraid", new String[] {"afraid"});
        expandMap.put("'nother", new String[] {"another"});
        expandMap.put("s'pose", new String[] {"suppose"});
        expandMap.put("t'other", new String[] {"the other"});

        //archaic specifics
        expandMap.put("'twas", new String[] {"it", "was"});
        expandMap.put("'twere", new String[] {"it", "were"});
        expandMap.put("'tis", new String[] {"it", "is"});
        expandMap.put("'twould", new String[] {"it", "would"});
        expandMap.put("'twill", new String[] {"it", "will"});
        expandMap.put("'twon't", new String[] {"it", "will", "not"});
        expandMap.put("'tain't", new String[] {"it", "ain't"});
        expandMap.put("'tisn't", new String[] {"it", "is", "not"});
        expandMap.put("th'art", new String[] {"thou", "art"});
        expandMap.put("n'art", new String[] {"art", "not"});
        expandMap.put("'gainst", new String[] {"against"});
        expandMap.put("heav'n", new String[] {"heaven"});
        expandMap.put("ha'e", new String[] {"have"});
        expandMap.put("th'", new String[] {"the"});
        expandMap.put("o'er", new String[] {"over"});
        expandMap.put("e'en", new String[] {"even"});
        expandMap.put("wi'", new String[] {"with"});

        //populate contractMap
        contractMap = new HashMap<>();
        for (Map.Entry<String,String[]> entry : expandMap.entrySet()) {
            if (entry.equals("i'm"))
                contractMap.put(new String[] {"I","am"},"i'm");
            else
                contractMap.put(entry.getValue(),entry.getKey());
        }
    }
    
    public static String[] guessExpansion(String contraction) {
        //if input is good
        if (contraction != null && contraction.length() > 0 && contraction.contains("'")) {
            if (contraction.endsWith("n't")) {
                contraction = contraction.replaceAll("n't","");
                return new String[]{contraction,"not"};
            }

            else if (contraction.endsWith("'re")) {
                contraction = contraction.replaceAll("'re","");
                return new String[]{contraction,"are"};
            }

            else if (contraction.endsWith("'m")) {
                contraction = contraction.replaceAll("'m","");
                return new String[]{contraction,"am"};
            }

            else if (contraction.endsWith("'s")) {
                contraction = contraction.replaceAll("'s","");
                return new String[]{contraction,"is"};
            }

            else if (contraction.endsWith("'ve")) {
                contraction = contraction.replaceAll("'ve","");
                return new String[]{contraction,"have"};
            }

            else if (contraction.endsWith("'ll")) {
                contraction = contraction.replaceAll("'ll","");
                return new String[]{contraction,"will"};
            }

            else if (contraction.endsWith("'d")) {
                contraction = contraction.replaceAll("'d","");
                return new String[]{contraction,"would"};
            }

            else if (contraction.endsWith("in'")) {
                contraction = contraction.replaceAll("in'","");
                return new String[]{contraction + "ing"};
            }

            else if (contraction.startsWith("'s")) {
                contraction = contraction.replaceFirst("'s","");
                return new String[]{"it", "is", contraction};
            }
        }

        //if contraction is bad or unrecognized, return null
        return null;
    }

    public static String guessContraction(String[] expansion) {
        //This method does not guarantee the contraction is common.
        //It assumes the caller wants this contraction even though it might be unheard of.

        //if input is good, two words
        if (expansion != null &&
            expansion.length == 2 &&
            expansion[0] != null &&
            expansion[0].length() > 0 &&
            expansion[1] != null &&
            expansion[1].length() > 1) {

            if (expansion[1].equals("is") || expansion[1].equals("has") || expansion[1].equals("does") || expansion[1].equals("us"))
                return expansion[0] + "'s";

            else if (expansion[1].equals("would") || expansion[1].equals("did") || expansion[1].equals("had"))
                return expansion[0] + "'d";

            else if (expansion[1].equals("will") || expansion[1].equals("shall"))
                return expansion[0] + "'ll";

            else if (expansion[1].equals("not"))
                return expansion[0] + "n't";

            else if (expansion[1].equals("are"))
                return expansion[0] + "'re";

            else if (expansion[1].equals("am"))
                return expansion[0] + "'m";

            else if (expansion[1].equals("have"))
                return expansion[0] + "'ve";
        }

        //if expansion is bad, not two words, or second word is unrecognized, return null
        return null;
    }

    public static boolean isContraction(String word) {
        return expandMap.keySet().contains(word.toLowerCase());
        //TODO if false, guess between possessive (the llama's laugh) and contraction (the llama's laughing)
    }

    public static Map<String, String[]> getExpandMap() {
        if (expandMap == null || contractMap == null)
            initializeMaps();
        return expandMap;
    }

    public static Map<String[], String> getContractMap() {
        if (expandMap == null || contractMap == null)
            initializeMaps();
        return contractMap;
    }

}

































/*
ain’t	am / is/are / has / have not [1]
amn’t	am not [2]
@aren’t	are not [3]
can’t	cannot
@could’ve	could have
@couldn’t	could not
@didn’t	did not
@doesn’t	does not
@don’t	do not[4]
gonna	going to
gotta	got to / got a
hadn’t	had not
hadn’t have	had not have
hasn't	has not
haven't	have not
he’d	he had / he would
he wouldn't	he would not
he hadn't	he had not
he would've	he would have
he’ll	he shall / he will
he’s	he has / he is
he isn't	he is not
how’d	how did / how would
how’ll	how will
how’s	how has / how is / how does
I’d	I had / I would
I hadn't	I had not
I wouldn't have	I would not have
I would’ve	I would have
I’ll	I shall / I will
I’m	I am
I’ve	I have
I haven't	I have not
@isn’t	is not
it’d	it would
it hadn't	it had not
it hadn't have	it had not have
it would've	it would have
it’ll	it shall / it will
it’s	it is
it isn’t	it is not
mightn’t	might not
might’ve	might have
mustn’t	must not
mustn't have	must not have
must’ve	must have
o’clock	of the clock
ol’	old
@oughtn’t	ought not
@shan’t	shall not
she’d	she had / she would
she hadn't	she had not
she wouldn't have	she would not have
she would've	she would have
she’ll	she shall / she will
she’s	she is
she isn't	she is not
@should’ve	should have
@shouldn’t	should not
somebody would've	somebody would have
somebody wouldn't have	somebody would not have
somebody’s	somebody is
someone’d	someone had / someone would
someone hadn't	someone had not
someone wouldn't have	someone would not have
someone would've	someone would have
someone’ll	someone shall / someone will
someone’s	someone has / someone is
something’d	something had / something would
something'dn't	something had not / something would not
something'dn't've	something had not have / something would not have
something’d’ve	something would have
something’ll	something shall / something will
something’s	something has / something is
that’ll	that will
that’s	that has / that is
that’d	that would / that had
there’d	there had / there would
there'dn't	there had not / there would not
there’d’ve	there would have
there’re	there are
there’s	there has / there is
they’d	they had / they would
they’dn’t	they had not / they would not
they’d’ve	they would have
they’ll	they shall / they will
they won't have	they will not have
they’re	they are
they’ve	they have
they haven't	they have not
@wasn’t	was not
we’d	we had / we would
we would've	we would have
we wouldn't	we would not
we wouldn't have	we would not have
we’ll	we will
we won't have	we will not have
we’re	we are
we’ve	we have
@weren’t	were not
what'd	what did
what’ll	what shall / what will
what’re	what are
what’s	what has / what is / what does
what’ve	what have
when’s	when has / when is
where’d	where did
where’s	where has / where is / where does
where’ve	where have
who’d	who would / who had / who did
who’d’ve	who would have
who’ll	who shall / who will
who’re	who are
who’s	who has / who is / who does
who’ve	who have
@why'd	why did
why’ll	why will
why’re	why are
why’s	why has / why is / why does
@won’t	will not
won’t’ve	will not have
would’ve	would have
wouldn’t	would not
wouldn’t’ve	would not have
@y’all / ya'll	you all, literally, "ya all"
you’d	you had / you would
@you’ll	you shall / you will
@you’re	you are
@you’ve	you have
 */














































































































































































