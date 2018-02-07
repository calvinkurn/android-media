package com.tokopedia.seller.shop.open.view.model;

import android.os.Parcel;
import android.text.TextUtils;

import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.Shipment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ShopOpenStepperModel implements StepperModel {
    private ResponseIsReserveDomain responseIsReserveDomain;

    /**
     * this is to convert string like "{"package_list":{"1":[1,2,3]}}" to our readable object.
     */
    public CourierServiceIdWrapper getSelectedCourierServices(){
        CourierServiceIdWrapper courierServiceIdWrapper = new CourierServiceIdWrapper();
        if (responseIsReserveDomain==null) {
            return courierServiceIdWrapper;
        }
        Shipment shipment = responseIsReserveDomain.getShipment();
        if (shipment == null) {
            return courierServiceIdWrapper;
        }
        JSONObject jsonObject = shipment.getPackageList();
        if (jsonObject == null) {
            return courierServiceIdWrapper;
        }
        try {
            Iterator<?> keys = jsonObject.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                List<String> courierServiceId = new ArrayList<>();
                for (int i = 0 ; i < jsonArray.length(); i++) {
                    courierServiceId.add(jsonArray.getString(i));
                }
                courierServiceIdWrapper.add(key, courierServiceId);
            }
            return courierServiceIdWrapper;
        } catch (JSONException e) {
            return new CourierServiceIdWrapper();
        }
    }

    public ShopOpenStepperModel() {
    }

    public ResponseIsReserveDomain getResponseIsReserveDomain() {
        return responseIsReserveDomain;
    }

    public void setResponseIsReserveDomain(ResponseIsReserveDomain responseIsReserveDomain) {
        this.responseIsReserveDomain = responseIsReserveDomain;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.responseIsReserveDomain, flags);
    }

    protected ShopOpenStepperModel(Parcel in) {
        this.responseIsReserveDomain = in.readParcelable(ResponseIsReserveDomain.class.getClassLoader());
    }

    public static final Creator<ShopOpenStepperModel> CREATOR = new Creator<ShopOpenStepperModel>() {
        @Override
        public ShopOpenStepperModel createFromParcel(Parcel source) {
            return new ShopOpenStepperModel(source);
        }

        @Override
        public ShopOpenStepperModel[] newArray(int size) {
            return new ShopOpenStepperModel[size];
        }
    };
}
