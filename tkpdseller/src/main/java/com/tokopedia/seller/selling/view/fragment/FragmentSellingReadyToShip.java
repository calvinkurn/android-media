package com.tokopedia.seller.selling.view.fragment;

/**
 * @author okasurya on 8/1/18.
 */
public class FragmentSellingReadyToShip extends FragmentSellingTransaction {
    public static FragmentSellingReadyToShip newInstance() {
        return new FragmentSellingReadyToShip();
    }

    @Override
    public String getFilter() {
        return "10";
    }
}
