package com.tokopedia.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 28/02/18.
 */

public class ProfileData {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("followers")
        @Expose
        private int followers;
        @SerializedName("following")
        @Expose
        private int following;
        @SerializedName("followed")
        @Expose
        private boolean followed;
        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("info")
        @Expose
        private String info;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("favorite")
        @Expose
        private int favorite;
        @SerializedName("iskol")
        @Expose
        private boolean iskol;
        @SerializedName("favorite_fmt")
        @Expose
        private String favoriteFmt;
        @SerializedName("followers_fmt")
        @Expose
        private String followersFmt;
        @SerializedName("following_fmt")
        @Expose
        private String followingFmt;
        @SerializedName("is_me")
        @Expose
        private boolean isMe;

        public int getFollowers() {
            return followers;
        }

        public void setFollowers(int followers) {
            this.followers = followers;
        }

        public int getFollowing() {
            return following;
        }

        public void setFollowing(int following) {
            this.following = following;
        }

        public boolean isFollowed() {
            return followed;
        }

        public void setFollowed(boolean followed) {
            this.followed = followed;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getFavorite() {
            return favorite;
        }

        public void setFavorite(int favorite) {
            this.favorite = favorite;
        }

        public boolean isIskol() {
            return iskol;
        }

        public void setIskol(boolean iskol) {
            this.iskol = iskol;
        }

        public String getFavoriteFmt() {
            return favoriteFmt;
        }

        public void setFavoriteFmt(String favoriteFmt) {
            this.favoriteFmt = favoriteFmt;
        }

        public String getFollowersFmt() {
            return followersFmt;
        }

        public void setFollowersFmt(String followersFmt) {
            this.followersFmt = followersFmt;
        }

        public String getFollowingFmt() {
            return followingFmt;
        }

        public void setFollowingFmt(String followingFmt) {
            this.followingFmt = followingFmt;
        }

        public boolean isMe() {
            return isMe;
        }

        public void setMe(boolean me) {
            isMe = me;
        }
    }
}
