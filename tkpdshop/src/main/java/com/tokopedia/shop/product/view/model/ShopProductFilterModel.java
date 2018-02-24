package com.tokopedia.shop.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

public class ShopProductFilterModel extends ShopProductViewModel {
    String name;
    String key;
    String value;
    String inputType;

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return The inputType
     */
    public String getInputType() {
        return inputType;
    }

    /**
     * @param inputType The input_type
     */
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }


    @Override
    public String toString() {
        return getName();
    }
}
