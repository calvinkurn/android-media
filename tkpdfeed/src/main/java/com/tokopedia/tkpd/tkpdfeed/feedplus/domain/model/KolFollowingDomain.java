package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingDomain {
    private final int id;
    private final String name;
    private final String avatarUrl;
    private final boolean isVerified;

    public KolFollowingDomain(int id, String name, String avatarUrl, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.isVerified = isVerified;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public boolean isVerified() {
        return isVerified;
    }
}
