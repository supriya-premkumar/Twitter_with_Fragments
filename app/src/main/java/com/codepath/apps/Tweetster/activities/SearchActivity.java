package com.codepath.apps.Tweetster.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.codepath.apps.Tweetster.EndlessRecyclerViewScrollListener;
import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.TwitterApplication;
import com.codepath.apps.Tweetster.TwitterClient;
import com.codepath.apps.Tweetster.adapters.DividerItemDecoration;
import com.codepath.apps.Tweetster.adapters.TweetsRecyclerViewAdapter;
import com.codepath.apps.Tweetster.models.TweetModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    private static final String STATUSES = "statuses";
    private static final String SINCE_ID_STR = "since_id_str";
    private static final String MAX_ID_STR = "max_id_str";
    LinearLayoutManager layoutManager;
    ArrayList<TweetModel> mTweets;
    @BindView(R.id.rvSearch)
    RecyclerView mRecyclerViewTweets;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipeContainerSearch)
    SwipeRefreshLayout mSwipeRefreshLayout;
    String queryString;

    String sinceId = "1";
    String maxId = "0";

    private TwitterClient mTwitterClient;
    TweetsRecyclerViewAdapter recyclerViewTweetsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTweets = new ArrayList<>();
        recyclerViewTweetsAdapter = new TweetsRecyclerViewAdapter(this, mTweets);
        mRecyclerViewTweets.setAdapter(recyclerViewTweetsAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerViewTweets.addItemDecoration(itemDecoration);

        // Setup layout manager for items
        layoutManager = new LinearLayoutManager(this);
        // Control orientation of the items
        // also supports LinearLayoutManager.HORIZONTAL
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);
        // Set layout manager to position the items
        // Attach the layout manager to the recycler view
        mRecyclerViewTweets.setLayoutManager(layoutManager);

        mRecyclerViewTweets.addOnScrollListener(
                new EndlessRecyclerViewScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to the bottom of the list
                        Toast.makeText(getApplicationContext(),
                                "Loading more...", Toast.LENGTH_SHORT).show();
                        // Send an API request to retrieve appropriate data using the offset value as a parameter.
                        // Deserialize API response and then construct new objects to append to the adapter
                        // Add the new objects to the data source for the adapter
                        // For efficiency purposes, notify the adapter of only the elements that got changed
                        // curSize will equal to the index of the first element inserted because the list is 0-indexed
//                        populateTweetsFromSearch(queryString, true, false);

                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                populateTweetsFromSearch(queryString, false, true);
            }
        });

        mTwitterClient = TwitterApplication.getRestClient(); //singleton client

        SearchView etSearch = (SearchView) findViewById(R.id.etSearch);
//        etSearch.setFocusable(true);
//        etSearch.setQueryHint(getString(R.string.search));
        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

}

