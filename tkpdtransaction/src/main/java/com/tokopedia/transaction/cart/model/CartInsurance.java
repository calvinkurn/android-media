package com.tokopedia.transaction.cart.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 11/16/16.
 */

public class CartInsurance {
    private String code;
    private String info;

    public CartInsurance(String code, String info) {
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

    public static List<CartInsurance> createListForAdapter() {
        List<CartInsurance> list = new ArrayList<>();
        list.add(0, new CartInsurance("0", "Tidak"));
        list.add(1, new CartInsurance("1", "Ya"));
        return list;
    }

    public static int getIndexSelection(List<CartInsurance> cartInsuranceList,
                                        CartInsurance selected) {
        for (int i = 0, cartInsuranceListSize = cartInsuranceList.size();
             i < cartInsuranceListSize; i++) {
            CartInsurance data = cartInsuranceList.get(i);
            if (selected.getCode().equals(data.getCode())) return i;
        }
        return 0;
    }
}
