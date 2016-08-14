package com.codepath.apps.Tweetster.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.Tweetster.TwitterApplication;
import com.codepath.apps.Tweetster.TwitterClient;
import com.codepath.apps.Tweetster.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by supriya on 8/12/16.
 */
public class UserTimelineFragment extends TweetsFragment {
    private TwitterClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        populateTimeline(0);


    }

    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static UserTimelineFragment newInstance( String screen_name) {
        UserTimelineFragment fragmentDemo = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }


    public void populateTimeline(int page) {
        String screenName = getArguments().getString("screen_name");
        long since_id = 1;
        long max_id = 1;
        if (page == 0) {
            tweets.clear();
        }
        if (!tweets.isEmpty()) {
            max_id = Long.valueOf(((TweetModel) tweets.get(0)).getTweetId()) - 1;
            since_id = Long.valueOf(((TweetModel) tweets.get(tweets.size() - 1)).getTweetId());
        }

        if (!isOnline()) {
            addAll(TweetModel.fetchTweetsOffline());
            Log.d("OFFLINE IDINI", tweets.toString());

        } else {
            client.getUserTimeline( screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("onSuccess Timeline: ", json.toString());
                    //JSON HERE
                    //Deserialize JSON
                    //Create models and add them to the adapter
                    //load the model data into list view

                    addAll(TweetModel.fromJsonArray(json));
//                    xxxxxxxxxxxxxxxxxxxxx

                    Log.d("Arraylist", json.toString());
//                rvTweets.scrollToPosition(0);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("onFailure: Timeline ", errorResponse.toString());
                }
            });

        }


    }

}
