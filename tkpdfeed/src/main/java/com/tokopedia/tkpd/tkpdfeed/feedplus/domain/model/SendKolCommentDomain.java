package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.KolCommentUserDomain;

/**
 * @author by nisie on 11/3/17.
 */

public class SendKolCommentDomain {

    private final int id;
    private final String comment;
    private final String time;
    private final KolCommentUserDomain domainUser;

    public SendKolCommentDomain(int id, String comment, String time, KolCommentUserDomain domainUser) {
        this.id = id;
        this.comment = comment;
        this.time = time;
        this.domainUser = domainUser;
    }

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public KolCommentUserDomain getDomainUser() {
        return domainUser;
    }
}
