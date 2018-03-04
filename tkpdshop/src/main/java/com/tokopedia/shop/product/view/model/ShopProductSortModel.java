package com.tokopedia.shop.product.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.product.view.adapter.ShopProductSortAdapterTypeFactory;

/**
 * Created by normansyahputa on 2/24/18.
 */

public class ShopProductSortModel implements Visitable<ShopProductSortAdapterTypeFactory> {
    String name;
    String key;
    String value;
    String inputType;
    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

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

    @Override
    public int type(ShopProductSortAdapterTypeFactory shopProductFilterAdapterTypeFactory) {
        return shopProductFilterAdapterTypeFactory.type(this);
    }
}
