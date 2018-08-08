package com.tokopedia.session.data.viewmodel.login;

/**
 * @author by nisie on 5/26/17.
 */

public class ShopReputationDomain {

    private final String tooltip;
    private final ReputationBadgeDomain reputationBadge;
    private final String reputationScore;
    private final int minBadgeScore;
    private final int score;

    public ShopReputationDomain(String tooltip,
                                ReputationBadgeDomain reputationBadge,
                                String reputationScore,
                                int minBadgeScore,
                                int score) {
        this.tooltip = tooltip;
        this.reputationBadge = reputationBadge;
        this.reputationScore = reputationScore;
        this.minBadgeScore = minBadgeScore;
        this.score = score;
    }
}
