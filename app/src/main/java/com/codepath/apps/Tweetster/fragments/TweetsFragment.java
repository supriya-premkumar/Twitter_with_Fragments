package com.codepath.apps.Tweetster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.adapters.TweetsRecyclerViewAdapter;
import com.codepath.apps.Tweetster.models.TweetModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by supriya on 8/11/16.
 */
public class TweetsFragment extends Fragment {
    public TweetsRecyclerViewAdapter adapter;
    public ArrayList<TweetModel> tweets;
    //    @BindView(R.id.rvTweets)
    public RecyclerView rvTweets;
    //    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;


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
        return v;
    }

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
