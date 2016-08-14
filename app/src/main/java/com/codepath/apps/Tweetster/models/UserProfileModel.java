package com.codepath.apps.Tweetster.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

/**
 * Created by supriya on 8/13/16.
 */

@Parcel(analyze={UserProfileModel.class})
@Table(name = "User")
public class UserProfileModel extends Model {

    // This is the unique id given by the server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long id;
    @Column(name = "dummy_id")
    public long dummy_id;
    @Column(name = "Name")
    String name;
    @Column(name = "screen_name")
    String screen_name;
    @Column(name = "profile_image_url")
    String profile_image_url;
    @Column(name = "profile_banner_url")
    String profile_banner_url;
    @Column(name = "profile_background_color")
    String profile_background_color;
    @Column(name = "description")
    String description;
    @Column(name = "favourites_count")
    String favourites_count;
    @Column(name = "followers_count")
    String followers_count;
    @Column(name = "friends_count")
    String friends_count;

    public long getId1() {
        return id;
    }

    public long getDummy_id() {
        return dummy_id;
    }

    public String getName() {
        return name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public String getProfile_background_color() {
        return profile_background_color;
    }

    public String getDescription() {
        return description;
    }

    public String getFavourites_count() {
        return favourites_count;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public String getFriends_count() {
        return friends_count;
    }

    public UserProfileModel(){

    }

    public UserProfileModel(JSONObject object) {
        super();

        try {
            this.id =object.getLong("id");
            this.name = object.getString("name");
            this.profile_image_url =object.getString("profile_image_url");
            this.screen_name =object.getString("screen_name");
            this.profile_banner_url=object.has("profile_banner_url")?object.getString("profile_banner_url"):"";
            this.description=object.getString("description");
            this.followers_count=object.getString("followers_count");
            this.friends_count=object.getString("friends_count");
            this.favourites_count=object.getString("favourites_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static UserProfileModel fromJson(JSONObject userInfo) {

        UserProfileModel user = new UserProfileModel(userInfo);
        user.save();
        return user;
    }

    public static UserProfileModel fetchOfflineUserInfo(){
        List<UserProfileModel> user = new Select().from(UserProfileModel.class).execute();
        return user.get(0);

    }

//    public static UserProfileModel getCurrentUser() {
//        return new Select().from(UserProfileModel.class).where("dummy_id = 0").executeSingle();
//    }


}
