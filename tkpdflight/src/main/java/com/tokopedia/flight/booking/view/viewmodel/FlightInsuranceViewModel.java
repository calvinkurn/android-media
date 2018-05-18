package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FlightInsuranceViewModel implements Parcelable{
    private String id;
    private String name;
    private String description;
    private long totalPriceNumeric;
    private boolean defaultChecked;
    private String tncAggreement;
    private String tncUrl;
    private List<FlightInsuranceBenefitViewModel> benefits;

    public FlightInsuranceViewModel() {
    }

    protected FlightInsuranceViewModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        totalPriceNumeric = in.readLong();
        defaultChecked = in.readByte() != 0;
        tncAggreement = in.readString();
        tncUrl = in.readString();
        benefits = in.createTypedArrayList(FlightInsuranceBenefitViewModel.CREATOR);
    }

    public static final Creator<FlightInsuranceViewModel> CREATOR = new Creator<FlightInsuranceViewModel>() {
        @Override
        public FlightInsuranceViewModel createFromParcel(Parcel in) {
            return new FlightInsuranceViewModel(in);
        }

        @Override
        public FlightInsuranceViewModel[] newArray(int size) {
            return new FlightInsuranceViewModel[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTotalPriceNumeric() {
        return totalPriceNumeric;
    }

    public void setTotalPriceNumeric(long totalPriceNumeric) {
        this.totalPriceNumeric = totalPriceNumeric;
    }

    public boolean isDefaultChecked() {
        return defaultChecked;
    }

    public void setDefaultChecked(boolean defaultChecked) {
        this.defaultChecked = defaultChecked;
    }

    public String getTncAggreement() {
        return tncAggreement;
    }

    public void setTncAggreement(String tncAggreement) {
        this.tncAggreement = tncAggreement;
    }

    public String getTncUrl() {
        return tncUrl;
    }

    public void setTncUrl(String tncUrl) {
        this.tncUrl = tncUrl;
    }

    public List<FlightInsuranceBenefitViewModel> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<FlightInsuranceBenefitViewModel> benefits) {
        this.benefits = benefits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeLong(totalPriceNumeric);
        parcel.writeByte((byte) (defaultChecked ? 1 : 0));
        parcel.writeString(tncAggreement);
        parcel.writeString(tncUrl);
        parcel.writeTypedList(benefits);
    }
}
