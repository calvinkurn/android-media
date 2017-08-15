package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class DebugInboxReputationDomain {
    private int inboxID;
    private int reputationID;
    private int userID;
    private int shopID;
    private int role;
    private String invoiceRefNum;
    private int status;
    private int readStatus;
    private String searchKeyword;
    private int createBy;
    private String createTime;
    private UpdateByDomain updateBy;
    private UpdatetimeDomain updatetime;
    private ReputationDataDomain reputationData;

    public DebugInboxReputationDomain(int inboxID, int reputationID, int userID,
                                      int shopID, int role, String invoiceRefNum,
                                      int status, int readStatus, String searchKeyword,
                                      int createBy, String createTime, UpdateByDomain updateBy,
                                      UpdatetimeDomain updatetime,
                                      ReputationDataDomain reputationData) {
        this.inboxID = inboxID;
        this.reputationID = reputationID;
        this.userID = userID;
        this.shopID = shopID;
        this.role = role;
        this.invoiceRefNum = invoiceRefNum;
        this.status = status;
        this.readStatus = readStatus;
        this.searchKeyword = searchKeyword;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updatetime = updatetime;
        this.reputationData = reputationData;
    }
}
