package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import javax.annotation.Nullable;

/**
 * @author by nisie on 11/3/17.
 */

public class KolRecommendationItemDomain {

    private final @Nullable
    String userName;

    private final @Nullable int userId;

    private final @Nullable String userPhoto;

    private final @Nullable boolean isFollowed;

    private final @Nullable String info;

    private final @Nullable  String url;

    public KolRecommendationItemDomain(String userName, int userId, String userPhoto,
                                       boolean isFollowed, String info, String url) {
        this.userName = userName;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.isFollowed = isFollowed;
        this.info = info;
        this.url = url;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    @Nullable
    public int getUserId() {
        return userId;
    }

    @Nullable
    public String getUserPhoto() {
        return userPhoto;
    }

    @Nullable
    public boolean isFollowed() {
        return isFollowed;
    }

    @Nullable
    public String getInfo() {
        return info;
    }

    @Nullable
    public String getUrl() {
        return url;
    }
}
