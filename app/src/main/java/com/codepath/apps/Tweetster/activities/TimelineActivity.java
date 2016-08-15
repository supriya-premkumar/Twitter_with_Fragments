package com.codepath.apps.Tweetster.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.Tweetster.R;
import com.codepath.apps.Tweetster.fragments.HomeTimelineFragment;
import com.codepath.apps.Tweetster.fragments.MentionsTimelineFragment;
import com.codepath.apps.Tweetster.fragments.TweetComposeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends FragmentActivity implements DialogInterface.OnDismissListener {


    //    implements DialogInterface.OnDismissListener
    private HomeTimelineFragment homeTimelineFragment;
    private Toolbar toolbar;

    @BindView(R.id.fabCompose)
    FloatingActionButton fabCompose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeTweetFragment();
            }
        });

        //Get the viewpager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //set the viewpager adapter to the pager
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //Attach the tabstrip to the viewpager
        tabStrip.setViewPager(viewPager);
    }


    private void composeTweetFragment() {
        FragmentManager fm = getSupportFragmentManager();
        TweetComposeFragment filterSettingsFragment = TweetComposeFragment.newInstance("Filter Results");
        filterSettingsFragment.show(fm, "filter_settings_fragment");
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        homeTimelineFragment.populateTimeline(0);

    }


    // Return the order of the pager in the view fragment
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};

        //Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        //The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                homeTimelineFragment = new HomeTimelineFragment();
                return homeTimelineFragment;
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        //returns the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //how many fragments there are to swipe between?
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    public void onProfileView(View view) {
        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);
        Toast.makeText(TimelineActivity.this,
                "Toolbar icon clicked", Toast.LENGTH_SHORT).show();
    }

    public void onSearchView(View view) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
    }
}
