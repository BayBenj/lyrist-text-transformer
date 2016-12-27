//package inspirationCHECK;
//
//import java.util.Scanner;
//
//import java.util.Random;
//
//import main.InspirationSource;
//import main.ProgramArgs;
//
//public class Inspiration {
//    //Ekman (1993): happiness, surprise, anger, sadness, fear, disgust
//    public static final String[] ARRAY_OF_EMOTIONS = new String[]{"happy","surprised","angry","sad","afraid","disgusted"};
//    private float[] emotions = new float[]{0,0,0,0,0,0}; // sum should be <= 1.0
//    private static final Random RAND = new Random();
//    private String explaination;
//
//    public Inspiration(InspirationSource source) {
//        switch(source)
//        {
//            case RANDOM:
//                emotions[RAND.nextInt(ARRAY_OF_EMOTIONS.length)] = (float) 1.0;
//                break;
//            case ENVIRONMENT:
//                throw new UnsupportedOperationException("Not Implemented");
//            case UNSET:
//                throw new RuntimeException("Invalid inspiring source: " + source);
//            case USER:
//                switch (ProgramArgs.userInterfaceSetting)
//                {
//                    case UNSET:
//                        throw new RuntimeException("Invalid UI setting:" + ProgramArgs.userInterfaceSetting);
//                    case COMMANDLINE:
//                        promptUserForEmotion();
//                        break;
//                    case GUI:
//                        throw new UnsupportedOperationException("Not Implemented");
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void promptUserForEmotion() {
//        System.out.println("What's on your mind these days? How are you feeling?");
//        Scanner scan = new Scanner(System.in);
//        String input = scan.nextLine();
//        scan.close();
//        analyzeInputForEmotion(input);
//    }
//
//    public void analyzeInputForEmotion(String input) {
//        for (int i = 0; i < ARRAY_OF_EMOTIONS.length; i++) {
//            String emotion = ARRAY_OF_EMOTIONS[i];
//            emotions[i] = analyzeInputForEmotion(emotion, input);
//        }
//        normalizeEmotions();
//    }
//
//    private void normalizeEmotions() {
//        float sum = (float) 0.0;
//
//        for (int i = 0; i < emotions.length; i++) {
//            sum += emotions[i];
//        }
//
//        for (int i = 0; i < emotions.length; i++) {
//            emotions[i] /= sum;
//        }
//
//    }
//
//    private float analyzeInputForEmotion(String emotion, String input) {
//        //TODO: sentiment analysis here?
//        if(emotion.equalsIgnoreCase(input))
//            return (float) 1.0;
//        else
//            return (float) 0.0;
//    }
//
//    public String getMaxEmotion() {
//        int maxIdx = -1;
//        double maxEmotion = -1.0;
//
//        for (int i = 0; i < emotions.length; i++) {
//            if (emotions[i] > maxEmotion) {
//                maxEmotion = emotions[i];
//                maxIdx = i;
//            }
//        }
//
//        assert(maxIdx != -1);
//
//        return ARRAY_OF_EMOTIONS[maxIdx];
//    }
//
//    public void setExplaination(String explaination) {
//        this.explaination = explaination;
//    }
//
//    public String getExplaination() {
//        return explaination;
//    }
//}
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
