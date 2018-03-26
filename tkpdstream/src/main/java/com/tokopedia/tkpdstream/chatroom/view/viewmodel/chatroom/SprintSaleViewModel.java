package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author by nisie on 3/26/18.
 */

public class SprintSaleViewModel implements Parcelable{


    private ArrayList<SprintSaleProductViewModel> listProduct;
    private String campaignName;
    private long startDate;
    private long endDate;
    private String redirectUrl;

    public SprintSaleViewModel(ArrayList<SprintSaleProductViewModel> listProduct,
                               String campaignName, long startDate, long endDate, String redirectUrl) {
        this.listProduct = listProduct;
        this.campaignName = campaignName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.redirectUrl = redirectUrl;
    }

    public ArrayList<SprintSaleProductViewModel> getListProduct() {
        return listProduct;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setListProduct(ArrayList<SprintSaleProductViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listProduct);
        dest.writeString(campaignName);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeString(redirectUrl);
    }


    protected SprintSaleViewModel(Parcel in) {
        listProduct = in.createTypedArrayList(SprintSaleProductViewModel.CREATOR);
        campaignName = in.readString();
        startDate = in.readLong();
        endDate = in.readLong();
        redirectUrl = in.readString();
    }

    public static final Creator<SprintSaleViewModel> CREATOR = new Creator<SprintSaleViewModel>() {
        @Override
        public SprintSaleViewModel createFromParcel(Parcel in) {
            return new SprintSaleViewModel(in);
        }

        @Override
        public SprintSaleViewModel[] newArray(int size) {
            return new SprintSaleViewModel[size];
        }
    };
}
