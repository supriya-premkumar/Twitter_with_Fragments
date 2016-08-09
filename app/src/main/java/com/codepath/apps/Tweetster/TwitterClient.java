package com.codepath.apps.Tweetster;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 *
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CONSUMER_KEY = "IxTyban0Uk0jGgXjNBcDzJDsZ";
    public static final String REST_CONSUMER_SECRET = "3DycMFsJq81KBFUQ6pCFZOGlRkDtAxV8OyT0C2xXMqJam577b9";
    public static final String REST_CALLBACK_URL = "oauth://CPTweetster"; // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }

    public void getHomeTimeline(long since_id, long max_id, int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        //specify the params

        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (page == 0) {
            Log.d("PAGINATIONMUDDU:", "Page: " + String.valueOf(page) + " since_id: " + String.valueOf(since_id) + " max_id: " + String.valueOf(max_id));
            params.put("since_id", since_id);

        } else {
            Log.d("PAGINATIONMUDDU:", "Page: " + String.valueOf(page) + " since_id: " + String.valueOf(since_id) + " max_id: " + String.valueOf(max_id));
            params.put("since_id", since_id);
            params.put("max_id", max_id);
        }

        //Execute the request
        getClient().get(apiUrl, params, handler);

    }

    //compose txt_tweet

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        getClient().post(apiUrl, params, handler);
    }

    public void postReply(String reply, String replyto_tweet_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", reply);
        params.put("in_reply_to_status_id", replyto_tweet_id);
        getClient().post(apiUrl, params, handler);

    }

}