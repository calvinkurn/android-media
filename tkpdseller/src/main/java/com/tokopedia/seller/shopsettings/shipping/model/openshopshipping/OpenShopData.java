
package com.tokopedia.seller.shopsettings.shipping.model.openshopshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.Courier;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ProvinceCitiesDistrict;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ShopShipping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenShopData implements Parcelable {

    public static final Parcelable.Creator<OpenShopData> CREATOR = new Parcelable.Creator<OpenShopData>() {
        @Override
        public OpenShopData createFromParcel(Parcel source) {
            return new OpenShopData(source);
        }

        @Override
        public OpenShopData[] newArray(int size) {
            return new OpenShopData[size];
        }
    };
    @SerializedName("courier")
    @Expose
    public List<Courier> courier = new ArrayList<>();
    @SerializedName("token")
    @Expose
    private Token token;
    @SerializedName("payment_options")
    @Expose
    private List<PaymentOption> paymentOptions = new ArrayList<PaymentOption>();
    @SerializedName("cannot_create")
    @Expose
    private CannotCreate cannotCreate;
    @SerializedName("provinces_cities_districts")
    @Expose
    private List<ProvinceCitiesDistrict> provincesCitiesDistricts = new ArrayList<>();
    private ShopShipping shopShipping = new ShopShipping();
    private HashMap<String, String> openShopHashMap = new HashMap<>();

    public OpenShopData() {
    }

    protected OpenShopData(Parcel in) {
        token = in.readParcelable(Token.class.getClassLoader());
        this.paymentOptions = new ArrayList<PaymentOption>();
        in.readList(this.paymentOptions, PaymentOption.class.getClassLoader());
        this.cannotCreate = in.readParcelable(CannotCreate.class.getClassLoader());
        this.provincesCitiesDistricts = in.createTypedArrayList(ProvinceCitiesDistrict.CREATOR);
        this.courier = in.createTypedArrayList(Courier.CREATOR);
        this.shopShipping = in.readParcelable(ShopShipping.class.getClassLoader());
        this.openShopHashMap = (HashMap<String, String>) in.readSerializable();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    /**
     *
     * @return
     *     The paymentOptions
     */
    public List<PaymentOption> getPaymentOptions() {
        return paymentOptions;
    }

    /**
     *
     * @param paymentOptions
     *     The payment_options
     */
    public void setPaymentOptions(List<PaymentOption> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    /**
     *
     * @return
     *     The cannotCreate
     */
    public CannotCreate getCannotCreate() {
        return cannotCreate;
    }

    /**
     *
     * @param cannotCreate
     *     The cannot_create
     */
    public void setCannotCreate(CannotCreate cannotCreate) {
        this.cannotCreate = cannotCreate;
    }

    /**
     *
     * @return
     *     The provincesCitiesDistricts
     */
    public List<ProvinceCitiesDistrict> getProvincesCitiesDistricts() {
        return provincesCitiesDistricts;
    }

    /**
     *
     * @param provincesCitiesDistricts
     *     The provinces_cities_districts
     */
    public void setProvinceCitiesDistricts(List<ProvinceCitiesDistrict> provincesCitiesDistricts) {
        this.provincesCitiesDistricts = provincesCitiesDistricts;
    }

    /**
     *
     * @return
     *     The shipment
     */
    public List<Courier> getShipment() {
        return courier;
    }

    /**
     * @param shipment The shipment
     */
    public void setShipment(List<Courier> shipment) {
        this.courier = shipment;
    }

    public ShopShipping getShopShipping() {
        return shopShipping;
    }

    public void setShopShipping(ShopShipping shopShipping) {
        this.shopShipping = shopShipping;
    }

    public HashMap<String, String> getOpenShopHashMap() {
        return openShopHashMap;
    }

    public void setOpenShopHashMap(HashMap<String, String> openShopHashMap) {
        this.openShopHashMap = openShopHashMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(token, flags);
        dest.writeList(this.paymentOptions);
        dest.writeParcelable(this.cannotCreate, flags);
        dest.writeTypedList(this.provincesCitiesDistricts);
        dest.writeTypedList(this.courier);
        dest.writeParcelable(this.shopShipping, flags);
        dest.writeSerializable(this.openShopHashMap);
    }
}
