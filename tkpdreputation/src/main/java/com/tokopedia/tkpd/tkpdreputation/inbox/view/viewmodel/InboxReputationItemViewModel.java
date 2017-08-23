package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationItemViewModel implements Visitable<InboxReputationTypeFactory> {
    private String revieweeName;
    private String createTime;
    private String revieweePicture;
    private boolean showLockingDeadline;
    private String reputationDaysLeft;
    private String invoice;
    private boolean showBookmark;
    private String reputationId;
    private String actionMessage;

    public InboxReputationItemViewModel(String reputationId, String revieweeName, String createTime,
                                        String revieweePicture, boolean showLockingDeadline,
                                        String reputationDaysLeft, String invoice,
                                        boolean showBookmark, String actionMessage) {
        this.reputationId = reputationId;
        this.revieweeName = revieweeName;
        this.createTime = createTime;
        this.revieweePicture = revieweePicture;
        this.showLockingDeadline = showLockingDeadline;
        this.reputationDaysLeft = reputationDaysLeft;
        this.invoice = invoice;
        this.showBookmark = showBookmark;
        this.actionMessage = actionMessage;
    }

    public String getRevieweeName() {
        return revieweeName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getRevieweePicture() {
        return revieweePicture;
    }

    public boolean showLockingDeadline() {
        return showLockingDeadline;
    }

    public String getReputationDaysLeft() {
        return reputationDaysLeft;
    }

    public String getInvoice() {
        return invoice;
    }

    public boolean showBookmark() {
        return showBookmark;
    }

    public String getReputationId() {
        return reputationId;
    }

    public boolean isShowLockingDeadline() {
        return showLockingDeadline;
    }

    public boolean isShowBookmark() {
        return showBookmark;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    @Override
    public int type(InboxReputationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
