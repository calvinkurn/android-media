package com.tokopedia.seller.product.view.model.upload;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductPhotoListViewModel implements Parcelable {
    private List<ImageProductInputViewModel> photos;
    private int productDefaultPicture;

    public ProductPhotoListViewModel() {
        photos = new ArrayList<>();
    }

    public List<ImageProductInputViewModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageProductInputViewModel> photos) {
        this.photos = photos;
    }

    public int getProductDefaultPicture() {
        return productDefaultPicture;
    }

    public void setProductDefaultPicture(int productDefaultPicture) {
        this.productDefaultPicture = productDefaultPicture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.photos);
        dest.writeInt(this.productDefaultPicture);
    }

    protected ProductPhotoListViewModel(Parcel in) {
        this.photos = new ArrayList<ImageProductInputViewModel>();
        in.readList(this.photos, ImageProductInputViewModel.class.getClassLoader());
        this.productDefaultPicture = in.readInt();
    }

    public static final Parcelable.Creator<ProductPhotoListViewModel> CREATOR = new Parcelable.Creator<ProductPhotoListViewModel>() {
        @Override
        public ProductPhotoListViewModel createFromParcel(Parcel source) {
            return new ProductPhotoListViewModel(source);
        }

        @Override
        public ProductPhotoListViewModel[] newArray(int size) {
            return new ProductPhotoListViewModel[size];
        }
    };
}
