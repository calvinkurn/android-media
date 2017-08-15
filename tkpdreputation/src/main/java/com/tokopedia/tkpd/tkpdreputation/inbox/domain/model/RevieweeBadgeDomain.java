package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class RevieweeBadgeDomain {

    private String tooltip;
    private String reputationScore;
    private int score;
    private int minBadgeScore;
    private String reputationBadgeUrl;
    private ReputationBadgeDomain reputationBadge;

    public RevieweeBadgeDomain(String tooltip, String reputationScore, int score,
                               int minBadgeScore, String reputationBadgeUrl,
                               ReputationBadgeDomain reputationBadge) {
        this.tooltip = tooltip;
        this.reputationScore = reputationScore;
        this.score = score;
        this.minBadgeScore = minBadgeScore;
        this.reputationBadgeUrl = reputationBadgeUrl;
        this.reputationBadge = reputationBadge;
    }
}
