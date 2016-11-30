package com.tokopedia.seller.selling.model;

import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;

import org.parceler.Parcel;
import org.parceler.ParcelClass;

/**
 * Created by Erry on 7/19/2016.
 */

@Parcel
public class SellingStatusTxModel {
    public String UserName;
    public String RefNum;
    public String AvatarUrl;
    public String Deadline;
    public String Invoice;
    public OrderShippingList DataList;
    public String Komisi;
    public String BuyerId;
    public String LastStatus;
    public String DeadlineFinish;
    public String Pdf;
    public String PdfUri;
    public String OrderStatus;
    public String OrderId;
    public String Permission;
    public String ShippingID;
    public int isPickUp;

}
