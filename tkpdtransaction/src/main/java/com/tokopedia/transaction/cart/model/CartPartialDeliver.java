package com.tokopedia.transaction.cart.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 11/21/16.
 */

public class CartPartialDeliver {

    private String code;
    private String info;

    public CartPartialDeliver(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return info;
    }

    public static List<CartPartialDeliver> createListForDeliverPartial() {
        List<CartPartialDeliver> list = new ArrayList<>();
        list.add(0, new CartPartialDeliver("1", "Kirimkan stok tersedia"));
        list.add(1, new CartPartialDeliver("0", "Batalkan pemesanan"));
        return list;
    }

    public static List<CartPartialDeliver> createListForCancelDeliverPartial() {
        List<CartPartialDeliver> list = new ArrayList<>();
        list.add(0, new CartPartialDeliver("0", "Batalkan pemesanan"));
        list.add(1, new CartPartialDeliver("1", "Kirimkan stok tersedia"));
        return list;
    }

}
