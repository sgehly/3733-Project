package edu.wpi.cs3733.d19.teamM.utilities;
import java.util.*;
import twitter4j.*;
import twitter4j.conf.*;

public class TwitterFeed {

    ArrayList<Status> statuses = new ArrayList<Status>();
    String user = "BrighamWomens";

    public static void main(String[] args) {
        TwitterFeed feed = new TwitterFeed();

    }
    /**
     * Constructor for the class which establishes the connection and populates everything
     */
    public TwitterFeed()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("0xpfqRbavISNYsxaXbkxcnGuF");
        cb.setOAuthConsumerSecret("iAwaz8KTMntu2ifYrjllhbEf6B8MijOP8WMvqL167JGZeLPYfd");
        cb.setOAuthAccessToken("261488966-sx7FJFKwyjGbxekVUX16n1hnFGTggUy3gYVcXYPK");
        cb.setOAuthAccessTokenSecret("DGbqiUQQg1qJL1oQVqmodIPOJ7CnAZDHQ2TebzvfgaN0Y");

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        int pageno = 1;

        while (true) {

            try {

                int size = statuses.size();
                Paging page = new Paging(pageno++, 100);

                statuses.addAll(twitter.getUserTimeline(user, page));

                if (statuses.size() == size)
                    break;
            }
            catch(TwitterException e) {

                e.printStackTrace();
            }

        }



        // gets Twitter instance with default credentials
     /*   Twitter twitter = new TwitterFactory().getInstance();
        try {
            List<Status> statuses;
            String user;
            if (true) {
                user = "google";
                statuses = twitter.getUserTimeline(user);
            } else {
                user = twitter.verifyCredentials().getScreenName();
                statuses = twitter.getUserTimeline();
            }
            System.out.println("Showing @" + user + "'s user timeline.");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }*/
       /* Twitter unauthenticatedTwitter = new TwitterFactory().getInstance();
//First param of Paging() is the page number, second is the number per page (this is capped around 200 I think.
        Paging paging = new Paging(1, 100);
        try {
            List<Status> statuses = unauthenticatedTwitter.getUserTimeline("google",paging);
        } catch (TwitterException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Prints the current feed to the console
     */
    public void printFeed()
    {
        System.out.println("Showing @" + user + "'s user timeline.");
        for (Status status : statuses) {

            System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            /*
            Stuff you can get from a status object:
            -You can get a user
                descrpition
                screen name
                status
                profile photo
                created at
                email
                friends
                name
                followers
                friends
                location
                etc.
            -You can get Text
                Anything you can do with a string (typically also contains a link to an image or video or something) <look at print method to see example>
             */
        }
        System.out.println("Total: "+statuses.size());
    }


    /**
     *
     * @param numUpdates: The number of updates you want
     * @return ArrayList<Status> the requested number of updates or all the updates
     */
    public ArrayList<Status> getUpdates(int numUpdates)
    {
        ArrayList<Status> returnList = new ArrayList<Status>();

        if(numUpdates<statuses.size())
        {
            for(int i = 0; i<numUpdates;i++)
            {
                returnList.add(statuses.get(i));
            }
        }
        else
        {
            returnList = statuses;
        }

        return returnList;
    }

    /**
     * This method returns all the status updates that the user has made
     * @return ArrayList<Status> the status updates arraylist
     */
    public ArrayList<Status> getAllUpdates()
    {
        return statuses;
    }

}
