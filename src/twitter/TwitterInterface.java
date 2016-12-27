package twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterInterface {

    public TwitterInterface() {

    }

    public void tweet(String text) {
        if (text.length() > 140) {
            System.out.println("String too long to tweet! String is " + text.length() + "characters.");
        }
        else {
            try {
                Twitter twitter = TwitterFactory.getSingleton();
                Status status = twitter.updateStatus(text.toString());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    public void mockTweet(String text) {
        if (text.length() > 140) {
            System.out.println("String too long to tweet! String is " + text.length() + "characters.");
        }
        else {
            Twitter twitter = TwitterFactory.getSingleton();
            System.out.println("TWEET:\n" + text);

        }
    }
}




































