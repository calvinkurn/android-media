package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

/**
 * @author by nisie on 11/8/17.
 */

public class KolCommentUserDomain {

    private final int id;
    private final boolean isKol;
    private final String photo;
    private final String name;

    public KolCommentUserDomain(int id, boolean iskol, String name, String photo) {
        this.id = id;
        this.isKol = iskol;
        this.name = name;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public boolean isKol() {
        return isKol;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }
}
