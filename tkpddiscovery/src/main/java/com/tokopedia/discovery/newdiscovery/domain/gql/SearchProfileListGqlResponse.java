
package com.tokopedia.discovery.newdiscovery.domain.gql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchProfileListGqlResponse {

    @SerializedName("aceSearchProfile")
    @Expose
    private AceSearchProfile aceSearchProfile;

    public AceSearchProfile getAceSearchProfile() {
        return aceSearchProfile;
    }

    public void setAceSearchProfile(AceSearchProfile aceSearchProfile) {
        this.aceSearchProfile = aceSearchProfile;
    }

    public class AceSearchProfile {

        @SerializedName("query")
        @Expose
        private String query;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("count_text")
        @Expose
        private String countText;
        @SerializedName("has_next")
        @Expose
        private Boolean hasNext;
        @SerializedName("profiles")
        @Expose
        private List<Profile> profiles = null;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getCountText() {
            return countText;
        }

        public void setCountText(String countText) {
            this.countText = countText;
        }

        public Boolean getHasNext() {
            return hasNext;
        }

        public void setHasNext(Boolean hasNext) {
            this.hasNext = hasNext;
        }

        public List<Profile> getProfiles() {
            return profiles;
        }

        public void setProfiles(List<Profile> profiles) {
            this.profiles = profiles;
        }

    }

    public class Profile {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("followed")
        @Expose
        private Boolean followed;
        @SerializedName("post_count")
        @Expose
        private Integer postCount;
        @SerializedName("iskol")
        @Expose
        private Boolean iskol;
        @SerializedName("isaffiliate")
        @Expose
        private Boolean isaffiliate;
        @SerializedName("following")
        @Expose
        private Integer following;
        @SerializedName("followers")
        @Expose
        private Integer followers;
        @SerializedName("thumbnails")
        @Expose
        private Object[] thumbnails;

        public Object[] getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Object[] thumbnails) {
            this.thumbnails = thumbnails;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public Boolean getFollowed() {
            return followed;
        }

        public void setFollowed(Boolean followed) {
            this.followed = followed;
        }

        public Integer getPostCount() {
            return postCount;
        }

        public void setPostCount(Integer postCount) {
            this.postCount = postCount;
        }

        public Boolean getIskol() {
            return iskol;
        }

        public void setIskol(Boolean iskol) {
            this.iskol = iskol;
        }

        public Boolean getIsaffiliate() {
            return isaffiliate;
        }

        public void setIsaffiliate(Boolean isaffiliate) {
            this.isaffiliate = isaffiliate;
        }

        public Integer getFollowing() {
            return following;
        }

        public void setFollowing(Integer following) {
            this.following = following;
        }

        public Integer getFollowers() {
            return followers;
        }

        public void setFollowers(Integer followers) {
            this.followers = followers;
        }

    }
}


