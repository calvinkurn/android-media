package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */
public class LastData implements Parcelable {

    public static final Parcelable.Creator<LastData> CREATOR = new Parcelable.Creator<LastData>() {
        @Override
        public LastData createFromParcel(Parcel source) {
            return new LastData(source);
        }

        @Override
        public LastData[] newArray(int size) {
            return new LastData[size];
        }
    };
    private SellerAddressData sellerAddress;
    private UserAwbData userAwb;
    private LastSolutionData solution;
    private String problem;
    private String status;
    private List<ComplainedProductData> complainedProducts;

    public LastData(SellerAddressData sellerAddress, UserAwbData userAwb, LastSolutionData solution, String problem, String status, List<ComplainedProductData> complainedProducts) {
        this.sellerAddress = sellerAddress;
        this.userAwb = userAwb;
        this.solution = solution;
        this.problem = problem;
        this.status = status;
        this.complainedProducts = complainedProducts;

    }

    protected LastData(Parcel in) {
        this.sellerAddress = in.readParcelable(SellerAddressData.class.getClassLoader());
        this.userAwb = in.readParcelable(UserAwbData.class.getClassLoader());
        this.solution = in.readParcelable(LastSolutionData.class.getClassLoader());
        this.problem = in.readString();
        this.status = in.readString();
        this.complainedProducts = in.createTypedArrayList(ComplainedProductData.CREATOR);
    }

    public SellerAddressData getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(SellerAddressData sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public UserAwbData getUserAwb() {
        return userAwb;
    }

    public void setUserAwb(UserAwbData userAwb) {
        this.userAwb = userAwb;
    }

    public LastSolutionData getSolution() {
        return solution;
    }

    public void setSolution(LastSolutionData solution) {
        this.solution = solution;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ComplainedProductData> getComplainedProducts() {
        return complainedProducts;
    }

    public void setComplainedProducts(List<ComplainedProductData> complainedProducts) {
        this.complainedProducts = complainedProducts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.sellerAddress, flags);
        dest.writeParcelable(this.userAwb, flags);
        dest.writeParcelable(this.solution, flags);
        dest.writeString(this.problem);
        dest.writeString(this.status);
        dest.writeTypedList(this.complainedProducts);
    }
}
