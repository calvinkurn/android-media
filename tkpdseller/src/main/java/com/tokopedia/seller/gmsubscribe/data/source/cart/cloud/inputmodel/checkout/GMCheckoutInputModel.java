package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.inputmodel.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMCheckoutInputModel {
    public static final int UNDEFINED_AUTO_EXTEND = -1;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("promocode")
    private String promocode;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public static GMCheckoutInputModel getBodyModel(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        GMCheckoutInputModel inputModel = new GMCheckoutInputModel();
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setProductId(selectedProduct);
        item.setQty(1);
        if(autoExtendSelectedProduct != UNDEFINED_AUTO_EXTEND) {
            Map<String, String> map = new HashMap<>();
            map.put(Item.AUTOEXTEND, String.valueOf(autoExtendSelectedProduct));
            item.setConfiguration(map);
        }
        items.add(item);
        inputModel.setItems(items);
        inputModel.setPromocode(voucherCode);
        return inputModel;
    }
}
