package com.codepath.apps.Tweetster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.TwitterApplication;
import com.codepath.apps.Tweetster.TwitterClient;
import com.codepath.apps.Tweetster.adapters.UserRecyclerViewAdapter;
import com.codepath.apps.Tweetster.models.UserProfileModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by supriya on 8/13/16.
 */
public class UsersListActivity extends AppCompatActivity {
    ArrayList<UserProfileModel> mFollow;
    UserRecyclerViewAdapter userAdapter;
    RecyclerView rvUsers;
    Long userId;
    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        userId = getIntent().getLongExtra("user_id", 0);
        getSupportActionBar().setTitle(getIntent().getStringExtra("users_type"));

        if (getIntent().getStringExtra("users_type").equals("Followers")) {
            fetchAllFollowers();
        }
        else if (getIntent().getStringExtra("users_type").equals("Following")) {
            fetchAllFollowing();
        }

        mFollow = new ArrayList<>();
        userAdapter = new UserRecyclerViewAdapter(this, mFollow);
        rvUsers = (RecyclerView) findViewById(R.id.rvUsersList);
        rvUsers.setAdapter(userAdapter);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvUsers.setLayoutManager(gridLayoutManager);
    }

    public void fetchAllFollowers() {
        TwitterApplication.getRestClient().getUserFollowing(userId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                try {
                    mFollow.addAll(UserProfileModel.fromJsonArray(response.getJSONArray("users")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userAdapter.notifyDataSetChanged();

            }

        });
        }

    public void fetchAllFollowing(){
        TwitterApplication.getRestClient().getUserFollowing(userId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.i("DEBUG", "timeline: " + response.toString());
                try {
                    mFollow.addAll(UserProfileModel.fromJsonArray(response.getJSONArray("users")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userAdapter.notifyDataSetChanged();
                // Try to get more tweets only if we got something in this try
                // This is to ensure no infinite loop

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                android.util.Log.e("tweets error", responseString);
                Toast.makeText(UsersListActivity.this, "Failed to get tweets", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                android.util.Log.e("tweets error", errorResponse.toString());
                Toast.makeText(UsersListActivity.this, "Failed to get tweets", Toast.LENGTH_SHORT).show();
            }
        });
    }

    }

