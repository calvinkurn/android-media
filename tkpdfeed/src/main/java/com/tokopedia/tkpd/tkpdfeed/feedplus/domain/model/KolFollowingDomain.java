package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingDomain {
    private final int id;
    private final String name;
    private final String avatarUrl;
    private final String profileApplink;
    private final boolean isInfluencer;

    public KolFollowingDomain(int id, String name, String avatarUrl, String profileApplink, boolean isInfluencer) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.profileApplink = profileApplink;
        this.isInfluencer = isInfluencer;
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

    public String getProfileApplink() {
        return profileApplink;
    }

    public boolean isInfluencer() {
        return isInfluencer;
    }
}
