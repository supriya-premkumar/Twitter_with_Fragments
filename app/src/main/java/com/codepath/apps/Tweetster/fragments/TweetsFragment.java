package com.codepath.apps.Tweetster.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.codepath.apps.Tweetster.adapters.TweetsRecyclerViewAdapter;
import com.codepath.apps.Tweetster.models.TweetModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by supriya on 8/11/16.
 */
public abstract class TweetsFragment extends Fragment {
    public TweetsRecyclerViewAdapter adapter;
    public ArrayList<TweetModel> tweets;
    //    @BindView(R.id.rvTweets)
    public RecyclerView rvTweets;
    //    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        //construct the adapter from data source
        adapter = new TweetsRecyclerViewAdapter(getActivity(), tweets);

    }

    public void addAll(List<TweetModel> tweet) {
        tweets.addAll(tweet);
        adapter.notifyDataSetChanged();

    }

    public boolean isOnline() {
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
