package com.codepath.apps.Tweetster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.activities.DetailedTweetActivity;
import com.codepath.apps.Tweetster.activities.UserProfileActivity;
import com.codepath.apps.Tweetster.fragments.TweetReplyFragment;
import com.codepath.apps.Tweetster.models.TweetModel;

import org.parceler.Parcels;

import java.util.List;

public class TweetsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        public ImageView ivProfilePhoto;
        public TextView tvUserName;
        public TextView tvName;
        public TextView tvBody;
        public TextView tvTimeStamp;
        public ImageView ivPoster;
        public ImageView ivReply;
        public ImageView ivRetweet;
        public ImageView ivStar;

        public ImageView getIvStar() {
            return ivStar;
        }

        public void setIvStar(ImageView ivStar) {
            this.ivStar = ivStar;
        }

        public ImageView getIvRetweet() {
            return ivRetweet;
        }

        public void setIvRetweet(ImageView ivRetweet) {
            this.ivRetweet = ivRetweet;
        }

        public VideoView vvVideo;


        public ImageView getIvPoster() {
            return ivPoster;
        }

        public ImageView getIvReply() {
            return ivReply;
        }

        public TextView getTvTimeStamp() {
            return tvTimeStamp;
        }


        public void setTvName(TextView tvName) {
            this.tvName = tvName;
        }

        public TextView getTvName() {
            return tvName;
        }


        public void setTvBody(TextView tvBody) {
            this.tvBody = tvBody;
        }

        public void setIvProfilePhoto(ImageView ivProfilePhoto) {
            this.ivProfilePhoto = ivProfilePhoto;
        }

        public void setTvUserName(TextView tvUserName) {
            this.tvUserName = tvUserName;
        }


        public TextView getTvUserName() {
            return tvUserName;
        }

        public ImageView getIvProfilePhoto() {
            return ivProfilePhoto;
        }

        public TextView getTvBody() {
            return tvBody;
        }


        public ViewHolder1(View itemView) {
            super(itemView);

            ivProfilePhoto = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserProfileName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            ivPoster = (ImageView) itemView.findViewById(R.id.ivMedia);
            ivReply = (ImageView) itemView.findViewById(R.id.ivReply);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);
            ivStar = (ImageView) itemView.findViewById(R.id.ivStar);
        }

    }

    private List<TweetModel> mTweets;
    private Context mContext;

    public TweetsRecyclerViewAdapter(Context context, List<TweetModel> tweets) {
        this.mTweets = tweets;
        this.mContext = context;
    }

    private Context getmContext() {
        return mContext;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v1 = inflater.inflate(R.layout.txt_tweet, parent, false);
        viewHolder = new ViewHolder1(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        ViewHolder1 vh1 = (ViewHolder1) viewHolder;
        vh1.getTvBody().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetailedTweetActivity(position);
            }
        });

        vh1.ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProfileActivity(position);
            }
        });

        vh1.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyToTweet(position);
            }
        });
        configureViewHolder(vh1, position);
    }

    private void startDetailedTweetActivity(int position) {
        Intent intent = new Intent(getmContext(), DetailedTweetActivity.class);
        Object tweets = mTweets.get(position);
        intent.putExtra("txt_tweet", Parcels.wrap((TweetModel) tweets));
        getmContext().startActivity(intent);
    }

    private void startProfileActivity(int position) {
        Intent intent = new Intent(getmContext(), UserProfileActivity.class);
        Object tweets = mTweets.get(position);
        intent.putExtra("screen_name", ((TweetModel) tweets).getScreenName() );
        getmContext().startActivity(intent);
    }

    private void configureViewHolder(ViewHolder1 vh1, int position) {
        TweetModel tweet = (TweetModel) mTweets.get(position);
        if (tweet != null) {
            vh1.getTvBody().setText(tweet.getTweet());
            vh1.getTvName().setText(tweet.getUserName());
            vh1.getTvUserName().setText(tweet.getScreenName());
            vh1.getIvProfilePhoto().setImageResource(0);
            vh1.getTvTimeStamp().setText(tweet.getTimeStamp());

            if (tweet.getMediaUrl() != null) {
                vh1.ivPoster.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(tweet.getMediaUrl()).into(vh1.ivPoster);
            } else {
                vh1.ivPoster.setVisibility(View.GONE);
            }


            String thumbnail = tweet.getProfileImageUrl();
            if (!TextUtils.isEmpty(thumbnail)) {
                Glide.with(getmContext()).load(thumbnail).fitCenter().into(vh1.getIvProfilePhoto());
            }
        }
    }

    private void replyToTweet(int position) {
        TweetModel tweet = mTweets.get(position);
        FragmentManager fm = ((FragmentActivity)getmContext()).getSupportFragmentManager();
        TweetReplyFragment replyFragment = TweetReplyFragment.newInstance(tweet.getTweetId(),tweet.getUserName(), tweet.getScreenName(), tweet.getProfileImageUrl());
        replyFragment.show(fm, "filter_settings_fragment");

    }


    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}