package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class UserDataDomain {

    private int userId;
    private String fullName;
    private String userEmail;
    private int userStatus;
    private String userUrl;
    private String userLabel;
    private String userProfilePict;
    private UserReputationDomain userReputation;

    public UserDataDomain(int userId, String fullName, String userEmail,
                          int userStatus, String userUrl, String userLabel,
                          String userProfilePict, UserReputationDomain userReputation) {
        this.userId = userId;
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.userStatus = userStatus;
        this.userUrl = userUrl;
        this.userLabel = userLabel;
        this.userProfilePict = userProfilePict;
        this.userReputation = userReputation;
    }
}
