package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationItemViewModel {
    private String revieweeName;
    private String createTime;
    private String revieweePicture;
    private boolean showLockingDeadline;
    private String reputationDaysLeft;
    private String invoice;
    private boolean showBookmark;

    public InboxReputationItemViewModel(String revieweeName, String createTime,
                                        String revieweePicture, boolean showLockingDeadline,
                                        String reputationDaysLeft, String invoice,
                                        boolean showBookmark) {
        this.revieweeName = revieweeName;
        this.createTime = createTime;
        this.revieweePicture = revieweePicture;
        this.showLockingDeadline = showLockingDeadline;
        this.reputationDaysLeft = reputationDaysLeft;
        this.invoice = invoice;
        this.showBookmark = showBookmark;
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
}
