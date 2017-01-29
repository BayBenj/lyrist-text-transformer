package bookofmormon;

import filters.*;
import main.ProgramArgs;
import elements.*;
import elements.Sentence;
import songtools.LyristReplacer;
import songtools.SongScanner;
import songtools.TemplateSongEngineer;
import songtools.WordReplacements;
import stanford.StanfordNlp;
import utils.Pair;
import utils.U;
import word2vec.W2vCommander;

import java.io.*;
import java.util.*;

public class BookOfMormonMain {

    public static void main(String[] args) {
        //Set the root path of Lyrist in U
        File currentDirFile = new File("");
        U.rootPath = currentDirFile.getAbsolutePath() + "/";

        //Load arguments
        ProgramArgs.loadProgramArgs(args); //TODO: right now it doesn't want any args

        //Set testing variable
        ProgramArgs.setTesting(false);

        //Setup StanfordNlp
        StanfordNlp stanfordNlp = new StanfordNlp();
        U.setStanfordNlp(stanfordNlp);

        //Setup W2vCommander
        W2vCommander w2v  = new W2vCommander("news-lyrics-bom2");
        U.setW2vCommander(w2v);

        BookOfMormonMain bom = new BookOfMormonMain();
        FilterUtils.setBibleWords(bom.readInVocabList("local-data/bom/bible-filterWords-all.txt"));
        FilterUtils.setCommonWords(bom.readInVocabList("local-data/vocab-lists/common-filterWords.txt"));
        FilterUtils.setModelWords(bom.readInVocabList("local-data/w2v/models/vocab-lists/news-lyrics-bom.txt"));

        for (int i = 0; i < 20; i++) {
            System.out.println(">" + bom.bomStories() + "\n");
        }
    }

    public String bomStories() {
        int tweetLength = this.getTweetLength();

        //Decide whether to use a hashtag
        String hashtag = null;
        int rnd = U.rand.nextInt(10);
        if (rnd <= 3) {
            hashtag = this.getHashTag();
            tweetLength -= (hashtag.length() + 1);
        }

        //Stitch multiple bom sentences together
        String officialTweet = this.stitchSentences(tweetLength);
        String originalsentences = new String(officialTweet);

        //Get sentences of filterWords
        List<Sentence> sentences = this.getWords(officialTweet);

        //Use filterWords in word2vec and filters
        officialTweet = this.useWord2VecAndWordFilters(sentences, officialTweet);

        //capitalize first letter
        officialTweet = this.capitalizeFirst(officialTweet);

        //Split into filterWords, use string filters
        String[] words = officialTweet.split("[^\\w\\d]");
        officialTweet = this.useStringFilters(words, officialTweet);

        //Ensure some change has been made
        if (originalsentences.equals(officialTweet))
            return bomStories();

        if (this.hasReference()) {
            officialTweet = this.addVerseNumNewLine(officialTweet);
            StringBuilder sb = new StringBuilder(officialTweet);
            sb.replace(0,0,getRealBook());
            officialTweet = sb.toString();
        }

        else if (this.hasVerseNum())
            officialTweet = this.addVerseNum(officialTweet);

        //Remove semicolon at the end
        if (endsinSemiColon(officialTweet))
            officialTweet = this.changeLastPunctuation(officialTweet);

        //Add any predetermined hashtag
        if (rnd <= 2)
            officialTweet += " " + hashtag;

        return officialTweet;
    }

    private String stitchSentences(int tweetLength) {
        String officialTweet = "";
        String tweet;
        int trycount = 0;
        int sentencecount = 0;
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
                if (trycount > 10)
                    break;
            }
        }
        while (officialTweet.length() > 0 &&
                        (officialTweet.charAt(officialTweet.length() - 1) == ':' ||
                        officialTweet.charAt(officialTweet.length() - 1) == '-'))
            officialTweet = stitchSentences(tweetLength);
        return officialTweet;
    }

    private String useStringFilters(String[] words, String officialTweet) {
        //Swap names
        StringFilterEquation stringFilters = new StringFilterEquation();
        stringFilters.add(new BomNameFilter(ReturnType.MATCHES));
        Set<String> names = stringFilters.run(new HashSet<>(Arrays.asList(words)));
        for (String name : names) {
            if (this.hasNameSwap()) {
                int nOfNames = BomNameFilter.getList().length;
                String newName = BomNameFilter.getList()[U.rand.nextInt(nOfNames)];
                newName = newName.substring(0, 1).toUpperCase() + newName.substring(1);
                officialTweet = officialTweet.replaceAll(name, newName);
            }
        }

        //Swap geographical filterWords
        stringFilters = new StringFilterEquation();
        stringFilters.add(new BomGeographyFilter(ReturnType.MATCHES));
        Set<String> geographicals = stringFilters.run(new HashSet<>(Arrays.asList(words)));
        for (String geographical : geographicals) {
            if (this.hasNameSwap()) {
                int nOfNames = BomGeographyFilter.getList().length;
                String newGeographical = BomGeographyFilter.getList()[U.rand.nextInt(nOfNames)];
                officialTweet = officialTweet.replaceAll(geographical, newGeographical);
            }
        }

        //Swap pluralized group names
        stringFilters = new StringFilterEquation();
        stringFilters.add(new BomGroupsFilter(ReturnType.MATCHES));
        Set<String> pluralGroups = stringFilters.run(new HashSet<>(Arrays.asList(words)));
        for (String pluralGroup : pluralGroups) {
            if (this.hasNameSwap()) {
                int nOfNames = BomGroupsFilter.getList().length;
                String newGroup = BomGroupsFilter.getList()[U.rand.nextInt(nOfNames)];
                officialTweet = officialTweet.replaceAll(pluralGroup, newGroup);
            }
        }

        //Swap singular group names
        stringFilters = new StringFilterEquation();
        stringFilters.add(new BomGroupFilter(ReturnType.MATCHES));
        Set<String> groups = stringFilters.run(new HashSet<>(Arrays.asList(words)));
        for (String group : groups) {
            if (this.hasNameSwap()) {
                int nOfNames = BomGroupFilter.getList().length;
                String newGroup = BomGroupFilter.getList()[U.rand.nextInt(nOfNames)];
                officialTweet = officialTweet.replaceAll(group, newGroup);
            }
        }

        return officialTweet;
    }

    private List<Sentence> getWords(String officialTweet) {
        //String noPunct = officialTweet.replaceAll("[^\\w\\d\\s]", "");
        return U.getStanfordNlp().parseTextToSentences(officialTweet);
    }

    private String useWord2VecAndWordFilters(List<Sentence> sentences, String officialTweet) {
        TemplateSongEngineer engi = new TemplateSongEngineer();

        //Get all filterWords
        Set<String> nonPunctWords = new HashSet<>();
        Map<String, Word> allWords = new HashMap<>();
        for (Sentence sentence : sentences) {
            for (Word word : sentence) {
                allWords.put(word.toString(), word);
                if (word.getClass() != Punctuation.class &&
                        (word.getPos() == Pos.NN ||
                                word.getPos() == Pos.NNS ||
                                word.getPos() == Pos.JJ ||
                                word.getPos() == Pos.JJR ||
                                word.getPos() == Pos.JJS ||
                                word.getPos() == Pos.VB ||
                                word.getPos() == Pos.VBG)
                        ) {
                    nonPunctWords.add(word.toString().toLowerCase());
                }
            }
        }

        //Find filterWords that aren't in the model's vocabulary
        StringFilterEquation stringFilter = new StringFilterEquation();
        stringFilter.add(new FilterINTERSECTION());
        stringFilter.add(new W2vModelVocabFilter(ReturnType.NON_MATCHES));
        Set<String> badStringsInModel = stringFilter.run(new HashSet<>(allWords.keySet()));
        Set<String> stringsInModel = new HashSet<>(allWords.keySet());
        stringsInModel.removeAll(badStringsInModel);

        //Find safe filterWords
        WordFilterEquation wordFilterEquation = new WordFilterEquation();
        wordFilterEquation.add(new FilterINTERSECTION());
        wordFilterEquation.add(new UnsafePosForPosTaggingFilter(ReturnType.MATCHES));
        wordFilterEquation.add(new FilterUNION());
        wordFilterEquation.add(new UnsafeWordFilter(ReturnType.MATCHES));
        allWords.values().retainAll(wordFilterEquation.removeMatches(new HashSet<>(allWords.values())));
        List<Word> allMarkableWordsList = new ArrayList<>(allWords.values());

        for (Map.Entry<String, Word> entry : allWords.entrySet()) {
            if (!stringsInModel.contains(entry.getKey()) ||
                    !allMarkableWordsList.contains(entry.getValue())) {
                allMarkableWordsList.remove(entry.getValue());
            }
        }

        //Mark all markable filterWords from template
        Set<Integer> allWordIndexes = new HashSet<Integer>();
        for (int i = 0; i < allMarkableWordsList.size(); i++)
            allWordIndexes.add(i);
        Set<Integer> markedIndexes = SongScanner.getRandomIndexes(allWordIndexes, .5);
        Set<Word> markedWords = new HashSet<Word>();
        for(int index : markedIndexes)
            markedWords.add(allMarkableWordsList.get(index));

        //Replace marked filterWords in original tweet w/ word2vec
        LyristReplacer lyristReplacer = new LyristReplacer();
        Pair<String,String> pair = this.getOldAndNewThemes();

//        TreeMap<Double,String> treeMap = new TreeMap<>(U.getW2vCommander().findSentiment(nonPunctWords, 1));
//        String theme;
//        if (treeMap.size() > 0)
//            theme = treeMap.firstEntry().getValue();
//        else
//            theme = pair.getFirst();

        WordReplacements wordReplacements = lyristReplacer.getWordSuggestions(markedWords,
                                                                            sentences,
//                                                                            this.getBomBibleWordsFilterEquation(),
                                                                            this.getStringFilters(),
                                                                            100,
                                                                            U.getW2vCommander(),
                                                                            pair.getFirst(),
                                                                            pair.getSecond(),
                                                                            null);

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

    private int getTweetLength() {
        //min is 5
        //max is 140

        double chance = U.rand.nextDouble();
        int chars = (int)(Math.pow(chance, .5) * 135 + 5);
        return chars;
    }

    private boolean endsinSemiColon(String s) {
        if (s.length() != 0 && s.charAt(s.length() - 1) == ';')
            return true;
        return false;
    }

    private boolean hasNameSwap() {
        double chance = U.rand.nextDouble();
        if (chance > .001)
            return true;
        return false;
    }

    private boolean hasVerseNum() {
        double chance = U.rand.nextDouble();
        if (chance > .8)
            return true;
        return false;
    }

    private boolean hasReference() {
        double chance = U.rand.nextDouble();
        if (chance > .8)
            return true;
        return false;
    }

    private String getBoMSentence() {
        List<String> list = new ArrayList();

        String path = U.rootPath + "local-data/bom/bom-all-sentences.txt";
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
        int punct = U.rand.nextInt(2);
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

    public VocabList readInVocabList(String path) {
        String filePath = U.rootPath + path;
        File file = new File(filePath);
        Set<String> set = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null)
                set.add(line.split(" ")[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VocabList(set);
    }

    private StringFilterEquation getStringFilters() {
        StringFilterEquation stringFilters = new StringFilterEquation();

        stringFilters.add(new FilterINTERSECTION());
        stringFilters.add(new BomWordsFilter(ReturnType.NON_MATCHES));
        stringFilters.add(new FilterUNION());
        stringFilters.add(new BibleWordsFilter(ReturnType.NON_MATCHES));
        stringFilters.add(new CommonStringFilter(ReturnType.NON_MATCHES));

//        List list = new ArrayList<Character>();
//        list.add('x');
//        stringFilters.add(new FirstLetterFilter(ReturnType.NON_MATCHES, new CharList(list, "x")));
//        stringFilters.add(new FilterUNION());

        stringFilters.add(new BadStringFilter(ReturnType.MATCHES));
        stringFilters.add(new DistastefulnessFilter(ReturnType.MATCHES));
        return stringFilters;
    }

    private String getHashTag() {
        int rnd = U.rand.nextInt(10);
        String[] hashtags;
        if (rnd == 0) {
            hashtags = new String[] {
                    "#PresMonson",
                    "#PresEyring",
                    "#PresUchtdorf",
                    "#PresNelson",
                    "#ElderOaks",
                    "#ElderBallard",
                    "#ElderHales",
                    "#ElderHolland",
                    "#ElderBednar",
                    "#ElderCook",
                    "#ElderChristofferson",
                    "#ElderAndersen",
                    "#ElderRasband",
                    "#ElderStevenson",
                    "#ElderRenlund",
                    "#JosephSmith",
                    "#BrighamYoung",
                    "#JohnTaylor",
                    "#WilfordWoodruff",
                    "#LorenzoSnow",
                    "#JosephFSmith",
                    "#HeberJGrant",
                    "#GeorgeAlbertSmith",
                    "#DavidOMcKay",
                    "#JosephFieldingSmith",
                    "#HaroldBLee",
                    "#SpencerWKimball",
                    "#EzraTaftBenson",
                    "#HowardWHunter",
                    "#GordonBHinckley"
            };
        }
        else if (rnd == 1) {
            hashtags = new String[] {
                    "#AKeyWasTurnedInLatterDays",
                    "#AMightyFortressIsOurGod",
                    "#APoorWayfaringManOfGrief",
                    "#AVoiceHathSpokenFromTheDust",
                    "#AbideWithMeTisEventide",
                    "#AbideWithMe",
                    "#AdamondiAhman",
                    "#AgainWeMeetAroundTheBoard",
                    "#AgainOurDearRedeemingLord",
                    "#AllCreaturesOfOurGodAndKing",
                    "#AllGloryLaudAndHonor",
                    "#AnAngelfromOnHigh",
                    "#AngelsWeHaveHeardOnHigh",
                    "#AriseOGloriousZion",
                    "#AriseOGodAndShine",
                    "#AsIhavelovedyou",
                    "#AsISearchTheHolyScriptures",
                    "#AsIwatchTherisingsun",
                    "#AsNowWeTakeTheSacrament",
                    "#AsSistersInZion",
                    "#AsTheDewfromHeavenDistilling",
                    "#AsTheShadowsFall",
                    "#AsZionsYouthInLatterDays",
                    "#AwakeAndArise",
                    "#AwakeYeSaintsOfGodAwake",
                    "#AwayInAManger",
                    "#BattleHymnOfTheRepublic",
                    "#BeStillMySoul",
                    "#BeThouHumble",
                    "#BeautifulZionBuiltAbove",
                    "#BecauseIHaveBeenGivenMuch",
                    "#BeforeTheeLordIBowMyHead",
                    "#BeholdTheGreatRedeemerDie",
                    "#BeholdThySonsAndDaughtersLord",
                    "#BeholdTheMountainOfTheLord",
                    "#BeholdARoyalArmy",
                    "#BlessOurFastWePray",
                    "#BrethrenpowrByearthlystandards",
                    "#BrightlyBeamsOurFathersMercy",
                    "#CalledToServe",
                    "#CarryOn",
                    "#CastThyBurdenuponTheLord",
                    "#ChildrenOfOurHeavenlyFather",
                    "#ChooseTheRight",
                    "#ChristTheLordIsRisenToday",
                    "#ComeAlongComeAlong",
                    "#ComeAwayToTheSundaySchool",
                    "#ComeUntoHim",
                    "#ComeUntoJesus",
                    "#ComeAllWhoseSoulsAreLighted",
                    "#ComeAllYeSaintsOfZion",
                    "#ComeAllYeSaintsWhoDwellOnEarth",
                    "#ComeAllYeSonsOfGod",
                    "#ComeComeYeSaints",
                    "#ComeFollowMe",
                    "#ComeLetUsAnew",
                    "#ComeLetUsSingAnEveningHymn",
                    "#ComeListenToAProphetsVoice",
                    "#ComeOThouKingOfKings",
                    "#ComeRejoice",
                    "#ComeSingToTheLord",
                    "#ComeThouGloriousDayOfPromise",
                    "#ComeWeThatLoveTheLord",
                    "#ComeYeChildrenOfTheLord",
                    "#ComeYeDisconsolate",
                    "#ComeYeThankfulPeople",
                    "#CountYourBlessings",
                    "#DearToTheHeartOfTheShepherd",
                    "#DearestChildrenGodIsNearYou",
                    "#DidYouThinkToPray",
                    "#DoWhatIsRight",
                    "#DoesTheJourneySeemLong",
                    "#EachLifeThatTouchesOursForGood",
                    "#EarthWithhertenthousandflowrs",
                    "#Ereyouleftyourroomthismorning",
                    "#FaithOfOurFathers",
                    "#FamiliesCanBeTogetherForever",
                    "#FarFarAwayOnJudeasPlains",
                    "#FatherInHeaven",
                    "#FatherInHeavenWeDoBelieve",
                    "#FatherCheerOurSoulsTonight",
                    "#FatherThisHourHasBeenOneOfJoy",
                    "#FatherThyChildrenToTheeNowRaise",
                    "#FirmasThemountainsaroundus",
                    "#ForAllTheSaints",
                    "#ForTheBeautyOfTheEarth",
                    "#ForTheStrengthOfTheHills",
                    "#FromAllThatDwellbelowTheSkies",
                    "#FromHomesOfSaintsGladSongsArise",
                    "#GentlyRaiseTheSacredStrain",
                    "#GloriousThingsAreSungOfZion",
                    "#GloriousThingsOfTheeAreSpoken",
                    "#GloryToGodOnHigh",
                    "#GoForthWithFaith",
                    "#GoYeMessengersOfGlory",
                    "#GoYeMessengersOfHeaven",
                    "#GodBeWithYouTillWeMeetAgain",
                    "#GodBlessOurProphetDear",
                    "#GodIsInHisHolyTemple",
                    "#GodIsLove",
                    "#GodLovedUsSoHeSentHisSon",
                    "#GodMovesInAMysteriousWay",
                    "#GodOfOurFathersKnownOfOld",
                    "#GodOfOurFathersWeComeUntoThee",
                    "#GodOfOurFathersWhoseAlmightyHand",
                    "#GodOfPowerGodOfRight",
                    "#GodSaveTheKing",
                    "#GodSpeedTheRight",
                    "#GodOurFatherHearUsPray",
                    "#GodsDailyCare",
                    "#GreatGodAttendWhileZionSings",
                    "#GreatGodToTheeMyEveningSong",
                    "#GreatIsTheLord",
                    "#GreatKingOfHeaven",
                    "#GuideMeToThee",
                    "#GuideUsOThouGreatJehovah",
                    "#HailToTheBrightnessOfZionsGladMorning",
                    "#HarkAllYeNations",
                    "#HarkTheHeraldAngelsSing",
                    "#HaveIDoneAnyGood",
                    "#HeDiedTheGreatRedeemerDied",
                    "#HeIsRisen",
                    "#HearThouOurHymnOLord",
                    "#HelpMeTeachWithInspiration",
                    "#HighOnTheMountainTop",
                    "#HolyTemplesOnMountZion",
                    "#HomeCanBeAHeavenOnEarth",
                    "#HopeOfIsrael",
                    "#HowBeautifulThyTemplesLord",
                    "#HowFirmAFoundation",
                    "#HowGentleGodsCommands",
                    "#HowGreatTheWisdomAndTheLove",
                    "#HowGreatThouArt",
                    "#HowLongOLordMostHolyAndTrue",
                    "#HowWondrousAndGreat",
                    "#IAmAChildOfGod",
                    "#IBelieveInChrist",
                    "#IhaveAfamlyhereOnearth",
                    "#IHaveWorkEnoughToDo",
                    "#IHeardTheBellsOnChristmasDay",
                    "#IKnowMyFatherLives",
                    "#IknowthatmyRedeemerlives",
                    "#INeedTheeEveryHour",
                    "#ISawAMightyAngelFly",
                    "#IStandAllAmazed",
                    "#IWanderThroughTheStillOfNight",
                    "#IWillNotDoubtIWillNotfear",
                    "#IllGoWhereYouWantMeToGo",
                    "#ImAPilgrimImAStranger",
                    "#IfYouCouldHieToKolob",
                    "#ImproveTheShiningMoments",
                    "#InAworldwheresorrow",
                    "#InFastingWeApproachThee",
                    "#InHumilityOurSavior",
                    "#InHymnsOfPraise",
                    "#InMemoryOfTheCrucified",
                    "#InOurLovelyDeseret",
                    "#InRemembranceOfThySuffering",
                    "#InSweetRemembranceOfThySon",
                    "#IsraelIsraelGodIsCalling",
                    "#ItCameuponTheMidnightClear",
                    "#JehovahLordOfHeavenAndEarth",
                    "#JesusOfNazarethSaviorAndKing",
                    "#JesusLoverOfMySoul",
                    "#JesusMightyKingInZion",
                    "#JesusmySaviortrue",
                    "#JesusOnceOfHumbleBirth",
                    "#JesusSaviorPilotMe",
                    "#JesusTheVeryThoughtOfThee",
                    "#JosephSmithsFirstPrayer",
                    "#JoyToTheWorld",
                    "#KeepTheCommandments",
                    "#KnowThisThatEverySoulIsFree",
                    "#LeadMeintoLifeEternal",
                    "#LeadKindlyLight",
                    "#LeanOnMyAmpleArm",
                    "#LetEarthsInhabitantsRejoice",
                    "#LetTheHolySpiritGuide",
                    "#LetUsAllPressOn",
                    "#LetUsOftSpeakKindWords",
                    "#LetZionInHerBeautyRise",
                    "#LikeTenThousandLegionsMarching",
                    "#LoTheMightyGodAppearing",
                    "#LordAcceptintoThyKingdom",
                    "#LordAcceptOurTrueDevotion",
                    "#LordDismissUsWithThyBlessing",
                    "#LordIWouldFollowThee",
                    "#LordWeAskTheeEreWePart",
                    "#LordWeComebeforeTheeNow",
                    "#LoveAtHome",
                    "#LoveOneAnother",
                    "#MasterTheTempestIsRaging",
                    "#MenAreThatTheyMightHaveJoy",
                    "#MineeyeshaveseenThegloryOfThecomingOfTheLord",
                    "#MoreHolinessGiveMe",
                    "#MyCountryTisOfThee",
                    "#MyRedeemerLives",
                    "#NaySpeakNoIll",
                    "#NearerDearSaviorToThee",
                    "#NearerMyGodToThee",
                    "#NowLetUsRejoice",
                    "#NowThankWeAllOurGod",
                    "#NowTheDayIsOver",
                    "#NowToheavnourprayerascending",
                    "#NowWellSingWithOneAccord",
                    "#OGodOurHelpInAgesPast",
                    "#OGodTheEternalFather",
                    "#OHomeBeloved",
                    "#OLittleTownOfBethlehem",
                    "#OLordOfHosts",
                    "#OLoveThatGlorifiesTheSon",
                    "#OMyFather",
                    "#OSaintsOfZion",
                    "#OSaviorThouWhoWearestACrown",
                    "#OThouKindAndGraciousFather",
                    "#OThouRockOfOurSalvation",
                    "#OThouBeforeTheWorldBegan",
                    "#OYeMountainsHigh",
                    "#OhSayWhatIsTruth",
                    "#OhComeAllYeFaithful",
                    "#OhHolyWordsOfTruthAndLove",
                    "#OhHowlovelywasThemorning",
                    "#OhMayMySoulCommuneWithThee",
                    "#OhWhatSongsOfTheHeart",
                    "#OnBendedKneesWithBrokenHearts",
                    "#OnThisDayOfJoyAndGladness",
                    "#OnceInRoyalDavidsCity",
                    "#OnwardChristianSoldiers",
                    "#OurFatherByWhoseName",
                    "#OurMountainHomeSoDear",
                    "#OurSaviorsLove",
                    "#PraiseGodfromWhomAllBlessingsFlow",
                    "#PraiseTheLordWithHeartAndVoice",
                    "#PraiseToTheLordTheAlmighty",
                    "#PraiseToTheMan",
                    "#PraiseYeTheLord",
                    "#PrayerIsTheSoulsSincereDesire",
                    "#PrayerOfThanksgiving",
                    "#PreciousSaviorDearRedeemer",
                    "#PressForwardSaints",
                    "#PutYourShoulderToTheWheel",
                    "#RaiseYourVoicesToTheLord",
                    "#RedeemerOfIsrael",
                    "#RejoiceTheLordIsKing",
                    "#RejoiceYeSaintsOfLatterDays",
                    "#RejoiceAGloriousSoundIsHeard",
                    "#ReverentlyAndMeeklyNow",
                    "#RingOutWildBells",
                    "#RiseUpOMenOfGod",
                    "#RiseYeSaintsAndTemplesEnter",
                    "#RockOfAges",
                    "#SabbathDay",
                    "#SaintsBeholdHowGreatJehovah",
                    "#SaviormayIlearnTolovethee",
                    "#SaviorRedeemerOfMySoul",
                    "#ScatterSunshine",
                    "#SchoolThyFeelings",
                    "#SecretPrayer",
                    "#SeeTheMightyPriesthoodGathered",
                    "#SeeTheMightyAngelFlying",
                    "#ShallTheyouthOfZionfalter",
                    "#ShouldYouFeelInclinedToCensure",
                    "#SilentNight",
                    "#SingPraiseToHim",
                    "#SingWeNowAtParting",
                    "#SoftlyBeamsTheSacredDawning",
                    "#SoftlyNowTheLightOfDay",
                    "#SonsOfMichaelHeApproaches",
                    "#SweetHourOfPrayer",
                    "#SweetIsThePeaceTheGospelBrings",
                    "#SweetIsTheWork",
                    "#TisSweetToSingTheMatchlessLove",
                    "#TwasWitnessedInTheMorningSky",
                    "#TeachMeToWalkInTheLight",
                    "#Testimony",
                    "#ThanksForTheSabbathSchool",
                    "#ThatEasterMorn",
                    "#TheDayDawnIsBreaking",
                    "#TheFirstNoel",
                    "#TheGloriousGospelLightHasShone",
                    "#TheHappyDayAtLastHasCome",
                    "#TheIronRod",
                    "#TheLightDivine",
                    "#TheLordBeWithUs",
                    "#TheLordIsMyLight",
                    "#TheLordIsMyShepherd",
                    "#TheLordMyPastureWillPrepare",
                    "#TheMorningBreaks",
                    "#ThePriesthoodOfOurLord",
                    "#TheSpiritOfGod",
                    "#TheStarSpangledBanner",
                    "#TheTimeIsFarSpent",
                    "#TheVoiceOfGodAgainIsHeard",
                    "#TheWintryDayDescendingToItsClose",
                    "#TheWorldHasNeedOfWillingMen",
                    "#ThereIsAGreenHillFarAway",
                    "#ThereIsAnhourOfPeaceAndRest",
                    "#ThereIsBeautyAllAround",
                    "#ThereIsSunshineInMySoulToday",
                    "#TheyTheBuildersOfTheNation",
                    "#ThisEarthWasOnceAGardenPlace",
                    "#ThisHouseWeDedicateToThee",
                    "#ThoughDeepeningTrials",
                    "#ThyHolyWord",
                    "#ThyServantsArePrepared",
                    "#ThySpiritLordHasStirredOurSouls",
                    "#ThyWillOLordBeDone",
                    "#TodayWhileTheSunShines",
                    "#TrueToTheFaith",
                    "#TruthEternal",
                    "#TruthReflectsuponOurSenses",
                    "#TurnYourHearts",
                    "#UpAwakeYeDefendersOfZion",
                    "#UponTheCrossOfCalvary",
                    "#WeAreAllEnlisted",
                    "#WeAreMarchingOnToGlory",
                    "#WeAreSowing",
                    "#WeEverPrayForThee",
                    "#WeGatherTogetherToAskTheLordsBlessing",
                    "#WeGiveTheeButThineOwn",
                    "#WeHavePartakenOfThyLove",
                    "#WeListenToAProphetsVoice",
                    "#WeLoveThyHouseOGod",
                    "#WeloveTohearThyholyword",
                    "#WeMeetAgainasSisters",
                    "#WeMeetAgainInSabbathSchool",
                    "#WeMeetDearLord",
                    "#WeThankTheeOGodForAProphet",
                    "#WeWillSingOfZion",
                    "#WellSingAllHailToJesusName",
                    "#WereNotAshamedToOwnOurLord",
                    "#WelcomeWelcomeSabbathMorning",
                    "#WhatGloriousScenesMineEyesBehold",
                    "#WhatWasWitnessedInTheHeavens",
                    "#WhenFaithEndures",
                    "#WhenInTheWondrousRealmsAbove",
                    "#WhenTheRosyLightOfMorning",
                    "#WhenUponLifesBillowsYouAreTempestTossed",
                    "#WhereCanITurnForPeace",
                    "#WhileOfTheseEmblemsWePartake",
                    "#WhileShepherdsWatchedTheirFlocks",
                    "#WhosOnTheLordsSide",
                    "#WithAllThePowerOfHeartAndTongue",
                    "#WithHumbleHeart",
                    "#WithSongsOfPraise",
                    "#WithWonderingAwe",
                    "#YeEldersOfIsrael",
                    "#YeSimpleSoulsWhoStray",
                    "#YeWhoAreCalledToLabor",
                    "#YouCanMakeThePathwayBright",
                    "#ZionStandsWithHillsSurrounded",
            };
        }
        else if (rnd >= 2 && rnd <= 5) {
            hashtags = new String[] {
                    "#LightTheWorld",
                    "#LDSconf",
                    "#MoTab",
                    "#LDSdevo",
                    "#ChristmasDevo",
                    "#LDSface2face",
                    "#BibleVideos",
                    "#Hallelujah",
                    "#ASaviorIsBorn",
                    "#IWasAStranger",

                    "#MormonMafia",
                    "#MormonProblems",
                    "#SUD",
                    "#CalledToServe",
                    "#EndureToTheEnd",
                    "#ICantImMormon",
                    "#LatterDays",
            };
        }
        else {
            hashtags = new String[] {
                    "#Mormon",
                    "#Mormonism",
                    "#BookOfMormon",
                    "#LDS",
                    "#LatterDaySaint",
                    "#Christian"
            };
        }
        String hashtag = hashtags[U.rand.nextInt(hashtags.length)];

        boolean lowercase = U.rand.nextBoolean();
        if (lowercase)
            hashtag = hashtag.toLowerCase();

        return hashtag;
    }

    private Pair<String, String> getOldAndNewThemes() {
        int rnd = U.rand.nextInt(10);
        rnd = 15;
        String[] churchProbs = {
                "homophobia",
                "racism",
                "sexism",
                "bigotry",
                "dogmatism",
                "dogma",
                "discrimination",
                "nativism",
                "nationalism",
                "chauvinism",
                "zealotry",
                "fanaticism",
                "superstition",
                "jingoism",
                "apartheid",
                "conservatism",
                "homogeneity",
                "ethnocentrism",
                "stubbornness",
                "intolerance",
                "xenophobia",
                "polygamy",
                "polyandry",
                "polygyny",
        };

        String[] virtues = {
                "good",
                "heavenly",
                "god",
                "light",
                "cleanliness",
                "order",
                "peace",
                "faith",
                "hope",
                "godliness",
                "grace",
                "chastity",
                "abstinence",
                "virtue",
                "purity",
                "knowledge",
                "truth",
                "wisdom",
                "honesty",
                "honor",
                "honour",
                "temperance",
                "justice",
                "humanity",
                "charity",
                "benevolence",
                "love",
                "devotion",
                "devoutness",
                "friendship",
                "brotherhood",
                "respect",
                "politeness",
                "piety",
                "holiness",
                "obedience",
                "friendship",
                "generosity",
                "diligence",
                "persistence",
                "fortitude",
                "ethics",
                "rectitude",
                "patience",
                "forgiveness",
                "mercy",
                "sufferance",
                "kindness",
                "satisfaction",
                "loyalty",
                "compassion",
                "integrity",
                "humility",
                "bravery",
                "courage",
                "modesty",
                "reverence",
                "altruism",
        };

        String[] sins = {
                "illegal",
                "ignorance",
                "badness",
                "irreverence",
                "dishonor",
                "ungodliness",
                "disobedience",
                "unholiness",
                "witchcraft",
                "soothsayer",
                "sorcery",
                "sorcerer",
                "magic",
                "immodesty",
                "adultery",
                "prostitution",
                "bestiality",
                "homosexuality",
                "sex",
                "sexuality",
                "bisexuality",
                "transexual",
                "transgender",
                "rape",
                "incest",
                "incest",
                "incest",
                "idolatry",
                "idleness",
                "idleness",
                "babbling",
                "strife",
                "murder",
                "lie",
                "whoredom",
                "whoremonger",
                "fornication",
                "wickedness",
                "rob",
                "robber",
                "persecute",
                "evil",
                "devil",
                "thief",
                "iniquity",
                "abomination",
                "astray",
                "infidelity",
                "darkness",
                "secret",
                "combination",
                "devilish",
                "carnal",
                "bloodshed",
                "sin",
                "war",
                "anger",
                "violence",
                "destruction",
                "carnage",
                "blood",
                "pestilence",
                "famine",
                "death",
                "enemy",
                "sorrow",
                "foul",
                "curse",
                "stiffnecked",
                "devourer",
                "infernal",
                "satan",
                "hell",
                "lucifer",
                "seduction",
                "hubris",
                "lust",
                "gluttony",
                "greed",
                "cupidity",
                "covetousness",
                "simony",
                "hoarding",
                "trickery",
                "manipulation",
                "sloth",
                "laziness",
                "wrath",
                "hatred",
                "rage",
                "revenge",
                "vengeance",
                "feud",
                "impatience",
                "misanthropy",
                "contempt",
                "envy",
                "jealousy",
                "pride",
                "selfishness",
                "corruption",
                "acedia",
                "depression",
                "melancholy",
                "sadness",
                "despair",
                "despondency",
                "apathy",
                "vanity",
                "vainglory",
                "narcissism",
        };
        String[] categories = {
                "animal",
                "monster",
                "food",
                "clothing",
                "body",
                "tool",
                "emotion",
                "geographic",
                "profession",
        };

        boolean themesInModelVocab = false;
        String oldTheme = "old";
        String newTheme = "new";
        while (!themesInModelVocab) {
            rnd = U.rand.nextInt(5);
            switch (rnd) {
                case 0:     //sin to virtue
                    oldTheme = sins[U.rand.nextInt(sins.length)];
                    newTheme = virtues[U.rand.nextInt(virtues.length)];
                    break;
                case 1:     //virtue to category
                    oldTheme = virtues[U.rand.nextInt(virtues.length)];
                    newTheme = categories[U.rand.nextInt(categories.length)];
                    break;
                case 2:     //virtue to churchProb
                    oldTheme = virtues[U.rand.nextInt(virtues.length)];
                    newTheme = churchProbs[U.rand.nextInt(churchProbs.length)];
                    break;
                case 3:     //male to female
                    oldTheme = "masculine";
                    newTheme = "feminine";
                    break;
                default:     //virtue to sin
                    oldTheme = virtues[U.rand.nextInt(virtues.length)];
                    newTheme = sins[U.rand.nextInt(sins.length)];
                    break;

            }
            Set<String> themes = new HashSet<>();
            themes.add(oldTheme);
            themes.add(newTheme);
            StringFilterEquation modelVocabFilter = new StringFilterEquation();
            modelVocabFilter.add(new FilterINTERSECTION());
            modelVocabFilter.add(new W2vModelVocabFilter(ReturnType.NON_MATCHES));
            Set<String> badStringsInModel = modelVocabFilter.run(new HashSet<>(themes));
            Set<String> themesInModel = new HashSet<>(themes);
            themesInModel.removeAll(badStringsInModel);
            if (themesInModel.size() == 2)
                themesInModelVocab = true;
        }
        return new Pair<>(oldTheme,newTheme);
    }

}





















































































