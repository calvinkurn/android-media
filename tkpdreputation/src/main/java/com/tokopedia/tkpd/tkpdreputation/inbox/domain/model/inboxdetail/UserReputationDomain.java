package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class UserReputationDomain {
    private int positive;
    private int neutral;
    private int negative;
    private String positivePercentage;
    private int noReputation;

    public UserReputationDomain(int positive, int neutral, int negative,
                                String positivePercentage, int noReputation) {
        this.positive = positive;
        this.neutral = neutral;
        this.negative = negative;
        this.positivePercentage = positivePercentage;
        this.noReputation = noReputation;
    }
}
