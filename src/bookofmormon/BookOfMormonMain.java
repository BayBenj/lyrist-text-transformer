package bookofmormon;

import filters.*;
import song.*;
import utils.Utils;
import word2vec.ReplacementJob;

import java.io.*;
import java.util.*;

public class BookOfMormonMain {

    public static void main(String[] args) {
        BookOfMormonMain bom = new BookOfMormonMain();
        FilterUtils.setBibleWords(bom.readInBibleWords());
        for (int i = 0; i < 10; i++) {
            System.out.println(bom.bomStories());
        }
    }


    public String bomStories() {
        String officialTweet = "";
        String tweet;
        int trycount = 0;
        int sentencecount = 0;
        int tweetLength = this.getTweetLength();
        while (true) {
            tweet = this.getBoMSentence();
            if (officialTweet.length() + tweet.length() <= tweetLength) {
                if (!endsinSemiColon(officialTweet))
                    tweet = this.capitalizeFirst(tweet);
                else
                    tweet = this.lowercaseFirst(tweet);
                if (sentencecount > 0)
                    officialTweet += " " + tweet;
                else
                    officialTweet += tweet;
                sentencecount++;
            }
            else {
                trycount++;
                if (trycount > 100) {
                    break;
                }
            }
        }

        //Get sentences of words
        List<Sentence> sentences = this.getWords(officialTweet);

        //Use words
        officialTweet = this.useWords(sentences, officialTweet);

        //capitalize first letter
        officialTweet = this.capitalizeFirst(officialTweet);

        //Split into words, use string filters
        String[] words = officialTweet.split("[^\\w\\d]");
        this.useStringFilters(words, officialTweet);

        if (this.hasReference()) {
            officialTweet = this.addVerseNumNewLine(officialTweet);
            StringBuilder sb = new StringBuilder(officialTweet);
            sb.replace(0,0,getRealBook());
            officialTweet = sb.toString();
        }

        else if (this.hasVerseNum())
            officialTweet = this.addVerseNum(officialTweet);

        if (endsinSemiColon(officialTweet))
            officialTweet = this.changeLastPunctuation(officialTweet);
        return officialTweet;
    }

    private void useStringFilters(String[] words, String officialTweet) {
        //Swap names
        StringFilterEquation stringFilters = new StringFilterEquation();
        stringFilters.add(new BomNameFilter(Direction.INCLUDE_MATCH));
        Set<String> names = stringFilters.run(new HashSet<>(Arrays.asList(words)));
        for (String name : names) {
            if (this.hasNameSwap()) {
                int nOfNames = BomNameFilter.getList().length;
                String newName = BomNameFilter.getList()[Utils.rand.nextInt(nOfNames)];
                newName = newName.substring(0, 1).toUpperCase() + newName.substring(1);
                officialTweet = officialTweet.replaceAll(name, newName);
            }
        }

        //Swap geographical words
        stringFilters = new StringFilterEquation();
        stringFilters.add(new BomGeographyFilter(Direction.INCLUDE_MATCH));
        Set<String> geographicals = stringFilters.run(new HashSet<>(Arrays.asList(words)));
        for (String geographical : geographicals) {
            if (this.hasNameSwap()) {
                int nOfNames = BomGeographyFilter.getList().length;
                String newGeographical = BomGeographyFilter.getList()[Utils.rand.nextInt(nOfNames)];
                officialTweet = officialTweet.replaceAll(geographical, newGeographical);
            }
        }

        //Swap group names
        stringFilters = new StringFilterEquation();
        stringFilters.add(new BomGroupFilter(Direction.INCLUDE_MATCH));
        Set<String> groups = stringFilters.run(new HashSet<>(Arrays.asList(words)));
        for (String group : groups) {
            if (this.hasNameSwap()) {
                int nOfNames = BomGroupFilter.getList().length;
                String newGroup = BomGroupFilter.getList()[Utils.rand.nextInt(nOfNames)];
                officialTweet = officialTweet.replaceAll(group, newGroup);
            }
        }
    }

    private List<Sentence> getWords(String officialTweet) {
        //String noPunct = officialTweet.replaceAll("[^\\w\\d\\s]", "");
        return Utils.getStanfordNlp().parseTextToSentences(officialTweet);
    }

    private String useWords(List<Sentence> sentences, String officialTweet) {
        TemplateSongEngineer engi = new TemplateSongEngineer();

        //Get all words
        Set<Word> allWords = new HashSet<Word>();
        for (Sentence sentence : sentences) {
            for (Word word : sentence) {
                allWords.add(word);
            }
        }

        //Filter out unsafe words so they can't be marked
        WordFilterEquation wordFilterEquation = new WordFilterEquation();
        wordFilterEquation.add(new UnsafePosFilter(Direction.EXCLUDE_MATCH));
        List<Word> allMarkableWordsList = new ArrayList<>(wordFilterEquation.run(allWords));

        //Mark all markable words from template
        Set<Integer> allWordIndexes = new HashSet<Integer>();
        for (int i = 0; i < allMarkableWordsList.size(); i++)
            allWordIndexes.add(i);
        HashSet<Integer> markedIndexes = engi.getRandomIndexes(allWordIndexes, 1);
        Set<Word> markedWords = new HashSet<Word>();
        for(int index : markedIndexes)
            markedWords.add(allMarkableWordsList.get(index));

        //Replace marked words in original tweet w/ word2vec
        ReplacementJob replacementJob = new ReplacementJob();
        WordReplacements wordReplacements = replacementJob.getAnalogousWords(markedWords,
                                                                            sentences,
//                                                                            this.getBomBibleWordsFilterEquation(),
                                                                            new StringFilterEquation(),
                                                                            1000,
                                                                            Utils.getW2vCommander());

        String[] words = officialTweet.split("[^\\w\\d]");

        for (String word : words) {
            for (Map.Entry<Word,Word> entry : wordReplacements.entrySet()) {
                if (word.equalsIgnoreCase(entry.getKey().toString())) {
                    officialTweet = officialTweet.replaceAll(word, entry.getValue().toString());
                }
            }
        }
        return officialTweet;
    }

    private StringFilterEquation getBomBibleWordsFilterEquation() {
        StringFilterEquation stringFilters = new StringFilterEquation();

        stringFilters.add(new BomWordsFilter(Direction.EXCLUDE_MATCH));
        stringFilters.add(new FilterUNION());

        stringFilters.add(new BibleWordsFilter(Direction.EXCLUDE_MATCH));
        stringFilters.add(new FilterUNION());

//        List list = new ArrayList<Character>();
//        list.add('x');
//        stringFilters.add(new FirstLetterFilter(Direction.EXCLUDE_MATCH, new CharList(list, "x")));
//        stringFilters.add(new FilterUNION());

        return stringFilters;
    }

    private int getTweetLength() {
        double chance = Utils.rand.nextDouble();
        int chars = (int)(Math.pow(chance, .5) * 139 + 1);
        return chars;
    }

    private boolean endsinSemiColon(String s) {
        if (s.length() != 0 && s.charAt(s.length() - 1) == ';')
            return true;
        return false;
    }

    private boolean hasNameSwap() {
        double chance = Utils.rand.nextDouble();
        if (chance > .001)
            return true;
        return false;
    }

    private boolean hasVerseNum() {
        double chance = Utils.rand.nextDouble();
        if (chance > .8)
            return true;
        return false;
    }

    private boolean hasReference() {
        double chance = Utils.rand.nextDouble();
        if (chance > .8)
            return true;
        return false;
    }

    private String getBoMSentence() {
        List<String> list = new ArrayList();

        String path = Utils.rootPath + "local-data/bom/bom-all-sentences.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }

            Random rnd = new Random();
            int rand = rnd.nextInt(12938);
            String sentence = list.get(rand);
            while (sentence.length() > 140) {
                rand = rnd.nextInt(12938);
                sentence = list.get(rand);
            }

            return list.get(rand);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    private String changeLastPunctuation(String s) {
        if (s.length() > 0) {
            StringBuilder sb = new StringBuilder(s);
            sb.replace(s.length() - 1, s.length(), this.getPunctuation());
            return sb.toString();
        }
        return null;
    }

    private String getPunctuation() {
        int punct = Utils.rand.nextInt(2);
        if (punct == 0)
            return ".";
        if (punct == 1)
            return "!";
        return "?";
    }

    private String capitalizeFirst(String s) {
        if (s.length() > 0) {
            StringBuilder sb = new StringBuilder(s);
            char first = sb.charAt(0);
            char capped = Character.toUpperCase(first);
            sb.replace(0,1,Character.toString(capped));
            return sb.toString();
        }
        return s;
    }

    private String lowercaseFirst(String s) {
        if (s.charAt(0) != 'I' && s.charAt(1) != ' ') {
            StringBuilder sb = new StringBuilder(s);
            char first = sb.charAt(0);
            char capped = Character.toLowerCase(first);
            sb.replace(0, 1, Character.toString(capped));
            return sb.toString();
        }
        return s;
    }

    private String addVerseNum(String s) {
        Random rand = new Random();
        double randDouble = rand.nextDouble();
        int verseNum = (int)(Math.pow(randDouble, 1.5) * 130) + 1;

        StringBuilder sb = new StringBuilder(s);
        sb.replace(0,0,verseNum + " ");
        return sb.toString();
    }

    private String addVerseNumNewLine(String s) {
        Random rand = new Random();
        double randDouble = rand.nextDouble();
        int verseNum = (int)(Math.pow(randDouble, 1.5) * 130) + 1;

        StringBuilder sb = new StringBuilder(s);
        sb.replace(0,0,verseNum + "\n");
        return sb.toString();
    }

    private String getRealBook() {
        Random rand = new Random();
        int randInt = rand.nextInt(15);
        String[] bookArray = {
                "1 Nephi",
                "2 Nephi",
                "Jacob",
                "Enos",
                "Jarom",
                "Omni",
                "Words of Mormon",
                "Mosiah",
                "Alma",
                "Helaman",
                "3 Nephi",
                "4 Nephi",
                "Mormon",
                "Ether",
                "Moroni"
        };
        double randDoub = rand.nextDouble();
        int chapter = (int)(Math.pow(randDoub,2) * 50) + 1;
        return bookArray[randInt] + " " + chapter + ":";
    }

    public VocabList readInBibleWords() {
        String filePath = Utils.rootPath + "local-data/bom/bible-words-all.txt";
        File file = new File(filePath);
        Set<String> set = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null)
                set.add(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VocabList(set);
    }


//    private String getHashTag() {
//
//    }

//    public void swapNames(String s) {
//        StringBuilder sb = new StringBuilder(s);
//
//        String[] words = s.split("\\[^\\w]");
//        for (String word : words) {
//            s.replaceAll();
//        }
//    }

}

/*
> Check that some change is made before approving a tweet.



Funny bom topics:
skins of blackness
violence
wo to bad guys
loins
Satan




Hashtags:
General Hashtags
#mormon
#BookOfMormon
#lds
#Christian
#[word in tweet]

Other
#LDSconf
#MoTab
#LDSdevo
#ChristmasDevo
#LDSface2face
#BibleVideos
#Hallelujah
#ASaviorIsBorn
#IWasAStranger

First Presidency & Quorum of the Twelve Apostles
#PresMonson
#PresEyring
#PresUchtdorf
#PresNelson
#ElderOaks
#ElderBallard
#ElderHales
#ElderHolland
#ElderBednar
#ElderCook
#ElderChristofferson
#ElderAndersen
#ElderRasband
#ElderStevenson
#ElderRenlund

 */



















































































































