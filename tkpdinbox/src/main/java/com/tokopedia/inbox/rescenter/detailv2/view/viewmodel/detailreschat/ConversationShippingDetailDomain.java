package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationShippingDetailDomain {

    private String awbNumber;
    private int id;
    private String name;

    public ConversationShippingDetailDomain(String awbNumber, int id, String name) {
        this.awbNumber = awbNumber;
        this.id = id;
        this.name = name;
    }

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
