package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.InvoiceTime;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ReviewInboxDatum;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ShopData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.UserData;

import java.util.List;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailItemDomain {
    private int reputationId;
    @SerializedName("review_inbox_data")
    @Expose
    private List<ReviewInboxDatum> reviewInboxData = null;
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    @SerializedName("shop_data")
    @Expose
    private ShopData shopData;
    @SerializedName("invoice_ref_num")
    @Expose
    private String invoiceRefNum;
    @SerializedName("invoice_time")
    @Expose
    private InvoiceTime invoiceTime;
}
