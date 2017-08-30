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
    private String reputationDaysLeft;
    private String invoice;
    private String reputationId;
    private ReputationDataViewModel reputationDataViewModel;
    private int role;

    public InboxReputationItemViewModel(String reputationId, String revieweeName, String createTime,
                                        String revieweePicture, String reputationDaysLeft, String invoice,
                                        ReputationDataViewModel reputationDataViewModel,
                                        int role) {
        this.reputationId = reputationId;
        this.revieweeName = revieweeName;
        this.createTime = createTime;
        this.revieweePicture = revieweePicture;
        this.reputationDaysLeft = reputationDaysLeft;
        this.invoice = invoice;
        this.reputationDataViewModel = reputationDataViewModel;
        this.role = role;
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


    public String getReputationDaysLeft() {
        return reputationDaysLeft;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getReputationId() {
        return reputationId;
    }

    public ReputationDataViewModel getReputationDataViewModel() {
        return reputationDataViewModel;
    }

    @Override
    public int type(InboxReputationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getRole() {
        return role;
    }
}
