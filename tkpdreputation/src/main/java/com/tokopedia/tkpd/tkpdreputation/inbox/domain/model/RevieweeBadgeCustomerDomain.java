package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/16/17.
 */

public class RevieweeBadgeCustomerDomain {

    private String positive;
    private String neutral;
    private String negative;
    private String positive_percentage;
    private String no_reputation;

    public RevieweeBadgeCustomerDomain(String positive, String neutral, String negative, String positive_percentage, String no_reputation) {
        this.positive = positive;
        this.neutral = neutral;
        this.negative = negative;
        this.positive_percentage = positive_percentage;
        this.no_reputation = no_reputation;
    }

    public String getPositive() {
        return positive;
    }

    public String getNeutral() {
        return neutral;
    }

    public String getNegative() {
        return negative;
    }

    public String getPositive_percentage() {
        return positive_percentage;
    }

    public String getNo_reputation() {
        return no_reputation;
    }
}
