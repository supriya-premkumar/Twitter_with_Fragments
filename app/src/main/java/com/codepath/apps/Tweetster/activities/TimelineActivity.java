package com.codepath.apps.Tweetster.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.codepath.apps.Tweetster.EndlessRecyclerViewScrollListener;
import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.TwitterApplication;
import com.codepath.apps.Tweetster.TwitterClient;
import com.codepath.apps.Tweetster.adapters.TweetsRecyclerViewAdapter;
import com.codepath.apps.Tweetster.fragments.TweetComposeFragment;
import com.codepath.apps.Tweetster.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private TwitterClient client;
    private TweetsRecyclerViewAdapter adapter;
    private ArrayList<Object> tweets;

    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fabCompose) FloatingActionButton fabCompose;
    @BindView(R.id.rvTweets) RecyclerView rvTweets;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                 Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(0);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        //fabCompose = (FloatingActionButton) findViewById(R.id.fabCompose);
        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeTweetFragment();
            }
        });

        //rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        // Create the arraylist (data source)
        tweets = new ArrayList<>();
        //construct the adapter from data source
        adapter = new TweetsRecyclerViewAdapter(this, tweets);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvTweets.setLayoutManager(gridLayoutManager);
        rvTweets.setAdapter(adapter);

        //get the client
        client = TwitterApplication.getRestClient(); //Singleton client
        populateTimeline(0);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                customLoadMoreDataFromApi(page);
                Log.d("SCROLLED X TIMES", "Abhi");
                populateTimeline(page);
            }
        });
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        populateTimeline(0);
    }

    private void populateTimeline(int page) {
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
            tweets.addAll(TweetModel.fetchTweetsOffline());
            Log.d("OFFLINE IDINI", tweets.toString());
            adapter.notifyDataSetChanged();

        } else {
            client.getHomeTimeline(since_id, max_id, page, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("onSuccess Timeline: ", json.toString());
                    //JSON HERE
                    //Deserialize JSON
                    //Create models and add them to the adapter
                    //load the model data into list view

                    tweets.addAll(TweetModel.fromJsonArray(json));
                    Log.d("Arraylist", json.toString());
                    adapter.notifyDataSetChanged();
//                rvTweets.scrollToPosition(0);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("onFailure: Timeline ", errorResponse.toString());
                }
            });

        }


    }

    private void composeTweetFragment() {
        FragmentManager fm = getSupportFragmentManager();
        TweetComposeFragment filterSettingsFragment = TweetComposeFragment.newInstance("Filter Results");
        filterSettingsFragment.show(fm, "filter_settings_fragment");
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 4.2.2.2");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;

    }


}