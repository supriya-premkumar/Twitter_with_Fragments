package com.codepath.apps.Tweetster.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.TwitterApplication;
import com.codepath.apps.Tweetster.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by supriya on 8/7/16.
 */
public class TweetComposeFragment extends DialogFragment {
    private TwitterClient client;
    public TweetComposeFragment() {

    }

    public static TweetComposeFragment newInstance(String title) {
        TweetComposeFragment frag = new TweetComposeFragment();
        frag.setHasOptionsMenu(true);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.tweet_compose_fragment, container, false);

//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) v.findViewById(R.id.filter_settings_toolbar);
//        toolbar.inflateMenu(R.menu.menu_items);

        client = TwitterApplication.getRestClient();
        TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        ImageView ivProfilePhoto = (ImageView) v.findViewById(R.id.ivProfileImage);
        TextView tvScreenName = (TextView) v.findViewById(R.id.tvUserScreenName);
        tvScreenName.setVisibility(View.GONE);
        TextInputLayout compose = (TextInputLayout) v.findViewById(R.id.textCompose);
        Button submitTweet = (Button) v.findViewById(R.id.btnsubmit);
        final EditText etTweet = (EditText) v.findViewById(R.id.etTweet);


        submitTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = etTweet.getText().toString();
                postTweet(tweet);
                dismiss();
            }
        });


        return v;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }


    private void postTweet(String tweet){
        client.postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("onSuccess Timeline: ", json.toString());
                //JSON HERE
                //Deserialize JSON
                //Create models and add them to the adapter
                //load the model data into list view

                Log.d("COMPOSETWEET:", json.toString());

//                rvTweets.scrollToPosition(0);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure: Timeline ", errorResponse.toString());
            }
        });

    }

}





