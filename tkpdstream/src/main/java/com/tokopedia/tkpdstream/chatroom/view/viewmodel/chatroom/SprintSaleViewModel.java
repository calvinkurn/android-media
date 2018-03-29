package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author by nisie on 3/26/18.
 */

public class SprintSaleViewModel implements Parcelable {

    public static final String TYPE_UPCOMING = "flashsale_upcoming";
    public static final String TYPE_ACTIVE = "flashsale_start";
    public static final String TYPE_FINISHED = "flashsale_end";

    private ArrayList<SprintSaleProductViewModel> listProduct;
    private String campaignName;
    private long startDate;
    private long endDate;
    private String redirectUrl;
    private String formattedStartDate;
    private String formattedEndDate;
    private String sprintSaleType;

    public SprintSaleViewModel(ArrayList<SprintSaleProductViewModel> listProduct,
                               String campaignName, long startDate, long endDate, String
                                       redirectUrl, String sprintSaleType) {
        Locale localeID = new Locale("in", "ID");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
        this.listProduct = listProduct;
        this.campaignName = campaignName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.redirectUrl = redirectUrl;
        this.formattedStartDate = sdfHour.format(startDate);
        this.formattedEndDate = sdfHour.format(endDate);
        this.sprintSaleType = sprintSaleType;
    }


    protected SprintSaleViewModel(Parcel in) {
        listProduct = in.createTypedArrayList(SprintSaleProductViewModel.CREATOR);
        campaignName = in.readString();
        startDate = in.readLong();
        endDate = in.readLong();
        redirectUrl = in.readString();
        formattedStartDate = in.readString();
        formattedEndDate = in.readString();
        sprintSaleType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listProduct);
        dest.writeString(campaignName);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeString(redirectUrl);
        dest.writeString(formattedStartDate);
        dest.writeString(formattedEndDate);
        dest.writeString(sprintSaleType);
    }

    @Override
    public int describeContents() {
        return 0;
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
        Locale localeID = new Locale("in", "ID");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
        this.startDate = startDate;
        this.formattedStartDate = sdfHour.format(startDate);
    }

    public void setEndDate(long endDate) {
        Locale localeID = new Locale("in", "ID");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
        this.endDate = endDate;
        this.formattedEndDate = sdfHour.format(endDate);
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getFormattedStartDate() {
        return formattedStartDate;
    }

    public String getFormattedEndDate() {
        return formattedEndDate;
    }

    public void setSprintSaleType(String sprintSaleType) {
        this.sprintSaleType = sprintSaleType;
    }

    public String getSprintSaleType() {
        return sprintSaleType;
    }
}
