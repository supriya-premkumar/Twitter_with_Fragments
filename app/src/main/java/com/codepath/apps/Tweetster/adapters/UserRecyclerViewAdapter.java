package com.codepath.apps.Tweetster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.models.UserProfileModel;

import java.util.List;

/**
 * Created by supriya on 8/14/16.
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView UserName;
        private TextView ScreenNames;
        private TextView description;

        public ImageView getProfileImage() {
            return profileImage;
        }

        public TextView getUserName() {
            return UserName;
        }

        public TextView getScreenNames() {
            return ScreenNames;
        }

        public TextView getDescription() {
            return description;
        }

        public ViewHolder1(View itemView) {
            super(itemView);

            profileImage = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            UserName = (TextView) itemView.findViewById(R.id.tvUserProfileName);
            ScreenNames = (TextView) itemView.findViewById(R.id.tvScreenProfileName);
            description = (TextView) itemView.findViewById(R.id.tvProfileText);

        }

    }


    private List<UserProfileModel> mTweets;
    private final int IMG_ARTICLE = 1, TXT_ARTICLE = 2;
    private Context mContext;

    public UserRecyclerViewAdapter(Context context, List<UserProfileModel> tweets) {
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

        View v1 = inflater.inflate(R.layout.user_item, parent, false);
        viewHolder = new ViewHolder1(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder1 vh1 = (ViewHolder1) holder;
        UserProfileModel user = (UserProfileModel) mTweets.get(position);
        vh1.getUserName().setText(user.getName());
        vh1.getScreenNames().setText(user.getScreen_name());
        vh1.getDescription().setText(user.getDescription());
        vh1.getProfileImage().setImageResource(0);

        String image = user.getProfile_image_url();
        if (!TextUtils.isEmpty(image)) {
            Glide.with(getmContext()).load(image).fitCenter().into(vh1.getProfileImage());
        }


    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
