package com.tokopedia.inbox.rescenter.detailv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/20/17.
 */

public class DetailResCenterLastSolution {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("refundAmt")
    private int refundAmt;
    @SerializedName("refundAmtIdr")
    private String refundAmtIdr;
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("receivedFlag")
    private int receivedFlag;
    @SerializedName("troubleType")
    private TroubleType troubleType;
    @SerializedName("problemType")
    private ProblemType problemType;
    @SerializedName("actionByStr")
    private String actionByText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(int refundAmt) {
        this.refundAmt = refundAmt;
    }

    public String getRefundAmtIdr() {
        return refundAmtIdr;
    }

    public void setRefundAmtIdr(String refundAmtIdr) {
        this.refundAmtIdr = refundAmtIdr;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public int getReceivedFlag() {
        return receivedFlag;
    }

    public void setReceivedFlag(int receivedFlag) {
        this.receivedFlag = receivedFlag;
    }

    public TroubleType getTroubleType() {
        return troubleType;
    }

    public void setTroubleType(TroubleType troubleType) {
        this.troubleType = troubleType;
    }

    public ProblemType getProblemType() {
        return problemType;
    }

    public void setProblemType(ProblemType problemType) {
        this.problemType = problemType;
    }

    public String getActionByText() {
        return actionByText;
    }

    public void setActionByText(String actionByText) {
        this.actionByText = actionByText;
    }

    public static class TroubleType {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ProblemType {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
