package bookofmormon;

import filters.Direction;
import filters.VocabListFilter;
import song.VocabList;

import java.util.*;

public class BomNameFilter extends VocabListFilter {

    private static String[] list = {
            "aaron",
            "aaron",
            "aaron",
            "abinadi",
            "abinadom",
            "abish",
            "aha",
            "ahah",
            "akish",
            "alma",
            "alma",
            "amaleki",
            "amaleki",
            "amalickiah",
            "amaron",
            "aminadab",
            "amgid",
            "aminadi",
            "amlici",
            "ammah",
            "ammaron",
            "ammon",
            "ammon",
            "ammoron",
            "amnigaddah",
            "amnor",
            "amoron",
            "amos",
            "amos",
            "amulek",
            "amulon",
            "antiomno",
            "antionah",
            "antionum",
            "antipus",
            "archeantus",
            "benjamin",
            "jared",
            "moroni",
            "cezoram",
            "chemish",
            "christ",
            "cohor",
            "cohor",
            "cohor",
            "com",
            "com",
            "corianton",
            "coriantor",
            "coriantum",
            "coriantum",
            "coriantumr",
            "coriantumr",
            "coriantumr",
            "corihor",
            "corihor",
            "corom",
            "cumenihah",
            "emer",
            "emron",
            "enos",
            "esrom",
            "ethem",
            "ether",
            "ezias",
            "gadianton",
            "gid",
            "giddianhi",
            "giddonah",
            "giddonah",
            "gideon",
            "gidgiddonah",
            "gidgiddoni",
            "gilead",
            "gilgah",
            "gilgal",
            "hagoth",
            "hearthom",
            "helam",
            "helaman",
            "helaman",
            "helaman",
            "helem",
            "helorum",
            "hem",
            "heth",
            "heth",
            "himni",
            "isabel",
            "isaiah",
            "ishmael",
            "ishmael",
            "jacob",
            "jacob",
            "jacob",
            "jacom",
            "jared",
            "jared",
            "jarom",
            "jeneum",
            "jeremiah",
            "jesus",
            "jonas",
            "jonas",
            "joseph",
            "josh",
            "kib",
            "kim",
            "kimnor",
            "king benjamin",
            "kish",
            "kishkumen",
            "korihor",
            "kumen",
            "kumenonhi",
            "laban",
            "lachoneus",
            "lachoneus",
            "lamah",
            "laman",
            "laman",
            "laman",
            "laman",
            "lamoni",
            "lehi",
            "lehi",
            "lehi",
            "lehi",
            "lehonti",
            "lemuel",
            "levi",
            "lib",
            "lib",
            "limhah",
            "limher",
            "limhi",
            "luram",
            "mahah",
            "manti",
            "mathoni",
            "mathonihah",
            "morianton",
            "morianton",
            "mormon",
            "mormon",
            "moron",
            "moroni",
            "moroni",
            "moronihah",
            "moronihah",
            "mosiah",
            "mosiah",
            "mulek",
            "muloki",
            "nehor",
            "nephi",
            "nephi",
            "nephi",
            "nephi",
            "nephihah",
            "neum",
            "nimrah",
            "noah",
            "noah",
            "omer",
            "omner",
            "omni",
            "orihah",
            "paanchi",
            "pachus",
            "pacumeni",
            "pagag",
            "pahoran",
            "pahoran",
            "riplakish",
            "sam",
            "samuel",
            "sariah",
            "seantum",
            "seezoram",
            "seth",
            "shared",
            "shem",
            "shemnon",
            "sherem",
            "shez",
            "shez",
            "shiblom",
            "shiblom",
            "shiblon",
            "shiz",
            "shule",
            "teancum",
            "teomner",
            "timothy",
            "tubaloth",
            "zarahemla",
            "zedekiah",
            "zeezrom",
            "zemnarihah",
            "zenephi",
            "zeniff",
            "zenock",
            "zenos",
            "zerahemnah",
            "zeram",
            "zoram",
            "zoram",
            "zoram",
            "aaron",
            "ablom",
            "agosh",
            "aiath",
            "akish",
            "alma",
            "ammonihah",
            "amnihu",
            "amulon",
            "anathoth",
            "angola",
            "ani-anti",
            "antionum",
            "antiparah",
            "antipas",
            "antum",
            "arpad",
            "assyria",
            "babylon",
            "bashan",
            "bethabara",
            "boaz",
            "bountiful",
            "calno",
            "carchemish",
            "chaldea",
            "comnor",
            "corihor",
            "cumeni",
            "cumorah",
            "damascus",
            "david",
            "desolation",
            "desolation",
            "eden",
            "edom",
            "egypt",
            "elam",
            "ephraim",
            "gad",
            "gadiandi",
            "gadiomnah",
            "gallim",
            "geba",
            "gebim",
            "gid",
            "gideon",
            "gideon",
            "gilgal",
            "gilgal",
            "gimgimno",
            "gomorrah",
            "hagoth",
            "hamath",
            "helam",
            "hermounts",
            "heshlon",
            "heth",
            "horeb",
            "irreantum",
            "ishmael",
            "israel",
            "jacob",
            "jacobugath",
            "jashon",
            "jershon",
            "jerusalem",
            "jerusalem",
            "jordan",
            "jordan",
            "josh",
            "joshua",
            "judah",
            "judea",
            "kishkumen",
            "laish",
            "laman",
            "lebanon",
            "lehi",
            "lehi-nephi",
            "lemuel",
            "madmenah",
            "manti",
            "melek",
            "michmash",
            "middoni",
            "midian",
            "migron",
            "minon",
            "moab",
            "mocum",
            "moriancumer",
            "morianton",
            "moriantum",
            "mormon",
            "mormon",
            "mormon",
            "moron",
            "camp",
            "moroni",
            "moronihah",
            "mulek",
            "nahom",
            "naphtali",
            "nazareth",
            "nehor",
            "nephi",
            "nephi",
            "nephihah",
            "nephihah",
            "nimrod",
            "noah",
            "ogath",
            "omner",
            "onidah",
            "onidah",
            "onihah",
            "palestina",
            "pathros",
            "ramah",
            "ramath",
            "riplah",
            "ripliancum",
            "salem",
            "samaria",
            "sebus",
            "shazer",
            "shelem",
            "shem",
            "shemlon",
            "sherrizah",
            "shiloah",
            "shilom",
            "shim",
            "shimnilom",
            "shinar",
            "shurr",
            "sidom",
            "river",
            "siani",
            "sinim",
            "siron",
            "sodom",
            "syria",
            "tarshish",
            "teancum",
            "babel",
            "zarahemla",
            "zarahemla",
            "zebulun",
            "zeezrom",
            "zerin",
            "zion"
    };
    private static Set set = new HashSet(Arrays.asList(list));

    public BomNameFilter() {
        super(new VocabList(set, "book of mormon people"));
    }

    public BomNameFilter(Direction direction) {
        super(direction, new VocabList(set, "book of mormon people"));
    }

    @Override
    public Set<String> doFilter(Set<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (super.getDirection() == Direction.INCLUDE_MATCH && super.vocabList.contains(s.toLowerCase()) ||
                    super.getDirection() == Direction.EXCLUDE_MATCH && !super.vocabList.contains(s.toLowerCase()))
                result.add(s);
        }
        return result;
    }

    public static String[] getList() {
        return list;
    }
}
















































































