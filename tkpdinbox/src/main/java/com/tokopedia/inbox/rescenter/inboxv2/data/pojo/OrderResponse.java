package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 24/01/18.
 */
public class OrderResponse {
    /**
     * awbNumber : JNE20171024001
     * url : https://www.tokopedia.com/invoice.pl?id=111813167&pdf=Invoice-13982149-1154650-20171024152035-WVlKVUNaRUQ.pdf
     * refNum : INV/20171024/XVII/X/111834532
     */

    @SerializedName("awbNumber")
    private String awbNumber;
    @SerializedName("url")
    private String url;
    @SerializedName("refNum")
    private String refNum;

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }
}
