package com.codepath.apps.Tweetster.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by supriya on 8/5/16.
 */
@Parcel(analyze = {TweetModel.class})
@Table(name = "Tweets")

public class TweetModel extends Model {
    @Column(name = "remote_id")
    public long remote_id;

    @Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String tweet_id;

    @Column(name = "tweet")
    String tweet;

    @Column(name = "userName")
    String userName;

    @Column(name = "screenName")
    String screenName;

    @Column(name = "timeStamp")
    String timeStamp;

    @Column(name = "profileImageUrl")
    String profileImageUrl;

    @Column(name = "mediaUrl")
    String mediaUrl;

    @Column(name = "videoUrl")
    String videoUrl;

    @Column(name = "bannerUrl")
    String bannerUrl;

    public String getBannerUrl() {
        return bannerUrl;
    }


    public TweetModel() {
        super();

    }

    public String getMediaUrl() {
        return mediaUrl;
    }


    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTweetId() {
        return tweet_id;
    }

    public void setTweetId(String id) {
        this.tweet_id = id;
    }


    public String getTimeStamp() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String duration = "";
        String relativeDate;
        try {
            long dateMillis = sf.parse(timeStamp).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            duration = relativeDate.replace("In", "");
            duration = duration.replace("ago", "");

            String[] durationComponents = duration.split(" +");
            String shortHand;
            if (durationComponents[1].contains("second")) {
                shortHand = "s";
            } else if (durationComponents[1].contains("minute")){
                shortHand = "m";
            } else {
                shortHand = "h";
            }
            duration = durationComponents[0] + shortHand;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return duration;

    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public TweetModel(JSONObject rawTweetData) {

        try {
            this.remote_id = rawTweetData.getLong("id");
            this.tweet_id = rawTweetData.getString("id_str");
            this.tweet = rawTweetData.getString("text");
            JSONObject user = rawTweetData.getJSONObject("user");
            this.userName = user.getString("name");
            this.screenName = "@" + user.getString("screen_name");
            this.profileImageUrl = user.getString("profile_image_url");
            this.bannerUrl = user.has("profile_banner_url") ? user.getString("profile_banner_url") : "";

            this.timeStamp = rawTweetData.getString("created_at");
            JSONObject media = rawTweetData.getJSONObject("entities");
            if (media.has("media")) {
                JSONArray mediaArray = media.getJSONArray("media");
                if (mediaArray.length() > 0) {
                    JSONObject mediaArrayObject = mediaArray.getJSONObject(0);
                    if (mediaArrayObject.getString("type").equals("photo")) {
                        this.mediaUrl = mediaArray.getJSONObject(0).getString("media_url");
                    } else if (mediaArrayObject.getString("type").equals("video")) ;
                    {
//                        this.videoUrl =

                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public static ArrayList<TweetModel> fromJsonArray(JSONArray array) {
        ArrayList<TweetModel> results = new ArrayList<>();


        for (int x = 0; x < array.length(); x++) {
            try {
                JSONObject rawTweetData = array.getJSONObject(x);
                TweetModel txtTweet = new TweetModel(rawTweetData);
                txtTweet.save();
                results.add(txtTweet);
                Log.d("xxxx", rawTweetData.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return results;
    }

    public static List<TweetModel> fetchTweetsOffline() {
        List<TweetModel> offlineTweets = new Select().from(TweetModel.class)
                .orderBy("tweet_id DESC").limit(100).execute();
        return offlineTweets;

        //return new Select().from(TweetModel.class).where("type = ?"+context.getResources().getString(R.string.HOME_TIMELINE)).orderBy("timestamp DESC").execute();
    }

}
