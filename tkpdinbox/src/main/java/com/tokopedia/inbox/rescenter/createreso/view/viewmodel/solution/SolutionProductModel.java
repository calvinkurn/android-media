package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProductModel implements Parcelable {

    private String name;
    private int price;
    private SolutionProductImageModel image;

    public SolutionProductModel(String name, int price, SolutionProductImageModel image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public SolutionProductImageModel getImage() {
        return image;
    }

    public void setImage(SolutionProductImageModel image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.price);
        dest.writeParcelable(this.image, flags);
    }

    protected SolutionProductModel(Parcel in) {
        this.name = in.readString();
        this.price = in.readInt();
        this.image = in.readParcelable(SolutionProductImageModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<SolutionProductModel> CREATOR = new Parcelable.Creator<SolutionProductModel>() {
        @Override
        public SolutionProductModel createFromParcel(Parcel source) {
            return new SolutionProductModel(source);
        }

        @Override
        public SolutionProductModel[] newArray(int size) {
            return new SolutionProductModel[size];
        }
    };
}
