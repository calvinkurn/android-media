package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartSellerItemModel implements Parcelable {

    private String shopId;
    private String shopName;
    private List<CartItemModel> cartItemModels;

    private CourierItemData courierItemData;

    private String totalPriceFormatted;
    private double totalPricePlan;

    private String totalWeightFormatted;
    private double totalWeightPlan;

    private String totalItemFormatted;
    private int totalItemPlan;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<CartItemModel> getCartItemModels() {
        return cartItemModels;
    }

    public void setCartItemModels(List<CartItemModel> cartItemModels) {
        this.cartItemModels = cartItemModels;
    }

    public CourierItemData getCourierItemData() {
        return courierItemData;
    }

    public void setCourierItemData(CourierItemData courierItemData) {
        this.courierItemData = courierItemData;
    }

    public String getTotalPriceFormatted() {
        return totalPriceFormatted;
    }

    public void setTotalPriceFormatted(String totalPriceFormatted) {
        this.totalPriceFormatted = totalPriceFormatted;
    }

    public double getTotalPricePlan() {
        return totalPricePlan;
    }

    public void setTotalPricePlan(double totalPricePlan) {
        this.totalPricePlan = totalPricePlan;
    }

    public String getTotalWeightFormatted() {
        return totalWeightFormatted;
    }

    public void setTotalWeightFormatted(String totalWeightFormatted) {
        this.totalWeightFormatted = totalWeightFormatted;
    }

    public double getTotalWeightPlan() {
        return totalWeightPlan;
    }

    public void setTotalWeightPlan(double totalWeightPlan) {
        this.totalWeightPlan = totalWeightPlan;
    }

    public String getTotalItemFormatted() {
        return totalItemFormatted;
    }

    public void setTotalItemFormatted(String totalItemFormatted) {
        this.totalItemFormatted = totalItemFormatted;
    }

    public int getTotalItemPlan() {
        return totalItemPlan;
    }

    public void setTotalItemPlan(int totalItemPlan) {
        this.totalItemPlan = totalItemPlan;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeTypedList(this.cartItemModels);
        dest.writeParcelable(this.courierItemData, flags);
        dest.writeString(this.totalPriceFormatted);
        dest.writeDouble(this.totalPricePlan);
        dest.writeString(this.totalWeightFormatted);
        dest.writeDouble(this.totalWeightPlan);
        dest.writeString(this.totalItemFormatted);
        dest.writeInt(this.totalItemPlan);
    }

    public CartSellerItemModel() {
    }

    protected CartSellerItemModel(Parcel in) {
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.cartItemModels = in.createTypedArrayList(CartItemModel.CREATOR);
        this.courierItemData = in.readParcelable(CourierItemData.class.getClassLoader());
        this.totalPriceFormatted = in.readString();
        this.totalPricePlan = in.readDouble();
        this.totalWeightFormatted = in.readString();
        this.totalWeightPlan = in.readDouble();
        this.totalItemFormatted = in.readString();
        this.totalItemPlan = in.readInt();
    }

    public static final Creator<CartSellerItemModel> CREATOR = new Creator<CartSellerItemModel>() {
        @Override
        public CartSellerItemModel createFromParcel(Parcel source) {
            return new CartSellerItemModel(source);
        }

        @Override
        public CartSellerItemModel[] newArray(int size) {
            return new CartSellerItemModel[size];
        }
    };
}
