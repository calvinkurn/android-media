package com.tokopedia.seller.selling.view.fragment;

import android.os.Bundle;

/**
 * @author okasurya on 8/1/18.
 */
public class FragmentSellingDelivered extends FragmentSellingTransaction {
    public static FragmentSellingDelivered newInstance() {
        return new FragmentSellingDelivered();
    }

    @Override
    public String getFilter() {
        return "7";
    }
}
