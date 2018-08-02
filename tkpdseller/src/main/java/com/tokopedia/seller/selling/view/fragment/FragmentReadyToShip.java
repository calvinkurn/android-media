package com.tokopedia.seller.selling.view.fragment;

import android.os.Bundle;

/**
 * @author okasurya on 8/2/18.
 */
public class FragmentReadyToShip extends FragmentSellingTransaction {
    public static FragmentReadyToShip newInstance() {
        return new FragmentReadyToShip();
    }

    @Override
    public String getFilter() {
        return "10";
    }
}
