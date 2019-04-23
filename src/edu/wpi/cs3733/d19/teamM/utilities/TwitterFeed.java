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

        Paging page = new Paging(1, 5);

        try{
            statuses.addAll(twitter.getUserTimeline(user, page));
        }catch(Exception e){
            e.printStackTrace();
        }
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
