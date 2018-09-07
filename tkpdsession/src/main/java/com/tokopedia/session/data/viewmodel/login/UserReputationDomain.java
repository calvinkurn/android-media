package com.tokopedia.session.data.viewmodel.login;

/**
 * @author by nisie on 5/26/17.
 */

public class UserReputationDomain {

    private final String positivePercentage;
    private final int noReputation;
    private final String negative;
    private final String positive;
    private final String neutral;

    public UserReputationDomain(String positivePercentage,
                                int noReputation,
                                String negative,
                                String positive,
                                String neutral) {
        this.positivePercentage = positivePercentage;
        this.noReputation = noReputation;
        this.negative = negative;
        this.positive = positive;
        this.neutral = neutral;
    }
}
