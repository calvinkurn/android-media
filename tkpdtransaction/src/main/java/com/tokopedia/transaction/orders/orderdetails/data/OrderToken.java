package com.tokopedia.transaction.orders.orderdetails.data;

/**
 * Created by baghira on 11/05/18.
 */

public class OrderToken {
    private String label;
    private String value;
    private String QRCodeUrl;

    public OrderToken(String label, String value, String QRCodeUrl) {
        this.label = label;
        this.value = value;
        this.QRCodeUrl = QRCodeUrl;
    }

    @Override
    public String toString() {
        return "[OrderToken:{ "
                + "label="+label +" "
                + "value="+value +" "
                + "QRCodeUrl="+QRCodeUrl
                + "}]";
    }
}
