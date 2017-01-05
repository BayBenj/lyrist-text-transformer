//package song;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import song.StressedPhoneme;
//
//public class WordPhonemePair extends SequencePair {
//
//    public class WordPhonemeAlignmentBuilder extends AlignmentBuilder {
//
//        StringBuilder alnWordBldr = new StringBuilder();
//        List<StressedPhoneme> alnPhoneBldr = new ArrayList<StressedPhoneme>();
//
//        @Override
//        public void appendCharSequence1(int i) {
//            alnWordBldr.append(word.charAt(i));
//        }
//
//        @Override
//        public void appendCharSequence2(int j) {
//            alnPhoneBldr.add(phonemes[j]);
//        }
//
//        @Override
//        public void appendIndelSequence1() {
//            alnWordBldr.append(AlignmentBuilder.INDEL_CHAR);
//        }
//
//        @Override
//        public void appendIndelSequence2() {
//            alnPhoneBldr.add(null);
//        }
//
//        @Override
//        public Alignment renderAlignment() {
//            return new WordPhonemeAlignment(alnWordBldr.toString(),alnPhoneBldr.toArray(new StressedPhoneme[0]), scores);
//        }
//
//        @Override
//        public void reverse() {
//            alnWordBldr.reverse();
//            Collections.reverse(alnPhoneBldr);
//            super.reverse();
//        }
//    }
//
//    String word;
//    String wordlc;
//    StressedPhoneme[] phonemes;
//
//    public WordPhonemePair(String word, StressedPhoneme[] phonemes) {
//        this.word = word;
//        this.wordlc = word.toLowerCase();
//        this.phonemes = phonemes;
//    }
//
//    @Override
//    public AlignmentBuilder newAlignmentBuilder() {
//        return new WordPhonemeAlignmentBuilder();
//    }
//
//    @Override
//    public double matchScore(int row_1, int i) {
//        switch(phonemes[i].phoneme){
//		/*case 0://			AA	vowel odd
//		case 3://			AO	vowel ought
//			return wordlc.charAt(row_1) == 'o' || wordlc.charAt(row_1) == 'a'? MATCH_SCORE : MISMATCH_SCORE;
//		case 1://			AE	vowel at
//			return wordlc.charAt(row_1) == 'a'? MATCH_SCORE : MISMATCH_SCORE;
//		case 2://			AH	vowel hut,sofa,genre
//			return wordlc.charAt(row_1) == 'a' || wordlc.charAt(row_1) == 'u' || wordlc.length() > row_1+2 && wordlc.substring(row_1,row_1+3).equals("ous")? MATCH_SCORE : MISMATCH_SCORE;
//		case 4://			AW	vowel HARD
//			return .5* wordlc.length() > row_1+1 && (wordlc.substring(row_1,row_1+2).equals("ao") || wordlc.substring(row_1,row_1+2).equals("ow"))? MATCH_SCORE : MISMATCH_SCORE;
//		case 5://			AY	vowel hide
//			return wordlc.charAt(row_1) == 'i' || wordlc.charAt(row_1) == 'y'? MATCH_SCORE : MISMATCH_SCORE;
//		case 6://			EH	vowel red
//			return wordlc.charAt(row_1) == 'e'? MATCH_SCORE : MISMATCH_SCORE;
//		case 8://			EY	vowel ate HARD
//			return .5* wordlc.charAt(row_1) == 'a' || wordlc.length() > row_1+1 && (wordlc.substring(row_1,row_1+2).equals("ey") || wordlc.substring(row_1,row_1+2).equals("ei"))? MATCH_SCORE : MISMATCH_SCORE;
//		case 9://			IH	vowel it
//			return wordlc.charAt(row_1) == 'i'? MATCH_SCORE : MISMATCH_SCORE;
//		case 10://			IY	vowel bee she HARD
//			return .5* wordlc.charAt(row_1) == 'e'|| wordlc.charAt(row_1) == 'i'? MATCH_SCORE : MISMATCH_SCORE;
//		case 11://			OW	vowel show,coat
//			return wordlc.charAt(row_1) == 'o'? MATCH_SCORE : MISMATCH_SCORE;
//		case 12://			OY	vowel
//			return .5* wordlc.length() > row_1+1 && (wordlc.substring(row_1,row_1+2).equals("oi") || wordlc.substring(row_1,row_1+2).equals("oy"))? MATCH_SCORE : MISMATCH_SCORE;
//		case 13://			UH	vowel could hood
//			return wordlc.charAt(row_1) == 'o'? MATCH_SCORE : MISMATCH_SCORE;
//		case 14://			UW	vowel two, loo,you, computer
//			return wordlc.charAt(row_1) == 'o' || wordlc.charAt(row_1) == 'u'? MATCH_SCORE : MISMATCH_SCORE; */
//            case 15: //B
//                return wordlc.charAt(row_1) == 'b'? MATCH_SCORE : MISMATCH_SCORE;
//            case 16: //CH
//                return wordlc.length() > row_1+1 && wordlc.substring(row_1,row_1+2).equals("ch")? MATCH_SCORE : MISMATCH_SCORE;
//            case 17: //D
//                return wordlc.charAt(row_1) == 'd'? MATCH_SCORE : MISMATCH_SCORE;
//            case 18: //DH	fricative
//            case 33: //TH	fricative
//                return wordlc.length() > row_1+1 && wordlc.substring(row_1,row_1+2).equals("th")? MATCH_SCORE : MISMATCH_SCORE;
//            case 19: //F	fricative
//                return wordlc.charAt(row_1) == 'f'? MATCH_SCORE : MISMATCH_SCORE;
//            case 20: //G	stop
//                return wordlc.charAt(row_1) == 'g'? MATCH_SCORE : MISMATCH_SCORE;
//            case 21: //HH	aspirate
//                return wordlc.charAt(row_1) == 'h'? MATCH_SCORE : MISMATCH_SCORE;
//            case 22: //JH	affricate
//                return wordlc.charAt(row_1) == 'j' || wordlc.charAt(row_1) == 'g'? MATCH_SCORE : MISMATCH_SCORE;
//            case 23: //K	stop
//                return wordlc.charAt(row_1) == 'c' || wordlc.charAt(row_1) == 'k' || wordlc.charAt(row_1) == 'x'|| wordlc.charAt(row_1) == 'q'? MATCH_SCORE : MISMATCH_SCORE;
//            case 24: //L	liquid
//                return wordlc.charAt(row_1) == 'l'? MATCH_SCORE : MISMATCH_SCORE;
//            case 25: //M	nasal
//                return wordlc.charAt(row_1) == 'm'? MATCH_SCORE : MISMATCH_SCORE;
//            case 26: //N	nasal
//                return wordlc.charAt(row_1) == 'n'? MATCH_SCORE : MISMATCH_SCORE;
//            case 27: //NG	nasal
//                return wordlc.length() > row_1+1 && wordlc.substring(row_1,row_1+2).equals("ng")? MATCH_SCORE : MISMATCH_SCORE;
//            case 28: //P	stop
//                return wordlc.charAt(row_1) == 'p'? MATCH_SCORE : MISMATCH_SCORE;
//            case 29: //R	liquid
//            case 7://			ER	vowel
//                return wordlc.charAt(row_1) == 'r'? MATCH_SCORE : MISMATCH_SCORE;
//            case 30: //S	fricative
//                return wordlc.charAt(row_1) == 's' || wordlc.charAt(row_1) == 'c'? MATCH_SCORE : MISMATCH_SCORE;
//            case 38: //ZH	fricative
//                return wordlc.charAt(row_1) == 's'? MATCH_SCORE : MISMATCH_SCORE;
//            case 31: //SH	fricative
//                return wordlc.length() > row_1+1 && wordlc.substring(row_1,row_1+2).equals("sh") ||
//                        wordlc.length() > row_1+1 && wordlc.substring(row_1,row_1+2).equals("ss") ||
//                        wordlc.length() > row_1+2 && wordlc.substring(row_1,row_1+3).equals("tio")? MATCH_SCORE : MISMATCH_SCORE;
//            case 32: //T	stop
//                return wordlc.charAt(row_1) == 't'? MATCH_SCORE : MISMATCH_SCORE;
//            case 34: //V	fricative
//                return wordlc.charAt(row_1) == 'v'? MATCH_SCORE : MISMATCH_SCORE;
//            case 35: //W	semivowel
//                return wordlc.charAt(row_1) == 'w'? MATCH_SCORE : MISMATCH_SCORE;
//            case 36: //Y	semivowel
//                return wordlc.charAt(row_1) == 'y'? MATCH_SCORE : MISMATCH_SCORE;
//            case 37: //Z	fricative
//                return wordlc.charAt(row_1) == 's' || wordlc.charAt(row_1) == 'z'? MATCH_SCORE : MISMATCH_SCORE;
//
//        }
//
//        // we assume that only vowel sounds are left, if the letter is a vowel, too, we'll say it's a viable match
//        return "aeiouy".indexOf(wordlc.charAt(row_1)) != -1 ? MATCH_SCORE:MISMATCH_SCORE;
//    }
//
//    @Override
//    public int seq1length() {
//        return word.length();
//    }
//
//    @Override
//    public int seq2length() {
//        return phonemes.length;
//    }
//
//}
