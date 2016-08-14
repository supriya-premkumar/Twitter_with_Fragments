package com.codepath.apps.Tweetster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.TwitterApplication;
import com.codepath.apps.Tweetster.TwitterClient;
import com.codepath.apps.Tweetster.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by supriya on 8/10/16.
 */
public class HomeTimelineFragment extends TweetsFragment {
    private TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_tweets, parent, false);

        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweets);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvTweets.setLayoutManager(gridLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        rvTweets.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline(0);
    }

    void populateTimeline(int page) {
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
            Toast.makeText(getActivity(), "Network unavailable. Try again later.", Toast.LENGTH_LONG).show();
            addAll(TweetModel.fetchTweetsOffline());
            Log.d("OFFLINE IDINI", tweets.toString());

        } else {
        client.getHomeTimeline(since_id, max_id, page, new JsonHttpResponseHandler() {
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








