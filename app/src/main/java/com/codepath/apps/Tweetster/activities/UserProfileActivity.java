package com.codepath.apps.Tweetster.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.TwitterApplication;
import com.codepath.apps.Tweetster.TwitterClient;
import com.codepath.apps.Tweetster.fragments.UserTimelineFragment;
import com.codepath.apps.Tweetster.models.UserProfileModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class UserProfileActivity extends AppCompatActivity {
    TwitterClient client;
    UserProfileModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String screenName = getIntent().getStringExtra("screen_name");

        if (!isOnline()) {
            Toast.makeText(this, "Network unavailable. Try again later.", Toast.LENGTH_LONG).show();
            user = UserProfileModel.fetchOfflineUserInfo();
            setProfileHeaderViewElements(user);

        } else {

            client = TwitterApplication.getRestClient();
            client.getUserInfo(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = UserProfileModel.fromJson(response);
                    setProfileHeaderViewElements(user);
                }
            });





            if (savedInstanceState == null) {
                //get the screenName for the activity that launches this
                // create the userTimeline Fragment
                UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
                //Display the user timeline fragment within this activity(dynamically)
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.viewpagerContainer, fragmentUserTimeline);
                fragmentTransaction.commit(); //changes the fragment
            }

        }
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

    public void setProfileHeaderViewElements(UserProfileModel user){
        getSupportActionBar().setTitle(user.getName());

        ImageView background = (ImageView) findViewById(R.id.ivHeader);
        if (user.getProfile_banner_url() != null)
            Glide.with(UserProfileActivity.this)
                    .load(user.getProfile_banner_url())
                    .bitmapTransform(new RoundedCornersTransformation(UserProfileActivity.this, 4, 1, RoundedCornersTransformation.CornerType.ALL))
                    .into(background);

        TextView Name = (TextView) findViewById(R.id.acTitle);
        TextView handle = (TextView) findViewById(R.id.acHandle);
        TextView desc = (TextView) findViewById(R.id.description);
        TextView following = (TextView) findViewById(R.id.numFollowing);
        final TextView followers = (TextView) findViewById(R.id.numFollowers);

        Name.setText(user.getName());
        handle.setText("@" + user.getScreen_name());
        desc.setText(user.getDescription());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        if (user.getFollowers_count() != null)
            followers.setText(formatter.format(Integer.parseInt(user.getFollowers_count())));
        if (user.getFriends_count() != null)
            following.setText(formatter.format(Integer.parseInt(user.getFriends_count())));


        ImageView UserImage = (ImageView) findViewById(R.id.UserImage);
        Glide.with(UserProfileActivity.this)
                .load(user.getProfile_image_url())
                .bitmapTransform(new RoundedCornersTransformation(UserProfileActivity.this, 4, 1, RoundedCornersTransformation.CornerType.ALL))
                .into(UserImage);

    }
}
