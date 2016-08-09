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

import com.bumptech.glide.Glide;
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
public class TweetReplyFragment extends DialogFragment {
    private TwitterClient client;
    public TweetReplyFragment() {

    }

    public static TweetReplyFragment newInstance(String tweet_id, String user_name, String screen_name, String profile_img_url) {
        TweetReplyFragment frag = new TweetReplyFragment();
        frag.setHasOptionsMenu(true);
        Bundle args = new Bundle();
        args.putString("tweet_id", tweet_id);
        args.putString("user_name", user_name);
        args.putString("screen_name", screen_name);
        args.putString("profile_img_url", profile_img_url);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.tweet_compose_fragment, container, false);

        final String tweet_id = getArguments().getString("tweet_id");
        Log.d("SUPRIYA", tweet_id);
        String user_name = getArguments().getString("user_name");
        String screen_name = getArguments().getString("screen_name");
        String profile_image_url = getArguments().getString("profile_img_url");

        client = TwitterApplication.getRestClient();
        TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        ImageView ivProfilePhoto = (ImageView) v.findViewById(R.id.ivProfileImage);
        TextView tvScreenName = (TextView) v.findViewById(R.id.tvUserScreenName);
        TextInputLayout compose = (TextInputLayout) v.findViewById(R.id.textCompose);
        Button submitTweet = (Button) v.findViewById(R.id.btnsubmit);
        final EditText etTweet = (EditText) v.findViewById(R.id.etTweet);
        etTweet.setText(screen_name);

        tvUserName.setText(user_name);
        tvScreenName.setText(screen_name);

        Glide.with(this)
                .load(profile_image_url)
                .into(ivProfilePhoto);

        submitTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reply = etTweet.getText().toString();
                postReply(reply, tweet_id);
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


    private void postReply(String reply, String tweet_id){
        client.postReply(reply, tweet_id, new JsonHttpResponseHandler() {
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





